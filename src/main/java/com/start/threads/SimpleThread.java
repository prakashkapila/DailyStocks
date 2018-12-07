package com.start.threads;

public class SimpleThread {	
	
	public void startThreads() {
		WorkerThread t1 = new WorkerThread();
		t1.init();
		 WorkerThread  t2 = new WorkerThread();
		 t2.init();
		 AnotherThread  t3 = new AnotherThread();
		 new Thread(t3).start();
		 t2.start();
		 t1.start();
		 		
	}
public static void main(String args[]) {
 SimpleThread threads = new SimpleThread();
 threads.startThreads();
}
}

class AnotherThread implements Runnable{
	public void run() {
		 for(int i=0; i<10;i++) {
		System.out.println("From Another Thread Coming to "+getClass().getName()+hashCode());
	}
}
}
class WorkerThread extends Thread{
	public void init() {
		super.setName("My Htread");
	}
	 
	public void run() {
		 for(int i=1; i<10;i++) {
		System.out.println("Coming to "+this.getName()+this.getId());
	}
	}
}
