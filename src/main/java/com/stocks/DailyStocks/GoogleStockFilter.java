package com.stocks.DailyStocks;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;



public class GoogleStockFilter implements Serializable {
	 
	private static final long serialVersionUID = 219134208972559550L;
	JavaSparkContext jsc;
	SparkSession session;
	public void sampleInit() {
		SparkConf config = new SparkConf(true);
		//C:/docs/quotes/input/googNVDA.txt
		session = SparkSession.builder().master("local").appName("StockTracker").getOrCreate();
		Dataset<Row> textfiles =session.read().text("C:\\docs\\quotes\\input\\googNVDA.txt","C:\\docs\\quotes\\googamd.txt",
				"C:\\docs\\quotes\\googhdp.txt", "C:\\docs\\quotes\\goognvda.txt");
		textfiles.printSchema();
		Dataset<Row> remainingLines = textfiles.filter(new FilterFunction<Row>() {
 			private static final long serialVersionUID = -5601562169226150992L;
			boolean start = false;
			public boolean call(Row arg0) throws Exception {
		 		String line = (String)arg0.getString(0);
				if(line!= null && line.contains("renderMarketData()"))
				{
					start = true;
				}
				if(start) {
					if(line != null && line.contains("</div>"))
						start = false;
				}
				return start;
			}});
		System.out.println("Remaining lines are "+remainingLines.count());
		print(remainingLines.collectAsList());
		
		Dataset<StockKeys> rows = remainingLines.map(new MapFunction<Row,StockKeys>(){
  			private static final long serialVersionUID = 1955797041484744959L;
			int key = 0;
			public StockKeys call(Row arg0) throws Exception {
				StockKeys keys = new StockKeys();
				String line = arg0.getString(0);
				String[] vals;
				if(line.contains("renderMarketData"))
				{
					key++;
				}
				keys.setKey(key);
				if(line.contains("="))
				{
					vals = line.split("=");
					if(vals.length >1) {
						keys.setKeyName(vals[0]);
						keys.setValue(vals[1].replaceAll("(\"|/>)",""));
					}
				}	 
	 			return keys;
			}}, Encoders.bean(StockKeys.class));
		rows.show(); 
		rows.collectAsList().forEach(new Consumer<StockKeys>() {
 			public void accept(StockKeys t) {
				System.out.println(t.key+t.keyName+t.value);
			}});
//		rows.flatMap(new Function1<StockKeys,SymbolQuoteVO>(){
//			private static final long serialVersionUID = 1L;
//
//			public Iterator<SymbolQuoteVO> call(StockKeys t) throws Exception {
//			 	return null;
//			}
//			}, SymbolQuoteVO.class);
	}
	
	 

	private void print(List<Row> list) {
		 for(Row row:list)
		 {
			 System.out.println(" Row = "+row.getString(0));
		 }
	}

	public static void main(String arg[])
	{
		GoogleStockFilter master = new GoogleStockFilter();
		master.sampleInit();
	}
}
