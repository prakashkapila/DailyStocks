package com.stocks.DailyStocks;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.RelationalGroupedDataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import com.stocks.DailyStocks.vo.SymbolQuoteVO;

public class SymbolQueryBuilder {

	private Dataset<SymbolQuoteVO> createDataset(JavaMongoRDD<Document> rows){
		JavaRDD<SymbolQuoteVO> vosRdd = rows.map(new SymbolDocumentConverter());
		SparkSession session = SparkSession.builder().sparkContext(rows.context()).getOrCreate();
		Dataset<SymbolQuoteVO> vosset= session.createDataset(vosRdd.rdd(), Encoders.bean(SymbolQuoteVO.class));
		return vosset;
	}
	private void print(List<?> row,String... strs) {
		System.out.println("passed table is "+strs[0]+"The Schema is ");
		Row ts = (Row) row.get(0);
		System.out.println(ts.schema().toString());
 		java.util.stream.Stream<Row> stream= (Stream<Row>) row.stream();
	 	stream.forEach(t ->{System.out.println(t.mkString());});
	 }
	
	private void printsf(List<StockFrequency> row) {
		System.out.println("The Schema is ");
		//System.out.println(row.get(0).schema().json());
		row.forEach(new Consumer<StockFrequency>() {
 			@Override
			public void accept(StockFrequency t) {
 				
 				System.out.println(t.toString());	
			}});
	}
	private Dataset<?>  addColumns(Dataset<?> any,Map<String,String> colsExpr) {
		for(Map.Entry<String, String> expr:colsExpr.entrySet()) {
			any = any.withColumn(expr.getKey(), functions.expr(expr.getValue()));	
		}
		return any;
	}
	
	public Dataset<Row> getFrequencyGroups(JavaMongoRDD<Document> rows){
		Dataset<SymbolQuoteVO> vosSet = createDataset(rows);
		vosSet.createOrReplaceGlobalTempView("StockSymbol");
		Dataset<Row> groupset = vosSet.groupBy("symbol").count()
				.withColumnRenamed("count", "TotalCounts")
				.sort(new Column("TotalCounts").desc());
		return groupset;
	}
	
	public Dataset<StockFrequency> buildSymbolgroup(JavaMongoRDD<Document> rows) {
		Dataset<SymbolQuoteVO> vosSet = createDataset(rows);
		vosSet.createOrReplaceGlobalTempView("StockSymbol");
		
		Dataset<Row> groupset = vosSet.groupBy("symbol").count()
				.withColumnRenamed("count", "TotalCounts")
				.sort(new Column("TotalCounts").desc());
		print(groupset.takeAsList(20));
		Map<String,String> map = new LinkedHashMap<>();
		map.put("gainers", "percent >0");
	//	map.put("losers", "percent <0");
		
		Dataset<?> vosSetnew =  addColumns(vosSet,map);
		print(vosSetnew.takeAsList(10),"all");
		
	 	RelationalGroupedDataset rgds = vosSetnew.groupBy("symbol","percent","gainers","volume");
		
	 	print(rgds.count().where("gainers ==true").takeAsList(10),"Only Gainers");
 		
		Dataset<Row> gainedstock=rgds.count().where("gainers ==true")
				.sort(new Column("count").desc())
				.withColumnRenamed("count", "GainedCount");
 		System.out.println("********Printing gained*****");
 		
		print(gainedstock.takeAsList(10),"Gainers sorted");
		
		//Dataset<StockFrequency> .map(new SymbolToFrequency(), Encoders.bean(StockFrequency.class));
		Dataset<Row> loststock=rgds.count().where("gainers ==false")
								.withColumnRenamed("gainers", "losers")
								.sort(new Column("count").desc())
								.withColumnRenamed("count", "LostCount");
								//.withColumnRenamed("symbolGained", "symbol");
		
		print(loststock.takeAsList(10),"loosers");
		Dataset<Row> joined = gainedstock.join(loststock, 
							(gainedstock.col("symbol").equalTo(loststock.col("symbol")))
							)
				.groupBy(gainedstock.col("symbol").toString(),"percent","losers","LostCount").count(); 

		print(joined.takeAsList(10),"joined");
		 
		Dataset<StockFrequency> result = joined.map(new SymbolToFrequency(), Encoders.bean(StockFrequency.class));
	 	//result = result.groupBy("symbol","percent","gainToggle","GainedCount","LostCount").map(new SymbolToFrequency(), Encoders.bean(StockFrequency.class));
	  	printsf(result.takeAsList(10));
		return result;
	}
		
}

class SymbolToFrequency  implements MapFunction<Row,StockFrequency>{

	private static final long serialVersionUID = -6726711486279696778L;
	@Override
	public StockFrequency call(Row arg0) throws Exception {
		StockFrequency sf = new StockFrequency();
		sf.setValues(arg0); 
		return sf;
	}
}


class SymbolDocumentConverter implements Function<Document,SymbolQuoteVO>{

	private static final long serialVersionUID = -4619461193752772972L;
	static final Gson gson = new Gson();
	@Override
	public SymbolQuoteVO call(Document arg0) throws Exception {
		return gson.fromJson(arg0.toJson() , SymbolQuoteVO.class);
 	}
	
}