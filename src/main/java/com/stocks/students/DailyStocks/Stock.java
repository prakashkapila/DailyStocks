package com.stocks.students.DailyStocks;

import java.io.Serializable;

public class Stock implements Serializable {
	private static final long serialVersionUID = 2587138397297841251L;
	private String ticker;
	private String exchange;
	private String url;
	private String name;
	
	private String file;
	private double price;
	private double priceChange;
	private double priceChangePercent;
	private String priceCurrency;
	 
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getPriceChange() {
		return priceChange;
	}
	public void setPriceChange(double priceChange) {
		this.priceChange = priceChange;
	}
	public double getPriceChangePercent() {
		return priceChangePercent;
	}
	public void setPriceChangePercent(double priceChangePercent) {
		this.priceChangePercent = priceChangePercent;
	}
	public String getPriceCurrency() {
		return priceCurrency;
	}
	public void setPriceCurrency(String priceCurrency) {
		this.priceCurrency = priceCurrency;
	}
 
	@Override
	 	public String toString() {
			StringBuilder sb = new StringBuilder();
			sb = sb.append(getExchange()).append(",").append(getTicker()).append(",").append(getPrice())
					.append(",").append(getPriceChange()).append(",").append(getPriceChangePercent())
					.append("\n");
			return sb.toString();
		}
	
	public String toString1() {
		return "Stock [ticker=" + ticker + ", exchange=" + exchange + ", url=" + url + ", price=" + price
				+ ", priceChange=" + priceChange + ", priceChangePercent=" + priceChangePercent + ", priceCurrency="
				+ priceCurrency + "]";
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public void setName(String val) {
		 this.name = val;
	}
	public String getName() {
		return name;
	}
}
