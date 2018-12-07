package com.stocks.DailyStocks;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class HistoryStockSeeker {
	StockManager manager = new StockManager();
	DateUriGenerator generator = new DateUriGenerator();
	
	public void collectData(String...uris) {
		Logger.getLogger("org").setLevel(Level.INFO);
		 try {
			manager.startCollecting(uris);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void collectData() throws IOException {
		NavigableMap<Long,List<String>> dates =generator.collect6Months();
		SparkSessionMgr mgr = new SparkSessionMgr();
		for(Map.Entry<Long, List<String>> entry:dates.entrySet())
		{
			manager.startCollecting(entry.getValue(),mgr.getSession());
		}
	}
	public static void main(String[] args) {
		Logger.getLogger("org").setLevel(Level.ERROR);
		HistoryStockSeeker history = new HistoryStockSeeker();
		try {
			history.collectData();
		} catch (IOException e) {
 			e.printStackTrace();
		}
	}

}
