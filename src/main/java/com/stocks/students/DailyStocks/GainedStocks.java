package com.stocks.students.DailyStocks;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

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
import org.apache.spark.sql.SparkSession;
import org.apache.spark.util.LongAccumulator;

import com.stocks.DailyStocks.StockFetcher;
import com.stocks.DailyStocks.vo.SymbolQuoteVO;

import scala.Tuple2;

public class GainedStocks implements Serializable {
	private static final long serialVersionUID = -6571074070117217257L;
	String nasdaqGained;
	String nyseGained;
	String nasdaqLosers;
	String nyseLosers;
	String exchange = "NSDQ";
	LongAccumulator count = null;
	SparkSession session;
	
	public SparkSession getSession(){
		if (session == null){
			SparkConf config = new SparkConf(true);
			session = SparkSession.builder()
					.master("local")
					.appName("StockGainFilter")
					.getOrCreate();
			if ( count == null ){
				count = session.sparkContext().longAccumulator();
			}
		}
//		Console con;
		return session;
	}
	
	public String filterMostGainedNyse(){
		Dataset<String> todaysGained = getSession().read().textFile(nyseGained);
		exchange = "NYSE";
		return filterMostGained(todaysGained);
	}
	
	public String filterMostGainedNasdaq(){
		Dataset<String> todaysGained = getSession().read().textFile(nasdaqGained);
		exchange="NSDQ";
		return filterMostGained(todaysGained);
	}
	
	public String filterMostGained(Dataset<String> todaysGained){
		Dataset<String> filtered = todaysGained.filter(new FilterFunction<String>(){
			private static final long serialVersionUID = 2991366116839787909L;
			String filterString="/public/quotes/main.html?symbol";
			boolean filteredVal=Boolean.FALSE;
			public boolean call(String arg0) throws Exception{
				if ( arg0.contains(filterString))
					filteredVal = Boolean.TRUE;
				if (filteredVal){
					if (arg0.contains("/tr")){
						filteredVal = Boolean.FALSE;
					}
				}
				return filteredVal;
		}});
//		print(filtered.takeAsList(5));
		JavaPairRDD<Long, String> values = filtered.toJavaRDD()
										.mapToPair(new PairFunction<String, Long,String>(){
 
											private static final long serialVersionUID = 1L;
											private boolean add = false;
											String filterString = "/public/quotes/main.html?symbol";
//											
											public Tuple2<Long, String> call(String t) throws Exception {
												if ( t.contains(filterString)){
													count.add(1);
												}
												Tuple2<Long,String> keyVal = new Tuple2<Long,String>(count.value(), t);
												return keyVal;
											}
											
										});
//		printList(values.take(20));
		JavaPairRDD<Long, Iterable<String>> valuesByKey = values.groupByKey();
		JavaRDD<SymbolQuoteVO> symbolsStore = valuesByKey.map(new Function<Tuple2<Long, Iterable<String>>, SymbolQuoteVO>(){
 
			private static final long serialVersionUID = 1L;
 
			public SymbolQuoteVO call(Tuple2<Long, Iterable<String>> v1) throws Exception {
				SymbolQuoteVO vo = new SymbolQuoteVO();
				if ( v1._1() == 0 )
					return null;
				vo.init(v1._2,"NYSE");
				return vo;
			}
			});
		String metaData = new SymbolQuoteVO().retrieveMetaData();
//		System.out.println(metaData);
//		symbolsStore.take(5).forEach(new Consumer<SymbolQuoteVO>(){
//			@Override
//			public void accept(SymbolQuoteVO t) {
//				System.out.println(t!=null?t.toString():"NULL");
//			}
			
//		});
		String file=null;
		try{
			file = exchange+String.valueOf(Calendar.getInstance().get(Calendar.HOUR));
			file = "c:/docs/quotes/StockGainers/"+file+".csv";
			System.out.println("Storing in file "+file);
			StockFileWriter.setWriter(file,metaData);
		}catch ( IOException e){
			e.printStackTrace();
		}
		symbolsStore.foreach(new StockFileWriter());
		try{
			StockFileWriter.close();
		}catch (IOException e){
			e.printStackTrace();
		}
		return file;
	}
	
	private void printList(List<Tuple2<Long, String>> take){
		for(Tuple2<Long,String> taken:take){
			System.out.println("key=" + taken._1 + "value is\t" +
								taken._2());
		}
	}
	
	private void print(List<String> takeAsList){
		for(String line:takeAsList){
			System.out.println("line="+line);
		}
	}
	
	public static void main(String[] args) throws IOException {
		Logger.getLogger("org").setLevel(Level.ERROR);
		//Get top stock gainers
		StockFetcher fetcher = new StockFetcher();
		String nsdqpath = fetcher.getNasdaqGainers();
		String nysepath = fetcher.getNyseGained();
		
		GainedStocks filter = new GainedStocks();
		filter.nasdaqGained = nsdqpath;
		filter.nyseGained = nysepath;
		filter.filterMostGainedNasdaq();
		filter.filterMostGainedNyse();
	}
}



