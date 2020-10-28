

##### Wrk安装



```bash
git clone https://github.com/wg/wrk.git  
cd wrk  
make  
```

##### 开始测试一下

```cpp
wrk -t12 -c100 -d30s http://www.baidu.com  
```

-------

**sb使用**



> java -jar -Xmx1g -Xms1g gateway-server-0.0.1-SNAPSHOT.jar

![image-20201028191536324](F:\gitee\java-core-notes\简化\web基础\image-20201028191536324.png)

![image-20201028191643354](wrk、sb记录和总结.assets\image-20201028191643354.png)





> sb -u http://localhost:8088/api/hello -c 20 -N 60

执行完后，会自动出现如下界面：下面的曲线会不断在变化

**执行完的信息：**

![image-20201028192023752](wrk、sb记录和总结.assets\image-20201028192023752.png)



![image-20201028191951139](F:\gitee\java-core-notes\简化\web基础\image-20201028191951139.png)



**现在正式进行测试**

> jps -lvm



![image-20201028192503424](wrk、sb记录和总结.assets\image-20201028192503424.png)



> jvisualvm

![image-20201028192724882](wrk、sb记录和总结.assets\image-20201028192724882.png)

![image-20201028192735266](wrk、sb记录和总结.assets\image-20201028192735266.png)

![image-20201028192904208](wrk、sb记录和总结.assets\image-20201028192904208.png)

**只看内存**

![image-20201028193004658](wrk、sb记录和总结.assets\image-20201028193004658.png)





再压一下：

> sb -u http://localhost:8088/api/hello -c 20 -N 60

![image-20201028193552464](wrk、sb记录和总结.assets\image-20201028193552464.png)

执行完后：

![image-20201028193642966](wrk、sb记录和总结.assets\image-20201028193642966.png)





![image-20201028193729955](wrk、sb记录和总结.assets\image-20201028193729955.png)



**分析**：



![image-20201028193903432](wrk、sb记录和总结.assets\image-20201028193903432.png)

![image-20201028193846620](wrk、sb记录和总结.assets\image-20201028193846620.png)

![image-20201028193935856](wrk、sb记录和总结.assets\image-20201028193935856.png)



压测前：

![image-20201028194121951](wrk、sb记录和总结.assets\image-20201028194121951.png)

压测后：

![image-20201028194058193](wrk、sb记录和总结.assets\image-20201028194058193.png)



吞吐量： 有上升趋势【结果和预期的不一样，和直播课件演示一样】

----------

 **使用飞行记录加速资源的消耗，演示效果**

> jmc

![image-20201028194509041](wrk、sb记录和总结.assets\image-20201028194509041.png)

![image-20201028194533570](wrk、sb记录和总结.assets\image-20201028194533570.png)





![image-20201028194634098](wrk、sb记录和总结.assets\image-20201028194634098.png)



> sb -u http://localhost:8088/api/hello -c 20 -N 60

![image-20201028194925970](wrk、sb记录和总结.assets\image-20201028194925970.png)





![image-20201028194957638](wrk、sb记录和总结.assets\image-20201028194957638.png)





![image-20201028195150618](wrk、sb记录和总结.assets\image-20201028195150618.png)



**可以启动不同的GC参数来观察**

--------------------------------

**测试结果统计**

**串行GC**

> java -jar -Xms512m -Xmx512m -XX:+UseSerialGC -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps gateway-server-0.0.1-SNAPSHOT.jar

![image-20201028200438606](wrk、sb记录和总结.assets\image-20201028200438606.png)

![image-20201028200636920](wrk、sb记录和总结.assets\image-20201028200636920.png)

![image-20201028200859318](wrk、sb记录和总结.assets\image-20201028200859318.png)

**并行GC**

> java -jar -Xms512m -Xmx512m -XX:+UseParallelGC -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps gateway-server-0.0.1-SNAPSHOT.jar

![image-20201028201124409](wrk、sb记录和总结.assets\image-20201028201124409.png)

![image-20201028201012091](wrk、sb记录和总结.assets\image-20201028201012091.png)



![image-20201028201026399](wrk、sb记录和总结.assets\image-20201028201026399.png)





**CMS GC**

> java -jar -Xms512m -Xmx512m -XX:+UseConcMarkSweepGC -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps gateway-server-0.0.1-SNAPSHOT.jar

![image-20201028201142294](wrk、sb记录和总结.assets\image-20201028201142294.png)



![image-20201028201213442](F:\gitee\java-core-notes\简化\web基础\image-20201028201213442.png)

![image-20201028201229910](wrk、sb记录和总结.assets\image-20201028201229910.png)

**G1 GC**

> java -jar -Xms512m -Xmx512m -XX:+UseG1GC -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps gateway-server-0.0.1-SNAPSHOT.jar

![image-20201028201311158](wrk、sb记录和总结.assets\image-20201028201311158.png)



![image-20201028201433302](wrk、sb记录和总结.assets\image-20201028201433302.png)



![image-20201028201442320](wrk、sb记录和总结.assets\image-20201028201442320.png)



初步根据测试结果：  此次测试，GC 策略的效果比较，并行GC的吞吐量< CMS的吞吐量< G1的吞吐量

**而串行GC的吞吐量却是最高的。** 



我的分析：串行GC 对单核CPU来说，简单高效，没有上下文之间的的切换。  可能这和应用中的测试接口是hello有关，不涉及IO操作，和较复杂的计算。 即 比较简单的程序（无或轻IO+计算简单），多核CPU的优势并没有得到充分体现。 【也许只是随机现象】



------





