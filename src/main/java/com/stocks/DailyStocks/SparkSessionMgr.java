package com.stocks.DailyStocks;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

public class SparkSessionMgr {
	SparkSession session = null;
	public void init() {
		 	if (session == null) {
				SparkConf config = new SparkConf(true)
						.set("spark.app.id", "MongoConnectorStock")
						  .set("spark.mongodb.input.uri", "mongodb://127.0.0.1/test.SymbolQuote")
					      .set("spark.mongodb.output.uri", "mongodb://127.0.0.1/test.SymbolQuote")
					      .set("spark.scheduler.mode", "FAIR") // for giving all tasks a chance to execute.
					    ;
				session = SparkSession.builder()
						.master("local")
						.appName("StocksManager")
						.config(config)
						.getOrCreate();
				 
	 	}
		
	}
	public SparkSession getSession() {
		if(session == null) {
			init();
		}
		return session;
	}
}
