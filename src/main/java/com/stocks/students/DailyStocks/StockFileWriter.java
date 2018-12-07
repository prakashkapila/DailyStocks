package com.stocks.students.DailyStocks;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.spark.api.java.function.VoidFunction;

import com.stocks.DailyStocks.vo.SymbolQuoteVO;

public class StockFileWriter extends SimpleWriter implements VoidFunction<SymbolQuoteVO>{
	private static final long serialVersionUID = -5500741082324409218L;
	 public void call(SymbolQuoteVO t) throws Exception {
		writer.write(t.toString() + "\n");
		writer.flush();
	}
}