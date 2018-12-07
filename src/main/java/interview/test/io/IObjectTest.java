package interview.test.io;

public interface IObjectTest {
	boolean test(Object obj);
}

class MyObjectTest implements IObjectTest{
 	@Override
	public boolean test(Object obj) {
 		return obj == null ?false:true;
	}} 