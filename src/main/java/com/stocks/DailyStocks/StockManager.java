package com.stocks.DailyStocks;

import java.io.IOException;
import java.util.List;
import java.util.Timer;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.SparkSession;

public class StockManager {
	static final int NSDQG = 0;
	static final int NYSEG = 1;
	static final int NYSEL = 2;
	static final int NSDQL = 3;
Timer timer;
	public void startCollecting(List<String>uriData,SparkSession session) throws IOException 
	{
		StockFetcher fetcher = new StockFetcher();
		GainedStocks filter = new GainedStocks();
		
		String nsqdpath = fetcher.getNasdaqGainers(uriData.get(NSDQG));
		String nysepath = fetcher.getNyseGained(uriData.get(NYSEG));
		filter.setSparkSession(session);
		filter.filterNasdaq(nsqdpath);
		filter.filterNyse(nysepath);
		nysepath = fetcher.getNyseLosers(uriData.get(NYSEL));
		nsqdpath = fetcher.getNasdaqLosers(uriData.get(NSDQL));
		filter.nasdaqGained = nsqdpath;
		filter.nyseGained = nysepath;
		filter.filterNasdaq(nsqdpath);
		filter.filterNyse(nysepath);
	}
	
	public void startCollecting(String... uriData) throws IOException {
		StockFetcher fetcher = new StockFetcher();
		GainedStocks filter = new GainedStocks();
		String nsqdpath = fetcher.getNasdaqGainers(uriData[NSDQG]);
		String nysepath = fetcher.getNyseGained(uriData[NYSEG]);
		filter.filterNasdaq(nsqdpath);
		filter.filterNyse(nysepath);
		nysepath = fetcher.getNyseLosers(uriData[NYSEL]);
		nsqdpath = fetcher.getNasdaqLosers(uriData[NSDQL]);
		filter.nasdaqGained = nsqdpath;
		filter.nyseGained = nysepath;
		filter.filterNasdaq(nsqdpath);
		filter.filterNyse(nysepath);

	}

	public static void currentDay() throws IOException {
		StockManager manager = new StockManager();
		String uris[] = new String[4];
		uris[NSDQG] = "http://www.wsj.com/mdc/public/page/2_3021-gainnnm-gainer.html"; // nasdaq gained
		uris[NYSEG] = "http://www.wsj.com/mdc/public/page/2_3021-gainnyse-gainer.html"; // nyse gained
		uris[NYSEL] = "http://www.wsj.com/mdc/public/page/2_3021-losenyse-loser.html"; // nyse lost
		uris[NSDQL] = "http://www.wsj.com/mdc/public/page/2_3021-losennm-loser.html";
 		manager.startCollecting(uris);
	}
	public static void main(String arg[]) throws IOException {

		Logger.getLogger("org").setLevel(Level.INFO);
		
	}
}
