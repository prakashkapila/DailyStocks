package com.test.interview;

import java.util.List;
import java.util.ArrayList;

public class Exceptions {

	public void createRutimeException() {
		CreateRuntime run = new CreateRuntime();
//		try {
		run.createRuntime();
//		}catch(Myexception esp) {
//			System.out.println("Not reaching here");esp.printStackTrace();
//			}
	}
	
	public static void main(String arg[])
	{
		Exceptions exp = new Exceptions();
		exp.createRutimeException();
		List<RuntimeException> esp = new ArrayList<RuntimeException>();esp.add(new Myexception (""));
	}
}

class Myexception extends RuntimeException{
	public Myexception(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1L;
}

class CreateRuntime {
	double a[] = new double[10];
	public void createRuntime() throws Myexception {
		for(int i=0;i< 12;i++)
		{
			if(i> a.length-1)
			{
				throw new Myexception (" INDEX value is less than the passed value");
			}
			a[i] = Math.random()*100;
		}
	}
}