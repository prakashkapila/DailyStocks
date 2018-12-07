package com.stocks.DailyStocks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

public class StockFetcher {
	String mostGainers ="";
	String mostLosers="";
	//http://www.wsj.com/mdc/public/page/2_3021-losennm-loser.html
	//http://www.wsj.com/mdc/public/page/2_3021-gainnnm-gainer-20180410.html /*April 10*/
	//http://www.wsj.com/mdc/public/page/2_3021-losennm-loser-20180416.html
	// "https://finance.yahoo.com/gainers";
	String filePath = "";

	private void fetchAndSave(String exchange,String urlstr) throws IOException{
		URL url = new URL(urlstr);
 		URLConnection conn = (URLConnection) url.openConnection();
		conn.connect();
		StringBuilder webpageDetails = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		do {
			webpageDetails.append(reader.readLine());
			webpageDetails.append("\n");
		} while (reader.ready());
		//printWebPage(webpageDetails.toString());
		saveStockQuote(exchange,webpageDetails.toString());
	}
	public String getNasdaqLosers() throws IOException {
		mostLosers = "http://www.wsj.com/mdc/public/page/2_3021-losennm-loser.html";
		return getNasdaqLosers(mostLosers);
	}
	
	public String getNasdaqLosers(String uri) throws IOException{
		fetchAndSave("NSDQL",uri);
		return this.filePath;
	}
	
	public String getNyseLosers() throws IOException{
		mostLosers="http://www.wsj.com/mdc/public/page/2_3021-losenyse-loser.html";
 		return getNyseLosers(mostLosers);
	}
	public String getNyseLosers(String uri) throws IOException{
		fetchAndSave("NYSEL",uri);
		return this.filePath;
	}
	public String getNasdaqGainers() throws IOException {
		mostGainers = "http://www.wsj.com/mdc/public/page/2_3021-gainnnm-gainer.html";
		return getNasdaqGainers(mostGainers);
	}
	
	public String getNasdaqGainers(String uri) throws IOException {
		fetchAndSave("NSDQ",uri);
		return this.filePath;
	}
	public String getNyseGained() throws IOException{
		mostGainers = "http://www.wsj.com/mdc/public/page/2_3021-gainnyse-gainer.html";
		return getNyseGained(mostGainers);
	}
	public String getNyseGained(String uri) throws IOException{
		fetchAndSave("NYSE",uri);
		return this.filePath;
	}
	
	private void saveStockQuote(String type,String allHtml) throws IOException {
		Calendar cal =Calendar.getInstance(); 
		String file = cal.get(Calendar.DATE) + "-" + cal.get(Calendar.HOUR)+ cal.get(Calendar.MINUTE);
		file = "C:/docs/quotes/ex/" + type+file + ".txt";
		System.out.println("saving to file"+file);
		File output = new File(file);
		if (!output.exists()) {
			output.createNewFile();
		}
		FileWriter writer = new FileWriter(output);
		writer.write(allHtml);
		writer.flush();
		this.filePath = file;
	}

	private void printWebPage(String webpageDetails) {
		System.out.println(webpageDetails);
	}

	public static void main(String arg[]) throws IOException {
		StockFetcher fetch = new StockFetcher();

		fetch.getNasdaqGainers();
		fetch.getNyseGained();
		
	}
}
