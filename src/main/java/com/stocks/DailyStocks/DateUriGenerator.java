package com.stocks.DailyStocks;

import java.util.function.Consumer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class DateUriGenerator {
	static final int NSDQG = 0;
	static final int NYSEG = 1;
	static final int NYSEL = 2;
	static final int NSDQL = 3;
	static final boolean DEBUG = true;
	boolean current = false;
	

	
	public String[] collectUris(int date,int month,String year,String[] uris ) {
		
		StringBuilder datet = new StringBuilder().append(year)
				.append(month < 9 ?"0"+(month+1):month+1)
		.append(date <9 ? "0"+date:""+date); 
		
		uris[NSDQG] = "http://www.wsj.com/mdc/public/page/2_3021-gainnnm-gainer-"+datet+".html"; // nasdaq gained
		uris[NYSEG] = "http://www.wsj.com/mdc/public/page/2_3021-gainnyse-gainer-"+datet+".html"; // nyse gained
		uris[NSDQL] ="http://www.wsj.com/mdc/public/page/2_3021-losennm-loser-"+datet+".html";
		uris[NYSEL] ="http://www.wsj.com/mdc/public/page/2_3021-losenyse-loser-"+datet+".html";
		if(current) {
			uris[NSDQG] = "http://www.wsj.com/mdc/public/page/2_3021-gainnnm-gainer.html"; // nasdaq gained
			uris[NYSEG] = "http://www.wsj.com/mdc/public/page/2_3021-gainnyse-gainer.html"; // nyse gained
			uris[NYSEL] = "http://www.wsj.com/mdc/public/page/2_3021-losenyse-loser.html"; // nyse lost
			uris[NSDQL] = "http://www.wsj.com/mdc/public/page/2_3021-losennm-loser.html";
		}
		return uris;
	}
	
	
	public NavigableMap<Long,List<String>> collect6Months() {
		ArrayList<String> dates = new ArrayList<String>();
		TreeMap<Long,List<String>> ret = new TreeMap<Long,List<String>>(); 
		 Calendar cal = Calendar.getInstance();
		//cal.add(Calendar.DAY_OF_MONTH, 1);
		Calendar halfYearly = Calendar.getInstance();
		halfYearly.add(Calendar.MONTH, -6);
	 	for(Calendar cals = halfYearly;cal.getTime().after(cals.getTime());cals.add(Calendar.DAY_OF_MONTH, 1))
		{
	 		if(cals.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||cals.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
	 			continue;
			ret.put(cals.getTimeInMillis(),Arrays.asList(collectUris(cals)));
		}
	 	return ret;
	}
	public String[] collectUris(Calendar date) {
		String[] uris = new String[4];
		
		collectUris(date.get(Calendar.DATE),date.get(Calendar.MONTH),String.valueOf(date.get(Calendar.YEAR)),uris);
	
		if(DEBUG) {
		Arrays.asList(uris).stream().forEach(new Consumer<String>() {
			@Override
			public void accept(String t) {
				System.out.println(t);	
			}});
	}
		return uris;
	}
	
	public static void main(String[] args) {
		DateUriGenerator mgr = new DateUriGenerator();
		mgr.collect6Months();
	}
}
