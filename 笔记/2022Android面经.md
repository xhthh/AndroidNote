# 2022Android面经

https://juejin.cn/post/7120896365840269348

## 快看漫画

- mvp架构，如何实现一个Fragment，base系列如何实现？

- 触摸事件的传递，View如何消费

- 自定义View中，getMeasuredWidth和getWidth有什么区别

- 线程池如何管理？异常如何处理？

- 应用启动速度优化。

- activity启动速度优化？

- view优化。viewstub原理，哪些方法被跳过？如何被替换？

- RecyclerView原理，RecylerView优化

  RecyclerView性能优化的本质就是针对`onCreateViewHolder`和`onBindViewHolder`的优化：

  https://blog.csdn.net/hxl517116279/article/details/107058425/

  - 减少 `onCreateViewHolder` 调用次数
    - 两个数据源大部分相似时使用swapAdapter代替setAdapter
    - 共用回收池
    - 增加RecycledViewPool缓存数量
  - 减少`onCreateViewHolder`执行时间
    - 减少item的过度绘制
    - Prefetch预取
  - 减少`onBindViewHolder`调用次数
    - 使用局部刷新
    - 使用DiffUtil去局部刷新数据
    - 视情况使用setItemViewCacheSize(size)来加大RecyclerView缓存数目，用空间换取时间提高流畅度
  - 减少`onBindViewHolder`执行时间
    - 数据处理与视图绑定分离
    - 有大量图片时，滚动时停止加载图片，停止后再去加载图片
    - 使用setHasFixedSize避免requestLayout
    - 不要在onBindViewHolder中设置点击事件
  - 其他
    - 对于RecyclerView，如果不需要动画，就把item动画取消
    - 使用getExtraLayoutSpace为LayoutManager设置更多的预留空间
    - 优化解耦 RecyclerView.Adapter

- hybrid和jsBridge。实例分析。flutter如何做呢？

- 数据本地化方案：文件、sp、数据库

- SharedPreference的commit和apply优化、有什么问题?如何解决?

- 权限适配问题。

## 牛客网（口头offer后都能给你放鸽子，你品）

### 一面：

- 项目
- lint检测原理，插件
- 组件化：分层的目的
- flutter

### 二面：

- NA端截图如何传递给h5？共享的方式呢？

  base64？

- Hummer了解么？

- 子线程能刷新UI么？为何只能主线程刷新UI？

- Activity间的通信方式。

- 系统为何不设计Activity#setData这种方式，让外部可以给activity提供数据呢？

- https安全性如何保证的？Charles能抓取https么？app如何防止抓包？

- 如何防止接口被mock，防止反爬虫？（内容签名，加盐）

- 设计模式了解么？举例说明。

- 算法题：[无重复字符的最长子串](https://link.juejin.cn?target=https%3A%2F%2Fleetcode-cn.com%2Fproblems%2Flongest-substring-without-repeating-characters%2F)

### 三面：

无

### 四面:

- 项目
- 路由方案
- python脚本
- 首页精细化刷新方案
- sdk
- 三个字概括自己
- 成就感最强的项目

## 蓝湖

### 一面：

- 项目
- 说一个你遇到的最难的问题
- 算法题：无重复字符的最长子串

### 二面：

- 项目
- 性能优化，举例
- 架构，好的架构如何设计
- kotlin
- sdk如何设计
- 引入sdk时有哪些指标要考量？
- 包体积优化
- 算法题：101. 对称二叉树

## 作业帮

### 一面：

- kotlin语法,函数为何设计为一等公民,协程
- 泛型，为何要擦除？好处是什么？坏处呢？kotlin的泛型呢？泛型如何不擦除？
- jvm、dalvik、art介绍下，解决了什么问题？
- UI优化，UI渲染原理。哪些是CPU做的，哪些是GPU做的？
- 为何掉帧？原理，怎么解决
- 卡顿，原理，如何解决。
- 如何量化UI优化的数据？
- TCP/UDP。三次握手、四次挥手的必要性。
- https的安全性如何保证的。
- jetpack

### 二面：

- 项目
- IM如何实现：长链接如何实现？乱序问题，丢失问题如何解决？

## 陌陌

- app启动流程，launcher之后的流程
- window是何时创建的？
- Handler，Looper为何不会导致ANR？Handler的延时消息如何保证？Looper和Thread如何保证关系？
- GC机制，回收算法，GCRoot的类型
- 多线程同步的方式有哪些？线程池？线程同步？
- synchorized和Lock的区别？synchorized是如何实现加锁的？偏向锁的实现？锁膨胀的过程
- https的密钥交换流程

## 集度

### 一面

- 对话框治理方案
- 网络：http演进过程：1.0 --> 1.1 --> 2.0。https：加密过程
- 锁：常见使用,分类,死锁
- 线程池核心参数
- binder:内存copy几次。mmap几次映射
- activity中，new Handler()，如果内存泄漏了，分析它的GCRoots引用链。
- 事件分发：从手接触开始，一个完整的流程。
- SharedPreference存在哪些问题？
- Glide：如何管理生命周期？Cache？如果一张下载一张大图，有两个尺寸不同的View去加载，在内存中几份缓存？
- View#onClickListener中，sleep(100s)，会不会anr？
- 三个线程依次打印，有哪些方式？
- 用两个栈实现队列。

### 二面

- 你的Kotlin能力如何？
- 你的架构设计能力如何?
- 设计模式：装饰者和适配器模式的异同
- 平时看哪些架构设计方面的书？
- Intent你如何理解？
- 跨进程传递数据，为何要序列化？
- 启动一个Activity，为何要设计成跨进程的方式？app自己做不行么？
- 下载一个几百M的大文件，如何校验文件正确性？如何修复？

## 白龙马

### 一面：

- 开发业务
- 稳定性排查
- 运行时性能优化
- 内存泄漏检测
- GCRoots类型，Handler内存泄漏的引用链分析
- 显示内存泄漏如何检测
- Handler机制原理。消息屏障原理
- 启动优化，启动流程，启动时间如何检
- ArrayMap和HashMap相比较
- 如何捕获crash，线上crash如何捕获
- OOM线上监测方案
- 卡顿检测方案
- 设计一个图片加载库，磁盘缓存如何实现。
- LRU实现
- 如何统计项目中所有Button的点击事件

### 二面：

- 性能优化相关的：首页卡顿优化；
- 负责的业务

## 搜狐视频

- 事件分发
- 消息机制
- 进程间交互方式
- binder优势、binder内存大小
- 锁、锁升级
- volatile语义，各自是如何实现的？
- Java内存模型
- JVM分区、栈帧包含什么？各个分区各自的回收方式
- 类加载机制、类加载步骤及细节
- TCP和UDP的区别，TCP建立连接的过程
- 设计模式。拦截器与责任链的区别
- 长链接实现
- 项目细节。
- 算法题：55. 跳跃游戏

## 拼多多

- 包体积优化，改进空间
- jsBridge原理
- 个保法整改，如何成体系
- AActivity在前台，跳转BActivity，生命周期方法有哪些会执行？A的onStop一定会执行么？
- Intent可以携带哪些数据？数组类型。
- 如何探测binder剩余多少空间?
- 线程池核心参数，有哪些问题？
- 死锁，如何避免。

## Bilibili

### Java:

- HashMap的数据结构,如何保证快速查找,容量为何要设计为2的n次方这样？对扩容有没有影响？

- ArrayMap 和 SparseArray

- ConcurrentHashMap

- HashTable，为何废弃

  > 线程安全，效率低，使用重量级锁 synchronized 修饰了整个方法；

- 多线程存在哪些问题？

- synchorized实现

- juc包下的lock如何实现的？有了synchronized，为何还要有这个？cas的原理?
  - AQS，底层是 CAS+volatile
  - Lock更灵活，能主动释放锁
  - 自旋，比较再交换

- AtomicInteger如何保证原子操作

- CAS如何保证原子操作

- volatile的特性。拿一个特性举例说明

- Java为何设计出可见性这个问题

### Android:

- View#request之后，流程是怎样的

- 事件分发：

  - View设置了一个onClickListener，那么它的onTouchEvent中，DOWN的时间，返回的是true还是false？为何?

    > DOWN 事件 返回 true
    >
    > onTouchEvent() 方法，如果是可点击的，默认返回 true

  - 如果一个View没有消费DOWN事件，那么MOVE和UP事件还会不会给它？

    > 如果返回false，子view不会接收到其它事件，父view可以接收到其他事件，
    > 因为父view的dispatchTransformedTouchEvent()的返回值由子view的dispatchTouchEvent()决定，如果子view返回false，则不能对mFirstTouchTarget进行赋值
    > 如果mFirstTouchTarget为空，后续事件调用 dispatchTransformedTouchEvent()时，传入的view为null，就会调用父view的dispatchTouchEvent()

  - CANCEL事件是如何触发的？

    > 

- 滑动冲突解决过么？举例说明

- getMeasuredWidth和getWidth有什么区别？什么时候会有不同？如何让getWidth的值跟getMeasuredWidth不同？

  > 生成时机不同，一个在 onMeasure 一个在 onLayout
  >
  > 一般情况下两个值都相等
  >
  > 不相等情况：重写 layout() 修改 super.layout()中参数的值

- activity#onResume中，View展示出来了？原理

- 通过view#post获取view宽高，可以获取到么?原理

- 如何把消息交给子线程的Handler执行？

- new一个Thread，在run方法里面写三行代码：

```vbnet
Loop.prepare；

Loop.loop;

输出一行log。

复制代码
```

问，这行log会不会执行。

- RecyclerView:

Adapter#notifyDataChanged和Adapter#notifyDataRange方法有何区别?加载下一页数据时，我调用哪个合适？在onBindViewHolder方法中，view#getParent有值么？ViewHolder#recycler方法，何时会被调用？stableId了解过么？如果设置了它，页面内的ViewHolder会走onCreateViewHolder和onBindViewHolder方法么？此时这些ViewHolder是放在哪个缓存里的？

## 美团

### 一面：

- 项目经验
- 图片压缩。RGB565，为何不是556呢？
- python脚本。
- dumpsys脚本
- 包体积优化：python脚本。
- 枚举类替换是否有必要？
- 性能优化：布局性能优化。首页刷新优化。
- Activity启动流程。
- 谁根据Intent中的信息去查找对应的进程呢？去查找对应的Activity呢？PKMS。
- PKMS和AMS都是SystemServer启动起来的，它两在同一个进程里面。

app的最近打开过的应用，知道谁做的么？关机开机后依旧保存，如何保存到磁盘的？谁做的？

- 算法题：[143.重排链表](https://link.juejin.cn?target=https%3A%2F%2Fleetcode-cn.com%2Fproblems%2Freorder-list%2F)







### 二面：

- 地理位置相关的，你了解多少？
- 对比前两家公司，地图特殊的方面，你做过什么
- 你在上家公司，成长阶段分为几部分？里程碑的节点有哪些？
- 最近看了哪些书
- 算法题：

[数组中的第K个最大元素](https://link.juejin.cn?target=https%3A%2F%2Fleetcode-cn.com%2Fproblems%2Fkth-largest-element-in-an-array%2F)

[08.09. 括号](https://link.juejin.cn?target=https%3A%2F%2Fleetcode-cn.com%2Fproblems%2Fbracket-lcci%2F)

## 字节-幸福里

### 一面：

- 合规排查，sdk如果调用了HttpURLConnection这种方式，你如何排查?
- 假设三天之内完成，但是sdk三天改不完，你如何sdk处理？
- SDK如果直接调用系统API呢?
- 如何具备经济擦屁股的能力？
- ASM能hook第三方sdk里面的类么？第三方的jar包呢？
- python扫描文件，跟IDE里面全局搜索，有区别么？
- 如何解决还没有暴露的合规问题？比如某天规则增加了？线上如何做？如何减少调整？你有没有比较好的解决方案?
- webview:WebView里面，js调用native方法，它底层的原理是什么？为什么js能调用到Java的方法？webkit提供了这个功能，那它是怎么实现的？js调用native的方法时，是同步的还是异步的?java方法return一个值，js那边能立即拿到么？通过js调用一个java方法，这个java方法都是@onJavaInterface注解过的，这个方法是运行在哪个线程？
- AB实验：分组，有没有需要注意的，或者关键点？怎么做到尽可能随机？

如果N个实验在并行，实验跟实验之间，怎么去防止相互干扰？行为之间会有相互影响。样本隔离开的话就会有问题，多个实验都想要大量数据，如何设计？如何统计实验的指标？如何确认两个指标的关联关系?SQL层面如何实现？

- 网络请求：https请求如何保证安全性？Charles能抓https请求么？能抓的话那安全性在哪？app如何防止代理抓包？

- UI性能优化，加载绘制等，有哪些方面可以做的?

- Java中静态方法，能不能被子类重写？编译会报错么？

  > 不能，但是子类可以声明与父类同名的静态方法，此时父类的静态方法将对子类隐藏，多态调用时会调用父类的方法；

- 匿名内部类能不能访问外部类的私有方法？匿名内部类编译后也是独立的外部类，它为何能访问外部类的私有方法？如果能访问，是不是破坏了java的语义？怎么做到的？

  > 在虚拟机中没有外部类内部类之分都是普通的类，但是编译器会偷偷的做点修改，让内部类中多一个常量引用指向外部类，自动修改内部类构造器，初始化这个常量引用，而外部类通过扫描内部类调用了外部类的哪些私有属性，为这些私有属性创造acess$xxx静态方法。这个方法是返回对应的私有属性的值。所以可以在一个类的外部获取一个类的私有属性的值。
  > 原文链接：https://blog.csdn.net/weixin_33647940/article/details/113366670

- 算法题，多线程顺序打印ABC。

### 二面：

- 图片压缩
- 自定义路由方案
- 注解处理器
- 运行时注解和编译期注解的例子
- 项目的产品指标
- View绘制
- ChoreoGrapher中Callback的类型
- 同步消息、异步消息、消息屏障
- RenderThread了解么
- 包体积优化：access优化、R文件、arsc文件优化
- 埋点方案设计
- jetpack用过么
- GCRoots分析
- 如何监控页面卡顿？ChoreoGrapher方案呢？
- mvi了解么？
- kotlin了解么
- 算法:反转链表，用递归

## 字节-懂车帝

- 项目经历。

- Transform原理，Transform中是否能修改sdk中的class文件。

- Handler机制，IdleHandler执行时机。Handler#postDelay(runnable, 20s) 一个消息，然后把手机时间调整为1分钟后，刚才的runnable会不会执行。

- ThreadLocal是如何做到线程间的不共享数据的，ThreadLocalMap里面的key和value是什么。

- 线程池的核心参数。如果core为5，提交了三个任务，分别被三个线程执行了，其中一个任务提前执行完毕了，此时再提交一个任务，这个任务是如何执行的？

- Jetpack：LifeCycle如何实现的？LiveData，连续set两个相同的数据，会收到几次；连续post两个相同的数据，会收到几次；liveData能在子线程接收么？ViewModel：如何实现Activity旋转之后，依旧能恢复数据的？

  > setValue()只能在主线程中调用：多次调用每次都会收到
  > postValue()可以在任何线程中调用：多次调用，只会收到最后一条更新（当然是在上一条没有发送之前，又收到一条消息时，前一条会被覆盖）
  >
  > postValue() 会通过两个字段比对进行判断，在线程池执行完任务后，会重置该标识，再执行完之前传再多次都会return掉，不会执行多次任务，而在执行任务时，用到的是最新值；

- Java泛型：为何会有Java泛型？泛型擦除；运行期能获取到泛型数据么？List<? extends Demo>; 与 List<? super Demo>的区别；PECS原理。

  > 泛型提供了编译时类型安全检测机制，本质是参数化类型，可以用在方法、类、接口。
  >
  > - 保证了类型的安全性
  >
  >   比如集合，设定了泛型类型，不同类型编译会不通过，没有泛型则添加不同类型，也会编译正常；
  >
  > - 消除强制类型转换
  >
  >   比如从集合中获取数据，没有泛型则需要强制转换成指定类型，使用泛型则不用转换；
  >
  > - 避免了不必要的装箱、拆箱操作，提高程序的性能
  >
  >   在非泛型编程中，将筒单类型作为Object传递时会引起Boxing（装箱）和Unboxing（拆箱）操作，这两个过程都是具有很大开销的。
  >
  > - 提高了代码的重用性
  >
  > 通过子类继承的方式可以获取到泛型的内容。是因为父类虽然擦除了泛型的具体类型，但是子类在继承之后生成的字节码，是通过桥方法转换，这样就保留了我们指定的类型。
  >
  > 
  >
  > PECS（Producer Extends Consumer Super）原则：
  >
  > - 频繁往外读取内容的，适合用上界Extends。? extends T，T及其子类
  > - 经常往里插入的，适合用下界Super。? super T，T及其父类

- View事件传递；事件是从哪里到达Activity的？ViewRootImpl的作用？有A、B两个Button，各自设置了OnClickListener，在A上按下，移动到B上抬起，会触发OnClickListener么？为何?UP事件谁接收到了？OnClickListener触发的条件是哪些？

  > 不会触发 OnClickListener，在 View 的 onTouchEvent() 的 MOVE 中会判断，如果滑出了 view 区域，会改变 pressed 状态，在 UP 中将不会执行到 performClick；
  >
  > 不会触发 B 的任何事件，因为 DOWN 事件没有落在 B 上；
  >
  > - 如果设置了 onTouchListener 比如返回 false 才会执行 onClickListener；
  > - clickable 为 true

- 算法题：①用两个栈实现队列②用栈实现最小栈③二叉搜索树，如何找到最小的公共父节点。

  > - 两个栈实现队列
  >
  >   栈a 只管进元素，出队列的话，如果栈b为空，将a中的出栈压入b中，如果b不为空，则直接将元素从b中pop
  >
  > - 











## 抖音

- 性能优化：UI绘制优化

- 讲一下你做的比较好的项目

- JVM的分区，各个分区的作用

  > - 程序计数器
  > - 虚拟机栈
  > - 本地方法栈
  > - 堆
  > - 方法区

- android里面的两种序列化方式，如何实现的

  > 要在本地持久化存储对象或是在网络上或进程间传输对象，必须首先将对象转换成字节流。将对象转换成字节流的过程，就是所谓的序列化。
  >
  > - Serializable
  > - Parcelable

- SharedPreferences：两种提交方式，缺点，如何解决

  > - apply是无返回值的，而commit是有返回值，所以使用apply提交之后，无法判定是否提交成功，而commit方式可以返回是否提交成功的布尔值，所以在需要得到返回值的情况下建议使用commit方式
  > - apply操作是一个原子性的操作，是先把数据提交到内存中，然后再通过异步任务提交到硬件磁盘上。而commit是个同步操作，直接把数据提交到硬件磁盘上。因此，在多个并发的提交commit的时候，他们会等待正在处理的commit保存到磁盘后在操作，从而降低了效率。而apply只是原子的提交到内容，后面有调用apply的函数的将会直接覆盖前面的内存数据，这样从一定程度上提高了很多效率。

- 网络：浏览器输入域名后，后续的过程。DNS的细节（先从本机开始）。

  > 1. 浏览器输入 www.bilibili.com，敲回车
  > 2. 浏览器会先查找本地的 DNS 缓存，如果有对应的记录， 就可以直接拿到域名对应的 IP 地址，然后就可以直接访问对应的服务器
  > 3. 假设缓存中找不到，就会先在本地的 hosts 文件中查找相应的域名和 IP 地址是否存在
  > 4. 假设 hosts 文件中也找不到，那么浏览器就会把查询请求发送到本地电脑网络设置中的 DNS 服务器上，一般是自动设置好的，自动设置的 DNS 地址一般是管理 wifi 路由器的 IP 地址；当然也可以手动设置，比如常见的 Google DNS 服务器 8.8.8.8
  > 5. 假设如果本地DNS服务器也失效：
  >
  > - 如果未采用转发模式（迭代），本地DNS就把请求发至13台根DNS，根DNS服务器收到请求后，会判断这个域名（如.com）是谁来授权管理，并返回一个负责该顶级域名服务器的IP，本地DNS服务器收到顶级域名服务器IP信息后，继续向该顶级域名服务器IP发送请求，该服务器如果无法解析，则会找到负责这个域名的下一级DNS服务器（如http://baidu.com）的IP给本地DNS服务器，循环往复直至查询到映射结束；
  > - 如果采用转发模式（递归），则此DNS服务器就会把请求转发至上一级DNS服务器，如果上一级DNS服务器不能解析，则继续向上请求。

- Java中的锁，实现。

  > - 自旋锁
  >
  > - 偏向锁
  >
  > - 轻量级锁
  >
  > - 重量级锁
  >
  >   Synchronized
  >
  >   - 对象头，其中的 mark_word 指向 Monitor，Monitor 中有几个字段，标识持有该锁的线程、该线程获取锁的次数、锁重入次数、等待队列；
  >
  >   - 同步代码块
  >
  >     编译成字节码后，有 monitorenter、monitorexit 两个指令，一个 enter，2个 exit，正常释放一个，异常释放一个；
  >
  >   - 同步方法
  >
  >     编译成字节码后，方法的 flag 属性会标记 acc_synchronized 标志，自动在方法的开始和结束添加 monitorenter 和 monitorexit 指令
  >
  > - ReentrantLock
  >
  >   底部实现依赖于 AQS
  >
  >   ```
  >   AQS 定义了模板，具体实现由各个子类完成，它提供了独享锁和共享锁两种模式；
  >   
  >   AQS是JDK提供的一个同步框架，抽象队列同步器，内部维护FIFO双向队列，即CLH同步队列，AQS依赖这个同步队列来管理同步状态（voliate修饰的state，用于标志是否持有锁）。
  >   
  >   当线程获取state失败时，会将当前线程和等待信息等构建成一个Node，将Node放在队列中，同步阻塞当前线程，当state释放时，会把队列中首节唤醒，使其获得同步状态state，底层阻塞线程使用的时park和unpark。
  >   ```

- ANR：定义、如何解决。广播、service的anr时间

- data/trace.txt文件格式

- 包体积优化

- 算法：k个一组反转链表







## 小米造车

### 一面：

- 自定义View有哪几种方式？核心流程。一次完整的绘制流程是怎样的？

  > 组合控件，继承控件，自绘控件

- 如何给控件的背景设置圆角。

  > - Xfermode
  > - BitmapShader
  > - ClipPath
  > - RoundedBitmapDrawable

- Activity生命周期是谁控制的?

  > 单纯的看 Activity 的 onCreate() 等方法是谁调用的话，是 Instrumentation，其中有 callActivityOnCreate() 等方法，调用 activity 对应的方法；
  >
  > 而 Instrumentation 这些方法是在 ActivityThread 中被调用，具体的控制应该是 AMS 中 ActivityStack 中管理，其中有 ActivityState，即 Activity 的各种状态；
  >
  > 后边的 sdk 源码，新增了 ClientTransaction 事务，各种 XXXActivityItem，通过 ActivityTaskManagerService 中提供的 ClientLifecycleManager 执行事务，进行生命周期的转换；
  >
  > 

- 异步消息如何区分？作用

  > Message 中有标志判断是否为异步
  >
  > - setAsynchronous(boolean async) 可设置消息为异步；
  > - isAsynchronous() 用于判断是否为异步消息；
  >
  > Handler 的构造方法中有 async 参数，赋值给 mAsynchronous，在通过 enqueueMessage() 插入消息时，通过 mAsynchronous 判断是否将消息设置为异步；
  >
  > 
  >
  > 异步消息+同步屏障，用于屏幕刷新机制，优先进行刷新任务；

- UI卡顿的原因有哪些？如何优化？

  > - 绘制任务太重、绘制一帧内容耗时太长；
  >
  >   - layout 布局过于复杂
  >
  >   - 过度绘制
  >
  >     > - 去除不必要的背景色
  >     > - 移除嵌套布局
  >     > - 使用 merge、include 标签
  >     > - 使用 ConstraintLayout 减少嵌套
  >     > - 减少透明色，即alpha属性的使用
  >     > - 使用ViewStub标签，延迟加载不必要的视图
  >     > - 使用AsyncLayoutInflater异步解析视图
  >
  > - 主线程太忙，导致 VSync 信号到来时还没有准备好数据从而导致丢帧；
  >
  >   - 主线程耗时操作
  >
  >     > 使用异步、缓存等方案
  >
  > - 主线程挂起
  >
  >   - 异步线程与主线程竞争 CPU 资源
  >
  >     > 设置异步线程优先级为Process.THREAD_PRIORITY_BACKGROUND，减少与主线程的竞争。
  >
  >   - 频繁 GC 使主线程挂起
  >
  >     - 比如内存抖动，导致频繁 GC，GC 时其他线程会停止
  >
  >       > 避免在 onDraw() 这种会频繁调用的方法里创建对象，或者是是 for 循环中创建对象；

- 内存泄漏常见的有哪几种？如何检测？LeakCanary原理，Activity、Fragment

  > 1.非静态内部类持有外部类的引用，如非静态handler持有activity的引用
  > 2.接收器、监听器注册没取消造成的内存泄漏，如广播，eventsbus
  > 3.Activity 的 Context 造成的泄漏，可以使用 ApplicationContext
  > 4.单例中的static成员间接或直接持有了activity的引用
  > 5.资源对象没关闭造成的内存泄漏(如: Cursor、File等)
  > 6.全局集合类强引用没清理造成的内存泄漏(特别是 static 修饰的集合)
  > ————————————————
  >
  > - 注册 Activity、Fragment 的生命周期监听，在 onDestroy() 时，走监听回调，去监测 Activity/Fragment 是否已经被回收；
  > - 通过弱引用持有该对象，并关联引用队列，分配一个 UUID 作为 key，将弱引用对象放入集合中；
  >   - 如果正常回收，则弱引用会进入引用队列，遍历引用队列，将集合中相同 key 的弱引用移除；
  >   - 最终结合中剩余的即为未被正常回收，存在泄漏的对象；
  > - 手动再次 GC，然后再次判断，如果没有被回收，则开始内存泄漏和展示信息的工作。

- 算法题：单链表中，无序，查找最小的n个数。

### 二面:

- framework了解么？

- View绘制？

- so奔溃如何解决？

- 卡顿如何解决？

- ANR如何解决，trace.txt如何看？

  > - ANR 原理是发送延迟消息，在规定的时间内完成任务，然后移除该消息，否则，即执行该消息，告知发生了 ANR；
  >
  > - **老版本Android 系统导出traces**
  >
  >   ```
  >   adb pull data/anr/traces.txt
  >   ```
  >
  >   针对Android 10 以下系统使用上面的命令可以导出traces 文件到运行该命令的当前目录下，如果导出到指定目录，只需在命令后面加上路径和文件名即可，例
  >
  >   ```
  >   adb pull data/anr/traces.txt ~/your path/traces.txt
  >   ```
  >
  >   > 这个命令在Android 10 及以上是不能导出traces 的，是因为以前ANR 一直放在traces文件中，多次出现有覆盖的问题，高版本系统做了优化，会根据时间戳分别生成一个文件，打包导出。
  >
  > - **新版本Android 导出traces**
  >
  >   ```
  >   adb bugreport
  >   ```
  >
  > - **找到目标 traces**
  >
  >   首先打开压缩包最外层目录下的`bugreport-***.txt`
  >    搜索`anr in` 关键字，然后在搜索到的结果匹配我们应用的application id，然后向上滑动，找到 `Dumping to /data/anr/filename` 这一行，其中filename 就是traces 文件名。

- 算法题：打印ViewGroup的信息，仿照adb dumpsys的输出。











