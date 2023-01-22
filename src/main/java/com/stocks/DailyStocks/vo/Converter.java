package com.stocks.DailyStocks.vo;
 
import java.util.Date;
import java.util.Locale;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Row;
import org.bson.Document;

public class Converter implements Serializable {

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
	public static String SYMBOL="";
	public static String COMP="";
	
	public static PriceVO convert(Document x, PriceVO vo) {
		 	x.entrySet().forEach(entry->{
				switch(entry.getKey()) {
				case TIME:{
				//	vo.setTime((String)entry.getValue());
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
	static int getDay(Date date)
	{
		String ret = "";
		Calendar form = Calendar.getInstance();
		form.setTime(date);
		if(form.get(Calendar.DAY_OF_WEEK)== 7)
		{
			log.info("Weekend is coming");
		}
		return form.get(Calendar.DAY_OF_WEEK);
		 
	}
	public static Date getDate(String[] date)
	{
		//yyyy-mm-dd
		Date dated = null;
		Calendar cal = Calendar.getInstance(Locale.US);
		cal.set(Calendar.YEAR, Integer.parseInt(date[0]));
		cal.set(Calendar.MONTH, Integer.parseInt(date[1])-1);
		cal.set(Calendar.DATE, Integer.parseInt(date[2]));
		dated = cal.getTime();
		return dated;
	}
	public static PriceVO convertRow(Row setRow ) throws ParseException
	{
		PriceVO vo = new PriceVO(); 
	//	log.info(setRow.mkString());
		vo.setDateStr(setRow.getString(0));
		//vo.setDate(DateFormat.getDateInstance().parse(setRow.getString(0) ));
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		//vo.setDate(sdf.parse(setRow.getString(0)));
		vo.setSymbol(SYMBOL);
		vo.setCompany(COMP);
		vo.setDate(getDate(vo.getDateStr().split("-")));
		vo.setOpenPrice(Double.parseDouble(setRow.getString(1)));
		vo.setHighPrice(Double.parseDouble(setRow.getString(2)));
		vo.setLowPrice(Double.parseDouble(setRow.getString(3)));
		vo.setClosingPrice(Double.parseDouble(setRow.getString(4)));
		vo.setPrice(vo.getClosingPrice());
		vo.setVolume(Long.parseLong(setRow.getString(6)));
		String openPerc=setRow.getString(7);
		openPerc = openPerc==null ? "0.0":openPerc;
		vo.setOpenPercent(Double.parseDouble(openPerc));
		vo.setPercent(((vo.getClosingPrice()-vo.getOpenPrice())/vo.getClosingPrice())*100);
		vo.setDayOfWeek(getDay(vo.getDate()));
		vo.setChangedBy(vo.getClosingPrice()-vo.getOpenPrice());
		vo.setTotalPercent(vo.getOpenPercent()+vo.getPercent());
		return vo;
	}
	
	
}
