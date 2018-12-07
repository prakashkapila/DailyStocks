package com.test.interview;

import java.awt.Button;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class ForEach {
List<Integer> rands = new ArrayList<Integer>();

@SuppressWarnings("unlikely-arg-type")
public void test() {
	int y = 128 >>1;
	System.out.print("y = "+y);
	System.out.println("y = "+(128 >>>1));
	System.out.println("y = "+(128 >>1));
	System.out.println("y = "+(128 >>1));
	System.out.println("y = "+(128 >>>1));
	
}
public void putVaues() {
	for(int i=0;i< 1000;i++)
	{
		rands.add(Double.valueOf(Math.random()*100).intValue());
	}
}
public void printValues() {

//	rands.forEach(new Consumer<Integer>() {
//		public void accept(Integer t) {
//			System.out.println("Va is "+t);
//		}});
	
	rands.forEach(new MyConsumer());
	}
	public static void main(String arg[]) {
		ForEach each = new ForEach() {
 		@Override
			public void add() {
				// TODO Auto-generated method stub
				
			}};
		each.putVaues();
		each.printValues();
		int y = 128 >>1;
		System.out.println("y1 = "+y);
		//System.out.println("y1 = "+""+(byte) (128 >>1)).byteValue());
		System.out.println("y2 = "+(128 >>12));
		System.out.println("y3 = "+(-128 <<1));
		System.out.println("y4 = "+(128 >>>2));
		System.out.println("john"=="john");
		Double x= 3.5;
		Byte b = (byte)256;
		String temp[] = {"a" ,"b", "c"};
		boolean x1 =2==3;
		if(2 !=3) {System.out.println("some");}
	}
	public   abstract void add();
	//public virtual add();
}
class MyConsumer implements Consumer<Integer>{
	public void accept(Integer t) {
		System.out.println("Va is "+t);
	}
}