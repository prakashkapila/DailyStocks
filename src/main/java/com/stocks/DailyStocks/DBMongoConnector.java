package com.stocks.DailyStocks;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RuntimeConfig;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.bson.Document;

import com.fasterxml.jackson.core.JsonGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mongodb.spark.MongoConnector;
import com.mongodb.spark.MongoSpark;
import com.mongodb.spark.config.ReadConfig;
import com.mongodb.spark.config.WriteConfig;
import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import com.stocks.DailyStocks.vo.SymbolQuoteVO;

import java.io.Serializable;
import java.text.BreakIterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.function.Consumer;

public class DBMongoConnector implements Serializable {

	private static final long serialVersionUID = -3459656215445752239L;
	static SparkSession session;

	public static void initSession() {
		session = session != null ? session
				: SparkSession.builder().master("local[4]").appName("MongoConnectorStock")
						.config("spark.app.id", "MongoConnectorStock")
						.config("spark.mongodb.input.partitioner", "MongoSplitVectorPartitioner")
						.config("spark.mongodb.input.uri", "mongodb://127.0.0.1/test.SymbolQuote")
						.config("spark.mongodb.output.uri", "mongodb://127.0.0.1/test.SymbolQuote")
						.getOrCreate();
	}

	public static SparkSession getSession() {
		if(session == null)
		{
			initSession();
		}
		return session;
	}
	
	public void insertRecords(JavaRDD<?> elements, SparkSession thisSession) {
		if(elements.takeSample(true, 1).get(0) instanceof SymbolQuoteVO ) 
		{
			Dataset<Row> rows = thisSession.createDataFrame(elements, SymbolQuoteVO.class);
			Dataset<SymbolQuoteVO> stocklist = rows.as(Encoders.bean(SymbolQuoteVO.class));
			//MongoConnector conn = MongoConnector.apply(session.sparkContext());
//			ReadConfig conf = new ReadConfig();
//			MongoSpark spark = new MongoSpark(session, conn, session.conf(), null);
//			
			MongoSpark.write(stocklist).option("collection", "test.StockQuotes").mode(SaveMode.Overwrite);
		}
		 
	}

	public void insertDataset(Dataset<?> dataset)
	{
		if(session == null) {
			session = dataset.sparkSession();
			}
		//session.conf().set(key, value);
	 	JavaSparkContext newcontext = new JavaSparkContext(session.sparkContext());
		Map<String, String> newConfig = new HashMap<String, String>();
		newConfig.put("collection", "Frequency");
		newConfig.put("readPreference.name", "secondaryPreferred");
		newConfig.put("spark.mongodb.input.uri", "mongodb://127.0.0.1/test.Frequency");
	//	newConfig.put("spark.mongodb.output.uri", "mongodb://127.0.0.1/test.Frequency");
		WriteConfig conff = WriteConfig.create(newcontext).withOptions(newConfig);
	 	MongoSpark.save(dataset, conff);
 	 }
	
	public JavaMongoRDD<Document> loadDataRecords(SparkSession session,String collection)
	{
		if((this.session=session) == null)
		{
			initSession();
		}
		JavaMongoRDD<Document> ret = null;
		JavaSparkContext ctx = new JavaSparkContext(session.sparkContext());
		HashMap<String,String> newConfig = new HashMap<String,String>(); 
		newConfig.put("collection", collection);
		newConfig.put("readPreference.name", "secondaryPreferred");
		newConfig.put("spark.mongodb.input.uri", "mongodb://127.0.0.1/test."+collection);
	 	newConfig.put("spark.mongodb.output.uri", "mongodb://127.0.0.1/test."+collection);
	 	ReadConfig config = ReadConfig.create(ctx).withOptions(newConfig);
	 	ret = MongoSpark.load(ctx, config);
		return ret;
	}
	
	public void insertDataset(Dataset<?> dataset,String collection)
	{
		System.out.println("inserting to mongodb://127.0.0.1/"+collection);
		if(session == null) {
			session = dataset.sparkSession();
			}
	  	JavaSparkContext newcontext = new JavaSparkContext(session.sparkContext());
		Map<String, String> newConfig = new HashMap<String, String>();
		newConfig.put("collection", collection);
		newConfig.put("readPreference.name", "secondaryPreferred");
		newConfig.put("spark.mongodb.input.uri", "mongodb://127.0.0.1/test."+collection);
	 	newConfig.put("spark.mongodb.output.uri", "mongodb://127.0.0.1/test."+collection);
		WriteConfig conff = WriteConfig.create(newcontext).withOptions(newConfig);
	 	MongoSpark.save(dataset, conff);
 	 }
	
	 
	public JavaMongoRDD<Document> readStocks() {
		initSession();
		JavaSparkContext jsc = new JavaSparkContext(session.sparkContext());
		JavaMongoRDD<Document> rows = MongoSpark.load(jsc);
		// .option("partitioner","spark.mongodb.input.partitioner" )
		// .load();
		print(rows);
		System.out.println("Count is " + rows.count());
		return rows;
	}

	private void print(JavaMongoRDD<Document> rows) {
		List<Document> sampleRows = rows.takeSample(false, 10);
		System.out.println("The type of document is " + sampleRows.get(0).getClass());
		sampleRows.forEach(new Consumer<Document>() {
			@Override
			public void accept(Document t) {
				Gson gson = new Gson();
				SymbolQuoteVO vo = gson.fromJson(t.toJson(), SymbolQuoteVO.class);
				// System.out.println(t.toJson());
				System.out.println(vo.toString());
			}
		});
	}

	Scanner sc;
	BreakIterator bkit;
	StringTokenizer st;

	public static void main(String args[]) {
		// Logger.getLogger().
		Logger.getLogger("org").setLevel(Level.INFO);
		DBMongoConnector connector = new DBMongoConnector();
		connector.readStocks();
		// connector.insertRecords(null, null);
	}
}
