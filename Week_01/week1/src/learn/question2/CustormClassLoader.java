package learn.question2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内
容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。文件群里提供
 *
 */
public class CustormClassLoader extends ClassLoader{

	private Path classFilePath;
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
			byte[] bytes;
			try {
				bytes = Files.readAllBytes(classFilePath);
				 return defineClass(name,decode( bytes), 0, bytes.length);
			} catch (IOException e) {
				e.printStackTrace();
				throw new ClassNotFoundException();
			}
	}
	
	private byte[] decode(byte[] src) {
		byte[] decode=new byte[src.length];
		for(int i=0;i<src.length;i++) {
			decode[i]= (byte)(255-src[i]);
		}
		return decode;
	}

	public CustormClassLoader setClassFilePath(Path classFilePath) {
		this.classFilePath = classFilePath;
		return this;
	}

	

}
