package com.stocks.DailyStocks;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.spark.api.java.function.FlatMapFunction;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.stocks.DailyStocks.vo.PriceVO;

public class ContentMapper implements FlatMapFunction<String, PriceVO> {

	private static final long serialVersionUID = 3729341456806278850L;

	final String searchNodeKey,symbol;

	public ContentMapper(String node,String symbol) {
		this.searchNodeKey = node;
		this.symbol = symbol;
	}

	JsonObject getSearchNode(Map.Entry<String, JsonElement> obj, JsonObject ret) {
		if( ret != null)
			return ret;
		if (obj.toString().contains(searchNodeKey)) {
			if (obj.getKey().equalsIgnoreCase(searchNodeKey)) {
				ret = (JsonObject) obj.getValue();
				return ret;
			}
			if (obj.getValue().isJsonObject()) {
				ret = ret != null ? ret :
						getSearchNode(((JsonObject) obj.getValue()).entrySet(), ret);
			}
		}
		return ret;
	}

	private JsonObject getSearchNode(Set<Entry<String, JsonElement>> entrySet, JsonObject ret) {
		for( Entry<String, JsonElement> entry:entrySet)
		{
			ret = getSearchNode(entry, ret);
			if( ret != null)
				break;
		}
		return ret;
	}

	@Override
	public Iterator<PriceVO> call(String value) throws Exception {
		List<PriceVO> vo = map(value);
	 	return vo.iterator();
	}
	
	public List<PriceVO> map(String value) {
		if(value == null || value.length()<100)
			return null;
		JsonParser parser = new JsonParser();
		JsonObject ret = null;
 		ArrayList<PriceVO> vo = new ArrayList<PriceVO>();
		String str = (String) value;
		str = str.replace("root.App.main = ", "");
		str = str.replaceAll("(this);", "");
		JsonReader reader = new JsonReader(new StringReader(str));
		reader.setLenient(true);
		JsonObject y = (JsonObject) parser.parse(reader);
		y = getSearchNode(y.entrySet(), ret);
		fillSearchNode(y, vo,symbol);
		return vo;
	}
		
	private void fillSearchNode(JsonObject ret2, List<PriceVO> vo,String symbol) {
		if(ret2 == null)
			return;
		ret2.entrySet().forEach(json->vo.addAll(fillSymbol(json.getValue(),symbol)));
 	}
 
 	private List<PriceVO> fillSymbol(JsonElement json,String symbol) {
		List<PriceVO> volist = new ArrayList<PriceVO>();
		if(json.isJsonArray())
		{
			JsonArray arr = (JsonArray) json;
			arr.forEach(jsonelem->volist.add(fillSymbol(jsonelem.toString(),symbol)));
	 	}
		return volist;
	}

	private PriceVO fillSymbol(String string,String symbol) {
		PriceVO vo = new PriceVO();
		vo.setSymbol(symbol);
		vo.initHist(string.split(","));
		return vo;
	}

}
