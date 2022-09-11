package com.stocks.DailyStocks.vo;
 
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Row;
import org.bson.Document;

public class Converter {

	static Logger log = LogManager.getLogger(Converter.class);
	final static String OP="openPrice";
	final static String HP="highPrice";
	final static String LP="lowPrice";
	final static String CP="closingPrice";
	final static String VOL="volume";
	final static String PER="percent";
 	final static String TIME="time";
	final static String DAY="dayMonth";
	final static String SYM="symbol";
	
	public static PriceVO convert(Document x, PriceVO vo) {
		 	x.entrySet().forEach(entry->{
				switch(entry.getKey()) {
				case TIME:{
					vo.setTime((String)entry.getValue());
					break;
				}
				case DAY:{
					vo.setDayMonth((String)entry.getValue());
					break;
				}
				case SYM:{
					vo.setSymbol((String)entry.getValue());
					break;
				}
				case OP:{
					vo.setOpenPrice((Double)entry.getValue());
					break;}
				case HP:{
					vo.setHighPrice((Double)entry.getValue());
					break;}
				case LP:{
					
					vo.setLowPrice((Double)entry.getValue());
					break;}
				case CP:{
					vo.setClosingPrice((Double)entry.getValue());
					break;}
				case VOL:{
					vo.setVolume((Long)entry.getValue());
					break;}
				case PER:{
					vo.setPercent((Double)entry.getValue());
					break;}
				 }
				
			});
			return vo;
		 
	}
	static String getDay(Date date)
	{
		String ret = "";
		Calendar form = Calendar.getInstance();
		form.setTime(date);
		switch(form.get(Calendar.DAY_OF_WEEK))
		{
		case Calendar.SUNDAY: return "SUNDAY";
		case Calendar.SATURDAY: return "SATURDAY";
 		case Calendar.MONDAY: return "MONDAY";
		case Calendar.TUESDAY: return "TUESDAY";
		case Calendar.WEDNESDAY: return "WEDNESDAY";
		case Calendar.THURSDAY: return "THURSDAY";
		case Calendar.FRIDAY: return "FRIDAY";
		
		}
		return ret;
	}
	public static PriceVO convertRow(Row setRow ) throws ParseException
	{
		PriceVO vo = new PriceVO();
		System.out.println(setRow.mkString());
		log.info(setRow.mkString());
		//vo.setDate(DateFormat.getDateInstance().parse(setRow.getString(0) ));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		vo.setDate(sdf.parse(setRow.getString(0)));
		vo.setOpenPrice(Double.parseDouble(setRow.getString(1)));
		vo.setHighPrice(Double.parseDouble(setRow.getString(2)));
		vo.setLowPrice(Double.parseDouble(setRow.getString(3)));
		vo.setClosingPrice(Double.parseDouble(setRow.getString(4)));
		vo.setVolume(Long.parseLong(setRow.getString(6)));
		vo.setPercent(((vo.getClosingPrice()-vo.getOpenPrice())/vo.getClosingPrice())*100);
		vo.setDayMonth(getDay(vo.getDate()));
		return vo;
	}
	
}
