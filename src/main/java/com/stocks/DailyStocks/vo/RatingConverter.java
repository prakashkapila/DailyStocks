package com.stocks.DailyStocks.vo;

import java.io.Serializable;

import org.apache.spark.sql.Row;

public class RatingConverter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6566422033354757459L;

	public static Rating convert(Row row) {
		
		Rating rat = new Rating();rat.parseRating(row.getString(0));
		return rat;
	}
}
