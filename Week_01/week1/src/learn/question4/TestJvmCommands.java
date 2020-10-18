package learn.question4;

import java.util.concurrent.TimeUnit;

/**
 * 
 *检查一下自己维护的业务系统的 JVM 参数配置，用 jstat 和 jstack、jmap 查看一下
 详情，并且自己独立分析一下大概情况，思考有没有不合理的地方，如何改进
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
