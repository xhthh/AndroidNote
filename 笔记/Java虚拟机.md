### Java虚拟机

#### 一、Java内存区域相关

##### 1、jvm内存结构，堆和栈的结构、栈帧？

Java虚拟机管理的内存分为以下几个运行时数据区域：

- **程序计数器**

  <font color='red'>一块较小的内存空间，可以看作是当前线程所执行的字节码的行号指示器。</font>

  分支、循环、跳转、异常处理、线程恢复都需要依赖程序计数器。

  由于Java虚拟机的多线程是通过线程轮流切换并分配处理器执行时间的方式来实现的，在任何一个确定的时刻，一个处理器只会执行一条线程中的指令。为了线程切换后能恢复到正确的执行位置，用程序计数器来记录代码已经执行到的位置，所以它是线程私有的。

  > <font color='red'>此内存区域是唯一一个在Java虚拟机规范中没有规定任何 OutOfMemoryError 情况的区域。</font>

- **Java虚拟机栈**

  线程私有，生命周期与线程相同。

  虚拟机栈描述的是Java方法执行的内存模型：<font color='red'>每个方法在执行的同时都会创建一个栈帧用于存储局部变量表、操作数栈、动态链接、方法出口等信息。每一个方法从调用直至执行完成的过程，就对应着一个栈帧在虚拟机中入栈到出栈的过程。</font>

  <font color='red'>局部变量表存放了编译期可知的各种基本数据类型、对象引用和returnAddress类型（指向了一条字节码指令的地址）。</font>

  > 异常：
  >
  > - 如果线程请求的栈深度大于虚拟机所允许的深度，将抛出 StackOverflowError 异常；
  > - 如果虚拟机栈可以动态扩展，如果扩展时无法申请到足够的内存，就会抛出 OutOfMemoryError 异常；

  <font color='red'>栈帧（Stack Frame）是用于支持虚拟机进行方法调用和方法执行的数据结构，每一个线程在执行某个方法时，都会为这个方法创建一个栈帧。</font>

- **本地方法栈**

  本地方法栈与虚拟机栈类似，针对的是本地（native）方法。

- **Java堆（线程共享）**

  虚拟机所管理的内存中最大的一块。线程共享，在虚拟机启动时创建。

  <font color='red'>几乎所有的对象实例都在堆中分配内存（逃逸分析，可能会在栈上分配）。</font>

  Java堆是垃圾收集器管理的主要区域，基本采用分代收集算法，分为新生代和老年代。

  > Java堆可以处于物理上不连续的内存空间中，只要逻辑上是连续的即可。
  >
  > 如果堆中没有内存完成实例分配，并且堆也无法再扩展时，将会抛出 OutOfMemoryError 异常。

- **方法区（线程共享）**

  用于存储已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。

  > 当方法区无法满足内存分配需求时，将抛出 OutOfMemoryError 异常。

- **运行时常量池**

  运行是常量池是方法区的一部分。Class 文件中除了有类的版本、字段、方法、接口等描述信息外，还有一项信息是常量池，用于存放编译期生成的各种字面量和符号引用，这部分内容将在类加载后进入方法区的运行时常量池中存放。

  一般来说，除了保存 Class 文件中描述的符号引用外，还会把翻译出来的直接引用也存储在运行时常量池中。

  > 异常同方法区



##### 2、方法区有什么东西？

##### 3、jvm的运行时数据结构。栈帧中会有什么异常？<font color='red'>方法区里面存放的是什么数据？</font>

TODO

##### 4、Java中进程间共享的数据是放在JVM那个分区的？Java中主进程和子进程间的通信，通过哪块内存区域？

TODO

##### 5、开线程影响哪块内存？

<font color='red'>每当有线程被创建的时候，JVM 就需要为其在内存中分配虚拟机栈和本地方法栈来记录调用方法的内容，分配程序计数器记录指令执行的位置，这样的内存消耗就是创建线程的内存代价。</font>





#### 二、垃圾回收相关

##### 1、垃圾回收机制？

- 判断对象已死
  - 引用计数法，实现简单，判定效率高，但是无法解决对象之间相互循环引用的问题。
  - 可达性分析，通过一系列的称为“GC Roots”的对象作为起始点，从这些节点开始向下搜索，搜索所走过的路径称为引用链，当一个对象到 GC Roots 没有任何引用链相连时，则证明此对象是不可用的。

- 什么时候回收
  - Allocation Failure：在堆内存中分配时，如果因为可用剩余空间不足导致对象内存分配失败，这时系统会触发一次 GC；
  - System.gc()：程序员主动调用 gc（不会立刻回收）；



##### 2、哪些对象可以作为GC Roots

- 虚拟机栈（栈帧中的本地变量表）中引用的对象。
- 方法区中静态类属性引用的对象。
- 方法区中常量引用的对象。
- 本地方法栈中JNI（即一般说的Native方法）引用 对象。



##### 3、垃圾回收算法？老年代有什么算法？

###### 3.1 标记-清除算法

> 1. 从 GC Roots 集合开始，将内存遍历一次，对存活对象进行标记
> 2. 标记完毕后，将垃圾对象清除

- 优点：实现简单，不需要移动对象
- 缺点：效率不高，这个算法需要中断进程内其他组件的执行（stop the world），会产生大量不连续的内存碎片，提高垃圾回收的频率（碎片多，容易在分配大对象时内存不够，导致触发另一次垃圾收集）

###### 3.2 复制算法

将可用内存按容量划分为大小相等的两块，每次只是用其中一块。当这一块内存使用完了，就将还存活着的对象复制到另一块，然后再把已使用过的内存空间一次清理掉。

适用于对象存活率低的场景，比如新生代。

- 优点：实现简单，效率高，不用考虑内存碎片的问题
- 缺点：可用内存缩小为原来的一半，对象存活率高时会频繁进行复制

###### 3.3 标记-整理算法

标记-整理算法采用标记-清除算法一样的方式进行对象的标记，清除时，将所有的存活对象都向一端移动，然后直接清理掉端边界以外的内存。

适用于对象存活率高的场景，如老年代。

- 优点：避免了碎片的产生，又不需要两块相同的内存空间
- 缺点：需要进行对象移动，一定程度上降低了效率

###### 3.4 分代收集算法

Java虚拟机根据对象存活周期的不同将内存划分为几块，一般是把Java堆分为新生代和老年代。

大多数情况下，对象在新生代 Eden 区中分配。新生代对象存活率较低，使用复制算法；老年代对象存活率较高，使用标记-清除或者标记-整理算法。

- 新生代

  新生代细分为3部分：Eden、Survivor0、Survivor1，比例8：1：1，内存分配如下：

  - 绝大多数刚被创建的对象会放在 Eden 区；
  - 当 Eden 区第一次满的时候，会进行垃圾回收。首先将 Eden 区的垃圾对象回收清除，并将存活的对象复制到 S0，此时 S1 是空的。
  - 下一次 Eden 区满时，再执行一次垃圾回收。此次会将 Eden 区和 S0 区中所有垃圾对象清除，并将存活对象复制到 S1，此时 S0 变为空。
  - 如此反复在 S0 和 S1 之间切换几次（默认15次）之后，如果还有存活对象，则转移到老年代。

- 老年代

  一个对象如果在新生代存活了足够长的时间而没有被清理掉，则会被复制到老年代。如果对象比较大（比如长字符串或者大数组），并且新生代的剩余空间不足，则这个大对象会直接被分配到老年代。



##### 4、四种引用

- **强引用**

  只要强引用还存在，垃圾收集器永远不会回收掉被引用的对象。

- **软引用**

  软引用是用来描述一些还有用但并非必需的对象。对于软引用关联着的对象，在系统将要发生内存溢出异常之前，将会把这些对象列进回收范围之中进行第二次回收。如果这次回收还没有足够的内存，才会抛出内存溢出异常。

  > 被软引用对象关联的对象会自动被垃圾回收器回收，但是软引用对象本身也是一个对象，这些创建的软引用并不会自动被垃圾回收器回收掉。

- **弱引用**

  弱引用也是用来描述非必需对象的，但是它的强度比软引用更弱一些，被弱引用关联的对象只能生存到下一次垃圾收集发生之前。当垃圾收集器工作时，无论当前内存是否足够，都会回收掉只被弱引用关联的对象。

  > 弱引用与引用队列，被弱引用指向的对象被回收的时候，该弱引用对象会被加入到引用队列中。LeakCanary原理

- **虚引用**

  它是最弱的一种引用关系。一个对象是否有虚引用的存在，完全不会对其生存时间构成影响，也无法通过虚引用来取得一个对象实例。为一个对象设置虚引用关联的唯一目的就是能在这个对象被收集器回收时收到一个系统通知。

**使用场景：**

> 软引用：比如网页缓存、图片缓存等；
>
> 弱引用：比如使用Handler时，使用弱引用持有外部类Activity的引用；



##### 5、内存分配与回收策略

###### 5.1 对象优先在Eden分配

​	大多数情况下，对象在新生代Eden区中分配。当Eden区没有足够空间进行分配时，虚拟机将发起一次 Minor GC。

​	新生代GC（Minor GC）：指发生在新生代的垃圾收集动作，因为Java对象大都具备朝生夕灭的特性，所以Minor GC 非常频繁，一般回收速度也比较快。

​	老年代GC（Major GC/Full GC）：指发生在老年代的GC，出现了 Major GC，经常会伴随至少一次的 Minor GC（但非绝对的，有的收集器的收集策略里就有直接进行 Minor GC 的策略选择过程）。Major GC 的速度一般会比 Minor GC 慢10倍以上。

###### 5.2 大对象直接进入老年代

​	所谓的大对象是指，需要大量连续内存空间的 Java 对象，最典型的大对象是那种很长的字符串以及数组。

###### 5.3 长期存活的对象将进入老年代

​	虚拟机给每个对象定义了一个对象年龄计数器。如果对象在Eden出生并经过第一次Minro GC后仍然存活，并且能够被Survivor容纳的话，将被移动到Survivor空间中，并且对象年龄设为1。对象在Survivor区中每“熬过”一次 Minor GC，年龄就加1岁，当它的年龄增加到一定程度（默认15岁），就将会被晋升到老年代中。

###### 5.4 动态对象年龄判断

###### 5.5 空间分配担保



###### 5.6 内存分配、回收示例：

```java
/**
 * VM agrs: -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails
 * -XX:SurvivorRatio=8
 */
public class MinorGCTest {
    private static final int _1MB = 1024 * 1024;

    public static void testAllocation() {
        byte[] a1, a2, a3, z4;
        a1 = new byte[2 * _1MB];
        a2 = new byte[2 * _1MB];
        a3 = new byte[2 * _1MB];
        a4 = new byte[1 * _1MB];
    }

    public static void main(String[] agrs) {
        testAllocation();
    }
}
```

由参数可知，堆总内存 20M，新生代 10M，Eden 8M，S1 1M，S0 1M，老年代 10M

- 执行代码，从 GC 日志可看出：a1、a2、a3、a4 四个对象都被分配在了新生代的 Eden 区；

- 将 a4 初始化改为 a4 = new byte[2 * _1MB]，结果 a4 在新生代 Eden 区，a1、a2、a3 转移到了老年代；

  <font color='red'>因为在给 a4 分配内存之前，Eden 区已经被占用 6M，已经无法再分配出 2M 来存储 a4 对象，因此会执行一次 Minor GC。并尝试将存活的对象 a1、a2、a3 复制到 S0区。但是 S0 区只有 1M 空间，所以没办法存储 a1、a2、a3 任意一个对象。这种情况下 a1、a2、a3 被转移到老年代，a4 保存在 Eden 区。</font>



#### 三、class类文件结构相关

##### 1、class文件的组成？常量池里面有什么内容？

Class 文件是一组以 8 位字节为基础单位的二进制流。根据 Java 虚拟机规范的规定，Class 文件格式采用一种类似于 C 语言结构体的伪结构来存储数据，这种伪结构中只有两种数据类型：<font color='red'>无符号数和表</font>。

> - 无符号数属于基本的数据类型，以 u1、u2、u4、u8 来分别代表 1 个字节、2 个字节、4 个字节和 8 个字节的无符号数，无符号数可以用来描述数字、索引引用、数量值或者按照 UTF-8 编码构成字符串值。
> - 表是由多个无符号数或者其他表作为数据项构成的复合数据类型，所有表都习惯性地以“_info”结尾。表用于描述有层次关系的复合结构的数据，整个 Class 文件本质上就是一张表。

![img](https://images2015.cnblogs.com/blog/990532/201610/990532-20161003162624442-764739020.png)

​	在class文件中，主要分为魔数与Class文件的版本号、常量池、访问标志、类索引（还包括父类索引和接口索引集合）、字段表集合、方法表集合、属性表集合。

- **魔数**

  - 每个 Class 文件的头 4 个字节称为魔数（Magic Number）。
  - 它的唯一作用是确定这个文件是否为一个能被虚拟机接受的 Class 文件。

  很多文件存储标准中都使用魔数来进行身份识别，譬如图片格式，如 gif 或者 jpeg 等在文件头中都存有魔数。使用魔数而不是扩展名来进行识别主要是基于安全方面的考虑，因为文件名可以随意的改动。

- **Class 文件的版本号**

  紧接着魔数的 4 个字节存储的是 Class 文件的版本号：

  - 第 5 和第 6 个字节是次版本号（Minor Version）
  - 第 7 和第 8 个字节是主版本号（Major Version）

- **常量池**

  常量池中主要存放两大类常量：

  - 字面量，字面量比较接近于 Java 语言层面的常量概念，如文本字符串、声明为 final 的常量值等。

  - 符号引用，属于编译原理方面的概念，包括了下面三类常量：

    - 类和接口的全限定名
    - 字段的名称和描述符
    - 方法的名称和描述符

    > 在 class 文件中不会保存各个方法、字段的最终内存布局信息，因此这些字段、方法的符号引用不经过运行期转换的话无法得到真正的内存入口地址，也就无法直接被虚拟机使用。当虚拟机运行时，需要从常量池获得对应的符号引用，再在类创建时或运行时解析、翻译到具体的内存地址之中。

- **访问标志**

  常量池结束后，紧接着的两个字节代表访问标志（access_flags），这个标志用于识别一些类或者接口层次的访问信息，包括：

  - 这个 Class 是类还是接口
  - 是否定义为 public 类型
  - 是否定义为 abstract 类型
  - 如果是类的话，是否被声明为 final 等

- **类索引、父类索引与接口索引集合**

  类索引（this_class）和父类索引（super_class）都是一个 u2 类型的数据，而接口索引集合（interfaces）是一组 u2 类型的数据的合集，Class 文件中由这三项数据来确定这个类的继承关系。

  - 类索引用于确定这个类的全限定名；
  - 父类索引用于确定这个类的父类的全限定名；
  - 接口索引集合用来描述这个类实现了哪些接口，这些被实现的接口按 implements 语句后的接口顺序从左到右排列在接口索引集合中。

- **字段表集合**

  字段表（field_info）用于描述接口或者类中声明的变量。字段（field）包括类级变量以及实例级变量，但不包括在方法内部声明的局部变量。

  在Java中一般通过如下几项描述一个字段：字段作用域（public、protected、private修饰符）、是类级别变量还是实例级别变量（static修饰符）、可变性（final修饰符）、并发可见性（volatile修饰符）、可序列化与否（transient修饰符）、字段数据类型（基本类型、对象、数组）以及字段名称。在字段表中，变量修饰符使用标志位表示，字段数据类型和字段名称则引用常量池中常量表示。

- **方法表集合**

  方法表的结构如同字段表一样，依次包括访问标志（access_flags）、名称索引（name_index）、描述符索引（descriptor_index）、属性表集合（attributes）几项。

- **属性表集合**

  在class文件、字段表、方发表都可以携带自己的属性表集合，以用于描述某些场景专有的信息。

##### 2、字符串最大长度

- 字符串有长度限制，在编译期，要求字符串常量池中的常量不能超过65535，并且在javac执行过程中控制了最大值为65534。
- 在运行期，长度不能超过Int的范围，否则会抛异常。

> Java中的UTF-8编码的Unicode字符串在常量池中以CONSTANT_Utf8类型表示。
>
> CONSTANTUtf8info是一个CONSTANTUtf8类型的常量池数据项，它存储的是一个常量字符串。常量池中的所有字面量几乎都是通过CONSTANTUtf8info描述的。CONSTANTUtf8_info的定义如下：
>
> CONSTANT_Utf8_info {
>     u1 tag;
>     u2 length;
>     u1 bytes[length];
> }
>
> CONSTANTUtf8info中有u2 length;表明了该类型存储数据的长度。
>
> u2是无符号的16位整数，因此理论上允许的的最大长度是2^16=65536。而 java class 文件是使用一种变体UTF-8格式来存放字符的，null 值使用两个 字节来表示，因此只剩下 65536－ 2 ＝ 65534个字节。



#### 四、类加载机制相关

##### 1、JVM类加载机制了解吗？类什么时候会被加载？类加载的过程具体生命周期是怎样的？

###### 1.1 类加载机制概念

<font color='red'>虚拟机把描述类的数据从 class 文件加载到内存，并对数据进行校验、转换解析和初始化，最终形成可以被虚拟机直接使用的 Java 类型，这就是虚拟机的类加载机制。</font>

###### 1.2 类加载的生命周期

类从被加载到虚拟机内存中开始，到卸载出内存为止，它的整个生命周期包括：加载、验证、准备、解析、初始化、使用和卸载 7 个阶段。其中验证、准备、解析 3 个部分统称为连接。

###### 1.3 类加载的时机

JVM 是什么时候加载某 .class 文件，Java 虚拟机规范中并没有严格规定，不同虚拟机会有不同实现。一般以下两种情况会对 class 进行加载操作：

- 隐式加载：在程序运行过程中，当碰到通过 new 等方式生成对象时，系统会隐式调用 ClassLoader 去加载对应的 class 到内存中。
- 显式加载：在代码中主动通过 ClassLoader 加载 class 对象，比如 Class.forName()、ClassLoader.loadClass()。

###### 1.4 类何时被加载器加载？？？？？？

- 调用类构造器
- 调用类中的静态变量或者静态方法



##### 2、类加载过程

- **加载**

  在加载阶段，虚拟机需要完成以下 3 件事情：

  - 通过一个类的全限定名来获取定义此类的二进制字节流；
  - 将这个字节流所代表的的静态存储结构转化为方法区的运行时数据结构；
  - 在内存中生成一个代表这个类的 java.lang.Class 对象，作为方法区这个类的各种数据的访问入口。

  > 这个二进制字节流不一定从 class 文件中获取，可以从 ZIP 包中读取、网络中获取、运行时计算生成（动态代理技术）、由其他文件生成、从数据库中获取。。。

- **验证**

  验证的目的是为了确保 Class 文件的字节流中包含的信息符合当前虚拟机的要求，并且不会危害虚拟机自身的安全。

  验证阶段包括以下 4 个阶段的检验动作：

  1. 文件格式验证
  2. 元数据验证
  3. 字节码验证
  4. 符号引用验证

- **准备**

  准备阶段是正式为类变量分配内存并设置类变量初始值的阶段，这些变量所使用的内存都将在方法区中进行分配。

  > - 这时候进行内存分配的仅包括类变量（被 static 修饰的变量），而不包括实例变量，实例变量将会在对象实例化时随着对象一起分配在 Java 堆中。
  > - 初始值“通常情况”下是数据类型的零值，比如 `public static int value = 123` 准备阶段过后的初始值是 0，赋值为 123 是在初始化阶段进行的。

- **解析**

  解析阶段是虚拟机将常量池内的符号引用替换为直接引用（也就是具体的内存地址）的过程。

  在这一阶段，JVM 会将常量池中的类、接口名、字段名、方法名等转换为具体的内存地址。

- **初始化**

  初始化阶段是执行类构造器 `<clinit>()` 方法的过程。

  > `<clinit>()`方法执行的过程有一些特点，其中一点是<font color='red'>静态内部类单例模式能保证线程安全的原因</font>：
  >
  > 虚拟机会保证一个类的 `<clinit>()`方法在多线程环境中被正确地加锁、同步，如果多个线程同时去初始化一个类，那么只会有一个线程去执行这个类的 `<clinit>()`方法，其他线程都需要阻塞等待，直到活动线程执行 `<clinit>()`方法完毕。如果一个类的 `<clinit>()`方法中有耗时很长的操作，就可能造成多个进程阻塞。

  虽然什么情况下开始加载阶段，在 Java 虚拟机中没有强制约束，但是<font color='red'>对于初始化阶段，严格规定了有且只有 5 种情况必须立即对类进行“初始化”</font>：

  1. 遇到 new、getstatic、putstatic 或 invokestatic 这 4 条字节码指令时，如果类没有进行过初始化，则需要先触发其初始化。生成这 4 条指令的最常见的 Java 代码场景是：
     - 使用 new 关键字实例化对象的时候
     - 读取或设置一个类的静态字段（被 final 修饰、已在编译期把结果放入常量池的静态字段除外）的时候
     - 调用一个类的静态方法的时候
  2. 使用 java.lang.reflect 包的方法对类进行反射调用的时候，如果类没有进行过初始化，则需要先触发其初始化。
  3. <font color='red'>当初始化一个类的时候，如果发现其父类还没有进行过初始化，则需要先触发其父类的初始化。</font>
  4. 当虚拟机启动时，用户需要指定一个要执行的主类（包含 main() 方法的那个类），虚拟机会先初始化这个主类。
  5. 当使用 JDK 1.7 的动态语言支持时，如果一个 java.lang.invoke.MethodHandle 实例最后的解析结果 REF_getStatic、REF_putStatic、REF_invokeStatic 的方法句柄，并且这个方法句柄所对应的类没有进行过初始化，则需要先触发其初始化。



##### 3、java加载对象的步骤？

##### 4、成员变量和局部变量的区别。为何成员变量需要jvm在对象初始话过程中赋默认值？

因为JVM规范规定，没有初始化的变量，是不能使用的。而成员变量，在对象初始化过程中，可能会被使用到，因此，JVM会自动给成员变量，按照数据类型设置一个默认值，这样就能够被使用了。

###### 4.1 成员变量、局部变量和静态变量的区别

|          | **成员变量**        | **局部变量**                       | **静态变量**        |
| -------- | ------------------- | ---------------------------------- | ------------------- |
| 定义位置 | *在类中**,**方法外* | *方法中**,**或者方法的形式参数*    | *在类中**,**方法外* |
| 初始化值 | 有默认初始化值      | *无**,**先定义**,**赋值后才能使用* | 有默认初始化值      |
| 调用方式 | 对象调用            | ---                                | 对象调用，类名调用  |
| 存储位置 | 堆中                | 栈中                               | 方法区              |
| 生命周期 | 与对象共存亡        | 与方法共存亡                       | 与类共存亡          |
| 别名     | 实例变量            | ---                                | 类变量              |

###### 4.2 为何成员变量需要 jvm 在对象初始化过程中赋值？

因为成员变量是在对象实例化时随着对象一起分配在 Java 堆中的。对象实例化时会触发类的初始化操作。

###### 4.3 总结一下对象的初始化顺序如下：

静态变量/静态代码块 -> 普通代码块 -> 构造函数

1. 父类静态变量和静态代码块；
2. 子类静态变量和静态代码块；
3. 父类普通成员变量和普通代码块；
4. 父类的构造函数；
5. 子类普通成员变量和普通代码块；
6. 子类的构造函数。



##### 5、类加载器

<font color='red'>对于任意一个类，都需要由加载它的类加载器和这个类本身一同确立其在 Java 虚拟机中的唯一性。</font>

###### 5.1 怎样判断两个 Class 是否相同？

- 类名相同
- 由同一个类加载器加载

###### 5.2 双亲委派模型

从 Java 虚拟机的角度来讲，只存在两种不同的类加载器：

1. 一种是启动类加载器（Bootstrap ClassLoader），这个类加载器使用 C++ 实现，是虚拟机自身的一部分；
2. 另一种是所有其他的类加载器，这些类加载器都由 Java 语言实现，独立于虚拟机外部，并且全部都继承自抽象类 `java.lang.ClassLoader`。
   - 扩展类加载器
   - 应用类加载器

![img](https://user-gold-cdn.xitu.io/2020/3/1/17095dc13b2406bf?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



<font color='red'>双亲委派模型的工作过程：</font>如果一个类加载器收到了类加载的请求，它首先不会自己去尝试加载这个类，而是把这个请求委派给父类加载器去完成，每一个层次的类加载器都是如此，因此所有的加载请求最终都应该传送给顶层的启动类加载器中，只有当父加载器反馈自己无法完成这个加载请求（它的搜索范围中没有找到所需的类）时，子加载器才会尝试自己去加载。

###### 5.3 为什么使用双亲委派模型？有什么好处？

- 保证类的唯一性，避免重复加载
- 出于安全考虑，防止用户自定义系统类进行加载

好处是 Java 类随着它的类加载器一起具备了一种带有优先级的层级关系。例如 `java.lang.Object`，它存放在 `rt.jar`之中，无论哪一个类加载器要加载这个类，最终都是委派给处于模型最顶端的启动类加载器进行加载，因此 Object 类在程序的各种类加载器环境中都是同一个类。如果没有使用双亲委派模型，由各个类加载器自行加载的话，如果用户自己编写了一个 `java.lang.Object` 类，放在程序的 ClassPath 中，那系统中将会出现多个不同的 Object 类，Java类型体系中最基础的行为就无法保证，应用程序也将会变得一片混乱。



###### 5.4 双亲委派模型的实现

```java
protected Class<?> loadClass(String name, boolean resolve)
        throws ClassNotFoundException {
    synchronized (getClassLoadingLock(name)) {
        // 检查该类是否已经加载过
        Class<?> c = findLoadedClass(name);
        if (c == null) {
            //如果该类没有加载，则进入该分支
            long t0 = System.nanoTime();
            try {
                if (parent != null) {
                    //当父类加载器不为空，则通过父类的loadClass()来加载该类
                    c = parent.loadClass(name, false);
                } else {
                    //当父类加载器为空，则调用启动类加载器来加载该类
                    c = findBootstrapClassOrNull(name);
                }
            } catch (ClassNotFoundException e) {
                //非空父类的类加载器无法找到相应的类，则抛出异常
            }

            if (c == null) {
                //当父类加载器无法加载时，则调用findClass方法来加载该类
                long t1 = System.nanoTime();
                c = findClass(name);//用户可以通过覆写该方法，来自定义类加载器
				//......
            }
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }
}
```



##### 6、DexClassLoader与PathClassLoader的区别？

- 8.0之前唯一的区别就是生不生成优化后的odex文件；

- 8.0之后统一不在传递optimizedDirectory

```java
optimizedDirectory this parameter is deprecated and has no effect since API level 26.
```



#### 五、编译期相关

##### 1、泛型与类型擦除

- 泛型

  本质是参数化类型的应用，也就是说所操作的数据类型被指定为一个参数。这种参数类型可以用在类、接口和方法的创建中，分别称为泛型类、泛型接口和泛型方法。

- 类型擦除

  <font color='red'>泛型信息只存在于代码编译阶段，在进入 JVM 之前，与泛型相关的信息会被擦除掉，即类型擦除。</font>

  在泛型类被类型擦除的时候，之前泛型类中的类型参数部分如果没有指定上限，如 `<T>` 则会被转译成普通的 Object 类型，如果指定了上限如 `<T extends String>` 则类型参数就被替换成类型上限。

  ```java
  List<String> l1 = new ArrayList<String>();
  List<Integer> l2 = new ArrayList<Integer>();

  System.out.println(l1.getClass() == l2.getClass());//true
  ```

  - 类型擦除会抹掉很多继承相关的特性，这是它带来的局限性；
  - 泛型的限制可以通过反射绕过；

##### 2、自动装箱发生在什么时候？编译期还是运行期？

自动装箱就是 Java 自动将原始类型值转换成对应的对象，比如将 int 的变量转换成 Integer 对象，这个过程叫做装箱，反之将 Integer 对象转换成 int 类型值，这个过程叫做拆箱。因为这里的装箱和拆箱是自动进行的非人工转换，所以就称作为自动装箱和拆箱。

<font color='red'>自动装箱发生在编译期。</font>



#### 六、Java内存模型相关

##### 1、Java 内存模型

**问题：**CPU运算速度和存储、通信速度差距过大。

所以增加了一层高速缓存（Cache）来作为内存与处理器之间的缓冲：

将运算需要使用到的数据复制到缓存中，让运算能快速进行，当运算结束后再从缓存同步回内存之中，这样处理器就无须等待缓慢的内存读写了。

1. <font color='red'>缓存一致性的问题</font>（多处理器系统中，每个处理器都有自己的高速缓存，而它们又共享同一主存，当多个处理器的运算任务都涉及同一块主内存区域时，将可能导致各自的缓存数据不一致）。

   为了解决一致性问题，需要各个处理器访问缓存时都遵循一些协议。

2. <font color='red'>指令重排问题</font>

   为了使得处理器内部的运算单元能尽量被充分利用，处理器可能会对输入代码进行乱序执行优化。

   因此，如果存在一个计算任务依赖另一个计算任务的中间结果，那么其顺序性并不能靠代码的先后顺序



**Java内存模型的目的：** Java虚拟机规范试图定义一种Java内存模型（Java Memory Model，JMM）来屏蔽掉各种硬件和操作系统的内存访问差异，以实现让Java程序在各种平台下都能达到一致的内存访问效果。

**Java内存模型的主要目标：** 定义程序中各个变量的访问规则，即在虚拟机中将变量存储到内存和从内存中取出变量这样的底层细节。此处的变量与 Java 编程中所说的变量有所区别，它包括了实例字段、静态字段和构成数组对象的元素，但不包括局部变量与方法参数，因为后者是线程私有的，不会被共享，自然就不会存在竞争问题。

**Java内存模型规定了所有的变量都存储在主内存中。每条线程还有自己的工作内存，线程的工作内存中保存了被该线程使用到的变量的主内存副本拷贝，线程对变量的所有操作（读取、赋值等）都必须在工作内存中进行，而不能直接读写主内存中的变量。不同的线程之间也无法直接访问对方的工作内存中的变量，线程间变量值的传递均需要通过主内存来完成。**

<font color='red'>volatile变量依然有工作内存的拷贝，但是由于它的特殊的操作顺序性规定，所以看起来如同直接在主内存中读写访问一般。</font>

> 这里的主内存、工作内存与 Java 内存区域中的堆、栈、方法区等并不是同一个层次的内存划分，如果勉强对应的话：
>
> - 从变量、主内存、工作内存的定义来看，主内存主要对应于 Java 堆中的对象实例数据部分，而工作内存则对应于虚拟机栈中的部分区域；
> - 从更低层次来看，主内存对应于物理硬件的内存，工作内存对应于寄存器和高速缓存；



普通变量的值在线程间传递均需要通过主存来完成，例如：

- 线程A修改一个普通变量的值，然后向主内存进行回写；
- 线程B在线程A回写完成了之后再从主内存进行读取操作，新变量值才会对线程B可见。



##### 2、volatile 关键字

###### 2.1 volatile 关键字的作用？

关键字 volatile 可以说是Java虚拟机提供的最轻量级的同步机制。

当一个变量定义为 volatile 之后，它将具备两种特性：

- **可见性**

  可见性是指当一条线程修改了这个变量的值，新值对于其他线程来说是可以立即得知的。

  在不符合以下两条规则的场景中，仍要<font color='red'>**通过加锁来保证原子性**</font>：

  - 运算结果并不依赖变量的当前值，或者能够确保只有单一的线程修改变量的值。
  - 变量不需要与其他的状态变量共同参与不变约束。

- **有序性**，禁止指令重排序优化

  - 指令重排序是指指令乱序执行，即在条件允许的情况下直接运行当前有能力立即执行的后续指令，避开为获取一条指令所需数据而造成的等待，通过乱序执行的技术提高执行效率。
  - 指令重排序会在被 volatile 修饰的变量的赋值操作前，添加一个<font color='red'>**内存屏障**</font>，指令重排序时不能把后面的指令重排序移到内存屏障之前的位置。

  > 比如在双锁检测的单例模式中，使用 volatile 来修饰单例类实例。



volatile 变量读操作的性能消耗与普通变量几乎没有什么差别，但是写操作则可能会慢一些，因为它需要在本地代码中插入许多内存屏障指令来保证处理器不发生乱序执行。

不过即便如此，大多数场景下 volatile 的总开销仍然要比锁低，我们在 volatile 与锁之中选择的唯一依据仅仅是 volatile 的语义能否满足使用场景的需求。



###### 2.2 怎么保证原子性呢？

<font color='red'>可以使用 synchronized 同步代码块、CAS原子类工具和 lock 锁机制。</font>

###### 2.3 synchronized和volatile的区别？为何不用volatile替代synchronized？类锁和对象锁互斥么？

1. 区别：https://blog.csdn.net/suifeng3051/article/details/52611233

2. 为啥：https://www.cnblogs.com/hollischuang/p/11386988.html

3. 不互斥

   类锁和对象锁不是一个东西，一个是类的 Class 对象的锁，一个是类的实例的锁。即一个线程访问静态 synchronized 的时候，允许另一个线程访问对象的实例 synchronized 方法。反过来也是成立的，因为它们需要的锁是不同的。



##### 3、可见性，原子性，有序性？

- **原子性**

  指一个操作或多个操作要么全部执行，要么执行失败，不会被中断，基本数据类型的访问读写是具备原子性的。如果应用场景需要一个更大范围的原子性保证，可以使用 synchronized 关键字，synchronized 块之间的操作也具备原子性。

- **可见性**

  可见性是指当一个线程修改了共享变量的值，其他线程能够立即得知这个修改。

- **有序性**

  如果在本线程内观察，所有的操作都是有序的；如果在一个线程中观察另一个线程，所有的操作都是无序的。前半句是指“线程内表现为串行的语义”，后半句是指“指令重排序”现象和“工作内存与主内存同步延迟”现象。

  Java语言提供了 volatile 和 synchronized 两个关键字来保证线程之间操作的有序性，volatile 关键字本身就包含了禁止指令重排序的语义，而 synchronized 则是由“一个变量在同一个时刻只允许一条线程对其进行 lock 操作”这条规则获得的，这条规则决定了持有同一个锁的两个同步块只能串行地进入。



#### 七、其他

##### 1、JVM和Art、Dalvik的对比

**JVM和DVM的区别：**

| Java虚拟机                                                   | Dalvik虚拟机                                                 |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| java虚拟机基于栈，基于栈的机器必须使用指令来载入和操作栈上数据 | Dalvik虚拟机基于寄存器                                       |
| java虚拟机运行的是java字节码。（java类会被编译成一个或多个字节码.class文件，打包到.jar文件中，java虚拟机从相应的.class文件和.jar获取相应的字节码） | Dalvik运行的是自己专属的.dex字节码格式。（java类被编译成.class文件后，会通过一个dx工具将所有的.class文件转换成一个.dex文件，然后dalvik虚拟机会从其中读取指令和数据） |
| -                                                            | 一个应用对应一个Diavik虚拟机实例，独立运行                   |
| JVM在运行的时候为每一个类装载字节码                          | Dalvik程序只包含一个.dex文件，这个文件包含了程序中所有的类   |



**Dalvik与Art的区别：**

1. Dalvik每次运行时，字节码都需要通过即时编译器转换为机器码（JIT），运行效率低；Art 在应用第一次安装的时候，字节码会预先编译成机器码（AOT），启动和运行都会变快，但是apk安装时间边长。
2. Art占用空间比Dalvik大（原生代码占用的存储空间更大），就是用“空间换时间”
3. Art减少编译，减少了CPU使用频率，使用明显改善电池续航
4. Art应用启动更快、运行更快、体验更流畅、触感反馈更及时