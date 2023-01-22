package com.stocks.DailyStocks.ml;

import java.io.IOException;
import java.io.Serializable;
import org.apache.hadoop.log.Log4Json;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;
 
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import com.durgaveg.learningSpark.SparkParent;
import com.stocks.DailyStocks.vo.RatingConverter;
import com.stocks.DailyStocks.vo.Rating;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;

public class PerformALS extends SparkParent implements Serializable{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 8613215899750331017L;
	private static Logger log;
	public void performALS(SparkSession spark) throws IOException {
	
		JavaRDD<Rating> stockRows = spark
				.read().textFile("d:/data/ALS.txt").javaRDD()
				.map(row->{Rating x =new Rating();x.parseRating(row); return x;});
	
		Dataset<Rating> ratings1 = spark.read().csv("D:/data/ALS.csv").map(
				new MapFunction<Row,Rating>(){
					private static final long serialVersionUID = 1L;
			@Override
			public Rating call(Row row) throws Exception {
				Rating x = new Rating();
				x.parseRating(row.getString(0));
			//	log.info(x.toString());
				return x;
			}
		}
		,Encoders.bean(Rating.class));
		 
		Dataset<Row> ratings = spark.createDataFrame(stockRows, Rating.class); //spark.read().csv("D:/data/ALS.csv");//stockRows.map(row->new Rating(row.getString(0)), Encoders.bean(Rating.class));
		
		ratings.show();
		Dataset<Row>[] splits = ratings.randomSplit(new double[]{0.8, 0.1,0.1});
		Dataset<Row> training = splits[0];
		Dataset<Row> test = splits[1];
		Dataset<Row> runtime =splits[2];
		
		// Build the recommendation model using ALS on the training data
		ALS als = new ALS()
				.setMaxIter(5)
				.setRegParam(0.01)
				.setUserCol("userId")
				.setItemCol("movieId")
				.setRatingCol("rating");
		
		ALSModel model = als.fit(training); // train the Model with data
		
	//	model.write().overwrite().save("d:/data/store/alsmodel");
		
		//model.load("d:/data/store/alsmodel.txt"); the saved Modal can be loaded like this
 		// Evaluate the model by computing the RMSE on the test data
		// Note we set cold start strategy to 'drop' to ensure we don't get NaN evaluation metrics
		
		
		Dataset<Row> hypothesisTest = model.transform(test); // test the Modal with Data

		hypothesisTest.show();
		model.setColdStartStrategy("drop");// Thos records that are not part of Modal training but we encountered in runtime are dropped. Other Strategy is return NaN ( Not a Number).
		
		Dataset<Row> actualPredictions = model.transform(runtime);
		log.info("Showing Actual Predictions");
		actualPredictions.show();
		RegressionEvaluator evaluator = new RegressionEvaluator()
				.setMetricName("rmse")
				.setLabelCol("rating")
				.setPredictionCol("prediction");
		
		double rmse = evaluator.evaluate(hypothesisTest );
		
		log.info("Root-mean-square error = " + rmse);

		// Generate top 10 movie recommendations for each user
		log.info("Generate top 10 movie recommendations for each user");
		Dataset<Row> userRecs = model.recommendForAllUsers(10);
		userRecs.show();
		// Generate top 10 user recommendations for each movie
		log.info("Generate top 10 user recommendations for each movie");
		Dataset<Row> movieRecs = model.recommendForAllItems(10);
	 	// $example off$
	 	movieRecs.show();
	 	log.info("Whole data");
		userRecs.collectAsList().forEach(rec->log.info(rec));
		
	 }
	
	public void performALS() {
		SparkSession session = getSession();
		 
		try {
			performALS(session);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String arg[]) throws IOException {
		BasicConfigurator.configure();
		BasicConfigurator.configure(new RollingFileAppender(new Log4Json(), "D:/log/ALS.log"));
		log = LogManager.getRootLogger();
		PerformALS als = new PerformALS();
		als.performALS();
	}
	}