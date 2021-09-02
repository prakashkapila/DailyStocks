package com.stocks.taxes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;

import com.stocks.DailyStocks.SparkSessionMgr;

public class AccountCollator implements Serializable{
	private static final long serialVersionUID = -3672338536573560197L;
	SparkSessionMgr mgr = new SparkSessionMgr();
	final String path = "D:\\Innovative Expenses\\2019filing\\summaries\\Result.csv";
	final String out = "D:\\Innovative Expenses\\2019filing\\summaries\\Grouped.csv";
 	static FileWriter fw;
	
	public void collateAndSave() {
		Dataset<Row> lines= mgr.getSession().read().csv(path);//.as(Encoders.bean(DeductionsModel.class));
		Dataset<DeductionsModel> model = lines.map((MapFunction<Row,DeductionsModel>)func->{
			String amt = func.getString(3);
			if(amt == null || amt.trim().equals("")|| amt.contains("null"))
			{
				int stop = 100;
				System.out.println(stop);
			}
			DeductionsModel ret = new DeductionsModel();
			ret.setDate(func.getString(0));
			ret.setAccount(func.getString(1));
			ret.setCategory(func.getString(2));
			ret.setAmount(Double.parseDouble(amt));
			
			return ret;
			}
		, Encoders.bean(DeductionsModel.class));
		model.show();
		performGrouping(model);
	//	Dataset<Row> grouped = model.groupBy(new Column[] {new Column("account"),new Column("category"),new Column("amount")}).sum("amount");
	//	grouped.show();
	//	saveGroup(grouped);
	}
	NavigableMap<String,String> nmapk = new TreeMap<>();// possible key
	NavigableMap<String,Double> nmapv = new TreeMap<>(); // sum values
	
	private void performGrouping(Dataset<DeductionsModel> model) {
		model.foreach(func->{
			String entry = nmapk.get(func.getAccount());
			if( entry == null)
			{
				entry = nmapk.floorEntry(func.getAccount()).getValue();
			}
			if( entry == null)
			{
				nmapk.put(func.getAccount(), func.getAccount());
				nmapv.put(func.getAccount(), func.getAmount());
			}
			else {
				Double sum = nmapv.get(func.getAccount())+func.getAmount();
				nmapv.put(func.getAccount(), sum);
			}
		});
	}
	private void saveGroup(Dataset<Row> rows) {
		File f = new File(out);
		try {
			fw = new FileWriter(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rows.foreach(func->{
			String row = "";
			for (int i=0;i< func.length();i++)
			{
				
				row+=func.get(i)+(","); 
			}
			row+="\n";
			fw.write(row);
			fw.flush();
 		});
		try {
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	}
	
	
	public static void main(String arg[])
	{
		AccountCollator acc = new AccountCollator();
		acc.collateAndSave();
	}
}
