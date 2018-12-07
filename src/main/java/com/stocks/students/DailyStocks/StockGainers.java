package com.stocks.students.DailyStocks;

import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.stocks.DailyStocks.StockFetcher;

public class StockGainers {
	private static String nasdaqFileName=null;
	private static String nyseFileName = null;
	
	public static void main(String[] args) throws IOException {
		Logger.getLogger("org").setLevel(Level.ERROR);
		// 1. load NASDAQ and NYSE top 100 stocks lists into two files
		StockFetcher fetcher = new StockFetcher();
		String nsdqPath = fetcher.getNasdaqGainers();
		String nysePath = fetcher.getNyseGained();
		
		// 2. Parse the two nsdq and nyse files and load the stocks and their prices into csv files
		GainedStocks filter = new GainedStocks();
		filter.nasdaqGained = nsdqPath;
		filter.nyseGained = nysePath;
		nasdaqFileName = filter.filterMostGainedNasdaq();
		nyseFileName   = filter.filterMostGainedNyse();
		
		// 2. For all the stocks in the above files,generate the files, parse and load into a CSV 
		GoogleStockFilter filter2 = new GoogleStockFilter();
		filter2.setNasdaqPath(nasdaqFileName);
		filter2.setNysePath(nyseFileName);
		filter2.sampleInit();
 	}

}
