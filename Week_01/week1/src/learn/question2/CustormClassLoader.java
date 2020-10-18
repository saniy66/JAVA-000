package learn.question2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * �Զ���һ�� Classloader������һ�� Hello.xlass �ļ���ִ�� hello ���������ļ���
����һ�� Hello.class �ļ������ֽڣ�x=255-x���������ļ����ļ�Ⱥ���ṩ
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
