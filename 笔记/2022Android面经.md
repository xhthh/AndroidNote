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
- 事件分发：View设置了一个onClickListener，那么它的onTouchEvent中，DOWN的时间，返回的是true还是false？为何?

如果一个View没有消费DOWN事件，那么MOVE和UP事件还会不会给它？

CANCEL事件是如何触发的？

- 滑动冲突解决过么？举例说明
- getMeasuredWidth和getWidth有什么区别？什么时候会有不同？如何让getWidth的值跟getMeasuredWidth不同？
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
- 匿名内部类能不能访问外部类的私有方法？匿名内部类编译后也是独立的外部类，它为何能访问外部类的私有方法？如果能访问，是不是破坏了java的语义？怎么做到的？
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
- Java泛型：为何会有Java泛型？泛型擦除；运行期能获取到泛型数据么？List<? extends Demo>; 与 List<? super Demo>的区别；PECS原理。
- View事件传递；事件是从哪里到达Activity的？ViewRootImpl的作用？有A、B两个Button，各自设置了OnClickListener，在A上按下，移动到B上抬起，会触发OnClickListener么？为何?UP事件谁接收到了？OnClickListener触发的条件是哪些？
- 算法题：①用两个栈实现队列②用栈实现最小栈③二叉搜索树，如何找到最小的公共父节点。

## 抖音

- 性能优化：UI绘制优化
- 讲一下你做的比较好的项目
- JVM的分区，各个分区的作用
- android里面的两种序列化方式，如何实现的
- SharedPreferences：两种提交方式，缺点，如何解决
- 网络：浏览器输入域名后，后续的过程。DNS的细节（先从本机开始）。
- Java中的锁，实现。
- ANR：定义、如何解决。广播、service的anr时间
- data/trace.txt文件格式
- 包体积优化
- 算法：k个一组反转链表

## 小米造车

### 一面：

- 自定义View有哪几种方式？核心流程。一次完整的绘制流程是怎样的？
- 如何给控件的背景设置圆角。
- Activity生命周期是谁控制的?
- 异步消息如何区分？作用
- UI卡顿的原因有哪些？如何优化？
- 内存泄漏常见的有哪几种？如何检测？LeakCanary原理，Activity、Fragment
- 算法题：单链表中，无序，查找最小的n个数。

### 二面:

- framework了解么？
- View绘制？
- so奔溃如何解决？
- 卡顿如何解决？
- ANR如何解决，trace.txt如何看？
- 算法题：打印ViewGroup的信息，仿照adb dumpsys的输出。