package com.stocks.DailyStocks.vo;

import java.io.Serializable;
 
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
 

/**
 * Hello world!
 *
 */
public class SymbolQuoteVO implements Serializable 
{
	 
	
	private static final long serialVersionUID = -1431027195343391925L;
	private String symbol;
	private double price;
	private String time;
	private double percent;
	private double changedBy;
	private long volume;
	private String company;
	private String currentStamp;
	private String exchange;
	private double closingPrice;
	private double highPrice;
	private double lowPrice;
 	private double openPrice;
	
 	public void setVolume(long volume) {
		this.volume = volume;
	}
	public double getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(double openPrice) {
		this.openPrice = openPrice;
	}
	public double getLowPrice() {
		return lowPrice;
	}
	public void setLowPrice(double lowPrice) {
		this.lowPrice = lowPrice;
	}
	
	public double getHighPrice() {
		return highPrice;
	}
	public void setHighPrice(double highPrice) {
		this.highPrice = highPrice;
	}
	
	
	public double getClosingPrice() {
		return closingPrice;
	}
	public void setClosingPrice(double closingPrice) {
		this.closingPrice = closingPrice;
	}
	public void setCurrentStamp()
	{
	 	java.time.Instant ins= Instant.now();
		this.currentStamp=ins.toString();
	}
	public void setCurrentStamp(String str)
	{
		this.currentStamp+=str;
	}
	
	public void setCurrentStamp(String price,String date)
	{
		
		this.currentStamp+=(price+","+date);
	}
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		if(symbol.contains("symbol")) {
		String vals[] = symbol.substring(symbol.indexOf("symbol")).split("=");
		if(vals.length > 0  ) {
		this.symbol = vals[1].substring(0, vals[1].indexOf("class"));
		this.company=vals[4].substring(vals[4].indexOf(">")+1);
		}
		}
		else
			this.symbol = symbol;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(String string) {
		int start =0;
		start = string.contains("$")?string.indexOf("$")+1:string.indexOf(">")+1;
		string = string.substring(start,string.indexOf("</"));
		this.price = Double.valueOf(string);
	}
	public String getTime() {
		return time;
	}
	public void setTime(long l)
	{
		Date dt = new Date(Calendar.getInstance().getTimeInMillis()+l);
		dt.setTime(l);
		this.time = new Date(l*1000).toString();
	}
	public void setTime() {
		Calendar time = Calendar.getInstance();
		this.time = new StringBuilder().
				append(time.get(Calendar.DATE))
				.append("-").append(time.get(Calendar.HOUR_OF_DAY))
				.append(":").append(time.get(Calendar.MINUTE))
				.toString();
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	public double getChangedBy() {
		return changedBy;
	}
	public void setChangedBy(String string) {
		string = string.substring(string.indexOf(">")+1,string.indexOf("</"));
		this.changedBy = Double.valueOf(string);
	}
	public double getVolume() {
		 return volume;
	}
	public void setVolume(double volume) {
	 		this.volume = Double.valueOf(volume).longValue();
	}
	public void setVolume(String string) {
		string = string.substring(string.indexOf(">")+1,string.indexOf("</"));
		string = string.replace(",", "");
		this.volume = Double.valueOf(string).longValue();
	}
	private void setPercent(String string) {
	 	string = string.substring(string.indexOf(">")+1,string.indexOf("</"));
		this.percent = Double.valueOf(string);	
	}
	private String getVal(String[] strs,int i) {
		String val = strs[i].substring(strs[i].indexOf(":")+1, strs[i].length());
		return val;
	}
	public void initHist(String[] strs)
	{
  //{"date":1529090728,"open":195.7899932861328,"high":197.07000732421875,"low":194.6699981689453,
			//"close":195.52999877929688,"volume":14805399,"adjclose":195.52999877929688},
			
		 	int i=0;
			String val = getVal(strs,0);
			setTime(Long.valueOf(val));
			val = getVal(strs,1);
			this.openPrice = Double.valueOf(val);
			val = getVal(strs,2);
			this.highPrice = Double.valueOf(val);
			val = getVal(strs,3);
			this.lowPrice = Double.valueOf(val);
			val = getVal(strs,4);
			this.closingPrice =  Double.valueOf(val);
			val = getVal(strs,5);
	 		this.volume = Long.valueOf(val);
	 		this.percent = ((this.openPrice-this.closingPrice)/this.openPrice)*100;
		 
	}
	public void init(Iterable<String> iterableStrings,String exchange) {
		java.util.Iterator<String> it = iterableStrings.iterator();
		int i=0;
		String str="";
		setTime();
		setCurrentStamp();
		this.exchange = exchange;
		do {
			str= it.next();
			if(checkString(str)) {
				switch(i++) {
				case 0:
				{
					setSymbol(str);
					break;
				}
				case 1:{
					setPrice(str);
					break;
				}

				case 2:{
					setChangedBy(str);
					break;
				}

				case 3:{
					setPercent(str);
					break;
				}

				case 4:{
					setVolume(str);
					break;
				}
				}
			}
		}while (it.hasNext());
	}

	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCurrentStamp() {
		return currentStamp;
	}
	public String getExchange() {
		return exchange;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public void setChangedBy(double changedBy) {
		this.changedBy = changedBy;
	}
	private void setExchange(String exchange) {
			this.exchange = exchange;
	}
	private boolean checkString(String str) {
		boolean ret = false;
		if(str == null || str.trim().equals(""))
			return false;
		if(str.indexOf("num")>0 )
			return true;
		if(str.indexOf("symbol")>0)
			ret= true;
		return ret;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(symbol).append(",")
		.append(this.changedBy).append(",")
		.append(percent).append(",")
		.append(volume).append(",")
		.append(price).append(",")
		.append(time);
 	return sb.toString();
	}
public String retrieveMetaData() {
	
	return	new StringBuilder().append("symbol").append(",changedBy").append(",percent").append(",volume").append(",price").append(",time").toString();
		
	}
}
