package com.stocks.taxes;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfStatementReader {

	public void read() throws IOException {
		 //Loading an existing document
	      File file = new File("D:\\Innovative Expenses\\2020filing\\bofa\\5302\\pdf\\eStmt_2020-01-17.pdf");
	      PDDocument document = PDDocument.load(file);
 	      PDFTextStripper pdfStripper = new PDFTextStripper();
 	      String text = pdfStripper.getText(document);
//	      System.out.println(text);
	      document.close();
	}
	
	public static void main(String ard[]) throws IOException
	{
		PdfStatementReader reader = new PdfStatementReader();
		reader.read();
	}
}
