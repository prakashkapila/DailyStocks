package com.stocks.DailyStocks;

import java.io.Serializable;

public class StockKeys implements Serializable{
	
	private static final long serialVersionUID = -2398888370279877701L;
	int key;
	String keyName;
	String value;
	String fileName;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
