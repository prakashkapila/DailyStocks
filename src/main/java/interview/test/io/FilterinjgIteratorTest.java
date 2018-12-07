package interview.test.io;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class FilterinjgIteratorTest  {
@Test
	public void test() {
	 Iterator it = getInputObjects();
	 FilteringIterator test = new FilteringIterator(it,new MyObjectTest());
	 for(int i=0;i<30;i++)
	 {
		 System.out.println(test.next());
	 }
	}

private Iterator getInputObjects() {
 	List<String> objs = new ArrayList<String>();
	for(int i=0;i<100;i++) {
		objs.add(String.valueOf(Math.random()*1000));
		if(i%5 ==0)
			objs.add(null);
	}
 	Iterator it = objs.iterator();
	return it;
}
}
