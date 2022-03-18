## Android

### Android基础

#### 一、Activity

##### 5、onNewIntent()

MainActivity 是 singleTask 的启动模式，且处于栈顶可见状态，再次启动该页面，只会走 onNewIntent()，不会再走其他的方法。

onNewIntent不会再调用onCreate方法了，会直接调用onStart与onResume。如果是已经不可见的Activity（调用了onStop的，则会先调用onRestart之后在调用onStart方法）。

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

MessageQueue#quit()：

```java
void quit(boolean safe) {
    if (!mQuitAllowed) {
        throw new IllegalStateException("Main thread not allowed to quit.");
    }
    //......
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

ThreadLocal 是一个线程内部的数据存储类，通过它可以在指定的线程中存储数据，数据存储以后，只有在指定线程中可以获取到存储的数据。

Thread 类中有个成员变量 threadLocals

```java
ThreadLocal.ThreadLocalMap threadLocals = null;
```

ThreadLocal#set() 方法：

```java
public void set(T value) {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value);
}
```

ThreadLocalMap 结构是数组，用来保存 key-value 的组合 Entry。key 是 ThreadLocal，value 是要存入的值。

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

当子 view 的 onTouchEvent() 返回 true，而父 View 在 MOVE 事件中进行拦截，即 onInterceptTouchEvent() 返回 true，此时子 View 会收到一个 CANCEL 事件。

比如在一个 ScrollView 中，点击一个 Button 后不抬起手，直接滑动，就会产生 CANCEL 事件。

##### 7、父view中两个button，点击一个然后手指不抬起，一直滑出屏幕

只会走第一个点到的 Button 的事件



##### 8、一个列表中，同时对父 View 和子 View 设置点击方法，优先响应哪个？为什么会这样？

子 View
子 View 处理后，就不会在走父 View 的 onTouchEvent() 也就不会再走到父 View 的点击事件。

<font color='red'>但是为什么，在子 View 的 onTouchEvent() 中 返回 true，就不会走到  onClick() 事件。</font>



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

- invalidate：

  view的invalidate不会导致ViewRootImpl的invalidate被调用，而是递归调用父view的invalidateChildInParent，直到ViewRootImpl的invalidateChildInParent，然后触发peformTraversals，会导致当前view被重绘,由于mLayoutRequested为false，**不会导致onMeasure和onLayout被调用，而OnDraw会被调用**

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


mWindowManager.addView(mDecor) --->ViewRootImpl.setView()--->IWindowSession.addToDisplay()--->WindowManagerService.addWindow()











