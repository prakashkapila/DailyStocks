package com.test.interview;

import java.util.concurrent.ConcurrentHashMap;

public class BalancedTreeConverter {
 ConcurrentHashMap mp;
 static final int HASH_BITS = 0x7fffffff; // usable bits of normal node hash
 
 static void spread(int... hash) {
	 System.out.println("HASH_BITS="+HASH_BITS);
	 
	int x=hash[0];
	int y = (3^3);
	 System.out.println("HASH_"+(x >>> 16)+"so="+y);
	 x =x ^( x >>> 16);

	 System.out.println("HASH_"+x);
	 System.out.println("AND="+(HASH_BITS & x));
 }
 public static void main(String[] arg)
 {
	 spread(64);
 }
}
