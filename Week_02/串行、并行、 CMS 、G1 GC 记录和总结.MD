java -XX:+PrintGCDetails GCLogAnalysis

java -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

1）模拟一下 OOM，java -Xmx128m -XX:+PrintGCDetails GCLogAnalysis

2）分别使用 512m,1024m,2048m,4086m,观察 GC 信息的不同

------

**编译GCLogAnalysis.java文件**

> javac -encoding GBK GCLogAnalysis.java



> java -XX:+PrintGCDetails GCLogAnalysis

![image-20201028174701147](串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028174701147.png)





> java -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

![image-20201028174831311](串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028174831311.png)

![image-20201028180312070](串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028180312070.png)



jdk 8 物理内存的1/4  



> java -Xmx128m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

![image-20201028181633126](串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028181633126.png)





> java -Xmx512m -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

![image-20201028181825451](串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028181825451.png)

![image-20201028181925130](串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028181925130.png)

不再报错，但GC次数很多



> java -Xmx1024m -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

 ![image-20201028182141742](串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028182141742.png)

**GC次数相对于512m时，减少。 说明增大内存，可以降低OOM概率**

生成对象数增大，可以一定程度上表明程序执行效率增大【不严谨，因为程序内部，是随机的对象生成】





----------

**串行GC**

###### java -XX:+UseSerialGC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

![image-20201028182416909](串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028182416909.png)



![image-20201028183912253](串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028183912253.png)



----

**并行GC**

###### java -XX:+UseParallelGC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

![image-20201028183635623](串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028183635623.png)



![image-20201028183711127](串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028183711127.png)



------

**CMS GC**

###### java -XX:+UseConcMarkSweepGC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

![image-20201028183729515](串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028183729515.png)

![image-20201028183802007](串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028183802007.png)



------

**G1 GC**

###### java -XX:+UseG1GC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

![image-20201028183827951](串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028183827951.png)

![image-20201028183848815](串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028183848815.png)

---------



**小结：**

**CMS 的阶段：**

Concurrent Marking（并发标记）

阶段 1: Initial Mark（初始标记）

阶段 2: Root Region Scan（Root区扫描）

阶段 3: Concurrent Mark（并发标记）

阶段 4: Remark（再次标记）

阶段 5: Cleanup（清理）



**工具**

**GCEasy**

**GCViewer**



**总结：**

**1、如何查看/分析不同 GC 配置下的日志信息？**

通过GC工具和JVM 命令方式，具体命令如下：

**串行GC**

> java -XX:+UseSerialGC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis



**并行GC**

> java -XX:+UseParallelGC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

**CMS GC**

> java -XX:+UseConcMarkSweepGC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

**G1 GC**

> java -XX:+UseG1GC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

-----

**2、各种 GC 有什么特点和使用场景？**

#### **串行GC特点和场景**

##### Serial收集器（新生代）

特点：采用复制算法进行垃圾收集。Serial进行垃圾收集时，不仅只用一条单线程执行垃圾收集工作，它还在收集的同时，所用的用户必须暂停。用户体验感极差。优势：简单高效，由于采用的是单线程的方法，因此与其他类型的收集器相比，对单个cpu来说没有了上下文之间的的切换，效率比较高。



适用场景：Client 模式（桌面应用）；单核服务器。

-XX:+UserSerialGC #选择Serial作为新生代垃圾收集器

##### Serial Old 收集器（年老代）

Serial Old是Serial收集器的老年代版本，同样是一个单线程收集器，使用标记-整理算法。

适用场景：Client模式；单核服务器；

与Parallel Scavenge收集器搭配；作为CMS收集器的后备方案，在并发收集发生Concurrent Mode Failure时使用。



#### **并行GC特点和场景**

##### Parallel Scavenge收集器（新生代）

Parallel Scavenge也是一款用于新生代的多线程收集器，也是采用复制算法。追求高吞吐量，高效利用CPU，是吞吐量优先，且能进行精确控制。

与ParNew的不同之处在于 Parallel Scavenge收集器的目的是达到一个可控制的吞吐量，而ParNew收集器关注点在于尽可能的缩短垃圾收集时用户线程的停顿时间。

所谓吞吐量就是CPU用于运行用户代码的时间与CPU总消耗时间的比值， 即吞吐量=运行用户代码时间/（运行用户代码时间+垃圾收集时间）。

适用场景：注重吞吐量高效利用CPU，需要高效运算，且不需要太多交互。

 -XX:MaxGCPauseMilis。 控制最大垃圾收集停顿时间

-XX:GCTimeRadio。设置吞吐量大小，参数值是一个(0,100)两侧均为开区间的整数。

-XX:+UserAdaptiveSizePolicy

##### Parallel Old收集器（年老代）

使用多线程和“标记-整理”算法，可以充分利用多核CPU的计算能力。

适用场景：注重吞吐量与CPU资源敏感的场合，与Parallel Scavenge 收集器搭配使用，jdk7和jdk8默认使用该收集器作为老年代收集器。使用参数进行指定。

-XX:+UserParallelOldGC



#### **CMS GC特点和场景**

CMS（Concurrent Mark Sweep）收集器是一种以获取最短回收停顿时间为目标的收集器。发收集，低停顿。

适用场景：重视服务器响应速度，要求系统停顿时间最短。

-XX:+UserConMarkSweepGC 

空间碎片优化：

-XX:UserCMSCompactAtFullCollection #开启碎片整理（默认是开的）

-XX:CMSFullGCsBeforeCompaction #执行多少次不压缩的Full GC之后，跟着来一次压缩的Full GC

缺点：

- CMS收集器对CPU资源非常敏感，CMS默认启动对回收线程数(CPU数量+3)/4，当CPU数量在4个以上时，并发回收时垃圾收集线程不少于25%，并随着CPU数量的增加而下降，但当CPU数量不足4个时，对用户影响较大。
- CMS无法处理浮动垃圾，可能会出现“Concurrent Mode Failure”失败而导致一次FullGC的产生。这时会地洞后备预案，临时用SerialOld来重新进行老年代的垃圾收集。由于CMS并发清理阶段用户线程还在运行，伴随程序运行自然还会有新的垃圾产生，这部分垃圾出现在标记过程之后，CMS无法在当次处理掉，只能等到下一次GC，这部分垃圾就是浮动垃圾。同时也由于在垃圾收集阶段用户线程还需要运行，那也就需要预留足够的内存空间给用户线程使用，因此CMS收集器不能像其他老年代几乎完全填满再进行收集。可以通过参数-XX:CMSInitiatingOccupancyFraction修改CMS触发的百分比。



 

**G1 GC特点和场景**

G1收集器是一款面向服务端应用的垃圾收集器，目前是JDK9的默认垃圾收集器。

1、并行与并发。G1能充分利用多CPU，多核环境下的硬件优势。

2、分代收集。能够采用不同的方式去处理新创建的对象和已经存活了一段时间的对象，不需要与其他收集器进行合作。/3、空间整合。G1从整体上来看基于“标记-整理”算法实现的收集器，从局部上看是基于复制算法实现的，因此G1运行期间不会产生空间碎片。

4、可预测的停顿。G1能建立可预测的时间停顿模型，能让使用者明确指定一个长度为M毫秒的时间片段内，消耗在垃圾收集上的时间不得超过N毫秒。

适用场景：要求尽可能可控 GC 停顿时间；内存占用较大的应用。

 -XX:+UseG1GC



----------

**图示收集器**

<img src="串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028184834988.png" alt="image-20201028184834988" style="zoom: 50%;" />

 

<img src="串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028190225747.png" alt="image-20201028190225747" style="zoom:67%;" />

<img src="串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028190145973.png" alt="image-20201028190145973" style="zoom:67%;" />

<img src="串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028190120941.png" alt="image-20201028190120941" style="zoom:67%;" />

<img src="串行、并行、 CMS 、G1 GC 记录和总结.assets\image-20201028190057616.png" alt="image-20201028190057616" style="zoom: 67%;" />

--------



 