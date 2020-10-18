package learn.question2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("all")
public class Test {
	
	
public static void main(String[] args) {
	try {
		Path classPath=Paths.get("resource/Hello.xlass");
		Class<?>	cls=new CustormClassLoader().setClassFilePath(classPath).findClass("Hello");
	    Method method= cls.getDeclaredMethod("hello", null);
	    method.setAccessible(true);
	    method.invoke(cls.newInstance(), null);
	} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SecurityException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
		e.printStackTrace();
	}
	
 }
}
