package com.durgaveg.learningSpark;

import org.apache.log4j.Level;
import org.apache.spark.sql.SparkSession;

public abstract class SparkParent {
	static SparkSession session;
	static boolean DEBUG=false;
	public SparkParent() {
	 
		System.setProperty("hadoop.home.dir", "D:\\winutils-master\\winutils-master\\hadoop-2.7.1");
		if(!DEBUG)
		org.apache.log4j.Logger.getLogger("org.apache.spark").setLevel(Level.ERROR);
 	}
	public void initSession() {
		if (session == null) {
			session = SparkSession.builder().master("local").appName("StockPrices")
					.config("spark.driver.memory","2G")
					.config("spark.driver.maxResultSize", "2G")
					.config("spark.executor.memory","8G")
					.config("spark.sql.shuffle.partitions","5")
					.config("hadoop.home.dir","D:\\winutils-master\\winutils-master\\hadoop-2.7.1")
					//.config("spark.driver.extraClassPath", "lib/spark-nlp-assembly-1.6.2.jar")
					.getOrCreate();
 		}
 	}
	
	public SparkSession getSession() {
		initSession();
		return session ;
	}
}

