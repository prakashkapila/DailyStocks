package com.stocks.DailyStocks.vo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PriceVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4077077933159489936L;
	private String symbol;
	private Double openPrice;
	private Double highPrice;
	private Double lowPrice;
	private Double closingPrice;
	private Long volume;
	private double percent;
	private String time;
	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}

	private String dayMonth;
	private Date date;
	///
	private double price;
	 private double changedBy;
	 private String company;
	
	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public double getChangedBy() {
		return changedBy;
	}


	public void setChangedBy(double changedBy) {
		this.changedBy = changedBy;
	}


	public String getCompany() {
		return company;
	}


	public void setCompany(String company) {
		this.company = company;
	}


	///
	public void initHist(String[] strs)
	{
  //{"date":1529090728,"open":195.7899932861328,"high":197.07000732421875,"low":194.6699981689453,
			//"close":195.52999877929688,"volume":14805399,"adjclose":195.52999877929688},
	 
			String val = getVal(strs,0);
			setActTime(Long.valueOf(val));
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
	 		this.percent = ((this.closingPrice-this.openPrice)/this.openPrice)*100;
		 
	}

	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Double getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(Double openPrice) {
		this.openPrice = openPrice;
	}

	public Double getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(Double highPrice) {
		this.highPrice = highPrice;
	}

	public Double getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(Double lowPrice) {
		this.lowPrice = lowPrice;
	}

	public Double getClosingPrice() {
		return closingPrice;
	}

	public void setClosingPrice(Double closingPrice) {
		this.closingPrice = closingPrice;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDayMonth() {
		return dayMonth;
	}

	public void setDayMonth(String dayMonth) {
		this.dayMonth = dayMonth;
	}

	private void setActTime(Long valueOf) {
		Date actDate =new Date(valueOf*1000); 
		time = actDate.toString();
		Calendar cal = Calendar.getInstance();
		cal.setTime(actDate);
		this.dayMonth = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)+cal.get(Calendar.DAY_OF_MONTH) ;
		
	}

	private String getVal(String[] strs,int i) {
		String val = strs[i].substring(strs[i].indexOf(":")+1, strs[i].length());
		return val;
	}
}
