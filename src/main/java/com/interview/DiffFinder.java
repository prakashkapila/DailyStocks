package com.interview;

import java.util.Arrays;

public class DiffFinder {

	public void findDifference(int diff, int[] array) {
		Arrays.sort(array);
		
		System.out.println("supplied diff is "+diff);
		System.out.println("supplied sorted array is " );
		Arrays.stream(array).forEach( x-> System.out.print(x+" ,"));
		
		
 		StringBuilder sb = new StringBuilder();
		String templ = "\nDifference of array indexes [ & , % ] numbers equals the difference $";
		if ((array[array.length - 1] - array[0]) < diff) {
			System.out.println(" Difference of no 2 numbers equals the difference" + diff);
		}
		else {
			int j = 0;
			String str = "";
			inner:	for (int i = 0; i <= j; i++) {
				j=i+1;
				if( i > array.length /2)
					break inner;
				for(;j<array.length;)	{
 	 				if(array[j++] -array[i] == diff ) {
					str = 
					templ.replace("&", String.valueOf(j-1))
						.replace("%", String.valueOf(i))
						.replace("$", String.valueOf(diff));
						sb.append(str);
					  	continue inner;
				 	}
				}
		}
	}
		System.out.println(sb.toString());
 }
	
	public static void main(String arg[]) {
		DiffFinder df = new DiffFinder();
	 	df.findDifference(10, new int[] {34,45,26,36,44,24,14});
	}
}
