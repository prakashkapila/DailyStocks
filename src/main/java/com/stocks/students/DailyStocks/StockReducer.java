package com.stocks.students.DailyStocks;

import java.io.IOException;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class StockReducer implements PairFunction<Tuple2<String,Iterable<String>>,String,String> {
	SparkContext session;
	StockFileWriter writer = new StockFileWriter();
	private static final long serialVersionUID = -5406121715979853323L;

	 

	private void initWriter() {
		if (StockFileWriter.writer == null) {
			String fileName = "c:/docs/quotes/StockGainers/NasdaqGainers.csv";
			String fileHeading = "Exchange,Ticker,Price,PriceChange,PriceChangePercent\n";
			try {
				StockFileWriter.setWriter(fileName, fileHeading);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void writeStock(Stock s) {
		initWriter();
		try {
			writer.writeObject(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String readStockFiles(Iterable<String> fileContents) {
		boolean start = false;
		StringBuilder sb = new StringBuilder("");
		
		for (String fileContent :fileContents ) {
			String[] lines = fileContent.split("\n");
			for(String line:lines) {
			if (line != null && line.contains("renderMarketData()")) {
				start = true;
			}
			if (start) {
				sb.append(line);
				sb.append("\n");
				
	 	 		if (line != null && line.contains("</div>")) {
					start = false;
				}
			}
			}
		}
		return sb.toString();
 	}
	
 
	@Override
	public Tuple2<String, String> call(Tuple2<String, Iterable<String>> t) throws Exception {
		 String res = readStockFiles(t._2);
 		return new Tuple2<String,String>(t._1,res);
	}
	 
	}

/*JavaRDD<StockKeys> rows = remainingLines.map(new Function<String, StockKeys>() {
			private static final long serialVersionUID = -3614149193652011610L;
			int key = 0;

			public StockKeys call(String arg0) throws Exception {
				StockKeys keys = new StockKeys();
				String line = arg0;//.getString(0);
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
		});//, Encoders.bean(StockKeys.class));
		// rows.show();
		
		// parseStockPricesRdd(rows, stock);
	

	
*/