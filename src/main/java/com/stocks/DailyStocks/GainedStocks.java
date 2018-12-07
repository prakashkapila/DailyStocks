package com.stocks.DailyStocks;

import java.io.*;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.Serializable;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.util.LongAccumulator;

import com.mongodb.spark.MongoSpark;
import com.stocks.DailyStocks.vo.SymbolQuoteVO;

import scala.Tuple2;

public class GainedStocks implements Serializable {
	private static final long serialVersionUID = 8195673612954119886L;
	static final boolean DEBUG=false;
	String nasdaqGained = "C:\\docs\\quotes\\29-11.txt";
	String nyseGained;
	String nasdaqLosers;
	String nyseLosers;
	String exchange="NSDQ";
	static LongAccumulator count = null;
	static SparkSession session;
	
	public  static  void setSparkSession(SparkSession sess) {
		session = sess;
		count = session.sparkContext().longAccumulator();
	}
	public SparkSession getSession() {
		if (session == null) {
			SparkConf config = new SparkConf(true);
			session = SparkSession.builder()
					.master("local")
					.appName("StocksManager")
					.getOrCreate();
			if (count == null) {
				count = session.sparkContext().longAccumulator();
			}
		} 
		return session;
	}
	
	public void filterNyse(String file) {
 		System.out.println("Reading from "+file);
		Dataset<String> todaysGained = getSession().read().textFile(file);
		String exc=file.replace(".txt", "").substring(file.indexOf("NYSE"));
		filterMostGained(todaysGained,exc,"NYSE"); 
	}
	
	public void  filterNasdaq(String file) {
		System.out.println("Reading from "+file);
		Dataset<String> todaysGained = getSession().read().textFile(file);
		String exc=file.replace(".txt", "").substring(file.indexOf("NSDQ"));
		filterMostGained(todaysGained,exc,"NSDQ");
	}
	
	public void filterMostGained(Dataset<String> todaysGained,String prefix,String exchange){
		Dataset<String> filtered = todaysGained.filter(new FilterFunction<String>() {
			private static final long serialVersionUID = 1L;
			String filterString = "/public/quotes/main.html?symbol";
			boolean filteredVal = Boolean.FALSE;
			public boolean call(String arg0) throws Exception {
				if (arg0.contains(filterString))
					filteredVal = Boolean.TRUE;
				if (filteredVal) {
					if (arg0.contains("/tr")) {
						filteredVal = Boolean.FALSE;
					}
				}
				return filteredVal;

			}
		});
		print(filtered.takeAsList(5)); // print sample

		JavaPairRDD<Long, String> values = filtered.toJavaRDD().mapToPair(new PairFunction<String, Long, String>() {
			private static final long serialVersionUID = 8661466693201458610L;
		 	String filterString = "/public/quotes/main.html?symbol";
			public Tuple2<Long, String> call(String arg0) throws Exception {
				if (arg0.contains(filterString)) {
					count.add(1);
				}
				Tuple2<Long, String> keyVal = new Tuple2<Long, String>(count.value(), arg0);
				return keyVal;
			}
		});
		if(DEBUG) {
			printlist(values.take(20));
		}
		JavaPairRDD<Long, Iterable<String>> valuesByKey = values.groupByKey();
		JavaRDD<SymbolQuoteVO> symbolsStore = valuesByKey
				.map(new Function<Tuple2<Long, Iterable<String>>, SymbolQuoteVO>() {
					private static final long serialVersionUID = -5114189666849290124L;
					public SymbolQuoteVO call(Tuple2<Long, Iterable<String>> arg0) throws Exception {
						SymbolQuoteVO vo = new SymbolQuoteVO();
						if (arg0._1() == 0)
							return null;
						vo.init(arg0._2(), exchange);
						return vo;
					}
				});
		String metaData = new SymbolQuoteVO().retrieveMetaData();
		System.out.println(metaData);
		if(DEBUG) {
		symbolsStore.take(5).forEach(new Consumer<SymbolQuoteVO>() {
			public void accept(SymbolQuoteVO t) {
				System.out.println(t != null ? t.toString() : "NULL");
			}
		});
		}
		
		storeRecords(symbolsStore,prefix,metaData);
		insertRecords(symbolsStore,session);
	}
	 private void storeRecords(JavaRDD<SymbolQuoteVO> symbolsStore,String prefix,String metaData) {
		 try {
				String file ="";// exchange+"-"+ String.valueOf(Calendar.getInstance().get(Calendar.DATE) + "-" + Calendar.getInstance().get(Calendar.HOUR));
				file = "c:/docs/quotes/StockGainers/"+prefix+".csv";
				System.out.println("Storing in file "+file);
				StockWriter.setWriter(file,metaData);
			} catch (IOException e) {
				e.printStackTrace();
			}
			symbolsStore.foreach(new StockWriter());
			try {
				StockWriter.close();
			} catch (IOException e) {
		 		e.printStackTrace();
			}
	}
	public  void insertRecords(JavaRDD<SymbolQuoteVO> elements,SparkSession thisSession) {
		 System.out.println("Total records are"+elements.count());
		  Dataset<Row> rows = thisSession.createDataFrame(elements,  SymbolQuoteVO.class);
		  Dataset<SymbolQuoteVO> stocklist= rows.as(Encoders.bean(SymbolQuoteVO.class));
		  MongoSpark.write(stocklist).option("collection", "SymbolQuote").mode(SaveMode.Append).save();
	 }
	private void printlist(List<Tuple2<Long, String>> take) {
		for (Tuple2<Long, String> taken : take) {
			System.out.println("key =" + taken._1() + " value is\t" + taken._2());
		}
	}

	private void print(List<String> takeAsList) {
		for (String line : takeAsList) {
			System.out.println("line =" + line);
		}
	}

	

	
}
 
class StockWriter implements VoidFunction<SymbolQuoteVO> {
	private static final long serialVersionUID = 7899387821113410879L;
	static FileWriter writer = null;

	static void close() throws IOException {
		
		writer.close();
	}
	static void setWriter(String fileName,String heading) throws IOException {
		writer = new FileWriter(fileName);
		writer.write(heading+"\n");
		writer.flush();
	}
	
	public void call(SymbolQuoteVO t) throws Exception {
		writer.write(t.toString()+"\n");
		writer.flush();
	}
}