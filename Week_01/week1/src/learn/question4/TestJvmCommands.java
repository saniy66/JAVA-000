package learn.question4;

import java.util.concurrent.TimeUnit;

/**
 * 
 *���һ���Լ�ά����ҵ��ϵͳ�� JVM �������ã��� jstat �� jstack��jmap �鿴һ��
 ���飬�����Լ���������һ�´�������˼����û�в�����ĵط�����θĽ�
 */
public class TestJvmCommands {

	public static void main(String[] args) throws InterruptedException {
		
		int loop=0;
		while(true) {
			loop++;
			System.out.println("loop times: "+loop);
			TimeUnit.SECONDS.sleep(5);
		}
	}
}
