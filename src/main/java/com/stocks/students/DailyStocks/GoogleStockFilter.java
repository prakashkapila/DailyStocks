package com.stocks.students.DailyStocks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import com.stocks.DailyStocks.StockKeys;

import scala.Tuple2;

public class GoogleStockFilter implements Serializable {
	private static final long serialVersionUID = 2046958384305820490L;
	static final String outFiles = "C:\\docs\\quotes\\output\\LatestFile";
	JavaSparkContext jsc;
	SparkSession session;
	private String nasdaqPath;
	private String nysePath;
	private String fileName;
	private StringBuilder sb;
	private GoogleQuoteFetcher quote;

	private void getStockList() {
		System.out.println(getNasdaqPath() + "," + getNysePath());
		quote = new GoogleQuoteFetcher();
		Dataset<String> textfilesDS = session.read().textFile(getNasdaqPath());
		Dataset<Stock> stocksDS = textfilesDS.map(new MapFunction<String, Stock>() {
			private static final long serialVersionUID = -8852888260183299569L;

			public Stock call(String line) throws Exception {
				Stock stock = new Stock();
				if (line.contains("symbol"))
					return stock;

				String s = line.split(",")[0];
				if (s != null) {
					fileName = quote.resetQuote(s, "NASDAQ");
					stock.setTicker(s);
					stock.setExchange("NASDAQ");
					stock.setFile(fileName);
				}
				return stock;
			}
		}, Encoders.bean(Stock.class));
		// pick up the symbols from file.
		JavaRDD<String> fileNames = stocksDS.javaRDD()
				.map(new org.apache.spark.api.java.function.Function<Stock, String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String call(Stock v1) throws Exception {
						if (v1 == null)
							return "";
						return v1.getFile();
					}
				});
		// System.out.println("Downloaded files "+fileNames.count());

		JavaPairRDD<String, String> fileContents = fileNames.mapToPair(new PairFunction<String, String, String>() {
			private static final long serialVersionUID = -823334818167173890L;
			BufferedReader reader = null;

			private String getContents(String fileName) {
				System.out.println("Reading file " + fileName);
				StringBuilder lines = new StringBuilder();
				try {
					reader = new BufferedReader(new FileReader(new File(fileName)));
					while (reader != null && reader.ready()) {
						lines.append(reader.readLine());
						lines.append("\n");
					}
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
					return "NULL";
				}

				return lines.toString();
			}

			@Override
			public Tuple2<String, String> call(String t) throws Exception {
				if (t == null || t.trim().equalsIgnoreCase("")) {
					return new Tuple2<String, String>("NULL", "NULL");
				}
				return new Tuple2<String, String>(t, getContents(t));
			}
		});

		// System.out.println(" Was able to load files"+fileContents.count());

		JavaPairRDD<String, String> allFiles = fileContents.groupByKey().mapToPair(new StockReducer());
		JavaRDD<StockKeys> rows = allFiles.map(new Function<Tuple2<String, String>, StockKeys>() {
			private static final long serialVersionUID = -3614149193652011610L;
			int key = 0;

			public StockKeys call(String arg0) throws Exception {
				StockKeys keys = new StockKeys();
				String line = arg0;
				String[] vals;
				if (line.contains("renderMarketData")) {
					key++;
				}
				keys.setKey(key);
				if (line.contains("=")) {

					vals = line.split("=");
					if (vals.length > 1) {
						System.out.println(vals[0] + "**" + vals[1]);
						keys.setKeyName(vals[0]);
						keys.setValue(vals[1].replaceAll("(\"|/>)", " "));
					}
				}
				return keys;
			}

			@Override
			public StockKeys call(Tuple2<String, String> v1) throws Exception {
				StockKeys keys = call(v1._2);
				return keys;
			}
		});

		JavaRDD<Stock> allStocks= rows.mapToPair(new PairFunction<StockKeys,Integer,StockKeys>(){
 		 	private static final long serialVersionUID = -2387216432577131541L;
 			@Override
			public Tuple2<Integer, StockKeys> call(StockKeys t) throws Exception {
 			 	return new Tuple2<Integer,StockKeys>(t.getKey(),t);
			}}).groupByKey().map(new Function<Tuple2<Integer,Iterable<StockKeys>>,Stock>(){
  				private static final long serialVersionUID = 7887130684631303494L;
 				@Override
				public Stock call(Tuple2<Integer, Iterable<StockKeys>> v1) throws Exception {
 					Stock ret = Transformer.convert(v1._2());
 					return ret;
 				}
 				});
		allStocks.collect().forEach(new Consumer<Stock>() {
			@Override
			public void accept(Stock t) {
				System.out.println("The Value is"+t.toString());
			}});
		
		System.out.println("The count is "+allStocks.count());
		//allStocks.foreach(new Writer(outFiles+System.currentTimeMillis()+".csv"));
 	}
	
	public void sampleInit() throws IOException {
		session = SparkSession.builder().master("local[4]").appName("StockTracker").getOrCreate();
		getStockList();
	}

	public static void main(String[] args) {
		Logger.getLogger("org").setLevel(Level.ERROR);
		GoogleStockFilter master = new GoogleStockFilter();
		try {
			master.sampleInit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getNysePath() {
		return nysePath;
	}

	public void setNysePath(String nysePath) {
		this.nysePath = nysePath;
	}

	public String getNasdaqPath() {
		return nasdaqPath;
	}

	public void setNasdaqPath(String nasdaqPath) {
		this.nasdaqPath = nasdaqPath;
	}
}

class Writer extends SimpleWriter implements VoidFunction<Stock> {
	private static final long serialVersionUID = -247853847474557039L;
	String fileName;
	 Writer(String str)
	{
		fileName = str;
		try {
			setWriter(fileName, getHeading(new Stock()));
		} catch (IOException e) {
			 	e.printStackTrace();
		}
 	}
	@Override
	public void call(Stock t) throws Exception {
	writeObject(t);
	}
	private String getHeading(Stock t)
	{
		StringBuilder sb = new StringBuilder();
		Field[] fields = t.getClass().getDeclaredFields();
		for(Field f: fields)
		{
			sb.append(f.getName());
			sb.append(",");
		}
		return sb.toString();
	}
}

class StockParser implements VoidFunction<Tuple2<Integer, Iterable<StockKeys>>> {

	private static final long serialVersionUID = 7824238226372299242L;
	Stock stock;

	public StockParser(Stock currentstock) {
		this.stock = currentstock;
	}

	@Override
	public void call(Tuple2<Integer, Iterable<StockKeys>> t) throws Exception {
		List<StockKeys> stockvals = new ArrayList<StockKeys>();
		t._2.forEach(new Consumer<StockKeys>() {
			@Override
			public void accept(StockKeys t) {
				stockvals.add(t);
			}
		});
		String str = "";
		String val = "";
		for (int i = 0; i < stockvals.size(); i++) {
			str = stockvals.get(i).getValue();
			val = stockvals.get(i + 1).getValue();
			if (str.equalsIgnoreCase("price")) {
				stock.setPrice(Double.valueOf(val));
			} else if (str.equalsIgnoreCase("priceChange")) {
				stock.setPriceChange(Double.valueOf(val));
			} else if (str.equalsIgnoreCase("priceChangePercent")) {
				stock.setPriceChangePercent(Double.valueOf(val));
			} else if (str.equalsIgnoreCase("priceCurrency")) {
				stock.setPriceCurrency(val);
			}
		}

	}
}
