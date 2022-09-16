## Android

### Android基础

#### 一、Activity

##### 1、四种启动模式

1. 标准模式（standard）

   每次启动一个 Activity 都会重新创建一个新的实例，不管这个实例是否已经存在。

2. 栈顶复用模式（singleTop）

   如果新 Activity 已经位于任务栈的栈顶，那么此 Activity 不会被重新创建，同时它的 onNewIntent() 方法会被回调，通过此方法的参数我们可以取出当前请求的信息。

3. 栈内复用模式（singleTask）

   只要 Activity 在一个栈中存在，那么多次启动此 Activity 都不会重新创建实例，回调 onNewIntent() ，同时 clearTop，将该 Activity 之上的 Activity 都弹出栈。

4. 单例模式（singleInstance）

   此模式的 Activity 只能单独位于一个任务栈中，同时栈内复用，后续的请求均不会创建新的 Activity 实例。

5. **TaskAffinity**

   这个参数标识了一个 Activity 所需要的任务栈的名字，默认情况下，所有 Activity 所需的任务栈的名字为应用的包名。

   可以在AndroidManifest.xml中设置 android:taskAffinity，用来指定Activity希望归属的栈。

   TaskAffinity 属性主要和 singleTask 启动模式或者 allowTaskReparenting 属性配对使用。（<font color='red'>单独使用 taskAffinity 属性不生效，两个启动模式均为 standard 的 Activity，指定不同的 taskAffinity 还是会创建在同一个任务栈中</font>）

   ①taskAffinity 与 FLAG_ACTIVITY_NEW_TASK 或者 singleTask 配合。如果新启动 Activity 的 taskAffinity 和栈的 taskAffinity 相同则加入到该栈中；如果不同，就会创建新栈。
   ②taskAffinity 与 allowTaskReparenting 配合。如果 allowTaskReparenting 为true，说明activity具有转移能力。一个在后台任务栈中的 Activity A，当有其他任务进入前台，并且 taskAffinity 与 A 相同，则会自动将 A 添加到当前启动的任务栈中。

|    启动模式    |     首次进入     |      在栈顶二次进入       |           不在栈顶二次进入            |   home键后进入    |   back键后进入   |
| :------------: | :--------------: | :-----------------------: | :-----------------------------------: | :---------------: | :--------------: |
|    Standard    | onCreate onStart |     onCreate onStart      |           onCreate onStart            | onRestart onStart | onCreate onStart |
|   SingleTop    | onCreate onStart | onNewIntent<br />onResume |           onCreate onStart            | onRestart onStart | onCreate onStart |
|   SingleTask   | onCreate onStart | onNewIntent<br/>onResume  | onNewIntent<br/>onRestart<br/>onStart | onRestart onStart | onCreate onStart |
| SingleInstance | onCreate onStart | onNewIntent<br/>onResume  | onNewIntent<br/>onRestart<br/>onStart | onRestart onStart | onCreate onStart |



##### 2、启动模式的使用

1. singleTop 的使用场景

   - 消息推送

     通知栏弹出Notification，点击Notification跳转到指定Activity，但是如果我现在页面就停留在那个指定的Activity，会再次打开我当前的Activity，这样返回的时候回退的页面和当前页面一样，感官上就会很奇怪。

   - 登录

   - 耗时操作返回页面

     一种场景 从activity A启动了个service进行耗时操作，或者某种监听，这个时候你home键了，service收集到信息，要返回activityA了，就用singleTop启动，实际不会创建新的activityA，只是resume了。不过使用standard又会创造2个A的实例。

2. singleTask

   - 一般App主页面会采用 singleTask 的启动模式

     保证任务栈中只有一个 MainActivity，不管打开多少个页面，再次打开 MainActivity 时都会将其上方所有的 Activity 弹出栈，保证退出应用的时候，Activity 都能够销毁。

##### 3、Application 的 Context 启动 Activity 会怎样？

使用 Application 和 Service 类的 Context 启动 standard 的 Activity 会报错，因为它们没有任务栈，需要为待启动的 Activity  指定 FLAG_ACTIVITY_NEW_TASK，但是此时 Activity 是以 singleTask 的模式启动的。

##### 4、 Activity A 启动 Activity B 的生命周期？A 一定会走 onStop() 吗？为什么要这样做？

由 ActivityStack 中的代码决定：

- A 启动 B

  A onPause()--->B onCreate()--->B onStart()--->B onResume()--->A onStop()

- B 按返回键

  B onPause()--->A onRestart()--->A onStart()--->A onResume()--->B onStop()--->B onDestroy()

如果 B 是透明主题的 Activity 就不会走 A 的 onStop()。

这样做的原因应该是为了，视觉效果体验上更好些，如果前一个 Activity 的 onStop() 先走 页面不可见，而后一个 Activity 的 onResume() 还没走 还没显示出来，可能会有一瞬间的白屏，体验不好。



##### 5、onNewIntent()

MainActivity 是 singleTask 的启动模式，且处于栈顶可见状态，再次启动该页面，会走 onNewIntent()--->onResume()。

onNewIntent不会再调用onCreate方法了，会直接调用onStart与onResume。如果是已经不可见的Activity（调用了onStop的，则会先调用onRestart之后在调用onStart方法）。

##### 6、Activity的生命周期

![img](C:\Users\E480\Desktop\文章图片\activity生命周期.png)

1. 下拉手机状态栏会走生命周期吗？

   不会

2. onStart() 与 onResume() 的区别？

   onStart() 表示 Activity正在启动，此时 Activity 已经出现，但还没有处于前台，无法与用户交互。

   onResume() 表示 Activity 已经可见，并且出现在前台，可以与用户交互。

3. onPause() 与 onStop() 的区别？

   onPause() 表示 Activity 正在停止，失去焦点，仍可见。

   onStop() 表示 Activity 不可见，处于后台。

   场景：

   - 锁屏的时候户依次调用onPause()和onStop()
   - Toast，Dialog，menu ，三者都不会使Activity调用onPause();
   - 一个非全屏的Activity在前面时，后面的Activity只调用onPause();

4. onPause() 中为什么不能做耗时操作做？

   **onPause中不能进行耗时操作，会影响到新Activity的显示。因为onPause必须执行完，新的Activity的onResume才会执行。**

   在onPause()中你应该避免执行占用大量CPU的工作，比如写入数据库。因为这会减慢你可视化转换到下一个activity的速度。

5. Activity 上有 Dialog 的时候按 Home 键时的生命周期

   弹出 Dialog 并不会使得 Activity 走任何生命周期。

   按下 Home 键后：

   onPause()--->onStop()

   我们弹出的AlertDialog对话框实际上是Activity的一个组件，我们对Activity并不是不可见而是被一个布满屏幕的组件覆盖掉了其他组件，所以我们无法对其他内容进行操作，也就是AlertDialog实际上是一个布满全屏的组件。

   > Activity 生命周期由 AMS 进行管理，当一个 Activity 启动另一个 Activity 时，会经历生命周期的转换。
   >
   > Dialog 和 Toast 都是通过 window.addView() 来显示的，可以看作当前 Activity 页面的一部分 View，所以不会对生命周期造成影响。
   
   <font color='red'>怎样使得，按 Home 键后，再次点击图标进入 App，还在之前的页面？</font>

##### 7、如何摧毁一个Activity？

- 按 Back 键
- 调用 finish()

##### 8、Activity 与 Activity 之间的通信方式？

- Intent

  大小限制 1M

- 借助类的静态变量

- 借助全局变量/Application

- 借助外部工具
  – 借助SharedPreference
  – 使用Android数据库SQLite
  – 赤裸裸的使用File
  – Android剪切板

- 借助Service

Activity A 跳转B B跳转C，A不能直接跳转到C，A如何传递消息给C？

- intent 传参
- 使用全局变量

##### 9、Activity 状态的保存于恢复

在Activity由于异常情况下终止时，系统会调用onSaveInstanceState来保存当前Activity的状态。这个方法的调用是在onStop之前，它和onPause没有既定的时序关系，该方法只在Activity被异常终止的情况下调用。当异常终止的Activity被重建以后，系统会调用onRestoreInstanceState，并且把Activity销毁时onSaveInstanceState方法所保存的Bundle对象参数同时传递给onRestoreInstanceState和onCreate方法。因此，可以通过onRestoreInstanceState方法来恢复Activity的状态，该方法的调用时机是在onStart之后。**其中onCreate和onRestoreInstanceState方法来恢复Activity的状态的区别：** onRestoreInstanceState回调则表明其中Bundle对象非空，不用加非空判断。onCreate需要非空判断。建议使用onRestoreInstanceState。



**onSaveInstanceState() 方法调用时机？**

系统配置发生改变时导致Activity被杀死并重新创建、资源内存不足导致低优先级的Activity被杀死
系统会用onSaveInstanceState()来保存Activity的状态，调用时机在onStop()之后（Android api为28时，onStop会在其之前，28以前是在其之后）。

场景：

- 横竖屏切换
- 按home键返回，不管是返回桌面还是切到其它程序，都会走
- 熄屏
- 启动一个新的Activity时

​    onSaveInstanceState方法和onRestoreInstanceState方法“不一定”是成对的被调用的，
​    onRestoreInstanceState被调用的前提是，activity A“确实”被系统销毁了，而如果仅仅是停留在有这种可能性的情况下，则该方法不会被调用

​    比如②③④都不会走onRestoreInstanceState()



##### 10、横竖屏切换

- 默认情况

  onPause()-> onStop()->onSaveInstanceState()->onDestroy()->onCreate()->onStart()->onRestoreInstanceState->onResume()

- 在 Mainfest 中配置 configChanges="orientation|screenSize"

  只会走 onConfigurationChanged()



##### 11、SharedPreferences 使用及原理

SharedPreferences只能保存简单类型的数据，例如，String、int等。一般会将复杂类型的数据转换成Base64编码，然后将转换后的数据以字符串的形式保存在 XML文件中，再用SharedPreferences保存。

getSharedPreferences()方法本身是Context这个接口中定义的一个抽象方法，由ContextImpl类负责实现。SharedPreferences 是一个接口，声明抽象方法，具体实现是由 SharedPreferencesImpl 来完成。



> - SP 不能跨进程使用；
> - commit 是同步操作，apply 是异步操作
> - commit 有返回值，apply 没有返回值

原理：

> SharedPreference的数据本质上是保存在一个xml文件中，这个xml文件存放在/data/data/应用包名/shared_prefs/目录下。
>
> SharedPreferences是一个接口，里面定义了很多数据存储与获取的接口。
>
> getSharedPreferences() 实现在 ContextImpl 中，创建一个 SharedPreferences 对象，其中会先判断是否存在对应 xml 文件，如果发现存在则会有一个预加载操作，这个操作是把 xml 文件的内容通过 I/O 操作和 XmlUitl 解析后存入一个 map 对象中，所以我们调用 SharedPreferences::getString();等 get 操作实际上是不会对文件做 I/O 操作，而是直接访问刚刚的 map 集合的内容，这提高了效率，如果对应的 xml 不存在则重新创建一个对应的 xml 文件。
>
> put 写操作:写操作也有两步，一是把数据先写入内存中，即 map 集合，二是把数据写入硬盘文件中。

https://juejin.cn/post/6884505736836022280

https://juejin.cn/post/6844903758355234824

###### 1、线程安全

SP 是线程安全的，一共用到了3个锁，mLock、mWritingToDiskLock、mEditorLock；

- 读

  getXXX()，读取内存中`mMap`的值并返回，一个锁搞定；

- 写

  - putXXX()，会先保存到一个 map 中，即 mModified，这里用了一把锁，即 mEditorLock；

    > 不和`mMap`公用同一把锁的原因估计是，在`apply()`被调用之前，`getXXX`和`putXXX`理应是没有冲突的。

  - commit() 和 apply() 都会调用 commitToMemory()，将 mModified 和 mMap 两个中的数据进行合并，此时会使用到 mLock 和 mEditorLock 两个锁；

  - 最后进行文件更新时也会用到一把锁 mWritingToDiskLock；

###### 2、ANR

`apply()`方法设计的初衷是为了规避主线程的`I/O`操作导致`ANR`问题的产生，但是还是会造成 ANR，原因归根到底则是`Android`的另外一个机制；

> 在`apply()`方法中，首先会创建一个等待锁，根据源码版本的不同，最终更新文件的任务会交给`QueuedWork.singleThreadExecutor()`单个线程或者`HandlerThread`去执行，当文件更新完毕后会释放锁。
>
> 但当`Activity.onStop()`以及`Service`处理`onStop`等相关方法时，则会执行 `QueuedWork.waitToFinish()`等待所有的等待锁释放，因此如果`SharedPreferences`一直没有完成更新任务，有可能会导致卡在主线程，最终超时导致`ANR`。
>
> 比如太频繁无节制的`apply()`，导致任务过多。

Google 在 Activity 和 Service 调用 onStop 之前阻塞主线程来处理 SP，我们能猜到的唯一原因是尽可能的保证数据的持久化。因为如果在运行过程中产生了 crash，也会导致 SP 未持久化，持久化本身是 IO 操作，也会失败。



###### 3、进程安全

SP 注释中写明了不支持跨进程，由于没有使用跨进程的锁，`SharedPreferences`是进程不安全的，在跨进程频繁读写会有数据丢失的可能。

如何保证`SharedPreferences`进程的安全呢?

实现思路很多，比如：

- 使用文件锁，保证每次只有一个进程在访问这个文件；
- 或者对于`Android`开发而言，`ContentProvider`作为官方倡导的跨进程组件，其它进程通过定制的`ContentProvider`用于访问`SharedPreferences`，同样可以保证`SharedPreferences`的进程安全；

###### 4、文件损坏 & 备份机制

`SharedPreferences`的写入操作正式执行之前，首先会对文件进行备份，将初始文件重命名为增加了一个`.bak`后缀的备份文件，这之后，尝试对文件进行写入操作，写入成功时，则将备份文件删除。

若因异常情况（比如进程被杀）导致写入失败，进程再次启动后，若发现存在备份文件，则将备份文件重名为源文件，原本未完成写入的文件就直接丢弃。

现在，通过文件备份机制，我们能够保证数据只会丢失最后的更新，而之前成功保存的数据依然能够有效。



#### 二、Service

##### 1、Service 的两种启动方式及其生命周期？原理？

**1）startService / stopService**

生命周期顺序：onCreate->onStartCommand->onDestroy

如果一个Service被某个Activity 调用 Context.startService方法启动，那么不管是否有Activity使用bindService绑定或unbindService解除绑定到该Service，该Service都在后台运行，直到被调用stopService，或自身的stopSelf方法。当然如果系统资源不足，android系统也可能结束服务，还有一种方法可以关闭服务，在设置中，通过应用->找到自己应用->停止。

**注意点：**

①第一次 startService 会触发 onCreate 和 onStartCommand，以后在服务运行过程中，每次 startService 都只会触发 onStartCommand

②不论 startService 多少次，stopService 一次就会停止服务

**2）bindService / unbindService**

生命周期顺序：onCreate->onBind->onUnBind->onDestroy

如果一个Service在某个Activity中被调用bindService方法启动，不论bindService被调用几次，Service的onCreate方法只会执行一次，同时onStartCommand方法始终不会调用。

当建立连接后，Service会一直运行，除非调用unbindService来接触绑定、断开连接或调用该Service的Context不存在了（如Activity被Finish——**即通过bindService启动的Service的生命周期依附于启动它的Context**），系统在这时会自动停止该Service。

**注意点：**

第一次 bindService 会触发 onCreate 和 onBind，以后在服务运行过程中，每次 bindService 都不会触发任何回调

##### 2、两种启动方式的区别？

startService()启动的service和调用者的生命周期没有关系，是独立的，调用者结束之后不会结束service，当调用者再次调用stopService()可以结束service

bindService()启动的service当调用者生命结束了，系统会自动调用unBindService()

当同时执行了启动和绑定同一个service时，则只有当以上两个条件都满足时service才会结束掉



##### 3、Service 与 Activity 之间的通信方式？

- Binder

  Service 中 自定义 MyBinder，提供一个获取 Service 的方法 getService()，在 Service 类中 重写 onBind() 方法，返回 MyBinder 对象。

  Activity 中 在 ServiceConnection 的回调用中获取 Binder 并获取 Service 实例，就可调用 Service 中的方法。

- 广播 BroadcastReceiver

  Service 中 sendBroadcast(intent)

  Activity 中注册广播接收器，接收 Service 发送的消息。

  比如Service要向多个Activity发送同样的消息的话，用这种方法就更好

##### 4、一个 service 和 5 个 activity 绑定，有几个 service实例？ 4 个 activity 退出，service 还在吗？

多个activity与service绑定只会在第一个activity绑定时走 onCreate、onBind() 回调，service的实例是在多个activity之间共享的，当一个activity 解除绑定或者退出销毁时，只要还有其他activity与service绑定是不会走 onBind() 和 onDestroy() 方法的，但是会走 serviceConnection() 中的回调，只有最后一个activity解除绑定、退出销毁时 service 才会走 onBind() 和 onDestroy() 方法。



#### 三、广播 BroadcastReceiver

一个全局的监听器，四大组件之一。

##### 1、作用及应用场景

用于监听 / 接收 应用发出的广播消息，并做出响应。

应用场景：

- 同一 App 内部的同一组件内的消息通信（单个或多个线程之间）；
- 同一 App 内部的不同组件之间的消息通信（单个进程）；
- 同一 App 具有多个进程的不同组件之间的消息通信；
- 不同 App 之间的组件之间消息通信；
- Android系统在特定情况下与App之间的消息通信，如：网络变化、电池电量、屏幕开关等。

##### 2、实现原理

- `Android`中的广播使用了设计模式中的**观察者模式**：基于消息的发布/订阅事件模型。

  > 因此，Android将广播的**发送者 和 接收者 解耦**，使得系统方便集成，更易扩展

- 模型中有3个角色：

  1. 消息订阅者（广播接收者）
  2. 消息发布者（广播发布者）
  3. 消息中心（`AMS`，即`ActivityManagerService`）

[![img](https://camo.githubusercontent.com/5b295c041938cebb9de6d544245a000073888c94/687474703a2f2f75706c6f61642d696d616765732e6a69616e7368752e696f2f75706c6f61645f696d616765732f3934343336352d303839366261386439313535313430652e706e673f696d6167654d6f6772322f6175746f2d6f7269656e742f7374726970253743696d61676556696577322f322f772f31323430)](https://camo.githubusercontent.com/5b295c041938cebb9de6d544245a000073888c94/687474703a2f2f75706c6f61642d696d616765732e6a69616e7368752e696f2f75706c6f61645f696d616765732f3934343336352d303839366261386439313535313430652e706e673f696d6167654d6f6772322f6175746f2d6f7269656e742f7374726970253743696d61676556696577322f322f772f31323430)

- 原理描述：

  1. 广播接收者 通过 `Binder`机制在 `AMS` 注册

  2. 广播发送者 通过 `Binder` 机制向 `AMS` 发送广播

  3. `AMS` 根据 广播发送者 要求，在已注册列表中，寻找合适的广播接收者

     > 寻找依据：`IntentFilter / Permission`

  4. `AMS`将广播发送到合适的广播接收者相应的消息循环队列中；

  5. 广播接收者通过 消息循环 拿到此广播，并回调 `onReceive()`

**特别注意**：广播发送者 和 广播接收者的执行 是 **异步**的，发出去的广播不会关心有无接收者接收，也不确定接收者到底是何时才能接收到。

##### 3、广播的使用

1. 自定义广播接收器

   onReceive() 中接收广播

2. 注册广播

   - 静态注册 在AndroidMainfest中注册
   - 动态注册 需要解注册

3. 发送广播

##### 4、广播的分类

每个广播都有特定的Intent - Filter（包括具体的action），通过这两个数据进行注册者的匹配。

广播的类型主要分为5类：

- 普通广播（Normal Broadcast）

- 系统广播（System Broadcast）

  如开机、网络状态变化、拍照等等

- 有序广播（Ordered Broadcast）

  - 发送出去的广播被广播接收者按照先后顺序接收。
  - 按照 Priority 属性从大到小，相同则动态注册的优先。
  - 先接收的广播接受者可以拦截、修改广播。

- 粘性广播（Sticky Broadcast）

- App应用内广播（Local Broadcast）

  - 背景 Android中的广播可以跨App直接通信（exported对于有intent-filter情况下默认值为true）

  - 冲突可能出现的问题：

    - 其他App针对性发出与当前App intent-filter相匹配的广播，由此导致当前App不断接收广播并处理；
    - 其他App注册与当前App一致的intent-filter用于接收广播，获取广播具体信息； 即会出现安全性 & 效率性的问题。

  - 解决方案 使用App应用内广播（Local Broadcast）

    - App应用内广播可理解为一种局部广播，广播的发送者和接收者都同属于一个App。
    - 相比于全局广播（普通广播），App应用内广播优势体现在：安全性高 & 效率高

  - 具体使用1\- 将全局广播设置成局部广播

    - 注册广播时将exported属性设置为*false*，使得非本App内部发出的此广播不被接收；
    - 在广播发送和接收时，增设相应权限permission，用于权限验证；
    - 发送广播时指定该广播接收器所在的包名，此广播将只会发送到此包中的App内与之相匹配的有效广播接收器中。

  - 具体使用2 \- 使用封装好的LocalBroadcastManager类 使用方式上与全局广播几乎相同，只是注册/取消注册广播接收器和发送广播时将参数的context变成了LocalBroadcastManager的单一实例。

    > 注：对于LocalBroadcastManager方式发送的应用内广播，只能通过LocalBroadcastManager动态注册，不能静态注册

##### 5、广播 ANR

默认情况下，广播接收器运行在UI线程，因此，onReceive方法不能执行耗时操作，否则将导致ANR。

广播的超时时间是定义在AMS里：
BROADCAST_FG_TIMEOUT：10s
BROADCAST_BG_TIMEOUT：60s

##### 6、广播可以请求网络数据吗？

不建议，网络请求一般都是耗时操作，而广播实在主线程运行的，耗时操作会导致线程阻塞，很容易导致ANR。可以在广播中使用子线程进行网络请求，但不建议，更建议在Service中进行。广播可以用于监听网络变化。

##### 7、本地广播原理

- 核心 LocalBroadcastManager，初始化时构建了 Handler；

- 两个静态内部类

  - ReceiverRecord

    用来标识注册的单个广播，包括intentFilter和receiver，每次注册就会生成一个ReceiverRecord。

  - BroadcastRecord

    BroadcastRecord记录一个意图对应的多个接收者，发送回复信息给Receiver时可以通过这个结构查找接收者。

- 注册广播 registerReceiver(receiver, filter)

  将广播接收器构建成 ReceiverRecord 保存到 HashMap 中；

- 发送广播 sendBroadcast(intent)

  调用 handler 发送消息，遍历广播接收器，调用其 onReceive()；



#### 四、Fragment

##### 1、Fragment 的生命周期？与 Activity 的声明周期比对？

![img](https://camo.githubusercontent.com/5ddd93479e9841a249ce3d34cceddc4b509a2169/687474703a2f2f75706c6f61642d696d616765732e6a69616e7368752e696f2f75706c6f61645f696d616765732f313738303335322d663835383462633730663363313439632e706e673f696d6167654d6f6772322f6175746f2d6f7269656e742f7374726970253743696d61676556696577322f322f772f31323430)



启动一个包含 Fragment 的 Activity，生命周期如下：

```java
Activity---onCreate()
Fragment---onAttach()
Fragment---onCreate()
Fragment---onCreateView()
Fragment---onActivityCreated()
Fragment---onStart()
Activity---onStart()
Activity---onResume()
Fragment---onResume()
Fragment---onPause()
Activity---onPause()
Fragment---onStop()
Activity---onStop()
Fragment---onDestroyView()
Fragment---onDestroy()
Fragment---onDetach()
Activity---onDestroy()
```

##### 2、Fragment 的使用场景

- 适配手机平板。

- 一般主页面Activity+多个fragment，减少内存消耗，提升性能。

- 面对非常复杂的页面时，可以把它拆分成几个fragment，这样就分担了activity的压力。

  activity只需要负责管理fragment的生命周期和数据传递，具体的业务逻辑由对应的fragmnet处理。

- 需要对某个页面进行反复替换——动态添加碎片。

- 结合 ViewPager 作为导航栏，实现切换。

##### 3、Activity与Fragment之间的通信方式及什么场景使用哪种方式？

1. 创建fragment时传入数据，通过bundle
2. 通过接口回调
3. 在Activity中定义方法，fragment中getActivity()获取
4. 通过EventBus之类
5. 广播

##### 4、Fragment设置数据为什么使用setArguments的方式？

设备横竖屏切换的话，当前展示给用户的Activity默认情况下会重新创建并展现给用户，那依附于Activity的Fragment会进行如何处理呢？
    由于我们的Fragment是由FragmentManager来管理，所以可以跟进FragmentManager.restoreAllState()方法，最终会通过反射无参构造实例化一个新的Fragment，并且给mArguments初始化为原先的值，而原来的Fragment实例的数据都丢失了，并重新进行了初始化通过上面的分析，我们可以知道Activity重新创建时，会重新构建它所管理的Fragment，原先的Fragment的字段值将会全部丢失，但是通过`Fragment.setArguments(Bundle bundle)`方法设置的bundle会保留下来。
    所以尽量使用Fragment.setArguments(Bundle bundle)方式来传递参数

##### 5、Fragment 怎样监听返回？

https://blog.csdn.net/harvic880925/article/details/45013501#

- 在 Fragment 类中定义一个 onBackPressed() 方法，来处理回调事件；
- 利用回调将要处理回退事件的 Fragment 实例传给 Activity；
- 拿到 Fragment 实例后，就可以在 Activity 的 onBackPress() 方法中，调用这个 fragment 实例的 onBackPressed() 方法了；

```java
public class Fragment3 extends Fragment {
	//定义回调函数及变量
    protected BackHandlerInterface backHandlerInterface;
    public interface BackHandlerInterface {
        public void setSelectedFragment(Fragment3 backHandledFragment);
    }
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		//回调函数赋值
        if(!(getActivity()  instanceof BackHandlerInterface)) {
            throw new ClassCastException("Hosting activity must implement BackHandlerInterface");
        } else {
            backHandlerInterface = (BackHandlerInterface) getActivity();
        }
    }
 
    @Override
    public void onStart() {
        super.onStart();
        //将自己的实例传出去
        backHandlerInterface.setSelectedFragment(this);
    }
    
    public boolean onBackPressed(){  
        if (!mHandledPress){  
            tv.setText("Fragment3 \n 捕捉到了回退事件哦！");  
            mHandledPress = true;  
            return true;  
        }  
        return false;  
    }
}

public class MainActivity extends FragmentActivity implements Fragment3.BackHandlerInterface {
    private Fragment3 selectedFragment;
	…………
    @Override
    public void setSelectedFragment(Fragment3 backHandledFragment) {
        this.selectedFragment = backHandledFragment;
    }
 
    @Override
    public void onBackPressed() {
        if(selectedFragment == null || !selectedFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }
 
}
```



##### 6、Fragment之间怎样进行通信？

1. 通过宿主 Activity

2. getActivity() 根据 tag 找到对应的 Fragment，调用其方法

3. 接口回调

4. Eventbus

5. 广播

6. kotlin Fragment.kt 中的扩展函数 Fragment.setFragmentResult，通过 onFragmentResult() 回调接收返回信息

   Result API的原理非常简单，FragmentA 通过 Key 向 FragmentManager 注册 ResultListener，FragmentB 返回 result 时， FM 通过 Key 将结果回调给FragmentA 。需要特别注意的是只有当 FragmentB 返回时，result才会被真正回传，如果 setFragmentResult 多次，则只会保留最后一次结果。

7. ViewModel

> - ResultAPI 主要适用于那些一次性的通信场景（FragmentB返回结果后结束自己）。如果使用 ViewModel，需要上提到的 Fragment 共同的父级 Scope，而 Scope 的放大不利于数据的管理。
> - 非一次性的通信场景，由于 FragmentA 和 FragmentB 在通信过程中共存，推荐通过共享 ViewModel 的方式，再借助 LiveData 等进行响应式通信。

##### 7、ViewPager+三个Fragment的生命周期？加载第一个的时候，第一第二的生命周期，第三个会不会走？怎样实现懒加载？

- 默认预加载1个，刚进入页面，前两个都会走到onResume()，第三个 Fragment 不会走生命周期，等点击第二个 tab 的时候，第三个才 onResume()，此时第一个 hiden()；

  切换到第三个时，第一个会onDestroyView()；

- 懒加载，自定义一个基类，通过setUserVisibleHint()来实现



##### 8、add与replace的区别

replace() 会重新创建 Fragment 的实例，add()/hide() 不会走 Fragment 的生命周期。





#### 五、Context

##### 1、一个应用中有多少个 Context？

应用中 Context 的具体实现类是 Application、Activity、Service。

Context 数量 = Activity数量 + Service数量 + 1（当然，默认是单进程的 App）



##### 2、Android 都有哪些类型的 Context？

![image-20201103102812673](C:\Users\E480\Desktop\文章图片\Context继承关系.png)



凡是跟 UI 有关的，都应该用 Activity 作为 Context 来处理，否则要么会报错，要么 UI 会使用系统默认的主题。

![img](https://upload-images.jianshu.io/upload_images/1187237-fb32b0f992da4781.png?imageMogr2/auto-orient/strip|imageView2/2/w/589/format/webp)



##### 3、Application Context 对象和 Activity Context 对象的区别？

Application 的 Context 通过 getApplicationContext() 或者 getApplication() 获得，两个方法用起来没区别，都是返回应用的 Application 对象；但是从来源上， getApplication 是 Activity 、 Service 里的方法， 而 getApplicationContext 则是 Context 里的抽象方法，所以能调用到的它们的地方不一样。

Activity 的 Context 是其本身，在 Activity 中可以直接用 this，或者 XXXActivity.this。



- 两者生命周期不同；

- 使用场景、作用域不同：

  - 使用Application和Service 启动 launchmode 为 standard 的 activity   会报错，因为它们没有任务栈，需要为待启动的 activity 指定 FLAG_ACTIVITY_NEW_TASK，但此时 activity 则是以 singleTask 模式启动的；

  - 使用Application 和 Service 加载布局时，会使用系统默认的主题样式，所以建议使用 Activity 启动；

  - Application 和 Service 的 Context 均不能启动一个 Dialog

    > 会抛出异常，Unable to add window -- token null is not valid; is your activity running?

    - Activity 在被创建后会调用 attach()，其中会创建 PhoneWindow 对象，给当前 window 绑定 mToken，它可以用来标识 Window，做一些校验工作。

      Dialog 通过 getSystemService() 获取 WindowManager 对象，最终显示出来。

      Activity 重写了 getSystemService() 返回了一个 已经绑定了 mToken 的对象；

      而 Application 和 Service 调用默认的 ContextImpl#getSystemService() 没有 mToken。

    - 启动 Activity 的时候，会构建表示 Activity 信息的 ActivityRecord 对象，其构造函数中会实例化 Token 对象

    - AMS 在接着上一步之后，会利用创建的 Token 构建 AppWindowContainerController 对象，最终将 Token 存储到 WMS 中的 mTokenMap 中

    - WMS 在 addWindow 时，会根据当前 Window 对象的 Token 进行校验



#### 六、Android中的消息机制和线程机制

##### 1、Handler 机制简介

Android 的消息机制主要是指 Handler 的运行机制以及 Handler 所附带的 MessageQueue 和 Looper 的工作流程。

1. 使用 Handler 首先要创建 Looper，通过 prepare() 创建，然后调用 loop() 进入死循环，不断的取消息；
2. 通过 Handler 发送一小消息，最终会通过 MessageQueue#enqueueMessage() 将该消息插入到 MessageQueue 中；
3. Looper.loop() 中不断调用 MessageQueue#next() 方法取消息，并将取到的消息交给 Handler 来处理。



##### 2、延迟消息的原理？

```java
public final boolean sendMessageDelayed(Message msg, long delayMillis)
{
    if (delayMillis < 0) {
        delayMillis = 0;
    }
    return sendMessageAtTime(msg, SystemClock.uptimeMillis() + delayMillis);
}
```

SystemClock.uptimeMillis() 系统启动以来的时间，不使用 System.currentTimeMillis() 是这个时间可以通过修改系统时间来修改，不可靠。

msg.when = SystemClock.uptimeMillis() + delayMillis

> enqueueMessage() 中有一个判断，if(msg.target == null) {//抛出异常}，这是为了通过 target 来判断是哪个 Handler 发送的消息。
>
> 但是并不是所有的 msg，target 都必须不为空。handler 的同步屏障就是一个 target 为空的 msg，用来优先执行异步方法的。
>
> 同步屏障有一个很重要的使用场所就是接受垂直同步 Vsync 信号，用来刷新页面 View 的。因为为了保证 View 的流畅度，所以每次刷新信号到来的时候，要把其他的任务先放一放，优先刷新页面。

MessageQueue 中的 enqueueMessage() 会按照 msg 的实际执行之间 when 进行排序，插入到 queue 里面。



通过 next() 取消息，这里也是一个 for 循环，核心变量是 nextPollTimeoutMillis，默认值是 0，它是休眠时间。循环中会调用 nativePollOnce() 方法，传入 nextPollTimeoutMillis。

如果取到的消息不为空，且当前时间小于 msg.when，即该延迟消息还未到处理时间，则对 nextPollTimeoutMillis 重新赋值，nextPollTimeoutMillis = (int) Math.min(msg.when - now, Integer.MAX_VALUE);

进入下一循环，调用 nativePollOnce，进行休眠，直到该延迟消息处理时间到为止，将该消息取出并返回。

如果消息池没有消息，则将 nextPollTimeoutMillis 赋值为 -1，一直休眠，直到 enqueueMessage() 有新的消息插入，会调用 nativeWake() 进行唤醒。

```java
Message next() {
    int nextPollTimeoutMillis = 0;
    for (;;) {
        //。。。
        nativePollOnce(ptr, nextPollTimeoutMillis);

        synchronized (this) {
            // Try to retrieve the next message.  Return if found.
            final long now = SystemClock.uptimeMillis();
            Message prevMsg = null;
            Message msg = mMessages;
            if (msg != null && msg.target == null) {
                // Stalled by a barrier.  Find the next asynchronous message in the queue.
                do {
                    prevMsg = msg;
                    msg = msg.next;
                } while (msg != null && !msg.isAsynchronous());
            }
            if (msg != null) {
                if (now < msg.when) {
                    // Next message is not ready.  Set a timeout to wake up when it is ready.
                    nextPollTimeoutMillis = (int) Math.min(msg.when - now, Integer.MAX_VALUE);
                } else {
                    // Got a message.
                    mBlocked = false;
                    if (prevMsg != null) {
                        prevMsg.next = msg.next;
                    } else {
                        mMessages = msg.next;
                    }
                    msg.next = null;
                    if (DEBUG) Log.v(TAG, "Returning message: " + msg);
                    msg.markInUse();
                    return msg;
                }
            } else {
                // No more messages.
                nextPollTimeoutMillis = -1;
            }
    }
}
```

##### 3、postDelayed() 怎样实现的？

```java
public final boolean postDelayed(Runnable r, long delayMillis) {
    return sendMessageDelayed(getPostMessage(r), delayMillis);
}

private static Message getPostMessage(Runnable r) {
    Message m = Message.obtain();
    m.callback = r;
    return m;
}
```

postDelayed() 最终还是调用的 sendMessage() 方法，它传入一个 runnable 对象，通过 getPostMessage(r) 构建成一个 Message 对象，将 runnable 赋值给 msg 的 callback。

这里通过 Message.obtain() 构建 msg 对象。



**Message 的复用机制**

Message通过静态单链表来全局完成消息的复用，而在每次回收的过程中消息数据重置防止Message持有其他对象而造成内存泄漏操作，
所有在日常开发中尽量使用Message.obtain 来获取Message，如果手动创建Message，也不是不行，
因为Looper的消息在使用完都会自动调用recycle的，但是一旦消息链表到达上限，那么如果大量发送消息 ，
仍然存在大量Message对象需要在堆中回收的问题。

```java
private static Message sPool;
private static int sPoolSize = 0;

private static final int MAX_POOL_SIZE = 50;//消息链表上限50

public static Message obtain() {
    synchronized (sPoolSync) {
        if (sPool != null) {
            Message m = sPool;
            sPool = m.next;
            m.next = null;
            m.flags = 0; // clear in-use flag
            sPoolSize--;
            return m;
        }
    }
    return new Message();
}
```



不管是 MessageQueue 的 quit() 还是 removeMessage()   最终都是调用的 Message 的 recycleUnchecked() 该方法对 Message 进行回收，将一个 message 对象的属性置为空（如 target、callback），用以复用。



##### 4、Handler 怎样处理消息？

```java
public void dispatchMessage(Message msg) {
    //callback 的值为通过 Handler.post(Runnable) 方法发送消息时传入的 Runnable
    if (msg.callback != null) {
        handleCallback(msg);
    } else {
        // 这个 mCallback 是调用在 Handler 构造函数时可选传入的
        // 传入 CallBack 就省得为了重载 handleMessage 而新写一个 Handler 的子类
        if (mCallback != null) {
            if (mCallback.handleMessage(msg)) {
                return;
            }
        }
        // 最后就是交给 Handler 自己处理 Message 啦，这个是派生 Handler 的子类重写的方法
        handleMessage(msg);
    }
}
```

mCallback 处理完如果返回 false，还是会继续往下走，再交给 Handler.handleMessage 处理的。所以这边可以通过反射去 hook 一个 Handler ，可以监听 Handler 处理的每个消息，也可以改 msg 里面的值。



##### 5、Handler 是如何切换线程的？

<font color='red'>在同一进程中线程和线程之间资源是共享的，也就是对于任何变量在任何线程都是可以访问和修改的，只要考虑好并发性做好同步就行。</font>

比如从子线程发送消息到主线程中去处理，只要拿到主线程的 MessageQueue 的实例，就可以往主线程的 MessageQueue 放入消息，主线程在轮询时就可以在主线程处理这个消息。

使用 Handler 前需要创建 Looper 实例，Looper 通过 ThreadLocal 和当前线程绑定，在 Looper 的构造函数中创建 MessageQueue 的实例，即一个线程只有一个Looper，也只有一个 MessageQueue。

Handler 的构造函数中通过 Looper.myLooper() 获取到 mLooper（mThreadLocal.get()），并通过 mLooper.mQueue 获取到 MessageQueue 的实例，这样就可以在子线程中发送消息，插入到主线程的 MessageQueue。

最后通过 Looper 轮询取出消息交由 Handler 处理。



结果就是：<font color='red'>Handler 对象在哪个线程下构建（Handler的构造函数在哪个线程下调用），那么Handler就会持有这个线程的Looper引用和这个线程的消息队列的引用。因为持有这个线程的消息队列的引用，意味着这个Handler对象可以在任意其他线程给该线程的消息队列添加消息，也意味着Handler的handlerMessage 肯定也是在该线程执行的。</font>

##### 6、Looper 死循环为什么不会导致应用卡死，会消耗大量资源吗？

对于线程即是一段可执行的代码，当可执行代码执行完成后，线程生命周期便该终止了，线程退出。但对于主线程，我们需要保证能一直存活，Android 使用死循环的方式实现。

既然是死循环又如何去处理其他事务呢？<font color='red'>通过创建新线程的方式。</font>

<font color='red'>真正会卡死主线程的操作是在回调方法 onCreate/onStart()/onResume() 等操作时间过长，会导致掉帧，甚至发生 ANR，looper.loop() 本身不会导致应用卡死。</font>

主线程的 MessageQueue 没有消息时，便阻塞在 loop() 的 queue.next() 中的 nativePollOnce() 里，此时主线程会释放 CPU 资源进入休眠状态，直到下个消息到达或者有事务发生，通过往 pipe 管道写端写入数据来唤醒主线程工作。所以主线程大多数都是出于休眠状态，并不会大量消耗 CPU 资源。



ANR，全名Application Not Responding。当我发送一个绘制UI 的消息到主线程Handler之后，经过一定的时间没有被执行，则抛出ANR异常。Looper的死循环，是循环执行各种事务，包括UI绘制事务。Looper死循环说明线程没有死亡，如果Looper停止循环，线程则结束退出了。Looper的死循环本身就是保证UI绘制任务可以被执行的原因之一。同时UI绘制任务有同步屏障，可以更加快速地保证绘制更快执行。



##### 7、为什么子线程不能访问UI？子线程更新 UI 的方法有哪些？

因为 Android 的 UI 控件不是线程安全的，如果在多线程中并发访问可能会导致 UI 控件处于不可预期的状态。

那么为什么系统不给 UI 控件的访问加上锁机制呢？

- 首先加上锁机制会让 UI 访问的逻辑变得复杂
- 锁机制会降低 UI 访问的效率，因为锁机制会阻塞某些线程的执行。

子线程中更新 UI 的方式：

1. 通过 Handler 发送消息到主线程去更新；
2. Activity 中的 runOnUiThread()；
3. View.Post(Runnable r)；

后面两种方式均是通过 Handler 来实现的。

> 这个说法就不太严谨，应该说UI线程才能更新UI，非UI线程不能更新。 UI线程指的是ViewRootImpl创建时的线程，通常activity关联的ViewRootImpl是在主线程创建的，所以便有了这个不严谨的说法。
>
> 试想一下自己启动一个Thread 并且实现looper， 在这个子线程里弹出个dialog。那么这个dialog理论上就只能在这个子线程里才能更新，主线程更新就会crash（基本上没有人这样干）。SurefaceView就是在子线程去创建了一个新的ViewRootImpl,所有它只能在独立的线程里更新。

##### 8、子线程中可以弹 Toast 和 Dialog 吗？怎么实现？

可以

```java
new Thread(new Runnable() {
    @Override
    public void run() {
        Looper.prepare();
        Toast.makeText(mContext, "子线程中弹 toast", Toast.LENGTH_SHORT).show();
        Looper.loop();
    }
}).start();
```

如果不调用 Looper 的 prepare() 和 loop() 方法，直接弹会报错：<font color='red'>java.lang.RuntimeException: Can't toast on a thread that has not called Looper.prepare()</font>

Toast本质是通过window显示和绘制的（操作的是window），而子线程不能更新UI 是因为ViewRootImpl的checkThread方法的判断。 Toast中TN类使用Handler是为了用队列和时间控制排队显示Toast，所以为了防止在创建TN时抛出异常，需要在子线程中使用Looper.prepare();和Looper.loop();（但是不建议这么做，因为它会使线程无法执行结束，导致内存泄露）



Dialog 可以弹，但是同样必须调用 Looper.prepare() 和 Looper.loop()，否则虽然不会报错，但是 dialog 不会显示出来，并且会打印日志进行提示。

```java
2020-11-04 11:13:22.598 7642-7723/com.xht.androidnote E/Handler: This is not main thread, and the caller should invoke Looper.prepare()  and Looper.loop()called byandroid.os.Handler.<init>:122 android.app.Dialog.<init>:154 android.app.AlertDialog.<init>:205 android.app.AlertDialog$Builder.create:1112 android.app.AlertDialog$Builder.show:1137 com.xht.androidnote.module.thread.ThreadTestActivity$2.run:64 java.lang.Thread.run:929 <bottom of call stack> 

```

##### 9、Handler 内存泄漏的问题？

解决Handler内存泄露主要2点：

1. 有延时消息，要在Activity销毁的时候移除Messages；
2. 匿名内部类导致的泄露改为匿名静态内部类，并且对上下文或者Activity使用弱引用。



##### 10、Looper 的退出？

Looper 提供了 quit() 和 quitSafely() 来退出一个 Looper。

二者的区别是：

- quit() 会直接退出 Looper；
- quitSafely() 只是设定一个退出标记，然后把消息队列中的已有消息处理完毕后才安全地退出。

这两个方法均调用了 MessageQueue#quit()

在子线程中，如果手动为其创建了 Looper，那么在所有的事情完成以后应该调用 quit() 来终止消息循环，否则这个子线程就会一直处于等待的状态，而如果退出 Looper 以后，这个线程就会立刻终止，因此建议不需要的时候终止 Looper。

<font color='red'>主线程不能退出，主线程退出应用就停止了。</font>

```java
//Looper.java
//该方法由 ActivityThread#main 调用
public static void prepareMainLooper() {
    prepare(false);//这个false 即是否允许退出
    synchronized (Looper.class) {
        if (sMainLooper != null) {
            throw new IllegalStateException("The main Looper has already been prepared.");
        }
        sMainLooper = myLooper();
    }
}

//MessageQueue.java
void quit(boolean safe) {
    if (!mQuitAllowed) {
        throw new IllegalStateException("Main thread not allowed to quit.");
    }
    //....
}
```



##### 10、Handler 如何保证 MessageQueue 并发访问安全？

循环加锁，配合阻塞唤醒机制。



##### 11、同步屏障

一种使得异步消息可以被更快处理的机制。

如果向主线程发送了一个UI更新的操作Message，而此时消息队列中的消息非常多，那么这个Message的处理就会变得缓慢，造成界面卡顿。所以通过同步屏障，可以使得UI绘制的Message更快被执行。

这个“屏障”其实是一个Message，插入在MessageQueue的链表头，且其target==null。

如果遇到同步屏障，即 MessageQueue 的 next() 方法中 判断 msg.target == null，那么会循环遍历整个链表找到标记为异步消息的Message，即isAsynchronous返回true，其他的消息会直接忽视，那么这样异步消息，就会提前被执行了。



##### 12、IdleHandler

当MessageQueue为空或者目前没有需要执行的Message时会回调的接口对象。

```java
@Override
protected void onResume() {
    super.onResume();
    Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
        @Override
        public boolean queueIdle() {
            // 此处做一些耗时操作
            return false;
        }
    });
}
```



1. 当调用next方法的时候，会给`pendingIdleHandlerCount`赋值为-1
2. 如果队列中没有需要处理的消息的时候，就会判断`pendingIdleHandlerCount`是否为`<0`，如果是则把存储IdleHandler的list的长度赋值给`pendingIdleHandlerCount`
3. 把list中的所有IdleHandler放到数组中。这一步是为了不让在执行IdleHandler的时候List被插入新的IdleHandler，造成逻辑混乱
4. 然后遍历整个数组执行所有的IdleHandler
5. 最后给`pendingIdleHandlerCount`赋值为0。然后再回去看一下这个期间有没有新的消息插入。因为`pendingIdleHandlerCount`的值为0不是-1，所以IdleHandler只会在空闲的时候执行一次
6. 同时注意，如果IdleHandler返回了false，那么执行一次之后就被丢弃了。



##### 13、ThreadLocal

https://blog.csdn.net/weixin_39646018/article/details/113364069

ThreadLocal 是一个线程内部的数据存储类，通过它可以在指定的线程中存储数据，数据存储以后，只有在指定线程中可以获取到存储的数据。

Thread 类中有个成员变量 threadLocals

```java
ThreadLocal.ThreadLocalMap threadLocals = null;
```

###### 1、ThreadLocal#set() 方法：

```java
public void set(T value) {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value);
}

//ThreadLocalMap.java
private void set(ThreadLocal<?> key, Object value) {
	Entry[] tab = table;
	int len = tab.length;
	int i = key.threadLocalHashCode & (len-1);

	for (Entry e = tab[i];
		 e != null;
		 e = tab[i = nextIndex(i, len)]) {
		ThreadLocal<?> k = e.get();

		if (k == key) {
			e.value = value;
			return;
		}

		if (k == null) {
			replaceStaleEntry(key, value, i);
			return;
		}
	}

	tab[i] = new Entry(key, value);
	int sz = ++size;
	if (!cleanSomeSlots(i, sz) && sz >= threshold)
		rehash();
}
```

- ThreadLocalMap 结构是数组，用来保存 key-value 的组合 Entry。key 是 ThreadLocal，value 是要存入的值。
- Entry 数组，在 ThreadLocalMap 构造函数中初始化，初始长度为 16，扩容是扩大为原来的 2 倍；
- Entry对象里面也是设计成key/value的形式解决hash冲突的。所以你可以想象成ThreadLocalMap是个数组，而存储在数组里面的各个对象是以key/value形式的Entry对象。
- ThreadLocalMap#set(ThreadLocal<?> key, Object value)
  - 数组位置计算方式和 HashMap 一样，int i = key.threadLocalHashCode & (len-1)；
  - 再往下是个for循环，里面是寻找可插入的位置，
    - 如果需要插入的key在数组中已存在，则直接把需要插入的value覆盖到数组中的vaule上；
    - 如果 key 为空，则创建出Entry对象，放在该位置上；
    - 如果上面两种情况都不满足，那就寻找下一个位置i，继续循环上面的两个判断，直到找到可以插入或者刷新的位置。

###### 2、ThreadLocal#get()

```java
public T get() {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T)e.value;
            return result;
        }
    }
    return setInitialValue();
}
//ThreadLocalMap.java
private Entry getEntry(ThreadLocal<?> key) {
    int i = key.threadLocalHashCode & (table.length - 1);
    Entry e = table[i];
    if (e != null && e.get() == key)
        return e;
    else
        return getEntryAfterMiss(key, i, e);
}

private Entry getEntryAfterMiss(ThreadLocal<?> key, int i, Entry e) {
    Entry[] tab = table;
    int len = tab.length;

    while (e != null) {
        ThreadLocal<?> k = e.get();
        if (k == key)
            return e;
        if (k == null)
            expungeStaleEntry(i);
        else
            i = nextIndex(i, len);
        e = tab[i];
    }
    return null;
}
```

- 先获取到当前线程t，随后通过getMap方法获取ThreadLocalMap对象，再通过getEntry获取到Enety对象；

- 根据 hashCode 获取数组中的位置；

- 如果该位置的Entry对象中的key跟当前的TreadLocal一致，则返回该Entry对象；否则继续执行getEntryAfterMiss方法；

- getEntryAfterMiss() 开启循环查找

  - 如果当前ThreadLocal跟数组下标i对应的Entry对象的key相等，则返回当前Entry对象；

  - 如果数组下标I对应的Entry对象的key为空，则执行expungeStaleEntry(i)方法，从方法命名就知道，删除废弃的Entry对应，其实就是做了次内存回收；

    如果数组中，某个Entry对象的key为空，该方法会释放掉value对象和Entry对象。

  - 如果ThreadLocal跟数组下标i对应的Entry对象的key既不相等，也不为空，则调用nextIndex方法，向下查找，跟set方法的nextIndex方法一致。



###### 3、为什么Entry对象要key设置成弱引用呢？还有ThreadLocal是否存在内存泄露呢？

key如果不是WeakReference弱引用，则如果某个线程死循环，则ThreadLocalMap一直存在，引用住了ThreadLocal，导致ThreadLocal无法释放，同时导致value无法释放；当是WeakReference弱引用时，即使线程死循环，当创建ThreadLocal的地方释放了，ThreadLocalMap的key会同样被被释放，在调用getEntry时，会判断如果key为null，则会释放value，内存泄露则不存在。当然ThreadLocalMap类也提供remove方法，该方法会帮我们把当前ThreadLocal对应的Entry对象清除，从而不会内存泄露，所以如果我个人觉得如果每次在不需要使用ThreadLocal的时候，手动调用remove方法，也不存在内存泄露。

###### 4、为什么Looper对象要存在ThreadLocal中，为什么不能公用一个呢，或者每个线程持有一个呢？

- 共用一个的话，需要处理线程同步问题，加锁会降低运行效率，增加 ANR 的几率；
- 每一个线程都持有 Looper 的话，会造成内存的浪费；



##### 14、HandlerThread、IntentService

**HandlerThread**

继承自 Thread，它是一种可以使用 Handler 的 Thread，在 run 方法中通过 Looper.prepare() 来创建消息队列，并通过 Looper.loop() 来开启消息循环。

可以通过 HandlerThread.getLooper()，来创建 Handler 对象，用来发送消息，处理耗时操作。

因为 HandlerThread 的 run() 是一个无限循环，因此当明确不需要使用时需要 quit() 或者 quitSafely() 来终止线程。

它的一个使用场景是 IntentService。

**IntentService**

抽象类，继承自 Service，使用时需要创建它的子类。它封装了 HandlerThread 和 Handler。

用于执行后台耗时的任务，当任务执行后它会自动停止，同时由于 IntentService 是服务的原因，它的优先级比单纯的线程要高很多，所以 IntentService 适合执行一些高优先级的后台任务，不容易被系统杀死。

<font color='red'>若启动IntentService多次，那么每个耗时操作则以队列的方式在IntentService的onHandleIntent回调方法中依次执行，执行完自动结束。</font>



#### 七、View 的事件分发



##### 1、简述事件分发

当用户手指触摸屏幕时，Android 会将对应的事件包装成一个事件对象 MotionEvent ，从 ViewTree 的顶部至上而下地分发传递。用户从手指接触屏幕至离开屏幕会产生一系列的事件，事件是以 down 开始，up 或 cancel 结束，中间无数个 move ; **一个事件的分发顺序是：Activity 到 ViewGroup 再到 View。**

##### 2、事件分发的流程

![img](https://img-blog.csdnimg.cn/2019030316284612.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2IxNDgwNTIxODc0,size_16,color_FFFFFF,t_70)

简单来说，流程如下：

- 当屏幕被触摸，Linux 内核将硬件产生的触摸事件保存下来，系统会不断读取事件，一路分发走到 ViewRootImpl 的 WindowInputEventReceiver，最终走到 mView.dispatchPointerEvent(event);

- ViewRootImpl 中的 mView 在 setView() 方法中被赋值，setView() 在 WindowManagerGlobal 中的 addView() 被调用，而 addView() 在 ActivityThread#handleResumeActivity() 中被调用，传入的 view 即 DecorView；

- dispatchPointerEvent() 是 View 中的方法，最终调用 dispatchTouchEvent()，回到 DecorView#dispatchTouchEvent()

  ```java
  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
      final Window.Callback cb = mWindow.getCallback();
      return cb != null && !mWindow.isDestroyed() && mFeatureId < 0
          ? cb.dispatchTouchEvent(ev) : super.dispatchTouchEvent(ev);
  }
  ```

  Window.callback 接口被 Activity、Dialog 实现（Activity 在 attach() 中创建完 PhoneWindow 后，通过 mWindow.setCallback(this) 来设置）。

- Activity#dispatchTouchEvent()--->getWindow().superDispatchTouchEvent(ev)--->PhoneWindow#superDispatchTouchEvent()--->DecorView#superDispatchTouchEvent()--->super.dispatchTouchEvent() 即事件分发到了根 View。

> - IMS 从系统底层接收到事件之后，会从 WMS 中获取 window 信息，并将事件信息发送给对应的 viewRootImpl；
> - viewRootImpl 接收到事件信息，封装成 motionEvent 对象后，发送给 view；
> - view 会根据自身的类型，对事件进行分发还是自己处理；
> - 顶层 viewGroup 一般是 DecorView，DecorView 会根据自身 callBack 的情况，选择调用 callBack 的 dispatchTouchEvent() 还是父类 ViewGroup 的方法；
>   - Activity 和 Dialog 实现了 callBack 接口，顶级 View 是 DecorView；
>   - PopupWindow 的根 View 是 PopupDecorView，直接继承于 FrameLayout；
> - 不管顶层 viewGroup 类型如何，最终到会到达 ViewGroup 对事件的分发；



<font color='red'>为什么从 ViewRootImpl-->DecorView--->Window.Callback--->Activity--->PhoneWindow--->DecorView？</font>

解耦吧，ViewRootImpl 并不知道 Activity 的存在，而 Activity 只负责生命周期，Window 负责视图的显示，它持有 DecorView 的引用。



##### 3、事件分发相关方法以及它们之间的关系

```java
// 事件分发到某个具体的 ViewGroup，会直接调用 dispatchTouchEvent() 方法
public boolean dispatchTouchEvent(MotionEvent ev) {
    //代表是否消费事件
    boolean consume = false;

    if (onInterceptTouchEvent(ev)) {
    // 如果 onInterceptTouchEvent() 返回 true 则代表当前 View 拦截了事件
    // 则该事件则会交给当前View进行处理
    // 即调用 onTouchEvent() 方法去处理事件
      consume = onTouchEvent (ev) ;
    } else {
      // 如果 onInterceptTouchEvent() 返回 false 则代表当前 View 不拦截事件
      // 则该事件则会继续传递给它的子元素
      // 子元素的 dispatchTouchEvent() 就会被调用，重复上述过程
      // 直到事件被最终处理为止
      consume = child.dispatchTouchEvent(ev); //遍历处理
    }
    return consume;
}
```

##### 4、ViewGroup 的分发

**1）事件的拦截，关键字段** 

- **mFirstTouchTarget** 

  当事件由 ViewGroup 的子元素成功处理时，mFirstTouchTarget 会被赋值并指向子元素。反之，一旦事件由当前 ViewGroup 拦截时 mFirstTouchTarget != null 就不成立。

- **FLAG_DISALLOW_INTERCEPT**

  标记位，通过 requestDisallowInterceptTouchEvent() 设置，一旦设置成功，ViewGroup 将无法拦截除了 ACTION_DOWN 以外的事件。因为 DOWN 事件会重置标记位。

```java
// 一个新的触摸事件序列，如果是 DOWN，将会重置反拦截标记位，同时将 mFirstTouchTarget 置为空
if (actionMasked == MotionEvent.ACTION_DOWN) {
    cancelAndClearTouchTargets(ev);
    resetTouchState();
}

final boolean intercepted;
/*
	如果 ViewGroup 在 DOWN 事件时决定拦截事件，mFirstTouchTarget == null，所以当 MOVE 和 UP 事		件来临时，下面的条件不成立，也就不会再调用 onInterceptTouchEvent()，走 else 分支，继续拦截事件。
*/
if (actionMasked == MotionEvent.ACTION_DOWN || mFirstTouchTarget != null) {
    final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
    if (!disallowIntercept) {
        intercepted = onInterceptTouchEvent(ev);
        ev.setAction(action); // restore action in case it was changed
    } else {
        intercepted = false;
    }
} else {
    // There are no touch targets and this action is not an initial down
    // so this view group continues to intercept touches.
    intercepted = true;
}
```

<font color='red'>所以 onInterceptTouchEvent() 不是每次都会被调用，如果想提前处理所有的点击事件，要选择 dispatchTouchEvent()。</font>

> 如果 子 View 的 dispatchTouchEvent() 返回 false，则 mFirstTouchTarget 不会被赋值，则后续事件序列来临时，不会再走 onInterceptTouchEvent()；

**2）不拦截**

```java
if (!canceled && !intercepted) {
    //找到一个能子view处理事件
    //......

    if (actionMasked == MotionEvent.ACTION_DOWN
        || (split && actionMasked == MotionEvent.ACTION_POINTER_DOWN)
        || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {
        //......
        final int childrenCount = mChildrenCount;
        if (newTouchTarget == null && childrenCount != 0) {
            //遍历找到一个能接收事件的子元素
            //......
            for (int i = childrenCount - 1; i >= 0; i--) {
                final int childIndex = getAndVerifyPreorderedIndex(
                    childrenCount, i, customOrder);
                final View child = getAndVerifyPreorderedView(
                    preorderedList, children, childIndex);

                //......
				//判断子view是否能够接收事件
                if (!canViewReceivePointerEvents(child)
                    || !isTransformedTouchPointInView(x, y, child, null)) {
                    ev.setTargetAccessibilityFocus(false);
                    continue;
                }

                //......
                if (dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)) {
                    //给mFirstTouchTarget赋值
                    newTouchTarget = addTouchTarget(child, idBitsToAssign);
                    alreadyDispatchedToNewTouchTarget = true;
                    break;
                }
            }
        } 

        //没有子元素
        if (newTouchTarget == null && mFirstTouchTarget != null) {
            // Did not find a child to receive the event.
            // Assign the pointer to the least recently added target.
            newTouchTarget = mFirstTouchTarget;
            while (newTouchTarget.next != null) {
                newTouchTarget = newTouchTarget.next;
            }
            newTouchTarget.pointerIdBits |= idBitsToAssign;
        }
    }
}
```

- 遍历所有子元素；

- 判断子元素是否能够接收事件；

- 调用 dispatchTransformedTouchEvent() 方法，实际上是调用子元素的 dispatchTouchEvent()；

  ```java
  private boolean dispatchTransformedTouchEvent(MotionEvent event, boolean cancel,
          View child, int desiredPointerIdBits) {
      final boolean handled;
      // Perform any necessary transformations and dispatch.
      if (child == null) {
          handled = super.dispatchTouchEvent(transformedEvent);
      } else {
          handled = child.dispatchTouchEvent(transformedEvent);
      }
      return handled;
  }
  ```

- 如果子元素 dispatchTouchEvent() 返回 true，则为 mFirstTouchTarget 赋值，跳出循环；如果子元素返回 false，则继续循环遍历，寻找下一个能接收事件的子元素。

- 如果遍历所有的子元素后事件都没有被合适地处理，可能有两种情况：

  - ViewGroup 没有子元素；
  - 子元素处理了点击事件，但是在 dispatchTouchEvent() 中返回了 false，这一般是因为子元素在 onTouchEvent() 中返回了 false；

  ```java
  // Dispatch to touch targets.
  if (mFirstTouchTarget == null) {
      // No touch targets so treat this as an ordinary view.
      handled = dispatchTransformedTouchEvent(ev, canceled, null,                                    TouchTarget.ALL_POINTER_IDS);
  }
  ```

  此处 child 传入的是 null，会调用 super.dispatchTouchEvent()，这样就走到了 View 的 dispatchTouchEvent()。
  
  > - 如果子 View dispatchTouchEvent() 返回 false，mFirstTouchTarget 就不会被赋值，dispatchTransformedTouchEvent() 中 child 传 null，走 super.dispatchTouchEvent() 即父 View 的父类即其本身作为 View 的 dispatchTouchEvent() 方法；
  > - 且后续事件序列 MOVE、UP 不会再传递给子 View，因为父 View 的 dispatchTouchEvent() 方法中，开始处判断，if (actionMasked == MotionEvent.ACTION_DOWN || mFirstTouchTarget != null)    MOVE 和 UP 且 mFirstTouchTarget 为 null，所以走 else 分支，intercept 赋为 true；
  > - intercept 为 true，也就不会再进入 dispatchTransformedTouchEvent() 所在的这段代码，即不会再将 MOVE 和 UP 事件传递给子 View；

##### 5、View 的分发

- 首先判断是否设置了 OnTouchListener，如果 OnTouchListener 中的 onTouch() 返回了 true，那么 onTouchEvent() 就不会被调用。OnTouchListener 是为了方便处理点击事件，不需要自定义view。
- onTouchEvent()  哪怕是 disable 状态，只要设置了 CLICK 或者 LONG_CLICK 就能消费事件，即 onTouchEvent() 返回 true。
- ACTION_UP 事件会触发 performClick()，里面判断是否设置了 onClickListener，有的话就执行 onClick()。

```java
public boolean dispatchTouchEvent(MotionEvent event) {
    booelan result = false;
    if (onFilterTouchEventForSecurity(event)) {
        ListenerInfo li = mListenerInfo;
        if (li != null && li.mOnTouchListener != null
            && (mViewFlags & ENABLED_MASK) == ENABLED
            && li.mOnTouchListener.onTouch(this, event)) {
            result = true;
        }

        //根据onTouch()的返回值判断，是否执行onTouchEvent()
        if (!result && onTouchEvent(event)) {
            result = true;
        }
    }

    return result;
}
```

##### 6、cancel 事件什么情况下会发生？

- 在子View处理事件的过程中，父View对事件拦截

  当子 view 的 onTouchEvent() 返回 true，而父 View 在 MOVE 事件中进行拦截，即 onInterceptTouchEvent() 返回 true，此时子 View 会收到一个 CANCEL 事件。

  比如在一个 ScrollView 中，点击一个 Button 后不抬起手，直接滑动，就会产生 CANCEL 事件。

- 子View被设置了PFLAG_CANCEL_NEXT_UP_EVENT标记时

- 在子View处理事件的过程中被从父View中移除时

- 当View从Window中分离时

```java
// ViewGroup.dispatchTouchEvent()
public boolean dispatchTouchEvent(MotionEvent ev) {
    if (mFirstTouchTarget == null) {
    } else {
        // 有子 View 获取了事件
        TouchTarget target = mFirstTouchTarget;
        while (target != null) {
            final TouchTarget next = target.next;
            final boolean cancelChild = resetCancelNextUpFlag(target.child)
                    || intercepted;
            // 父 View 此时如果拦截了事件，cancelChild 是 true
            if (dispatchTransformedTouchEvent(ev, cancelChild,
                    target.child, target.pointerIdBits)) {
                handled = true;
            }
        }
    }
}

private boolean dispatchTransformedTouchEvent(MotionEvent event, boolean cancel,
        View child, int desiredPointerIdBits) {
    final int oldAction = event.getAction();
    // 如果 cancel 是 true，则发送 ACTION_CANCEL 事件
    if (cancel || oldAction == MotionEvent.ACTION_CANCEL) {
        event.setAction(MotionEvent.ACTION_CANCEL);
        if (child == null) {
            handled = super.dispatchTouchEvent(event);
        } else {
            handled = child.dispatchTouchEvent(event);
        }
        event.setAction(oldAction);
        return handled;
    }
}

```

> 滑出子 View 区域不会触发 CANCEL，但是不会触发 onClick() 事件；
>
> 在 View 的 onTouchEvent() 的 ACTION_MOVE 事件中，会判断是否超出了 View 的边界，是的话会改变 pressed 的状态，而在 ACTION_UP 事件中会根据 pressed 的状态进行判断，是否执行 performClick() 方法，即是否会执行到 onClick()

##### 7、父view中两个button，点击一个然后手指不抬起，一直滑出屏幕

只会走第一个点到的 Button 的事件



##### 8、一个列表中，同时对父 View 和子 View 设置点击方法，优先响应哪个？为什么会这样？

子 View
子 View 处理后，就不会在走父 View 的 onTouchEvent() 也就不会再走到父 View 的点击事件。



##### 9、如何让父 View 和子 View 都处理事件？（有疑问）

如果是DOWN事件，只要在子view的onTouchEvent()中return false即可，

如果其他事件，需要在父view的dispatchTouchEvent()中处理，并在子view的dispatchTouchEvent()中return true。

##### 10、一个ScrollView嵌套两个RecyclerView1和RecyclerView2，什么时机事件交由谁处理？

RecyclerView1滑动到底部的时候，交由外层View来处理

<font color='red'>怎样判断 RecyclerView 是否滑动到了底部？</font>

监听 RecyclerView 的滑动

1. 通过 canScrollVertically() 判断
2. 通过 LayoutManager#findFirstCompletelyVisibleItemPosition() 判断



##### 11、一个ViewPager嵌套一个RecyclerView都是横向的，滑动的话是谁动？

​    RecyclerView动，因为ViewPager里面做了处理，如果子View能够滑动，交由子View处理。



##### 12、ScrollView 嵌套 RecyclerView 为什么会滑动冲突？(有疑问)

​    应该是两个view 都在move中进行了滑动的处理，一个事件分发到子view的同时，如果被子view消费掉，
​    它还是会走父view的各种事件



##### 13、滑动冲突的解决方式？

1. 外部拦截法

   点击事件都先经过父容器的拦截处理，通过重写父容器的 onInterceptTouchEvent() 根据需求做相应的拦截。

   - ACTION_DOWN 这个事件里父容器必须返回 false，即不拦截ACTION_DOWN事件，因为一旦拦截了那么后续的 ACTION_MOVE、ACTION_UP都由父容器去处理，事件就无法传到子view了
   - ACTION_MOVE 事件可以根据需要来进行拦截或者不拦截
   - ACTION_UP 这个事件必须返回false，就会导致子View无法接受到UP事件，这个时候子元素中的onClick()事件就无法处触发。

2. 内部拦截法

   父容器不拦截任何事件，如果子元素需要此事件就直接消耗掉，否则就交由父容器进行处理。配合 requestDisallowInterceptTouchEvent()。

   重写子元素的 dispatchTouchEvent()，根据需求条件调用 requestDisallowInterceptTouchEvent()，父容器不能拦截 DOWN 事件，否则子类接收不到任何事件。



#### 八、View的绘制流程

##### 1、MeasureSpec

MeasureSpec 代表一个 32 位 int 值，高 2 位代表 SpecMode 测量模式，低 30 位代表 SpecSize 规格大小。

- EXACTLY：精确测量模式，视图宽高指定为match_parent或具体数值时生效，表示父视图已经决定了子视图的精确大小，这种模式下View的测量值就是SpecSize的值。
- AT_MOST：最大值测量模式，当视图的宽高指定为wrap_content时生效，此时子视图的尺寸可以是不超过父视图允许的最大尺寸的任何尺寸。
- UNSPECIFIED：不指定测量模式, 父视图没有限制子视图的大小，子视图可以是想要的任何尺寸，通常用于系统内部，应用开发中很少用到。



对于DecorView而言，它的MeasureSpec由窗口尺寸和其自身的LayoutParams共同决定；对于普通的View，它的MeasureSpec由父视图的MeasureSpec和其自身的LayoutParams共同决定。

![img](https://user-gold-cdn.xitu.io/2020/3/1/17095ea6bb1e4bac?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



##### 2、measure 过程

###### 1）View 的 measure

View 中的 measure() 是一个 final 类型的方法，其中会调用 onMeasure()

```java
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                         getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
}
```

setMeasuredDimension() 会设置 View 宽/高的测量值。

直接继承 View 的自定义控件需要重写 onMeasure() 并设置 wrap_content 时的自身大小，否则在布局中使用 wrap_content 就相当于使用 match_parent。

###### 2）ViewGroup 的 measure

- ViewGroup 除了完成自己的 measure 过程以外，还会遍历调用所有子元素的 measure 方法，各个子元素再递归去执行这个过程。
- 测量某个指定 View 时，根据父容器的 MeasureSpec 和子 View 的 LayoutParams 等信息计算子 View 的 MeasureSpec.
- 将计算出的 MeasureSpec 传入 View 的 measure()。

ViewGroup 是一个抽象类，没有实现 onMeasure，具体测量过程由各个子类实现。

以 LinearLayout 竖直方向为例：

- 遍历子元素对每个子元素执行 measureChildBeforeLayout()，这个方法内部会调用子元素的 measure()；
- 系统会通过 mTotalLength 变量来存储竖直方向上的高度。每测量一个子元素，mTotalLength就会增加，增加的部分主要包括了子元素的高度以及子元素在竖直方向上的margin等。
- 当子元素测量完毕后，LinearLayout 会测量自己的大小；
- 最终通过 setMeasureDimension() 设置测量后的宽高；



由于 View 的 measure 过程和 Activity 的生命周期方法不是同步执行的，因此无法保证 Activity 执行了 onCreate()、onStart()、onResume() 时某个 View 已经测量完毕了。

正确获取 View 宽高的方式：

1. Activity/View#onWindowFocusChanged

   此时 View 已经初始化完毕，当 Activity 的窗口得到焦点和失去焦点时均会被调用一次，如果频繁地进行 onResume() 和 onPause()，那么 onWindowFocusChanged() 也会被频繁地调用。

2. view.post(runnable)

   通过 post() 可以将一个 runnable 投递到消息队列的尾部，然后等待 Looper 调用此 runnable 的时候，View 已经初始化好了。

   > 因为 View.post() 将 runnable 存到了 HandlerActionQueue#mActions 数组中，而执行这些任务的方法在 ViewRootImpl#performTraversals() 中被调用 getRunQueue().executeActions(mAttachInfo.mHandler)，而 perfromTraversals() 同样是被封装到 Runnable 中通过 Handler 执行，根据消息机制，消息执行的先后顺序，所以当执行 View.post() 中的 runnable 时，View已经绘制完成，所以能够拿到 View 的宽高。

3. ViewTreeObserver#addOnGlobalLayoutListener，当 View 树的状态发生改变或者 View 树内部的 View 的可见性生改变时，onGlobalLayout() 将会被回调。

4. View.measure(int widthMeasureSpec, int heightMeasureSpec)

   - match_parent 时不知道 parentSize 的大小，测不出；
   - 具体数值时，通过 makeMeasureSpec() 传入固定值，得到 MeasureSpec，然后调用 view.measure()即可。
   - wrap_content 时，在最大化模式下，用 View 理论上能支持的最大值去构造 MeasureSpec 是合理的。

##### 3、layout 过程

当 ViewGroup 的位置被确定后，它在 onLayout 中会遍历所有的子元素并调用其 layout 方法。

<font color='red'>layout() 确定 View 本身的位置，而 onLayout() 确定所有子元素的位置。</font>



顶级 View 是 DecorView 它的位置由屏幕决定，然后一级一级往下，父视图确定子视图的位置。

```java
private void performLayout(WindowManager.LayoutParams lp, int desiredWindowWidth,
        int desiredWindowHeight) {
    final View host = mView;//这个mView是DecorView
    if (host == null) {
        return;
    }
    
    //测量时，DecorView的MeasureSpec由窗口大小和自身LayoutParams确定
    try {
        host.layout(0, 0, host.getMeasuredWidth(), host.getMeasuredHeight());
    } finally {
    }
}
```

View 中的 layout() 流程如下：

1. 首先通过 setFrame() 来设定 View 的四个顶点的位置，即初始化 mLeft，mRight，mTop 和 mBottom 四个值。四个顶点一旦确定，那么 View 在父容器中的位置也就确定了。
2. 调用 onLayout()，这个方法的用途是父容器确定子元素的位置，如果是 ViewGroup 就重写这个方法。

以 LinearLayout 的 onLayout() 竖直方向为例：

1. 遍历子元素，调用 setChildFrame()，确定子元素的位置。其中的childTop会逐渐增大，意味着后面的子元素会被放置在靠下的位置。

2. setChildFrame() 中仅仅是调用子 view 的 layout()，layout() 中会通过 setFrame() 去设置四个顶点的位置，这样子元素的位置就确定了。

   setChildFrame() 中使用的 width 和 height 实际上就是子元素的测量宽/高。

注意：在View的默认实现中，View的测量宽/高和最终宽/高是相等的，只不过测量宽/高形成于View的measure过程，而最终宽/高形成于View的layout过程，即两者的赋值时机不同，测量宽/高的赋值时机稍微早一些。在一些特殊的情况下则两者不相等：

- 重写View的layout方法,使最终宽度总是比测量宽/高大100px。
- View需要多次measure才能确定自己的测量宽/高，在前几次测量的过程中，其得出的测量宽/高有可能和最终宽/高不一致，但最终来说，测量宽/高还是和最终宽/高相同。



##### 4、draw 过程

1. 绘制背景	 drawBackground(canvas)
2. 绘制内容     onDraw(canvas)
3. 绘制子元素 dispatchDraw(canvas)
4. 绘制装饰     onDrawForeground(canvas)

> setVillNotDraw() 如果一个 View 不需要绘制任何内容，那么设置这个标记位为 true 后，系统会进行相应的优化。默认情况下，View 没有启用，ViewGroup 默认启用了这个标志；



##### 5、View.post()原理

```java
public boolean post(Runnable action) {
    final AttachInfo attachInfo = mAttachInfo;
    if (attachInfo != null) {//
        return attachInfo.mHandler.post(action);
    }

    // Postpone the runnable until we know on which thread it needs to run.
    // Assume that the runnable will be successfully placed after attach.
    getRunQueue().post(action);
    return true;
}
```

mAttachInfo 是在 View#dispatchAttachedToWindow() 中被赋值的，此方法被 ViewRootImpl#performTraversals() 中被调用，attachInfo中的mHandler 绑定的是主线程。

即如果 View 还没有被 attachedToWindow 会先将 runnable 缓存起来，然后在 dispatchAttachedToWindow() 时被调用，与此同时 performTraversals() 会调用 measure、layout、draw，因为消息机制通常情况下是一个 Message 执行完后才去取下一个 Message 来执行，所以View.post(Runnable) 中的 Runnable 操作肯定会在 performMeaure() 之后才执行（因为 performTraversals() 也是封装在一个 Runnable 中，发送到 MessageQueue 中去执行的），所以此时可以获取到 View 的宽高。



![img](https://img-blog.csdn.net/20180423152550668?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3JldXhmaGM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

##### 6、requestLayout() 和 invalidate() 以及 postInvalidate() 的区别？

- requestLayout：

  requestLayout会直接递归调用父窗口的requestLayout，直到ViewRootImpl,然后触发peformTraversals，由于mLayoutRequested为true，**会导致onMeasure和onLayout被调用。不一定会触发OnDraw**。requestLayout触发onDraw可能是因为在在layout过程中发现l,t,r,b和以前不一样，那就会触发一次invalidate，所以触发了onDraw，也可能是因为别的原因导致mDirty非空（比如在跑动画）

  > 每一个View 都有一个父 View（除了顶级 DecorView），即 View 中的 `protected ViewParent mParent;` 该值在 assignParent(ViewParent parant) 中赋值，这个方法在 ViewGroup 中的 addViewInner() 中被调用，addViewInner() 在 addView(View child, LayoutParams params) 中被调用，此 addView() 方法在 LayoutInflater 中解析 xml 布局时被调用，所以顶级 View 以下都会赋值 mParent；

- invalidate：

  view的invalidate不会导致ViewRootImpl的invalidate被调用，而是递归调用父view的invalidateChildInParent，直到ViewRootImpl的invalidateChildInParent，然后触发peformTraversals，会导致当前view被重绘,由于mLayoutRequested为false，**不会导致onMeasure和onLayout被调用，而OnDraw会被调用**

  > 当子View调用了invalidate方法后，会为该View添加一个标记位，同时不断向父容器请求刷新，父容器通过计算得出自身需要重绘的区域，直到传递到ViewRootImpl中，最终触发performTraversals方法，进行开始View树重绘流程(只绘制需要重绘的视图)。

- postInvalidate：

  postInvalidate是在非UI线程中调用，invalidate则是在UI线程中调用。

> 所以当我们进行View更新时，若仅View的显示内容发生改变且新显示内容不影响View的大小、位置，则只需调用invalidate方法；
> 若View宽高、位置发生改变且显示内容不变，只需调用requestLayout方法；
> 若两者均发生改变，则需调用两者，按照View的绘制流程，推荐先调用requestLayout方法再调用invalidate方法。



##### 7、requestlayout()，onLayout()，onDraw()，drawChild() 区别与联系？

- requestlayout()

  会导致调用 measure() 过程和 layout() 过程，将会根据标志位判断是否需要 onDraw()。

- onLayout()

  如果是 ViewGroup，需要重写该方法，对子元素进行布局。

- onDraw()

  每个 View 都需要实现该方法，绘制视图本身，ViewGroup 不需要实现。

- drawChild() 

  ViewGroup 通过 dispatchDraw() 调用 drawChild() 去调用每个子元素的 draw()

  ```java
  protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
      return child.draw(canvas, this, drawingTime);
  }
  ```

##### 8、自定义 view 流程

![img](https://upload-images.jianshu.io/upload_images/2099469-68e56bd5d78069f7.png?imageMogr2/auto-orient/strip|imageView2/2/w/620/format/webp)

> 从View继承一般需要忙活的方法是onDraw这里
>
> 从ViewGroup继承一般需要忙活的方法是onLayout这里



##### 9、自定义 WIFI 信号显示

- 继承 ImageView
- 自定义广播接收器，接收 WiFi 状态
- 重写 onAttachedToWindow() 注册广播， onDetachedFromWindow() 解注册
- 广播接收器 onReceive() 中，解析接收到的信息，通过 Handler 切换到 UI 线程，根据状态分别设置不同的 WiFi 图片

##### 10、自定义 View 的优化方向

1. onDraw() 中，不应该在这里做内存分配的事情，因为它会导致内存抖动触发GC，从而导致卡顿。在初始化或者动画间隙期间做分配内存的动作。不要在动画正在执行的时候做内存分配的事情。
2. 减少调用 invalidate() 的次数，它会导致 onDraw() 的调用。如果可能的话，尽量调用含有4个参数的invalidate()方法而不是没有参数的invalidate()。没有参数的invalidate会强制重绘整个view。
3. 任何时候执行requestLayout()，会使得Android UI系统去遍历整个View的层级来计算出每一个view的大小。如果找到有冲突的值，它会需要重新计算好几次。另外需要尽量保持View的层级是扁平化的，这样对提高效率很有帮助。
4. 如果你有一个复杂的UI，你应该考虑写一个自定义的ViewGroup来执行他的layout操作。与内置的view不同，自定义的view可以使得程序仅仅测量这一部分，这避免了遍历整个view的层级结构来计算大小。



##### 11、自定义 View 机型适配

1. 尺寸问题

   自定义中`getWidth()、getHeight()、canvas.drawCircle(）`等方法获取的值和设置的值是`px`值。

   可以使用工具方法 dp2px()

2. 针对不同的机型，使用不同的布局文件放在对应的目录下



##### 12、View 优化

1. 布局优化

   - 减少布局层级，选取合适的布局
   - 使用 `<include>`、`<merge>`标签和 ViewStub

2. 绘制优化

   - onDraw() 中不要做耗时操作

     View 的绘制帧率 60fps 是最佳的，即每帧绘制的时间不超过 16ms

   - onDraw() 中不要创建对象

##### 13、布局对比

- RelativeLayout会让子View调用2次onMeasure，LinearLayout 在有weight时，也会调用子View2次onMeasure
- RelativeLayout的子View如果高度和RelativeLayout不同，则会引发效率问题，当子View很复杂时，这个问题会更加严重。如果可以，尽量使用padding代替margin。
- 在不影响层级深度的情况下,使用LinearLayout和FrameLayout而不是RelativeLayout。



RelativeLayout 的 onMeasure() 过程：

1. 对子view的相对位置依赖进行排序
2. 横向测量每个子view
3. 纵向测量每个子view
4. 设置baseLine
5. 根据当前布局宽高是否确定，修正每个子view的宽高及四个端点
6. 根据当前布局重心，设置每个子view的端点
7. 如果当前布局是从左往右显示，就再次对每个子view的左右端点进行调整
8. 保存当前布局view的尺寸



**ConstraintLayout的优点：**

兼顾LinearLayout与RelativeLayout的优点, 非常适合构建复杂布局, 降低布局的层级, 加快渲染速度。



##### 14、计算布局的层级

```java
private int maxDeep(View view) {
    //当前的view已经是最底层view了，不能往下累加层数了，返回0，代表view下面只有0层了
    if (!(view instanceof ViewGroup)) {
        return 0;
    }
    ViewGroup vp = (ViewGroup) view;
    //虽然是viewgroup，但是如果并没有任何子view，那么也已经是最底层view了，不能往下累加层数了，返回0，代表view下面只有0层了
    if (vp.getChildCount() == 0) {
        return 0;
    }
    //用来记录最大层数
    int max = 0;
    //广度遍历view
    for (int i = 0; i < vp.getChildCount(); i++) {
        //由于vp拥有子view，所以下面还有一层，因为可以+1，来叠加一层，然后再递归几岁算它的子view的层数
        int deep = maxDeep(vp.getChildAt(i)) + 1;
        //比较哪个大就记录哪个
        if (deep > max) {
            max = deep;
        }
    }
    return max;
}
```



##### 15、自定义View 设置圆角

https://blog.csdn.net/bobo_zai/article/details/104653340

- clipPath()  在onDraw()中，canvas.clipPath(mPath);  mPath.addRoundPath()
  简单，但是带锯齿
- Xfermode
  可定义高，支持多图层的圆角，但是麻烦
- BitmapShader   遮罩方式
   mPaint.setShader(mShader);
   canvas.drawRoundRect()
   简单有效，仅支持Bitmap圆角，如果是LayerDrawable就显得手足无措。

|              | `BitmapShader` | `Xfermode`     | `clipPath` | `RoundedBitmapDrawable` |
| ------------ | -------------- | -------------- | ---------- | ----------------------- |
| 实现方式难易 | 简单           | 简单           | 简单       | 最简单                  |
| 空白的背景   | 不能设置       | 可以设置       | 可以设置   | 不能设置                |
| 抗锯齿       | 可以设置，有效 | 可以设置，有效 | 不可去除   | 可以设置，有效          |
| 推荐指数     | 推荐           | 推荐           | 不推荐     | 推荐                    |





#### 九、Activity、Window、DecorView、ViewRootImpl

##### 1、Activity、Window、View 之间的关系？页面从不可见到可见的过程？

![img](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTgwMzAxMTAyMjUyOTk1)

一个 Activity 对应一个 Window 也就是 PhoneWindow，一个 PhoneWindow 持有一个 DecorView 的实例，DecorView 本身是一个 FrameLayout。

- 启动一个 Activity 最终走到 `ActivityThread#performLaunchActivity()`，其中会通过反射创建相应的 Activity，并调用 activity.attach() 方法；

  在 Activity#attach() 中 会创建 PhoneWindow 对象；

- 然后 ActivityThread#performLaunchActivity() 中会回调 Activity 的 onCreate()；

- 当我们新建一个 Activity 时，在 onCreate() 中会调用 setContentView() 设置相应的布局，这个方法其实是调用 window.setContentView()，即 PhoneWindow 中对应的方法；

- 在 PhoneWindow#setContentView() 中会创建 DecorView，mContentParent 是 @android:id/content 对应的 FrameLayout，将 Activity 中设置的布局通过 LayoutInflater 加载到此 View 上；

- 回到 ActivityThread，最终调用 handleResumeActivity()，其中会调用 Activity 的 onResume() 但是此时页面还是不可见状态；

  继续往下走，会通过 window.getDecorView() 获取到 decor，然后通过 windowManager.addView(decor)，添加进 windowManager，此时还是不可见；

- WindowManager 的 实现类是 WindowManagerImpl，查看 addView() 方法，它是通过委托给 WindowManagerGlobal 来完成 window 的添加、删除、更新等操作。

- 在 WindowManagerGlobal#addView() 中会创建 ViewRootImpl，并调用其 setView() 方法

- ViewRootImpl#setView() 最终会调用 requestLayout()，进行 View 的绘制流程。

- 回到 ActivityThread#handleResumeActivity() 中，执行 activity.makeVisible() 方法，在 Activity#makeVisible() 中调用了 mDecor.setVisibility(View.VISIBLE); 最终页面显示出来。

   ![img](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTgwMzAxMTAyMzE3ODMx)

##### 2、Dialog 和 Window 

- 在 Dialog 的构造函数中，创建 PhoneWindow() 对象
  通过 setContentView 设置布局，最终是通过 PhoneWindow#setContentView()生成 DecorView
- 在 Dialog 中的 show() 中，通过 mWindow.getDecorView() 获取 DecorView 实例，
  调用 mWindowManager.addView(mDecor) 进行添加，WindowManager 实现类 WindowManagerImpl，它委托给 WindowManagerGlobal 处理 添加、更新、移除等操作
  在 addView() 方法中，生成 ViewRootImpl，并调用其 setView() 方法，最终进行 View 的绘制流程
- Dialog 构造函数中，会调用 context.getSystemService() 来获取 windowManager，Dialog 只能由 Activity 类型的上下文调用，因为有个 token 需要校验，只有 Activity 重写了 getSystemService() 将其赋值了；


mWindowManager.addView(mDecor) --->ViewRootImpl.setView()--->IWindowSession.addToDisplay()--->WindowManagerService.addWindow()



#### 十、LayoutInflater、布局解析优化、动态换肤

##### 1、LayoutInflater.inflate()





##### 2、Factory2

**应用场景**：

- 全局替换字体等属性

- 动态换肤功能

  对做了标记的View进行识别，然后在onCreateView遍历到它的时候，更改它的一些属性，比如背景色等，然后再交给系统去生成View。

- 无需编写 shape、selector，直接在 xml 设置值

  自定义Factory类，只需要在onCreateView方法里面，判断attrs的参数名字，比如发现名字是我们制定的stroke_color属性，就去通过代码手动帮他去设置这个值



##### 3、动态换肤

**换肤实现思路：**

- 资源打包静态替换方案

  指定资源路径地址，在打包时将对应资源打包进去；

  在 build.gradle 中进行对应配置：

  ```groovy
  sourceSets {
  // 测试版本和线上版本用同一套资源
    YymTest {
        res.srcDirs = ["src/Yym/res", "src/YymTest/res"]
        assets.srcDirs = ["src/Yym/assets"]
     }
   }
  ```

  适合发布马甲版本的app需求。

- 动态换肤方案

  应用运行时，选择皮肤后，在主 app 中拿到对应皮肤包的 Resource，将皮肤包中的资源动态加载到应用中展示。

  动态换肤一般步骤：

  - 下载并加载皮肤包；
  - 拿到皮肤包 Resource 对象；
    - 根据皮肤包路径，通过 PackageManager 得到皮肤包包名；
    - 通过 AssetManager 反射调用 "addAssetPath"，传入皮肤包路径，加载皮肤资源；
    - 传入 assetManager 构建 Resources 对象；
  - 标记需要换肤的 View；
    - 在 xml 中通过 skin:enable="true"  进行标记需要换肤的 View；
    - 通过 LayoutInflater.setFactory2() 设置自定义 Factory2，在接口回调 onCreateView() 中可以拦截从 xml 中映射的 View，进行相关 View 的创建操作，包括对 View 属性的设置，以实现换肤效果；
    - 如果 onCreateView() 返回 null 的话，会将创建 View 的操作交给系统默认的 Factory2 处理；
    - 如果 skin:enable 为 true，则创建 View， 并将这个 View 的所有属性与支持换肤的属性进行对比，在换肤时如果存在某个属性，则替换，同时将这个需要换肤的 View 进行保存；
  - 切换时即时刷新页面；
  - 制作皮肤包；
  - 换肤整体框架的搭建；



### Android 进阶

#### 一、自定义控件

##### 1、自定义控件的分类

- 自定义组合控件：多个控件组合成为一个新的控件，方便多处复用；
- 继承系统 View 控件：继承自 TextView 等系统控件，在系统控件的基础功能上进行扩展；
- 继承 View：不复用系统控件逻辑，继承 View 进行功能定义；
- 继承系统 ViewGroup：继承自 LinearLayout 等系统控件，在系统控件的基础上进行扩展；
- 继承 ViewGroup：不复用系统控件逻辑，继承 ViewGroup 进行功能定义；

##### 2、View 的绘制流程

View的绘制基本由measure()、layout()、draw()这个三个函数完成：

- measure：测量View的宽高，主要是View中的 measure()，setMeasuredDimension()，onMeasure() 方法。
- layout：计算当前View以及子View的位置，主要是View中的 layout()，onLayout()，setFrame() 方法。
- draw：视图的绘制工作，主要是View中的 draw()，onDraw() 方法。



##### 3、Android 屏幕坐标系

在Android坐标系中，以屏幕左上角作为原点，这个原点向右是X轴的正轴，向下是Y轴正轴。

| **方法**                        | **描述**                                         |
| ------------------------------- | ------------------------------------------------ |
| **View的获取坐标的方法：**      |                                                  |
| getTop()                        | 获取View自身的定边到其父布局顶边的距离。         |
| getLeft()                       | 获取View自身的左边到父布局左边的距离。           |
| getRight()                      | 获取View自身的右边到父布局左边的距离。           |
| getBottom()                     | 获取View自身的底边到父布局顶边的距离。           |
| **MotionEvent获取坐标的方法：** |                                                  |
| getX()                          | 获取点击事件距离控件左边的距离，即视图坐标。     |
| getY()                          | 获取点击事件距离控件顶边的距离，即视图坐标。     |
| getRawX()                       | 获取点击事件距离整个屏幕左边的距离，即绝对坐标。 |
| getRawY()                       | 获取点击事件距离屏幕顶边的距离，即绝对坐标。     |

视图的宽度和高度，可以使用如下方式计算：

- width = getRight – getLeft
- height = getBottom – getTop



##### 4、自定义 View 流程

![img](https://img-blog.csdnimg.cn/img_convert/3f3504af3d6d3cd6f300f84f3a9aeee8.png)

- 自定义属性

- 重写构造函数

  - 解析获取自定义属性
  - 初始化画笔

- onMeasure

  - ViewGroup--->child.measure()
  - View--->setMeasuredDimension()

- onLayout

  ViewGroup--->child.layout，View 不操作

- onDraw

  - ViewGroup--->dispatchDraw()
  - View--->canvas 绘制

- dispatchTouchEvent、onTouchEvent() 处理事件

  当事件触发时，则要重新布局，改变其位置。

- 数据更新

  数据更新以后，UI也要重新布局，而View则需要重新绘制。



重点有以下几个：

- 控件属性的定义、设置和使用
- 交互处理：事件交互和处理属于重中之重，常常要和事件分发结合在一起研究。
- Canvas和Paint：在进行自定义View开发时，往往会通过画布自己使用画笔进行绘制，这就要求要对Paint、Path、Canvas要做着重的掌握。



#### 二、setVisibility() 源码

总结：

- setVisibility=View.VISIBLE

  invalidate自己，parent，child

- setVisibility=View.INVISIBLE

  改变标记位 PFLAG_DRAWN，以便下次 invalidate() 

- setVisibility=View.GONE

  requestLayout，invalidate parent，然后设置 PFLAG_DRAWN 以便下次 invalidate



- invisible

  view设置为invisible时，view在layout布局文件中会占用位置，但是view为不可见，该view还是会创建对象，会被初始化，会占用资源。

- gone

  view设置gone时，view在layout布局文件中不占用位置，但是该view还是会创建对象，会被初始化，会占用资源。



#### 三、Activity 启动流程，关键点

##### 1、Activity 启动发起

- **Activity#startActivity(intent)**

  - startActivityForResult() ---> Instrumentation#execStartActivity()
  - 传入参数 mMainThread.getApplicationThread()，这是 ActivityThread 内部类 ApplicationThread 对象，其继承自 IApplicationThread.Stub，是个Binder对象；
  - Instrumentation具有跟踪application及activity生命周期的功能，用于android 应用测试框架中代码检测。

- **Instrumentation#execStartActivity()**

  - 这里 Activity 的启动又交给了 ActivityTaskManager.getService()，它是获取一个跨进程服务，ActivityTaskManagerService（ATMS）；

  - 它继承于IActivityTaskManager.Stub，是个Binder对象，并且是通过单例提供服务的。 ATMS是用于**管理Activity及其容器**（任务、堆栈、显示等）的系统服务，运行在系统服务进程（system_server）之中。

    > **ATMS是在Android10中新增的，分担了之前ActivityManagerService（AMS）的一部分功能（activity task相关）。 在Android10 之前 ，这个地方获取的是服务是AMS。理解上，ATMS就隶属于AMS。**

  - ActivityTaskManager.getService().startActivity() 有个返回值 result，且调用了checkStartActivityResult(result, intent)

    这是用来检查Activity启动的结果，如果发生致命错误，就会抛出对应的异常。看到第一个case中就抛出了 have you declared this activity in your AndroidManifest.xml?——如果Activity没在Manifest中注册就会有这个错误。

##### 2、Activity  的管理，ATMS

- **ActivityTaskManagerService#startActivity()**

  - 通过 getActivityStartController().obtainStarter() 方法获取 ActivityStarter 实例 然后调用一系列方法，最后的execute()方法是开始启动 activity；

  - executeRequest(mRequest)

    > 启动一个 activity，用 intent 传递参数，被启动的目标，最终一个 Activity 的信息都封装在一个 ActivityRecord 中，这个在 ActivityStarter#executeRequest() 中创建，创建所需要的信息是从 ActivityTaskManagerService 中的 startActivityAsUser() 构建 ActivityStarter 传过来的；

  - startActivityUnchecked()

  - startActivityInner()

  - ActivityStack#startActivityLocked()

  - 根据变量 mDoResume，决定是否调用 RootWindowContainer.resumeFocusedStacksTopActivities() 将其置于栈顶显示处于活动状态

  > **ActivityStack**：`Activity`在`AMS`的栈管理类，`activity`的单个堆栈的状态和管理在这个类里。

- **RootWindowContainer#resumeFocusedStacksTopActivities()**

  > 在API29中这个方法位于`RootActivityContainer`类中。

- **ActivityStack#resumeTopActivityUncheckedLocked()**

  - resumeTopActivityInnerLocked()

  > ActivityStackSupervisor：负责所有Activity栈的管理。内部管理了mRunningTasks和mRecentTasks两个Activity栈。其中，mRunningTasks是Helper类抽象出用于获取当前运行的任务集的逻辑；mRecentTasks管理的是最近任务的历史列表，包括非活动任务。]

- **ActivityStack#resumeTopActivityInnerLocked()**

  - **startPausingLocked() 执行上一个 Activity 的 onPause()**

    - mAtmService.getLifecycleManager().scheduleTransaction()
    - `mAtmService`即是`ActivityTaskManagerService`，`mAtmService.getLifecycleManager()`返回的是`ClientLifecycleManager`的实例。
    - **ClientLifecycleManager**：该类能够组合多个生命周期转换请求和/或回调，并将它们作为单个事务执行。

  - 启动了的Activity就发送ResumeActivityItem事务给客户端了

  - **next.showStartingWindow(null , false ,false)，这里就是 冷启动时 出现白屏 的原因了：取根activity的主题背景 展示StartingWindow**

  - mStackSupervisor.startSpecificActivity(next, true, true);

    普通activity的正常启动

- **ActivityStackSupervisor.startSpecificActivity()**

  判断应用是否启动了，如果是，就启动 Activity，没有就创建应用进程

  ```java
  void startSpecificActivity(ActivityRecord r, boolean andResume, boolean checkConfig) {
      if (wpc != null && wpc.hasThread()) {
          realStartActivityLocked(r, wpc, andResume, checkConfig);
      }
      mService.startProcessAsync()
      //ActivityTaskManagerService
  }
  ```

- **realStartActivityLocked**()

  ```java
  boolean realStartActivityLocked() {
      // Create activity launch transaction.
      final ClientTransaction clientTransaction = ClientTransaction.obtain(
          	proc.getThread(), r.appToken);
      clientTransaction.addCallback(LaunchActivityItem.obtain(new Intent(r.intent),...);
      // Set desired final state.
      clientTransaction.setLifecycleStateRequest(lifecycleItem);
      // Schedule transaction.
      mService.getLifecycleManager().scheduleTransaction(clientTransaction);
  }
  ```

  - 通过 ClientTransaction.obtain( proc.getThread(), r.appToken)获取了clientTransaction，其中参数proc.getThread()是IApplicationThread，就是前面提到的ApplicationThread在系统进程的代理。

  - **ClientTransaction** 是包含一系列的待客户端处理的事务的容器，客户端接收后取出事务并执行。包括一个回调列表和一个最终的生命周期状态。

  - **ActivityLifecycleItem**：用以请求Activity应该到达的生命周期状态。继承自ClientTransctionItem，主要的子类有DestoryActivityItem、PauseActivityItem、StopActivityItem、ResumeActivityItem等。

    LaunchActivityItem 是用来启动 Activity 的。

  - mService.getLifecycleManager().scheduleTransaction(clientTransaction)

- **ClientLifecycleManager#scheduleTransaction()**

  - 执行的是参数 ClientTransaction 对象的 schedule()
  - **ClientTransaction#schedule()** 中 调用 **mClient.scheduleTransaction(this)**，mClient 即通过 ClientTransaction.obtain(proc.getThread, r.appToken) 构建 ClientTransaction 时传入的 IApplicationThread；
  - 由于 IApplicationThread 是 **ApplicationThread** 在系统进程的代理，所以真正执行的地方就是 客户端的ApplicationThread 中了。也就是说，**Activity启动的操作又跨进程的还给了客户端**。

- 启动Activity的操作从客户端 跨进程 转移到 ATMS，ATMS 通过 ActivityStarter、ActivityStack、ActivityStackSupervisor 对 Activity 任务、activity 栈、Activity 记录 管理后，又用过跨进程把正在启动过程又转移到了客户端。

##### 3、线程切换及消息处理

- ApplicationThread#scheduleTransaction()

  - 执行 ActivityThread.this.scheduleTransaction(transaction);

  - 由于 ActivityThread 继承自 ClientTransactionHandler，scheduleTransaction() 方法是调用其父类的；

    ```java
    void scheduleTransaction(ClientTransaction transaction) {
        transaction.preExecute(this);
        sendMessage(ActivityThread.H.EXECUTE_TRANSACTION, transaction);
    }
    ```

  > 这里通过 ActivityThread 中的自定义 Handler 把消息发送到主线程；
  >
  > 那么是从哪个线程发送的呢？那就要看看ApplicationThread的scheduleTransaction方法是执行在哪个线程了。根据[IPC](https://link.juejin.cn?target=https%3A%2F%2Fblog.csdn.net%2Fhfy8971613%2Farticle%2Fdetails%2F79688664)知识，我们知道，服务器的Binder方法运行在Binder的线程池中，也就是说系统进行跨进程调用ApplicationThread的scheduleTransaction就是执行在Binder的线程池中的了。

- ActivityThread#H#handleMessage()

  ```java
  final ClientTransaction transaction = (ClientTransaction) msg.obj;
  mTransactionExecutor.execute(transaction);
  ```

- **TransactionExecutor#execute()**

  ```java
  public void execute(ClientTransaction transaction) {
      if (DEBUG_RESOLVER) Slog.d(TAG, tId(transaction) + "Start resolving transaction");
      final IBinder token = transaction.getActivityToken();
      //...
      executeCallbacks(transaction);
      executeLifecycleState(transaction);
      //...
  }
  ```

- **executeCallbacks(transaction)**

  遍历callbacks，调用ClientTransactionItem的execute方法

- **LaunchActivityItem#execute()**

  ```java
  public void execute(ClientTransactionHandler client, IBinder token,
          PendingTransactionActions pendingActions) {
      ActivityClientRecord r = new ActivityClientRecord(token, mIntent, ...);
      client.handleLaunchActivity(r, pendingActions, null /* customIntent */);
      Trace.traceEnd(TRACE_TAG_ACTIVITY_MANAGER);
  }
  ```

  - 里面调用了 client.handleLaunchActivity 方法，client 是 ClientTransactionHandler的实例，是在TransactionExecutor 构造方法传入的，TransactionExecutor 创建是在 ActivityThread 中， 传入的是 ActivityThread 本身 this，
  - 所以，client.handleLaunchActivity 方法就是 ActivityThread 的 handleLaunchActivity() 方法。

- 到这里 **ApplicationThread 把启动 Activity 的操作，通过 mH 切到了主线程，走到了 ActivityThread 的 handleLaunchActivity() 方法**。

##### 3、Activity启动核心实现——初始化及生命周期

- **ActivityThread#handleLaunchActivity()**

- **ActivityThread#performLaunchActivity()**

  ```java
/**  activity 启动的核心实现. */
  private Activity performLaunchActivity(ActivityClientRecord r, Intent customIntent) {
      //1、从ActivityClientRecord获取待启动的Activity的组件信息
      ActivityInfo aInfo = r.activityInfo;
      ComponentName component = r.intent.getComponent();
      //创建ContextImpl对象
      ContextImpl appContext = createBaseContextForActivity(r);
      //2、反射创建activity实例
      Activity activity = null;
      try {
          java.lang.ClassLoader cl = appContext.getClassLoader();
          activity = mInstrumentation.newActivity(
              	cl, component.getClassName(), r.intent);
      }
      try {
          //3、创建Application对象（如果没有的话）
          Application app = r.packageInfo.makeApplication(false, mInstrumentation);
          if (activity != null) {
              appContext.setOuterContext(activity);
              //4、attach方法为activity关联上下文环境
              activity.attach(appContext, this, getInstrumentation(),...);
              //5、调用生命周期onCreate
              if (r.isPersistable()) {
                  mInstrumentation.callActivityOnCreate(activity, r.state, r.persistentState);
              } else {
                  mInstrumentation.callActivityOnCreate(activity, r.state);
              }
          }
      }
  }
  ```
  
  performLaunchActivity主要完成以下事情：
  
  1. 从ActivityClientRecord获取待启动的Activity的组件信息
  2. 通过mInstrumentation.newActivity方法使用类加载器创建activity实例
  3. 通过LoadedApk的makeApplication方法创建Application对象，内部也是通过mInstrumentation使用类加载器，创建后就调用了instrumentation.callApplicationOnCreate方法，也就是Application的onCreate方法。
  4. 创建ContextImpl对象并通过activity.attach方法对重要数据初始化，关联了Context的具体实现ContextImpl，attach方法内部还完成了window创建，这样Window接收到外部事件后就能传递给Activity了。
  5. 调用Activity的onCreate方法，是通过 mInstrumentation.callActivityOnCreate方法完成。
  
  **到这里Activity的onCreate方法执行完，那么onStart、onResume呢？**
  
  - LaunchActivityItem 远程App端的onCreate生命周期事务
  - ResumeActivityItem 远程App端的onResume生命周期事务
  - PauseActivityItem 远程App端的onPause生命周期事务
  - StopActivityItem 远程App端的onStop生命周期事务
  - DestroyActivityItem 远程App端onDestroy生命周期事务
  - ClientTransaction 客户端事务控制者
  - ClientLifecycleManager 客户端的生命周期事务控制者
  - TransactionExecutor 远程通信事务执行者
  
- 回到 ActivityStackSupervisor#realStartActivityLocked()

  - 通过 clientTransaction.addCallback() 添加 LaunchActivityItem 实例；

  - 接着调用了 clientTransaction.setLifecycleStateRequest(lifecycleItem) 方法，lifecycleItem 是 ResumeActivityItem 或PauseActivityItem 实例；
  
  - 通过 clientTransaction.setLifecycleStateRequest(lifecycleItem); 进行设置；
  
    mLifecycleStateRequest 表示执行 transaction 后的最终的生命周期状态。
  
    ```java
    /**
     * Final lifecycle state in which the client activity should be after the transaction is executed.
     */
    private ActivityLifecycleItem mLifecycleStateRequest;
    public void setLifecycleStateRequest(ActivityLifecycleItem stateRequest) {
        mLifecycleStateRequest = stateRequest;
    }
    ```
  
- 继续 mService.getLifecycleManager().scheduleTransaction(clientTransaction) 流程；

  一路追踪回到 TransactionExecutor#execute()，之前看的是 executeCallbacks(transaction)，现在看 executeLifecycleState(transaction)

- **TransactionExecutor#executeLifecycleState()**

  这里取出了 ActivityLifecycleItem 并且调用了它的 execute 方法，实际就是 ResumeActivityItem 的方法。

  ```java
  @Override
  public void execute(ClientTransactionHandler client, IBinder token,
          PendingTransactionActions pendingActions) {
      Trace.traceBegin(TRACE_TAG_ACTIVITY_MANAGER, "activityResume");
      client.handleResumeActivity(token, true /* finalStateRequest */, mIsForward,
              "RESUME_ACTIVITY");
      Trace.traceEnd(TRACE_TAG_ACTIVITY_MANAGER);
  }
  ```
  
- **进入 ActivityThread#handleResumeActivity()**

  handleResumeActivity() 主要做了以下事情：

  1. 调用生命周期：通过 performResumeActivity() 方法，内部调用生命周期 onStart、onResume 方法
  2. 设置视图可见：通过 activity.makeVisible() 方法，添加 [window](https://link.juejin.cn?target=https%3A%2F%2Fblog.csdn.net%2Fhfy8971613%2Farticle%2Fdetails%2F103241153)、设置可见。（所以视图的真正可见是在onResume方法之后）
  
- **performResumeActivity()**

  - 调用 r.activity.performResume(r.startsNotResumed, reason);

  - 进入 Activity#performResume()
  
    ```java
    final void performResume(boolean followedByPause, String reason) {
        performRestart(true /* start */, reason);
        // mResumed is set by the instrumentation
        mInstrumentation.callActivityOnResume(this);
        //这里是走fragment的onResume
        mFragments.dispatchResume();
        mFragments.execPendingActions();
    }
    ```
  
    - performStart() 内部调用了 mInstrumentation.callActivityOnStart(this)，也就是 **Activity 的 onStart()** 方法了。
    - 然后是 mInstrumentation.callActivityOnResume，也就是 **Activity 的 onResume()** 方法了。到这里启动后的生命周期走完了。
  
- **设置视图可见，即activity.makeVisible()方法**

  ```java
//Activity
  void makeVisible() {
      if (!mWindowAdded) {
          ViewManager wm = getWindowManager();
          wm.addView(mDecor, getWindow().getAttributes());
          mWindowAdded = true;
      }
      mDecor.setVisibility(View.VISIBLE);
  }
  ```
  
  - 这里把 activity 的顶级布局 mDecor 通过 windowManager.addView() 方法，把视图添加到[window](https://link.juejin.cn/?target=https%3A%2F%2Fblog.csdn.net%2Fhfy8971613%2Farticle%2Fdetails%2F103241153)，并设置 mDecor可见。到这里视图是真正可见了。值得注意的是，视图的真正可见是在 onResume 方法之后的。
  - 另外一点，Activity 视图渲染到 Window 后，会设置 window 焦点变化，先走到 DecorView 的 onWindowFocusChanged() 方法，最后是到 Activity 的 onWindowFocusChanged() 方法，表示首帧绘制完成，此时Activity可交互。

![img](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2020/7/12/17343b491419f3e9~tplv-t2oaga2asx-zoom-in-crop-mark:1304:0:0:0.awebp)

**涉及的类梳理如下：**

| 类名                                   | 作用                                                         |
| -------------------------------------- | ------------------------------------------------------------ |
| ActivityThread                         | 应用的入口类，系统通过调用main函数，开启消息循环队列。ActivityThread所在线程被称为应用的主线程（UI线程） |
| ApplicationThread                      | 是ActivityThread的内部类，继承IApplicationThread.Stub，是一个IBinder，是ActiivtyThread和AMS通信的桥梁，AMS则通过代理调用此App进程的本地方法，运行在Binder线程池 |
| H                                      | 继承Handler，在ActivityThread中初始化，即主线程Handler，用于主线程所有消息的处理。本片中主要用于把消息从Binder线程池切换到主线程 |
| Intrumentation                         | 具有跟踪application及activity生命周期的功能，用于监控app和系统的交互 |
| ActivityManagerService                 | Android中最核心的服务之一，负责系统中四大组件的启动、切换、调度及应用进程的管理和调度等工作，其职责与操作系统中的进程管理和调度模块相类似，因此它在Android中非常重要，它本身也是一个Binder的实现类。 |
| ActivityTaskManagerService             | 管理activity及其容器（task, stacks, displays）的系统服务（Android10中新增，分担了AMS的部分职责） |
| ActivityStarter                        | 用于解释如何启动活动。该类收集所有逻辑，用于确定Intent和flag应如何转换为活动以及相关的任务和堆栈 |
| ActivityStack                          | 用来管理系统所有的Activity，内部维护了Activity的所有状态和Activity相关的列表等数据 |
| ActivityStackSupervisor                | 负责所有Activity栈的管理。AMS的stack管理主要有三个类，ActivityStackSupervisor，ActivityStack和TaskRecord |
| ClientLifecycleManager                 | 客户端生命周期执行请求管理                                   |
| ClientTransaction                      | 是包含一系列的 待客户端处理的事务 的容器，客户端接收后取出事务并执行 |
| LaunchActivityItem、ResumeActivityItem | 继承ClientTransactionItem，客户端要执行的事务信息，启动activity |

以上就是一个 **普通Activity** 启动的完整流程。

##### 4、根 Activity 的启动

我们知道，想要启动一个应用程序（App），需要点击手机桌面的应用图标。Android系统的桌面叫做**Launcher**，有以下作用：

- 作为Android系统的启动器，用于启动应用程序。
- 作为Android系统的桌面，用于显示和管理应用程序的快捷图标和其他桌面组件。

Launcher本身也是一个应用程序，它在启动过程中会请求PackageManageService（系统的包管理服务）返回系统中已经安装的app的信息，并将其用快捷图标展示在桌面屏幕上，用户可以点击图标启动app。



###### 4.1 应用进程的创建

- 当点击 app 图标后，Launcher 会在桌面 activity（此activity就叫[Launcher](https://link.juejin.cn?target=https%3A%2F%2Fgithub.com%2Famirzaidi%2FLauncher3%2Fblob%2Ff7951c32984036eef2f2130f21abded3ddf6160a%2Fsrc%2Fcom%2Fandroid%2Flauncher3%2FLauncher.java)）内调用 startActivitySafely() 方法，startActivitySafely() 方法会调用 startActivity() 方法。接下来的部分就和上面分析的 Activity 启动的发起 过程一致了，即通过 IPC 走到了 ATMS，直到 ActivityStackSupervisor 的 startSpecificActivityLocked() 方法中对应用进程是否存在的判断。

  > 应用进程存在的判断条件是：wpc != null && wpc.hasThread()，这里判断 **IApplicationThread不为空 就代表进程已存在**，为啥这么判断呢？这里先猜测，进程创建后，一定会有给IApplicationThread赋值的操作。

- 创建进程是通过 ActivityTaskManagerService 的mH（继承handler）发送了一个消息，消息中第一个参数是ActivityManagerInternal::startProcess，ActivityManagerInternal 的实现是 AMS 的内部类 LocalService，LocalService 的 startProcess() 方法调用了 AMS 的 startProcessLocked() 方法。

- AMS#startProcessLocked() 方法一路向下，最终调用了 **Process.start()** 来创建进程；

- Process.start()

  - 调用了 ZYGOTE_PROCESS.start()
  - ZYGOTE_PROCESS 是用于保持与 Zygote 进程的通信状态，发送 socket 请求与 Zygote 进程通信。**Zygote 进程**是**进程孵化器**，用于创建进程。
    - Zygote 通过 fork 创建了一个进程
    - 在新建的进程中创建 Binder 线程池（此进程就支持了 Binder IPC）
    - 最终是通过反射获取到了 ActivityThread 类并执行了 main 方法

###### 4.2 根 Activity 的启动

- Zygote 进程 fork 出一个子进程后，通过反射最终调用到了 ActivityThread#main()；

  ```java
  public static void main(String[] args) {
      Looper.prepareMainLooper();
      ActivityThread thread = new ActivityThread();
      thread.attach(false, startSeq);
      Looper.loop();
  }
  ```

- 创建 ActivityThread 实例，调用 attach() 方法

  ```java
  private void attach(boolean system, long startSeq) {
      if (!system) {
          final IActivityManager mgr = ActivityManager.getService();
          try {
              //把ApplicationThread实例关联到AMS中
              mgr.attachApplication(mAppThread, startSeq);
          } catch (RemoteException ex) {
              throw ex.rethrowFromSystemServer();
          }
      }
  }
  ```

  > mgr 就是 AMS 在客户端的代理，所以 mgr 的 attachApplication 方法，就是 IPC 的走到 AMS 的attachApplication() 方法。

- AMS#attachApplication()

  调用 attachApplicationLocked()

- AMS#attachApplicationLocked()

  ```java
  private boolean attachApplicationLocked(@NonNull IApplicationThread thread,
          int pid, int callingUid, long startSeq) {
      //1、IPC操作，创建绑定Application
      thread.bindApplication(processName, appInfo, providerList, ...);
      //2、赋值IApplicationThread
      app.makeActive(thread, mProcessStats);
      // See if the top visible activity is waiting to run in this process...
      if (normalMode) {
          try {
              //3、通过ATMS启动 根activity
              didSomething = mAtmInternal.attachApplication(...);
          } catch (Exception e) {
              Slog.wtf(TAG, "Exception thrown launching activities in " + app, e);
              badApp = true;
          }
      }
  }
  ```

  AMS的attachApplicationLocked方法主要三件事：

  - 调用IApplicationThread的bindApplication方法，IPC操作，创建绑定Application；
  - 通过makeActive方法赋值IApplicationThread，即验证了上面的猜测（创建进程后赋值）；
  - 通过ATMS启动 **根activity**

- **ProcessRecord#makeActive()**

  ```java
  public void makeActive(IApplicationThread _thread, ProcessStatsService tracker) {
      //...
      thread = _thread;
      mWindowProcessController.setThread(thread);
  }
  ```

  > 使用 mWindowProcessController.setThread(thread) 确实完成了 IApplicationThread 的赋值。这样就可以依据 IApplicationThread 是否为空判断进程是否存在了。

- **创建绑定 Application 的过程**

  IApplicationThread 的 bindApplication 方法实现是客户端的 ApplicationThread 的 bindApplication 方法，它又使用 H 转移到了 ActivityThread 的 handleBindApplication 方法（从Binder线程池转移到主线程）

  ```java
  private void handleBindApplication(AppBindData data) {
      final LoadedApk pi = getPackageInfo(instrApp, data.compatInfo,
              appContext.getClassLoader(),...);
      final ContextImpl instrContext = ContextImpl.createAppContext(this, pi,
              appContext.getOpPackageName());
      try {
          final ClassLoader cl = instrContext.getClassLoader();
          //创建Instrumentation
          mInstrumentation = (Instrumentation)
              cl.loadClass(data.instrumentationName.getClassName()).newInstance();
      }
      try {
          //创建Application
          app = data.info.makeApplication(data.restrictedBackupMode, null);
      }
      try {
          //内部调用Application的onCreate方法
          mInstrumentation.callApplicationOnCreate(app);
      }
  }
  ```

  - 主要就是创建 Application，并且调用生命周期 onCreate 方法。你会发现在前面介绍的 ActivityThread 的 performLaunchActivity 方法中，也有同样的操作，只不过会先判断 Application 是否已存在。也就是说，**正常情况下 Application 的初始化是在 handleBindApplication 方法中的，并且是创建进程后调用的。performLaunchActivity 中只是做了一个检测，异常情况 Application 不存在时才会创建。**
  - 创建 Application 后，内部会 attach() 方法，attach() 内部会调用 attachBaseContext() 方法，**attachBaseContext()** 方法是我们能接触到的一个方法，接着才是 onCreate() 方法。

- 根activity 的启动，回到上面 AMS 的 attachApplicationLocked() 方法

  - 调用了 mAtmInternal.attachApplication() 方法，mAtmInternal 是 ActivityTaskManagerInternal 实例，具体实现是在 ActivityTaskManagerService 的内部类 LocalService；
  - 调用了 mRootWindowContainer.attachApplication(wpc)；

- **RootWindowContainer#attachApplication()**

  - 遍历 Activity 栈，此时理论上应该只有一个根 activity
  - 然后调用 mStackSupervisor.realStartActivityLocked() 方法
  - 通过 ClientTransaction 跨进程交给客户端处理，和上边普通 Activity 的启动一样

- **根 activity 的启动前需要创建应用进程，然后走到 ActivityThread 的 main 方法，开启主线程循环，初始化并绑定 Application、赋值 IApplicationThread，最后真正的启动过程和普通 Activity 是一致的。**

![img](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2020/7/12/17343b49162452f3~tplv-t2oaga2asx-zoom-in-crop-mark:1304:0:0:0.awebp)

##### 5、总结

关于 普通Activity 启动的流程的讲解，我们分成了几个阶段：启动的发起、AMS的管理、线程切换、启动核心实现，知道了启动过程经历了两次IPC，客户端到AMS、AMS到客户端，以及Activity创建和生命周期的执行。 然后又在此基础上 补充的根activity的启动：先创建应用进程，再绑定Application，最后真正启动跟Activity。
链接：https://juejin.cn/post/6847902222294990862



#### 四、HTTP请求加密

https://www.jianshu.com/p/cd69f9b031f2

https://www.freesion.com/article/1283256934/

https://blog.csdn.net/qq_41701363/article/details/115876339

![img](https://www.freesion.com/images/213/62c98d72bcbbd26b7f82809d02a8cafd.JPEG)

##### 1、加密算法

> [加密算法](https://so.csdn.net/so/search?q=加密算法&spm=1001.2101.3001.7020)整体可以分为：可逆加密、不可逆加密
>
> 可逆[加密](https://so.csdn.net/so/search?q=加密&spm=1001.2101.3001.7020)又可以分为对称加密和非对称加密

- URLEncode/URLDecode URL 编码传输

- Base64

- 对称加密

  对称加密算法，又称为共享密钥加密算法。在数据加密和解密时使用的是同一个密钥，这就导致密钥管理困难的问题。常见的对称加密算法有DES, 3DES, AES128, AES192, AES256。其中AES后面的数字代表的是密钥长度。对称加密算法的安全性相对较低，比较适合内网环境中加解密。

  - DES

    > DES加密算法是一种**分组密码**，以64位为分组对数据加密，它的密钥长度是56位，加解密用同一算法。

  - 3DES

    > 基于DES的加密算法，3DES（即Triple DES）是`DES`向`AES`过渡的加密算法，它使用**3个不同的56位的密钥对一块数据进行三次加密**，强度更高。

  - AES

    > `AES` 高级数据加密标准，能够有效抵御已知的针对`DES`算法的所有攻击，默认密钥长度为`128`位，还可以供选择`192`位，`256`位。这里顺便提一句这个位指的是bit。

- 非对称加密

  非对称加密算法，又称为公开密钥加密算法。这两个密钥完全不同但又完全匹配，只有使用匹配的一堆公钥和私钥，才能完成对明文的加密和解密过程。常见的非对称加密有`RSA`, `SM2` 等。

  > 非对称加密的特点是1 v n，服务器只需要一个私钥就能和多个客户端进行加密通信。而对称加密则需要针对每个客户端保存一个密钥。

  - RSA

    > `RSA` **加密算法** 基于一个十分简单的数论事实：将两个大 **素数** 相乘十分容易，但想要对其乘积进行 **因式分解** 却极其困难，因此可以将 **乘积** 公开作为 **加密密钥**。

  - ECC 算法

    > `ECC` 也是一种 **非对称加密算法**，主要优势是在某些情况下，它比其他的方法使用 **更小的密钥**，比如 `RSA` **加密算法**，提供 **相当的或更高等级** 的安全级别。不过一个缺点是 **加密和解密操作** 的实现比其他机制 **时间长** (相比 `RSA` 算法，该算法对 `CPU` 消耗严重)。

- 不可逆加密

  - MD5

    > MD5信息摘要算法（Message-Digest Algorithm），一种被广泛使用的密码散列函数，可以产生出一个128位（16字节）的散列值（hash value），用于确保信息传输完整一致。本质上，其就是一种哈希函数，用于对一段信息产生摘要，以**防止被篡改**。

  - SHA 系列

    > SHA1 是和 MD5 一样流行的 消息摘要算法，然而 SHA1 比 MD5 的 安全性更强。对于长度小于 2 ^ 64 位的消息，SHA1 会产生一个 160 位的 消息摘要。基于 MD5、SHA1 的信息摘要特性以及 不可逆 (一般而言)，可以被应用在检查 文件完整性 以及 数字签名 等场景。

##### 2、HTTPS 的工作流程

![img](https://img-blog.csdnimg.cn/20210419222407733.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQxNzAxMzYz,size_16,color_FFFFFF,t_70)



##### 3、Android 端 AES + RSA 加密

![img](https://upload-images.jianshu.io/upload_images/13750253-2cac4aa3e9b5c5b8.png?imageMogr2/auto-orient/strip|imageView2/2/w/700/format/webp)

Android端

> 服务器端(server)分别生成自己的RSA密钥对,并提供接口给Android客户端获取RSA公钥(rsaPublicKey)
>
> client生成AES密钥(aesKey)
>
> client使用自己的AES密钥(aesKey)对转换为json格式的请求明文数据(data)进行加密，得到加密后的请求数据encryptData
>
> client提供server提供的接口获取RSA公钥(rsaPublicKey)
>
> client使用获取RSA公钥(rsaPublicKey)对AES密钥(aesKey)进行加密，得到encryptAesKey
>
> client将encryptAesKey作为http请求头参数，将加密后的请求数据encryptData作为请求体一起传输给服务器端

服务器端

> server响应client的http请求，读取http请求头。获得client传过来的加密后的AES密钥(encryptAesKey)，读取http请求体，获得client传过来的加密后的请求数据(encryptData)。
>
> server使用自己的RSA私钥(rsaPrivateKey)对加密后的AES密钥(encryptAesKey)进行RSA解密，得到AES密钥(aesKey)
>
> 使用解密后的AES密钥(aesKey)对加密后的请求数据(encryptData),进行AES解密操作，得到解密后的请求数据(data)，该数据为json格式
>
> 对解密后的请求数据(data)进行json解析，然后做相关的响应操作。



##### 4、实际项目中的加密

- 违章查询

  对参数使用 MD5 加密，公共参数统一处理

- 悟空、普惠

  登录相关参数使用 RSA 加密