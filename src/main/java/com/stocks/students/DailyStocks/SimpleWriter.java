package com.stocks.students.DailyStocks;

import java.io.FileWriter;
import java.io.IOException;

public class SimpleWriter {
	static FileWriter writer = null;
	int written =0;
	static void close() throws IOException{
		writer.close();
	}
	
	static void setWriter(String fileName, String heading) throws IOException{
		writer = new FileWriter(fileName);
		writer.write(heading+"\n");
		writer.flush();
	}
	
	public void writeObject(Stock t) throws Exception {
		writer.write(t.toString() );
		writer.write("\n");
		if(written++ ==10000) {
			writer.flush();
			written=0;
		}
	}
}
