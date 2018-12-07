package com.stocks.DailyStocks;

import java.io.Serializable;
import java.util.List; 

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.util.LongAccumulator;

import com.stocks.DailyStocks.vo.PriceVO;

import scala.Tuple2;

public class ContentParser implements Serializable{
	private static final long serialVersionUID = -2128853889139644595L;
	String filterString = "HistoricalPriceStore";
	String filterStringmain = "root.App.main";
	
	boolean DEBUG=true;
 	static LongAccumulator count = null;
	SparkSession session = null;
	
	public Dataset<PriceVO> parseYahooContent(Dataset<String> jsonArchive,String symbol){
		count = count == null? session.sparkContext().longAccumulator():count;
		Dataset<String> filtered = jsonArchive.filter(new FilterFunction<String>() {
			private static final long serialVersionUID = 1L;
			boolean filteredVal = Boolean.FALSE;
			public boolean call(String arg0) throws Exception {
				if (arg0.contains(filterStringmain))
					filteredVal = Boolean.TRUE;
				if (filteredVal) {
					if (arg0.contains("</script>);")) {
						filteredVal = Boolean.FALSE;
					}
				}
				return filteredVal;
 			}
		}); 
		ContentMapper mapper = new ContentMapper(filterString,symbol);
		String head = filtered.head();
		Dataset<PriceVO> symbols=session.createDataset(mapper.map(head), Encoders.bean(PriceVO.class));
		System.out.println("count is "+symbols.count());
	 	return symbols;  
 	}
	
	public void insertRecords(Dataset<PriceVO> records,String symbol) {
		DBMongoConnector conn = new DBMongoConnector();
		conn.session = records.sparkSession();
		conn.insertDataset(records, "FBData");
	}
	private Dataset<String> readFile(String filepath,SparkSession sess)
	{
		this.session = sess;
		JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sess.sparkContext());
		JavaRDD<String> text = jsc.textFile(filepath);
		return sess.createDataset(text.rdd(), org.apache.spark.sql.Encoders.STRING());
 	}
	
	private void printlist(List<Tuple2<Long, String>> take) {
 		take.forEach((t)-> System.out.println(t._2));
	}

	private void print(List<String> takeAsList) {
	 	takeAsList.forEach(x -> System.out.println(x));
	}
	public static void main(String arg[])
	{
		DBMongoConnector.initSession();
		SparkSession session = DBMongoConnector.session;
		ContentParser cp = new ContentParser();
		Dataset<String> texts = cp.readFile("C:\\stocks\\DailyStocks\\src\\test\\resources\\testSources.txt", session);
		cp.insertRecords(cp.parseYahooContent(texts,"FB"),"FB");
		
	}
}
