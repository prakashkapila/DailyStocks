package org.durgaveg.com.veges;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

public class UrlPriceFetcher {
	String urlToFetch="https://www.bigbasket.com/pc/fruits-vegetables/fresh-vegetables/";
	String mostLosers="";
	//http://www.wsj.com/mdc/public/page/2_3021-losennm-loser.html
	//http://www.wsj.com/mdc/public/page/2_3021-gainnnm-gainer-20180410.html /*April 10*/
	//http://www.wsj.com/mdc/public/page/2_3021-losennm-loser-20180416.html
	// "https://finance.yahoo.com/gainers";
	String filePath = "";
	
	private String fetchUrl(String exchange,String urlstr) throws IOException {
		urlToFetch = urlstr;
		return fetchUrl();
	}
	private String fetchUrl() throws IOException{
		URL url = new URL(urlToFetch);
	 	CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
  		URLConnection conn = (URLConnection) url.openConnection();
  		conn.connect();
		StringBuilder webpageDetails = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		do {
			webpageDetails.append(reader.readLine());
			webpageDetails.append("\n");
		} while (reader.ready());
 		return webpageDetails.toString();
	}
	
	private void saveStockQuote(String exchange,String allHtml) throws IOException {
		Calendar cal =Calendar.getInstance(); 
		String file = cal.get(Calendar.DATE) + "-" + cal.get(Calendar.HOUR)+ cal.get(Calendar.MINUTE);
		file = "C:/docs/quotes/ex/" + exchange+file + ".txt";
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

	public static void main(String arg[]) throws IOException
	{
		UrlPriceFetcher fetcher = new UrlPriceFetcher();
		fetcher.printWebPage(fetcher.fetchUrl());
	}
}
