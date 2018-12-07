package com.stocks.students.DailyStocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.stocks.DailyStocks.StockKeys;

public class Transformer {
	public static Stock convert(Iterable<StockKeys> sk) {
		Stock ret = new Stock();
			List<StockKeys> keylist= new ArrayList<StockKeys>();
			sk.forEach(new Consumer<StockKeys>() {
				@Override
			public void accept(StockKeys t) {
					keylist.add(t);
			}});
			for(int i=0;i<keylist.size()-1;i++) {
				String key = keylist.get(i).getValue();
				String val = keylist.get(i+1).getValue();
				if(key == null || key.equalsIgnoreCase(""))
				{continue;}
				switch(key) {
				case ("tickerSymbol"):{
					ret.setTicker(val);
					break;
				}
				case ("exchange"):{
					ret.setExchange(val);
					break;
				}
				case("price"):{
					ret.setPrice(Double.valueOf(val));
					break;
				}
				case("priceChange"):{
					ret.setPrice(Double.valueOf(val));
					break;
				}
				case("priceChangePercent"):{
					ret.setPrice(Double.valueOf(val));
					break;
				}
				case("name"):{
					ret.setName(val);
					break;
				}
				}
			}
			return ret;
	}

}
