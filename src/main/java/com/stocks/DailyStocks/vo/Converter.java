package com.stocks.DailyStocks.vo;
 
import org.bson.Document;

public class Converter {

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
}
