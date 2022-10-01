package com.stocks.DailyStocks.ml; 
import java.io.Serializable;
import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.ml.regression.LinearRegressionSummary;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.sql.Column;
//import org.apache.spark.ml.clustering.
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.bson.Document;

import com.durgaveg.learningSpark.SparkParent;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import com.stocks.DailyStocks.DBMongoConnector;
import com.stocks.DailyStocks.vo.Converter;
import com.stocks.DailyStocks.vo.PriceVO;

public class StockLinearRegressionClassification extends SparkParent implements Serializable {

 
	private static final long serialVersionUID = 396857241591182611L;
	
	static Logger log = LogManager.getLogger(StockLinearRegressionClassification.class);
	/**
	 * Given the stock data we will try to find out the percent increase with
	 * respect to volume , date-month.
	 * 
	 **/
	public void performLinearRegression(Dataset<PriceVO> linearData) { 
		Dataset<PriceVO> trainData = null, modelData = null, testData = null;
	 	Dataset<PriceVO>[] values = linearData.randomSplit(new double[] { .8, .1, .1 });
	 	
		trainData = values[0];
		modelData = values[1];
		testData = values[2];
		trainData = linearData.limit(1230); 
		//trainData.foreach(vo->vo.setPrice(vo.getClosingPrice()));
		testData = testData.map(new MapFunction<PriceVO,PriceVO>() {
 			private static final long serialVersionUID = 1L;
 			@Override
			public PriceVO call(PriceVO value) throws Exception {
				value.setPrice(0);
				return value;
			}
		}, Encoders.bean(PriceVO.class));
		testData.show();
		LinearRegression lr = new LinearRegression()
				.setMaxIter(10)
				.setAggregationDepth(5)
 				.setStandardization(true)
  	 			.setElasticNetParam(0.8)
  	 			.setFeaturesCol("features")
  	 			.setLabelCol("price")
 				;
		
	 	VectorAssembler assembler = new VectorAssembler()
				.setInputCols(new String[] {"openPrice","closingPrice","volume","dayOfWeek" })
				.setOutputCol("features")
				;
	 	
	 	Dataset<Row> select = trainData.select("openPrice","closingPrice","volume","dayOfWeek","price");
	 	 Pipeline pipes = new Pipeline().setStages(new PipelineStage[]{assembler,lr});
	 	PipelineModel lm =  pipes.fit(select);
	    Dataset<Row> predictions =lm.transform(testData);

	    predictions.show();
		
		Dataset<Row> testSel = testData.select("openPrice","volume","closingPrice","dayOfWeek","price");
		select.show();
 	 Dataset<Row> ret = assembler.transform(select);
		 ret.show();
		LinearRegressionModel model = lr.fit(ret);
	  Dataset<Row> testRows =  assembler.transform(testSel);
	
		LinearRegressionSummary results = model.evaluate(testRows);
		results.predictions().show();
		//System.out.println("Showing transform");
		//model.transform(testVector).show();;
		
		
	}
	/**
	 * Given the stock data we will try to find out if the percent will
	 * increase/decrease with respect to volume , date-month.
	 * 
	 **/
	public void performLinearClassification(Dataset<?> dataset) {
	}
	
	private void fillData(Dataset<PriceVO>... linearData) {
		
 }

	public Dataset<PriceVO> getDataset(){
		DBMongoConnector conn= new DBMongoConnector();
		DBMongoConnector.initSession();
		JavaMongoRDD<Document> doc = conn.loadDataRecords(DBMongoConnector.getSession(), "FBData");
	  JavaRDD<PriceVO> voList = doc.map(x->{
		  System.out.println(x.toJson());
		  PriceVO vo = Converter.convert(x,new PriceVO());
		  return vo;
	  });
	  SparkSession session = SparkSession.builder().sparkContext(voList.context()).getOrCreate();
	  Dataset<PriceVO> voDataset = session.createDataset(voList.rdd(), Encoders.bean(PriceVO.class));
	  return voDataset;
	}
	public void perform() {
		Dataset<PriceVO> voDataset = getCsvDataset();
	 	performLinearRegression(voDataset);
	 	//performKmeans(voDataset);
	}
	
	private Dataset<PriceVO> getCsvDataset() {
		Converter.COMP="TESLA INC";
		Converter.SYMBOL="TSLA";
	 	SparkSession session = getSession();
		Dataset<Row> stockRows = session.read().option("header", true).csv("D:/stocks/TSLA.csv");
		stockRows.show();
		Dataset<PriceVO> stocks = stockRows.map(xy->(Converter.convertRow(xy)),Encoders.bean(PriceVO.class));
		stocks.show();
		return stocks;
	}
	private void performKmeans(Dataset<PriceVO> vosd) {
		 KMeans kmeans = new KMeans().setSeed(1L).setK(2);
		 KMeansModel model= kmeans.fit(vosd);
		 Dataset<Row> transformed = model.transform(vosd);
		 transformed.show(10);
		double cost =  model.computeCost(vosd);
		Arrays.asList(model.clusterCenters()).forEach(action ->
			System.out.println(action.toDense())
		);
		 //ClusteringEvaluator  eval = new ClusteringEvaluator();
	}
	public static void main(String arg[])
	{
		BasicConfigurator.configure();
		StockLinearRegressionClassification algo = new StockLinearRegressionClassification();
		algo.perform();
	}
}
