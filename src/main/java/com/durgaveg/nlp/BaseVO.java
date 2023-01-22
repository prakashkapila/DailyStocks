package com.durgaveg.nlp;

import java.util.LinkedList;

public class BaseVO {
	LinkedList<String> colNames = new LinkedList<>();

	public LinkedList<String> getColNames() {
		return colNames;
	}

	public void setColNames(LinkedList<String> colNames) {
		this.colNames = colNames;
	}
	
}
