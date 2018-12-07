package com.interview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleExcercise {
	//[1,1,3,5,5,6,4,7,8,9];
	
	public void printMissing(int x[])
	{
		Arrays.sort(x);
		int num = 1;
		StringBuilder sb = new StringBuilder();
		sb.append("THe number missing is ");
		
		for(int i=0;i<x.length;i++)
		{
			if(x[i]<1) { 
				continue;
			}
			if(x[i] == num) {
				continue;
			}
			if(x[i] > num+1)
			{
				System.out.println("The missing number "+num);
			}
		}
	}
	public boolean isPalindorme(int input) {
		boolean ret = false;
		List<Integer> individuals = fillNumbers(input);
		boolean checkNumbers = checkNumbers(individuals);
		StringBuilder sb = new StringBuilder()
				.append("The number ")
				.append(input)
				.append((checkNumbers ?"is":"is not"))
				.append("Palindrome");
		System.out.println(sb.toString());
		return ret;
	}
	private List<Integer>  fillNumbers(int input) {
		// TODO Auto-generated method stub
		// add numbers individually to list;
		List<Integer> listNums = new ArrayList<Integer>();
		int res = input;
		while(res > 10) {
			listNums.add(res % 10);
			res = res/10;
		}
		listNums.add(res);
		return listNums;
	}
	public boolean checkNumbers(List<Integer> numbs) {
		boolean ret = true;
		int size = numbs.size()-1;
		for(int i=0;i<numbs.size();i++)
		{
			if(i<size)
				ret = numbs.get(i) == numbs.get(size--);
		}
		return ret;
	}

	public static void test() {
		SampleExcercise se = new SampleExcercise();
		se.isPalindorme(343);
		for(int i=0;i<10;i++) {
			se.isPalindorme(Math.round(i)*100);
		}
	}
	public static void main(String arg[])
	{
//		SampleExcercise se = new SampleExcercise();
//		int x[] = new int[] {1,1,3,5,5,6,4,7,8,9};
//		se.printMissing(x);
	//se.isPalindorme(1);
//	for(int i=0;i<10;i++) {
//		se.isPalindorme(Math.round(i)*100);
//	}
		HashExcercise he = new HashExcercise();
		he.loop();
	}
}

class HashExcercise {
	
	public void loop() {
		for (int i=0;i<5;i++)
		{
			long val = 0x1F;
			long segment = i * 1000 & 0x1F;
			System.out.println("val = "+val+"segment = "+segment);
			doAnd() ;
		}
	}
	public long doAnd() {
		long hash = System.currentTimeMillis()*31;
		long hash1 = (hash << 7) - hash + (hash >>> 9) + (hash >>> 17);
		int[] table = new int[32];
		long indx = hash1 & table.length -1;
		System.out.println("hash = "+hash+"hash1 = "+hash1+"indx = "+indx+"table.length="+(table.length-1));
		return indx;
	}
}
