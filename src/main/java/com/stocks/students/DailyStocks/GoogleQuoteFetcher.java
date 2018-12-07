package com.stocks.students.DailyStocks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GoogleQuoteFetcher implements Serializable {
	private static final long serialVersionUID = -836382845736211081L;
	String symbol = "HDP";
	String exchange = "NASDAQ";
	final String type = "goog";
	private String googUrl = "https://www.google.com/finance?q="+exchange+"%3A"+symbol;
	String yahoourl= "https://finance.yahoo.com/quote/HDP";
	String file=null;
	
	public String resetQuote(String quote, String exchange) throws IOException{
		symbol = quote.trim().toUpperCase().replaceAll("\"", "");
		//yahoourl= "https://finance.yahoo.com/quote/" + symbol;
		googUrl = "https://www.google.com/finance?q="+exchange+"%3A"+symbol;
		URL url = new URL(googUrl);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		StringBuilder webpageDetails = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		do{
			webpageDetails.append(reader.readLine());
			webpageDetails.append("\n");
		}while(reader.ready());
		saveStockQuote(webpageDetails.toString());	
		return file;
	}
	
	public String getQuote(String quote) throws IOException{
		this.symbol=quote;
		StringBuilder webpageDetails = new StringBuilder();
		URL url = new URL(googUrl);
		HttpsURLConnection conn= (HttpsURLConnection) url.openConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		while ( reader.ready()){
			webpageDetails.append(reader.readLine());
			webpageDetails.append("\n");
		}
//		printWebPage(webpageDetails.toString());
		saveStockQuote(webpageDetails.toString());
		return webpageDetails.toString();
	}
	
	private void saveStockQuote(String allHtml) throws IOException{
	 	file = "C:/docs/quotes/input/"+type+symbol+".txt";
	 	System.out.println("Saving to file "+file);
		File output = new File(file);
 		if (!output.exists()){
			 output.createNewFile();
		}
		FileWriter writer = new FileWriter(output);
		writer.write(allHtml);
 		writer.flush();
 		writer.close();
	}
	
	private void printWebPage(String webpageDetails){
		System.out.println(webpageDetails);
	}
	
	public static void main(String[] args) throws IOException {
		GoogleQuoteFetcher quote = new GoogleQuoteFetcher();
		quote.resetQuote("HDP", "NASDAQ");
	}

}
