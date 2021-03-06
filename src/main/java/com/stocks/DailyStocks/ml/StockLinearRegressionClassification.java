package com.stocks.DailyStocks.ml; 
import java.util.Arrays;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.ml.regression.LinearRegressionSummary;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
//import org.apache.spark.ml.clustering.
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import com.stocks.DailyStocks.DBMongoConnector;
import com.stocks.DailyStocks.vo.Converter;
import com.stocks.DailyStocks.vo.PriceVO;

public class StockLinearRegressionClassification {

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
		StringIndexer st = new StringIndexer();
		st.setInputCol("dayMonth");
		st.setOutputCol("monthAndDay");
		 
		LinearRegression lr = new LinearRegression()
				.setMaxIter(1000)
				//.setAggregationDepth(5)
				//.setRegParam(0.8)
				
				.setStandardization(true)
	 			.setElasticNetParam(0.8)
				.setLabelCol("percent");
	 	VectorAssembler assembler = new VectorAssembler()
				.setInputCols(new String[] {"openPrice","closingPrice","volume"})
				.setOutputCol("features");
		
		Dataset<Row> select = trainData.select("openPrice","volume","closingPrice","percent");
		Dataset<Row> testSel = testData.select("openPrice","volume","closingPrice","percent");
		
		 Dataset<Row> ret = assembler.transform(select);
		 System.out.println("Showing dataset");
		// ret.show(10);
		 Dataset<Row> testVector = assembler.transform(testSel);
		 
		//Dataset<Row> testDatat =  assembler.transform(testData);
		//lr.setFeaturesCol("volume");
		 lr.setFeaturesCol("features");
		 lr.setLabelCol("percent");
		LinearRegressionModel model = lr.fit(ret);
		LinearRegressionSummary results = model.evaluate(testVector);
		//results.predictions().show();
		//System.out.println("Showing transform");
		model.transform(testVector).show();;
		
		
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
		Dataset<PriceVO> voDataset = getDataset();
	 	//performLinearRegression(voDataset);
	 	performKmeans(voDataset);
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
		StockLinearRegressionClassification algo = new StockLinearRegressionClassification();
		algo.perform();
	}
}
