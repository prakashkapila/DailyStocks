package com.stocks.DailyStocks;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.bson.Document;

import com.mongodb.spark.rdd.api.java.JavaMongoRDD;

public class QueryEngine {

	public void storeMostlyOccured() {
		DBMongoConnector connector = new DBMongoConnector();
		JavaMongoRDD<Document> docs = connector.readStocks();
		SymbolQueryBuilder builder = new SymbolQueryBuilder();
		//Dataset<StockFrequency> frequency = builder.buildSymbolgroup(docs);
		Dataset<Row> groups = builder.getFrequencyGroups(docs);
		connector.insertDataset(groups, "GroupSet");
		
	}
	public void getHistoricalDataMostlyOccurred() {
		DBMongoConnector connector = new DBMongoConnector(); 
		JavaMongoRDD<Document> docs = connector.loadDataRecords(null,"GroupSet");
		
	}
	public static void main(String[] arg) {
		Logger.getLogger("org").setLevel(Level.ERROR);
		//connector.insertDataset(frequency);
		QueryEngine qe = new QueryEngine();
		qe.storeMostlyOccured();
	}
}
	