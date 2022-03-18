题目分类temp

#### Java虚拟机

1. jvm虚拟机，堆和栈的结构
2. jvm虚拟机，堆和栈的结构，栈帧，JMM
3. java虚拟机与Dalvik和ART区别
4. 垃圾回收机制与jvm结构
5. Jvm的内存结构，Jvm的垃圾回收，方法区有什么东西？
6. JVM类加载机制了解吗，类什么时候会被加载？类加载的过程具体生命周期是怎样的？
7. jvm的运行时数据结构。栈帧中会有什么异常？方法区里面存放的是什么数据？
8. Java中进程间共享的数据是放在JVM那个分区的？Java中主进程和子进程间的通信，通过哪块内存区域？
9. jvm：运行时数据分区；类加载过程；GCRoot，垃圾回收算法。
10. 成员变量和局部变量的区别。为何成员变量需要jvm在对象初始话过程中赋默认值？
11. JVM,JMM,java加载对象的步骤，classLoader,GC回收算法
12. Java内存模型
13. JMM可见性，原子性，有序性，synchronized可以保证什么？
14. 拉圾回收的GCRoot是什么？
15. gcroot的类型
16. GC回收算法
17. 讲讲你对垃圾回收机制的了解，老年代有什么算法？
18. 说说Java的内存分区
19. 线程内存模型，线程间通信
20. PathClassLoader与DexClassLoader有什么区别
21. 说说你对类加载机制的了解？DexClassLoader与PathClassLoader的区别
22. class文件的组成？常量池里面有什么内容？
23. 自动装箱发生在什么时候？编译期还是运行期
24. 说说你对volatile字段有什么用途？
25. synchronized和volatile的区别？为何不用volatile替代synchronized？类锁和对象锁互斥么？
26. 讲下synchronized和volatile；读写锁和ReentrantLock，synchronized和读写锁的区别。
27. synchronized和volatile区别
28. volatile关键字的作用，怎么保证原子性呢？



#### Java多线程

1. 平常有用到什么锁，synchronized底层原理是什么

2. synchronized是公平锁还是非公平锁,ReteranLock是公平锁吗？是怎么实现的

3. synchronized跟ReentranLock有什么区别？

4. synchronized与ReentranLock发生异常的场景.

5. synchronized的同步原语

6. 讲一下锁，synchronized和Lock。CAS原理

7. synchronized实现。非静态方法A和B在同一个类中，方法A用synchronized修饰，当A方法因为多线程请求有线程阻塞在对象锁上的时候，B方法的访问受不受影响？

8. Lock的实现，以及与synchronized的区别

9. Java的线程同步方式；synchronized和Lock的实现及区别

10. Synchronized底层原理，java锁机制

11. 讲下synchronized和volatile；读写锁和ReentrantLock，synchronized和读写锁的区别。

12. 锁之间的区别

13. 锁的几种状态

14. AQS了解吗？

    

15. 多线程如何实现？有哪些方式？

16. 多线程为何不安全

17. 线程间同步的方法

18. 如何让两个线程循环交替打印

19. 怎么中止一个线程，Thread.Interupt一定有效吗？

20. Java为何会有线程安全问题？如何解决？

21. t1、t2、t3三个线程，如何让三个线程按照顺序依次打印1-100。手写。

    

22. 阿里编程规范不建议使用线程池，为什么？

23. 四种线程池原理？

24. 线程池了解多少？拒绝策略有几种,为什么有newSingleThread

25. 线程池的参数

26. 线程的使用。讲下线程池的类型，线程池对象的参数，线程池最大线程数和核心线程数的关系，task的优先级如何实现？（优先级队列）

27. 线程池如何配置，核心线程数你一般给多少

28. 线程池原理



#### 泛型

1. 泛型是怎么解析的，比如在retrofit中的泛型是怎么解析的

2. 泛型有什么优点？

3. 泛型为什么要擦除？kotlin的泛型了解吗？泛型的pecs原则

4. 泛型擦除，为何会有擦除？擦除的时机。通配符。
   下面这段代码有问题么？有什么问题？为何会有这个问题？

   ```java
   List<? extends Object> list = new ArrayList<>();
   list.add(123);
   Object obj = list.get(0);
   ```

5. 既然泛型有编译期类型擦除，那么运行时无法获取到具体类型；而反射能在运行时获取到Class的类型；它们一个获取不到，一个可以获取到，这不就是矛盾么？请解释下细节。

6. 泛型：为何会有协变和逆变，PECS规则。

7. kotlin泛型：out和in.

8. 泛型和泛型擦除。kotlin真泛型的实现；泛型中T和？的区别，List<?>和List有什么区别；泛型里的super和extends区别；泛型为何会有擦除；擦除的时机；泛型的编译器类型检查。

9. ArrayList和LinkendList区别，List泛型擦除，为什么反射能够在ArrayList< String >中添加int类型

10. java泛型，协变和逆变



#### Android基础

##### 一、四大组件 UI 相关

1. A Activity打开B Activity的生命周期变化，会有什么方法打断吗？

2. Activity生命周期

3. Activity启动模式，以及各启动模式生命周期问题

4. 讲一下Activity的TaskRecord，也就是四种启动模式。

5. activity启动模式：standard、singleTop、singleTask、singleInstance。taskAffinity，allowTaskReparting，常见应用场景。

6. activity生命周期。A启动B，生命周期；B回到A，生命周期

7. 启动模式及其用法。**A应用的A1页面启动B应用的B1页面，A1和B2都是standard模式，B1启动后B1在那个任务栈，按下back键后显示那个页面，再按一次back键呢？**

8. activity启动模式，有哪些不同

9. activity启动模式（给例子具体分析，A(标准)-》B(单例)-》C(singleTop)-》D(singleTask)，分析有几个栈，每个栈内的activity）

10. ActivityA启动了ActivityB后，两个Activity的生命周期是怎样的？

11. Activity生命周期，按Home按键后的生命周期

12. Activity的onSaveInstance方法何时调用？它跟onPause、onStop的调用顺序如何？

13. Activity A启动Activity B，调用顺序。最后Activity A的onStop一定会调用么？

    

14. Activity和Fragment的生命周期；Fragment#onHiddenChanged是生命周期方法么？如何触发？

15. Activity和fragment生命周期区别，fragment正常添加和viewpager添加的区别，fragment懒加载原理，FragmentPagerAdapter 和 FragmentStatePagerAdapter

16. Fragment hide show生命周期

17. Fragment hide show生命周期变化

18. Fragment replace生命周期变化

19. fragment周期，两个fragment切换周期变化，fragment通信

20. Activity和Fragment的通信方式；系统为何会设计Fragment#setArgument方法。

21. 四大组件，fileprovider和Contentprovide区别

22. Activity怎么启动Service，Activity与Service交互，Service与Thread的区别



#### 动画

1. 介绍一下android动画
2. 属性动画更新时会回调onDraw吗？
3. 补间动画与属性动画的区别，哪个效率更高？
4. 动画里面用到了什么设计模式？
5. 动画连续调用的原理是什么？





#### 启动流程

1. activity启动流程
2. 说说App的启动过程,在ActivityThread的main方法里面做了什么事，什么时候启动第一个Activity？
3. Launcher启动图标，有几个进程？
4. Activity启动流程：AMS、zygote、ApplicationThread之间交互；
5. Application启动流程
6. ContentProvider启动流程
7. 启动未注册的Activity
8. Launcher启动App的流程，中间有几种跨进程通信(socket)
9. startActivity方法是异步的么？同步的。



#### window、activity、decorview

1. Activity中的Window的初始化和显示过程
2. attachToWindow什么时候调用？
3. PhoneWindow是在哪里初始化的？
4. Window和Activity的对应关系。除了Activity还有别的方式显示Window出来么？
5. Activity,view,window联系





#### View 事件分发

1. 自定义View,事件分发机制讲一讲

2. 讲一下事件分发机制,RecyclerView是怎么处理内部ViewClick冲突的

3. 说说事件分发机制，怎么写一个不能滑动的ViewPager

   > 重写onInterceptTouchEvent() return false
   >
   > ViewPager 的滑动冲突是通过外部拦截法，在 onInterceptTouchEvent() 中根据手指滑动判断斜率，小于 0.5 即水平方向，进行拦截

4. 事件分发，多点触碰处理，是在onTouchEvent方法里面。

   > 每个 MotionEvent 中都包含了 当前屏幕所有触控点的信息，它的内部用了一个数组来存储不同的触控 id 所对应的坐标数值。
   >
   > 当一个子view消费了down事件后，ViewGroup 会为该view创建一个TouchTarget，这个TouchTarget就包含了该View的实例与触控id。这里的触控id可以是多个，也就是一个view可接受多个触控点的时间序列。
   >
   > 当一个MotionEvent到来之时，ViewGroup会将其中的触控点信息拆开，再分别发送给感兴趣的子view。从而达到精准发送触控点信息的目的。
   >
   > 为什么 TouchTarget 是一个链表？
   >
   > 对于 ViewGroup 来说，他有很多个子View，如果不同的子view接受了不同的触控点的down事件，就是通过 TouchTarget 链表来记录的。

5. 讲下事件分发。如果onInterceptTouchEvent返回true，但是onTouchEvent返回了false，是什么效果？如果还想让其他View接收事件，该怎么做？

   > 交由 Activity onTouchEvent() 处理；
   >
   > 不拦截呗

6. ViewPager中嵌套ViewPager怎么处理滑动冲突

   > 内部解决，在子ViewPager中 dispatchTouchEvent() 中判断，DOWN 事件请求父view不拦截，MOVE 事件判断子 ViewPager 是否滑动到了第一个或者最后一个，不是的话，请求父View不拦截，如果是的话，由父View拦截处理

7. 滑动冲突如何解决？有几种方式？具体从哪个事件开始拦截？在哪里拦截？比如双层ViewPager嵌套的滑动冲突如何解决。



#### RecyclerView、ListView

1. 讲一下RecyclerView的缓存机制,滑动10个，再滑回去，会有几个执行onBindView
2. 如何实现RecyclerView的局部更新，用过payload吗,notifyItemChange方法中的参数？
3. RecyclerView的缓存结构是怎样的？缓存的是什么？cachedView会执行onBindView吗?
4. RecyclerView嵌套RecyclerView，NestScrollView嵌套ScrollView滑动冲突
5. RecyclerView 缓存结构，RecyclerView预取，RecyclerView局部刷新
6. RecyclerView的特点和缓存
7. RecyclerView的缓存，局部刷新用过么？
8. recyclerview和listview区别
9. 嵌套滑动机制 NestedScrolling



#### ViewPager

1. ##### 自定义 ViewPager 思路

   > 1、继承 ViewGroup；
   > 2、构造函数中创建 Scroller 实例和 TouchSlop 的值，前者用于平滑滚动，后者用于判断用户是否在拖动；
   > 3、重写 onMeasure() 测量布局里的每一个子控件的大小；
   > 4、重写 onLayout() 为布局里的每一个子控件在水平方向上进行布局；
   > 5、重写 onInterceptTouchEvent() 记录用户手指按下的 x 坐标位置，以及用户手指在屏幕上拖动时的 x 坐标位置，大于 TouchSlop 值时，就认为用户正在拖动，然后就拦截事件；
   > 6、拦截事件后，会将事件交给 ViewPager 的 onTouchEvent() 来处理。如果是 ACTION_MOVE 事件，说明用户正在拖动，通过 scrollBy() 对布局内容进行拖动，用户拖动多少就 scrollBy 多少。
   > 或者通过 GestureDetector 监听用户滑动，进行view的滚动；
   >
   > 在 ACTION_UP 中根据滑动位置的判断，防止出界，通过 scrollTo() 滑回到正确位置；
   >
   > 7、通过 Scroller.startScroll() invalidate()
   > 8、重写 computeScroll() 在其内部完成平滑滚动；

2. ViewPager2原理

3. viewpager切换掉帧有什么处理经验？

4. ViewPager切换Fragment什么最耗时？

5. ViewPager中嵌套ViewPager怎么处理滑动冲突

6. 怎么写一个不能滑动的ViewPager



#### View 的绘制、自定义View、屏幕刷新机制

1. View绘制流程。onMeasure、onLayout、onDraw。

2. View的绘制流程。MeasureSpec，关键方法，

3. 绘制相关：

   > ①requestLayout调用后，都会调用哪些方法？
   > ②onMeasure、onLayout、onDraw这三个方法中，哪个最耗时？onMeasure和onLayout呢？
   > ③Choreography的作用。它的上游和下游各自是哪个。Choreography发布了订阅消息，同类型的Callback还有哪些？这些Callback之间的优先级如何？vsync机制。
   > ④Surface对象了解么？作用，何时初始化，怎么使用的。
   > ⑤一个Button的点击事件中，调用requestLayout，接下来哪些方法会被调用？
   > ⑥Surface和Window的关系
   > ⑦SurfaceView的实现
   > ⑧View#draw()方法细节
   > ⑨绘制的数据是如何提交到远端的SurfaceFlinger
   > ⑩GPU和surfaceFlinger之间的设计思想是什么？surfaceFlinger具体作用是什么？它对数据做了哪些操作？
   > ⑪硬件加速了解么？GPU如何高效绘制？

4. View绘制流程；requestLayout和invalidate区别；invalidate每次都会触发onDraw么？View#onLayout每次会触发么？

5. 讲下View绘制流程。canvas可以画Bitmap么？画布要做旋转、缩放、平移等操作该如何实现？

6. view绘制原理 （可以先说下基本view绘制，然后再说下屏幕刷新机制）

7. view绘制（从onSync()开始）

8. onMeasure,onLayout,onDraw关系

9. 讲下onMeasure方法：

   > ①如何测量
   > ②测量模式
   > ③入参为什么是int类型？
   > ④为什么会多次调用onMeasure和onLayout方法？

10. 自定义View的几种方式。onMeasure、onLayout、onDraw方法都何时需要重写。自定义属性的作用。

11. 讲下你的自定义View，为何如此设计？

12. 自定义View，测量模式的匹配

13. 自定义view，中英文字符串宽高测量显示，测量算法，可扩展性

14. 自定义圆角图片

15. 自定义LinearLayout，怎么测量子View宽高

16. 自定义View的关键步骤，注意事项，你的理解

17. 自定义View：支持换行的尾部标签的实现。

18. 如何自定义实现一个FlexLayout

19. 自定义实现一个九宫格如何实现

20. 说说你对屏幕刷新机制的了解，双重缓冲，三重缓冲，黄油模型

21. 为什么view.post可以获得宽高，有看过view.post的源码吗？

22. onCreate,onResume,onStart里面，什么地方可以获得宽高

23. 获取view的宽高，更新view的方式

24. 

