package com.stocks.taxes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import com.stocks.DailyStocks.SparkSessionMgr;

public class StatementReader implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1433602386322341382L;
	final String[] cols = new String[] { "Date", "Description", "Location", "AmountDeduct" };
	SparkSessionMgr mgr = new SparkSessionMgr();
	String bofafile = "D:\\Innovative Expenses\\2019filing\\bofa53022019YearEnd.txt";
	String jpmc2662 = "D:\\Innovative Expenses\\2019filing\\Chase2662_Activity20190101_20191231.CSV";
	String jpmc3908 = "D:\\Innovative Expenses\\2019filing\\Chase3908_Activity20190101_20191231_20200713.CSV";
	String barcl = "D:\\Innovative Expenses\\2019filing\\CreditCard_Barc_20190101_20191231.csv";
	String res = "D:\\Innovative Expenses\\2019filing\\summaries\\Result.csv";
	final int CARDONE = 1;
	final int CARDTWO = 2;

	private Dataset<DeductionsModel> readBarclaysCreditCard() {
		Dataset<Row> lines = mgr.getSession().read().csv(barcl);
		 // Transaction Date Description Category Amount
 		Dataset<DeductionsModel> dm = lines.map(new MapFunction<Row, DeductionsModel>() {
 	 		private static final long serialVersionUID = 3978878344903979192L;
 			@Override
			public DeductionsModel call(Row func) throws Exception {
				DeductionsModel ret = new DeductionsModel();
				String line = func.getString(0);
				String amt = func.getString(func.length() - 1);
				if (line.startsWith("Tr")) {
					 return ret;
				}
				ret.setDate(func.getString(0)); 
				ret.setAccount(func.getString(1));
				ret.setAmount(Double.parseDouble(func.getString(3)));
				return ret;
			}
		}, Encoders.bean(DeductionsModel.class));
		dm.show();
		return dm;
 	}

	private Dataset<DeductionsModel> readjpmCreditCard(int type) {
		String csv = type == CARDONE ? jpmc2662 : jpmc3908;
 		Dataset<Row> rows = mgr.getSession().read().csv(csv);
		// rows.show();
		Dataset<JPMDeductionsModel> lines = rows.map((MapFunction<Row, JPMDeductionsModel>) func -> {
			JPMDeductionsModel ret = new JPMDeductionsModel();
			// _c5, _c0, _c2, _c3, _c4, _c1
			// Transaction Date Post Date Description Category Type Amount
			String amt = func.getString(5);
			if (amt.contains("Amount")) {
				return ret;
			}
			ret.setAmount(Double.parseDouble(amt));
			ret.setCatgory(func.getString(3));
			ret.setDesc(func.getString(2));
			ret.setPostDate(func.getString(1));

			return ret;
		}, Encoders.bean(JPMDeductionsModel.class)).filter(new Column("amount").isNotNull());
		lines.show();
		Dataset<DeductionsModel> rowset = lines.map((MapFunction<JPMDeductionsModel, DeductionsModel>) func -> {
			DeductionsModel ret = new DeductionsModel();
			ret.setAccount(func.getDesc());
			ret.setAmount(func.getAmount());
			ret.setCategory(func.getCatgory());
			ret.setDate(func.getPostDate());
			ret.setDesc(func.getDesc());
			return ret;
		}, Encoders.bean(DeductionsModel.class));
		rowset.show();
		return rowset;
	}

	String catg;

	private Dataset<DeductionsModel> readBofaCC() {
		Dataset<Row> lines = mgr.getSession().read().csv(bofafile);
		lines.show();
		Dataset<Row> lines2 = lines.filter((FilterFunction<Row>) func -> {
			if (StringUtils.isEmpty(func.mkString()) || func.getString(0).startsWith("page"))
				return false;
			else
				return true;
		});
//Transaction Date	Description	Category	Amount

		Dataset<DeductionsModel> dm = lines2.map(new MapFunction<Row, DeductionsModel>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3978878344903979192L;

			@Override
			public DeductionsModel call(Row func) throws Exception {
				DeductionsModel ret = new DeductionsModel();
				String line = func.getString(0);

				String amt = func.getString(func.length() - 1);
				if (line.startsWith("#")) {
					catg = line.substring(1);
					return ret;
				}
				String fields[] = line.split(" ");
				if (fields.length > 1) {
					ret.setDate(fields[0]);
					if (fields.length > 2)
						ret.setAccount(fields[1] + " " + fields[2]);
					ret.setDesc(line.substring(8));
					if (amt != null && StringUtils.isNotBlank(amt)) {
						if (amt.endsWith("CR")) {
							amt = amt.substring(0, amt.indexOf("C"));
						}
						ret.setAmount(Double.parseDouble(amt));
					}
					ret.setCategory(catg);
				}
				return ret;
			}
		}, Encoders.bean(DeductionsModel.class));

		dm = dm.filter(new Column("date").isNotNull());
		Dataset<Row> grouped = dm
				.rollup(new Column[] { new Column("category"), new Column("desc"), new Column("account") })
				.sum("amount");
		// dm.show();
		// based on the account -> roll up the amount, create dates as array.
		grouped.show();
		return dm;
	}
	static FileWriter writer ;

	private void saveResult(Dataset<DeductionsModel> results) throws IOException
	{
		File f = new File(res);
		writer= new FileWriter(f);
		results.foreach(func->{
			writer.write(func.toString());
			writer.flush();
		});
		writer.close();
	}
	public void startProcessing() {
		Dataset<DeductionsModel> jpm1 = readjpmCreditCard(CARDONE);
		Dataset<DeductionsModel> jpm2 = readjpmCreditCard(CARDTWO);
		Dataset<DeductionsModel> barcl = readBarclaysCreditCard();
		Dataset<DeductionsModel> bofa = readBofaCC();
		Dataset<DeductionsModel> result = jpm1.union(jpm2); 
		result = result.union(barcl);
		result = result.union(bofa);
		try {
			saveResult(result);
		} catch (IOException e) {
		 	e.printStackTrace();
		}
	}

	public static void main(String ard[]) {
		StatementReader reader = new StatementReader();
		reader.startProcessing();
	}
}
