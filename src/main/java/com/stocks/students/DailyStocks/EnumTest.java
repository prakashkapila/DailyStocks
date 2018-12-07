package com.stocks.students.DailyStocks;

import java.util.EnumSet;

public class EnumTest {
enum VAT {STOCKS, BONDS, INDEXFUNDS};
enum ANOT {ANOTHER,SOMETHING,NOTHING};
enum POWER{ ADD, MULTIPLY,DIVIDE,SQUARE,CUBE,algebra};
VAT type = VAT.STOCKS;
ANOT anot;
public void findType(String str)
{
	VAT another = VAT.valueOf(str);
	System.out.println("VAT type is "+type.toString());
	System.out.println("VAT type Compare"+type.compareTo(another));
	System.out.println("VAT type Compare"+VAT.BONDS.compareTo(another));
	
}
public void doCalc(POWER pow,double...values) {
	
	switch(pow) {
	case ADD:{
		System.out.println("ADD IS"+values[0]+values[1]);
		break;
	}
	case MULTIPLY:{
 		System.out.println("MULTIPLY IS"+values[0]*values[1]);
 		break;
	}
	case SQUARE:{
		System.out.println("SQUARE IS"+Math.pow(values[0],2));
 		System.out.println("SQUARE IS"+Math.pow(values[1],2));
 		break;
	}
	case CUBE:{
		System.out.println("CUBE IS"+Math.pow(values[0],3));
 		System.out.println("CUBE IS"+Math.pow(values[1],3));
 		break;
	}
	}
}
public void addSet() {
	EnumSet<VAT> any = EnumSet.of(VAT.STOCKS, VAT.BONDS,VAT.INDEXFUNDS);
	if(any.contains(VAT.INDEXFUNDS))
	{
		System.out.println(any+" contains"+VAT.INDEXFUNDS);
	}
	//EnumSet<?> any2 =EnumSet.of(ANOT.ANOTHER,ANOT.NOTHING,ANOT.SOMETHING);
	
}
public static void main(String arg[])
{
	EnumTest test = new EnumTest();
	test.addSet();
	test.findType("STOCKS");
	test.findType("BONDS");
	test.findType("INDEXFUNDS");
	//test.findType("ANOTH");
	test.doCalc(POWER.MULTIPLY, 3.5,5.5);
}
}
