package com.stocks.DailyStocks;

import java.io.Serializable;
import org.apache.spark.sql.Row;

public class StockFrequency implements Serializable{
	private static final long serialVersionUID = 3215153480894700115L;
	String symbol;
	double gainPercent;
	double lossPercent;
	boolean gained,lost;
	int gainedCount;
	int lostCount;

  	public void setValues(Row arg0)
	{
 		symbol = (String)arg0.get(0);
		gainPercent = arg0.getDouble(1);
		gained = arg0.getBoolean(2);
		gainedCount = Long.valueOf(arg0.getLong(3)).intValue();
		lossPercent =arg0.getDouble(4); 
		lost=arg0.getBoolean(5);
		lostCount = Long.valueOf(arg0.getLong(6)).intValue();
	}
	

	public String toString() {
		return new StringBuilder()
				.append(symbol )
				.append(gainPercent)
				.append(gained)
				.append(gainedCount)
				.append(lossPercent)
				.append(lost)
				.append(lostCount)
				.toString();
	}


	public String getSymbol() {
		return symbol;
	}


	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


	public double getGainPercent() {
		return gainPercent;
	}


	public void setGainPercent(double gainPercent) {
		this.gainPercent = gainPercent;
	}


	public double getLossPercent() {
		return lossPercent;
	}


	public void setLossPercent(double lossPercent) {
		this.lossPercent = lossPercent;
	}


	public boolean isGained() {
		return gained;
	}


	public void setGained(boolean gained) {
		this.gained = gained;
	}


	public boolean isLost() {
		return lost;
	}


	public void setLost(boolean lost) {
		this.lost = lost;
	}


	public int getGainedCount() {
		return gainedCount;
	}


	public void setGainedCount(int gainedCount) {
		this.gainedCount = gainedCount;
	}


	public int getLostCount() {
		return lostCount;
	}


	public void setLostCount(int lostCount) {
		this.lostCount = lostCount;
	}
	
}
