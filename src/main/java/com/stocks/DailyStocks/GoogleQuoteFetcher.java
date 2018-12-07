package com.stocks.DailyStocks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class GoogleQuoteFetcher {
	String symbol = "amd";
	String exchange="NASDAQ";
	final String type = "goog";
	private  String googUrl= "https://www.google.com/finance?q="+exchange+"%3A"+symbol;
	String yahoourl= "https://finance.yahoo.com/quote/NVDA";
	
	public void resetQuote() throws IOException {
		URL url = new URL(googUrl);
		//URL url = new URL(yahoourl);
		
	HttpsURLConnection conn =  (HttpsURLConnection) url.openConnection();
	conn.setConnectTimeout(0);
	conn.connect();
	StringBuilder webpageDetails = new StringBuilder();
	BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		do  {
			webpageDetails.append(reader.readLine());
			webpageDetails.append("\n");
		}while(reader.ready());	
		printWebPage(webpageDetails.toString());
		saveStockQuote(webpageDetails.toString());
	}
	
	public String getQuote(String quote) throws IOException{
		this.symbol=quote;
		StringBuilder webpageDetails = new StringBuilder();
		URL url = new URL(googUrl);
		HttpsURLConnection conn =  (HttpsURLConnection) url.openConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		while (reader.ready()) {
			webpageDetails.append(reader.readLine());
			webpageDetails.append("\n");
		}	
	//	printWebPage(webpageDetails.toString());
		saveStockQuote(webpageDetails.toString());
		return webpageDetails.toString();
	}
	private void saveStockQuote(String allHtml) throws IOException {
		String file = "C:/docs/quotes/input/"+type+symbol+".txt";
		File output = new File(file);

		System.out.println("Savin to file "+file);
		if(!output.exists()) {
			output.createNewFile();
		}
		FileWriter writer = new FileWriter(output);
		writer.write(allHtml);
		
		writer.flush();
	}
	private void printWebPage(String webpageDetails) {
		System.out.println(webpageDetails);
	}
	public static void main(String arg[]) throws IOException
	{
		GoogleQuoteFetcher quote = new GoogleQuoteFetcher();
		quote.resetQuote();
	}
}
