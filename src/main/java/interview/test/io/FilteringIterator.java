package interview.test.io;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class FilteringIterator implements Iterator {
 	MyObjectTest mytest;
	Object[] objects;
	int indx=0;
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	public FilteringIterator(Iterator other,MyObjectTest mytest) {
 		this.mytest=mytest;
		if(other instanceof FilteringIterator)
		{
			objects = ((FilteringIterator) other).objects;
		}
		else
		{
			List<Object> objs = new ArrayList<Object>();
			other.forEachRemaining(new Consumer() {
				@Override
				public void accept(Object t) {
					objs.add(t);
				}});
			objects= objs.toArray();
		}
	}

	@Override
	public boolean hasNext() {
		 return indx<objects.length;
	}

	@Override
	public Object next() {
		Object obj = objects[indx++];
		boolean pass = mytest.test(obj);
		if(pass) 
			return obj;
		else
			return next();
	}
}
