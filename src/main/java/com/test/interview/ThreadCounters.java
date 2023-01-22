package com.test.interview;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ThreadCounters  {
	Odd odd,even,zero;
	//Even even;
	 void start() throws InterruptedException {
		 odd = new Odd(-1,true);
		 even = new Odd(0,false);
		 zero = new Odd(-10,false);
		 List<Callable<?>> tasks = new ArrayList<>();
		 tasks.add(even);
		 tasks.add(odd);
		 tasks.add(zero);
		ExecutorService exe = Executors.newCachedThreadPool();
		exe.invokeAll(tasks);
		
	}
	public static void main(String args[]) throws InterruptedException
	{
		new ThreadCounters().start();
	}
}
class Odd implements Callable<Integer>{
	int i;
	boolean oddFlag;
	final String name;
	int zero =-10;
	public Odd(int j,boolean odd)
	{
		name =odd?"Odd THread":j==zero?"ZERO Thread":"Even Thread";
		this.i=j;
		oddFlag = odd;
	}
	
	@Override
	public Integer call() throws Exception {
		 while(i<Integer.MAX_VALUE) {
			 Thread.sleep(2000);
			if(name.contains("ZERO"))
			{
				System.out.println(name +"THread Value of zero is "+(i-zero));
			}
			//for (int i=1;i<Integer.MAX_VALUE;i++) {
			else if(i==-1 || i%2 >0)
				{
					i+=2;
					System.out.println(name +"THread Value of Odd is "+i);
				}
				else if(i%2 ==0)
				{
					System.out.println(name +"THread Value of even is "+i);
					i+=2;
				}
				
		}
		return null;
	}
	
}

