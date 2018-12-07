package com.stocks.DailyStocks.ml;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.bson.Document;

import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import com.stocks.DailyStocks.DBMongoConnector;
import com.stocks.DailyStocks.vo.Converter;
import com.stocks.DailyStocks.vo.PriceVO;
 
import java.io.Serializable;

import org.apache.spark.ml.feature.LabeledPoint;
import org.apache.spark.ml.feature.OneHotEncoder;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.ml.feature.VectorIndexer;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.ml.param.Param;

//import org.apache.spark.ml.linalg.*;

import org.apache.spark.ml.regression.DecisionTreeRegressionModel;
import org.apache.spark.ml.regression.DecisionTreeRegressor;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.evaluation.RegressionEvaluator;

public class StockRandomForestPredictor implements Serializable{
 
	private static final long serialVersionUID = -7689170825724303440L;

	private void performRandomForest(Dataset<PriceVO> voDataset) {
	//	Dataset<LabeledPoint> dataset= getLabeledPointFromDataset(voDataset);
		Dataset<Row> rows = encodeText(voDataset,"dayMonth");
		Dataset<Row> rowsSet[] = rows.randomSplit(new double[] {.8,.2});
		performDecisionTrees(rowsSet[0], rowsSet[1]);
	}

	private void performDecisionTrees(Dataset<Row> trainset, Dataset<Row> testset) {
		System.out.println("Performing Decision Tree Regression on");
		System.out.println("Trainset rows"+trainset.count());
		System.out.println("Testset rows"+testset.count());
  		VectorIndexer indx = new VectorIndexer().setMaxCategories(7).setInputCol("features").setOutputCol("indexedFeatures");
	 	DecisionTreeRegressor dtr = new DecisionTreeRegressor().setFeaturesCol("indexedFeatures").setLabelCol("percent");
		Pipeline stages = new Pipeline().setStages( new PipelineStage[] {indx,dtr});
		PipelineModel model = stages.fit(trainset);
		Dataset<Row> predictedSet = model.transform(testset);
		predictedSet.show();
	}
	
	public Dataset<Row> encodeText(Dataset<PriceVO> dataset, String col)
	{ 
//		Dataset<PriceVO> dataset1 = dataset.where(new Column(col).isNotNull());
//		System.out.println("Non null rows are"+dataset1.count() );
		StringIndexer indexer = new StringIndexer().setHandleInvalid("skip").setInputCol(col).setOutputCol(col+"out");
		Param<String> str = indexer.handleInvalid();
		System.out.println(str.doc());
	//	indexer.setHandleInvalid("keep");
 //		Dataset<Row> dm = dataset.select("closingPrice");
		StringIndexerModel modlr = indexer.fit(dataset);
		Dataset<Row> encoded =  modlr.transform(dataset);
 		encoded.takeAsList(5).forEach(con->System.out.println(con.mkString()));
 		OneHotEncoder enc = new OneHotEncoder().setInputCol(col+"out").setOutputCol(col+"vec");
		encoded = enc.transform(encoded);
		encoded.show();
		return encoded;
	}
	
	public Dataset<LabeledPoint> getLabeledPointFromDataset(Dataset<PriceVO> voDataset)
	{
		LabeledPoint lb = null;//new LabeledPoint();
		Dataset<LabeledPoint> points = voDataset.map(x->{
			LabeledPoint ret = null;
			String dbs[]=x.getDayMonth().split("/"); 
			double db = Double.valueOf(dbs[0]+dbs[1]);
			Vector vec = Vectors.dense(new double[] {
					x.getClosingPrice(),x.getOpenPrice(),x.getHighPrice(),x.getLowPrice(),x.getPercent()
			});
			ret = new LabeledPoint(x.getHighPrice(),vec);
	
			return ret;}, Encoders.bean(LabeledPoint.class));
	return points;
	}
	
	public MulticlassMetrics getMetrics(DecisionTreeRegressionModel model,Dataset<LabeledPoint> points) {
	 	RegressionEvaluator eval = new RegressionEvaluator();
 		Dataset<PredictionResult> resultSet = points.map(labelPt->{
			PredictionResult ret=new  PredictionResult (
			model.predict(labelPt.features()),labelPt.label());
			return ret;
		}, Encoders.bean(PredictionResult.class));
 		MulticlassMetrics metrics = new MulticlassMetrics(resultSet.select("label","predictionColumn"));
 		return metrics;
	}
	
	public void perform() {
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
	  performRandomForest(voDataset);
	}
	
	public static void main(String arg[])
	{
		StockRandomForestPredictor algo = new StockRandomForestPredictor();
		algo.perform();
	}
}

class PredictionResult implements Serializable{
	 
	private static final long serialVersionUID = 1398867123634469590L;
	public PredictionResult(double predict, double label2) {
		// TODO Auto-generated constructor stub
		this.label=label2;
		this.predictionColumn=predict;
	}
	 
	public double getPredictionColumn() {
		return predictionColumn;
	}
	public void setPredictionColumn(double predictionColumn) {
		this.predictionColumn = predictionColumn;
	}
	public double getLabel() {
		return label;
	}
	public void setLabel(double label) {
		this.label = label;
	}

	double predictionColumn;
	double label;
}