package com.stocks.DailyStocks.ml; 
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.sql.Date;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
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
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.catalyst.expressions.Expression;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DateType;
import org.apache.spark.sql.types.IntegerType;
import org.bson.Document;

import com.durgaveg.learningSpark.SparkParent;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import com.stocks.DailyStocks.DBMongoConnector;
import com.stocks.DailyStocks.vo.Converter;
import com.stocks.DailyStocks.vo.PriceVO;

import shapeless.ops.product.ToMap;

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
		modelData = getModalData(linearData, 30);
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
	 	
	 	Dataset<Row> select = trainData.select("openPrice","closingPrice","volume","dayOfWeek","percent","price");
	 	select.show();
	 	//select = select.join(percent,"dayOfWeek" );
	 	select.show();
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
	
	public Dataset<PriceVO> getModalData(Dataset<PriceVO> linearData,int size){
		ArrayList<PriceVO> t30= new ArrayList<>();
		
		Dataset<Row> percentD =linearData.where("dateStr > '2022-09-01'").groupBy("dayOfWeek","weekDay").avg("percent","openPercent","volume");
		percentD.show();
		Map<String,Row>percent=percentD.collectAsList()
				.stream().collect(Collectors.toMap(x->x.getString(1),x->x));
		Calendar today = Calendar.getInstance();
		if(today.get(Calendar.DAY_OF_WEEK )== Calendar.SATURDAY )
		{
			today.add(Calendar.DATE, 2);
		}
		if(today.get(Calendar.DAY_OF_WEEK )== Calendar.SUNDAY )
		{
			today.add(Calendar.DATE, 1);
		}
		PriceVO latest =linearData.orderBy(functions.col("dateStr").desc()).first();
		
		for(int i=0;i<size;i++)
		{
			PriceVO vo = new PriceVO();
			vo.setDate(today.getTime());
			StringBuilder dt = new StringBuilder();
			dt.append(today.get(Calendar.MONTH)+1).append("-").append(today.get(Calendar.DATE)).append("-").append(today.get(Calendar.YEAR));
			vo.setDateStr(dt.toString());
			vo.setDayOfWeek(today.get(Calendar.DAY_OF_WEEK));
			vo.setOpenPrice(null);
			Row prev = percent.get(vo.getWeekDay());
			vo.setOpenPrice(latest.getClosingPrice()*Double.parseDouble(prev.getString(2))+latest.getClosingPrice());
			vo.setVolume(latest.getVolume());
			t30.add(vo);
			latest = vo;
		}
		
		Dataset<PriceVO> ret = linearData.sqlContext().createDataset(new ArrayList<PriceVO>(), Encoders.bean(PriceVO.class));
//		Dataset<Row> select = linearData.select("openPrice","closingPrice","volume","dayOfWeek","percent","price");
//	 	select.show();
//	 	select = select.join(percent,"dayOfWeek" );
		return ret;
	}
	public void evaluate30(Dataset<PriceVO> linearData) {
		linearData.groupBy("dayOfWeek").avg("percent").show();
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
	public static void main(String arg[]) throws IOException
	{
		BasicConfigurator.configure();
		Logger logr = Logger.getLogger("org.spark_project");
		logr.setLevel(Level.INFO);
		logr.addAppender(new RollingFileAppender(new PatternLayout(), "c:/Stocks/log/Stocks.log")); 
		StockLinearRegressionClassification algo = new StockLinearRegressionClassification();
		algo.perform();
	}
}
