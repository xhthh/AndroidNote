### 启动简述

参考资料：

1. https://juejin.cn/post/6844904116561379341#comment
2. Android进阶解密



#### 一、系统启动

1. init 进程启动
   
   init 进程是 Android 系统中用户空间的第一个进程。负责创建 Zygote 和属性服务。
   
   - 系统启动，Linux 内核启动，启动 init 进程；
   - 启动 Zygote 进程；
   
2. Zygote 进程启动

   孵化器进程，DVM 和 ART、应用程序进程及 SystemServer 进程都是由 Zygote 进程来创建的。（每个应用进程都是 Zygote 进程 fork 出来的，所以每个 app 都有一个虚拟机实例）

   - 创建 AppRuntime 并调用其 start 方法，启动 Zygote 进程；
   - 创建 Java 虚拟机并为 Java 虚拟机注册 JNI 方法；
   - 通过 JNI 调用 ZygoteInit#main() 进入 Zygote 的 Java 框架层；
   - 通过 registerServerSocketFromEnv()  创建服务器端 Socket，并通过 runSelectLoop() 等待 AMS 的请求来创建新的应用程序进程；
   - 启动 SystemServer 进程；

3. SystemServer 进程启动

   SystemServer 进程主要用于创建系统服务，比如 AMS、WMS 和 PMS 都是由它来创建的。

   - ZygoteInit#zygoteInit() 中启动 Binder 线程池，用于和其他进程进行通信；
   - ZygoteInit#zygoteInit() 中通过反射调用 SystemServer 的 main()，创建 SystemServiceManager，用于对系统的服务进行创建、启动和生命周期管理；
   - 启动各种系统服务，如 AMS；

4. Launcher 进程启动

   系统启动的最后一步是启动一个应用程序（Launcher）用来显示系统中已经安装的应用程序。Launcher 在启动过程中会请求 PackageManagerService 返回系统中已经安装的应用程序的信息，并将这些信息封装成一个快捷图标列表显示在系统屏幕上。

   启动 Launcher 的入口为 AMS 的 systemReady()，它在 SystemServer 的 startOhterService() 中被调用。

   - SystemServer#main()--->startOthersService()--->ams.systemReady()--->。。。--->startHomeActivityLocked()；
   - startHomeActivityLocked() 处理 homeIntent，匹配 Launcher
   - 最终走到 ActivityStartController#startHomeActivity()--->ActivityStarter#execute() 后面和 Activity 的启动过程一样；



#### 二、应用程序进程启动过程

AMS 在启动应用程序时会检查这个应用程序需要的应用程序进程是否存在，不存在就会请求 Zygote 进程通过 fork 创建该应用程序进程。在应用程序进程创建过程中除了获取虚拟机实例外，还创建了 Binder 线程池和消息循环，这样就可以使用 Binder 进行进程间通信以及处理消息了。

##### 1、AMS 发送启动应用程序进程请求

- AMS 通过调用 startProcessLocked() 向 Zygote 进程发送创建应用程序的请求，启动应用程序进程；

  这里 Process.start(entryPoint, ...) entryPoint 是 android.app.ActivityThread。

- Process.start() 调用 ZygoteProcess#start()，处理应用进程的启动参数，与 Zygote 进程建立 Socket 连接；

  > Zygote 进程启动过程中，main() 中创建了 Server 端的 Socket，这里调用 ZygoteState.connect() 与 Socket 建立连接。

##### 2、Zygote 接收请求并创建应用程序进程

- Zygote 进程启动时，ZygoteInit#main() 中创建了 Server 端的 Socket，启动 SystemServer 进程，通过 runSelectLoop() 等待 AMS 的请求；
- ZygoteInit#runSelectLoop()--->ZygoteConnection #processOneCommand()--->ZygoteConnection 处理请求数据；
  - 获取应用程序进程的启动参数；
  - 创建应用程序进程；
  - 处理应用程序进程，调用 ZygoteInit#zygoteInit() 方法，在新建的应用程序进程中创建 Binder 线程池，通过反射调用 ActivityThread#main()；
- 至此进入应用程序进程的 ActivityThread；



#### 二、应用程序启动

1. Launcher 点击 App 图标 ，调用 startActivitySafely()，最终也会调用 Activity#startActivity()；

2. Activity#startActivity()--->最终到 ActivityStackSupervisor#startSpecificActivityLocked()

   ```java
   void startSpecificActivityLocked(ActivityRecord r,
   		boolean andResume, boolean checkConfig) {
   	// Is this activity's application already running?
   	ProcessRecord app = mService.getProcessRecordLocked(r.processName,
   			r.info.applicationInfo.uid, true);
   	//app 是应用程序进程，如果不存在则调用 startProcessLocked()
   	if (app != null && app.thread != null) {
   		try {
   			if ((r.info.flags&ActivityInfo.FLAG_MULTIPROCESS) == 0
   					|| !"android".equals(r.info.packageName)) {
   				app.addPackage(r.info.packageName, r.info.applicationInfo.longVersionCode,
   						mService.mProcessStats);
   			}
   			realStartActivityLocked(r, app, andResume, checkConfig);
   			return;
   		}
   	}
   
   	mService.startProcessLocked(r.processName, r.info.applicationInfo, true, 0,
   			"activity", r.intent.getComponent(), false, false, true);
   }
   ```

   

3. AMS 调用 startProcessLocked() 发送启动应用程序进程的请求；

   - 创建新的 ProcessRecord，调用 Process.start() 创建应用程序进程，其中 entryPoint 参数是 ActivityThread；
   - 处理启动参数，与 Zygote 进程建立 Socket 连接；

4. Zygote 接收请求并创建应用程序进程

   - ZygoteInit#runSelectLoop() 处理 AMS 请求；
   - 获取启动请求参数，fork 创建新的进程；
   - 最终通过反射调用 ActivityThread#main()；

5. ActivityThread#main()

   - 开启消息循环；
   - activityThread#attach()，通过 Binder 和 AMS 通信，调用 AMS 的 attachApplication()，传入 ApplicationThread 实例，它是一个 Binder；
   
6. AMS#attachApplication()--->attachApplicationLocked()，此时处于 SystemServer 进程

   - 调用 ApplicationThread#bindApplication()；
     - sendMessage(H.BIND_APPLICATION, data);
     - ActivityThread#H 处理消息，handleBindApplication()；
     - 创建 Instrumentation，调用 LoadedApk#makeApplication()；
     - 使用 Instrumentation 通过反射创建 Application；
     - 调用 attach()，绑定上下文；
     - 调用 applicaton#onCreate()；
   - 然后检测是不是有Activity，Service，BroadcastReceiver 应该拉起。

   ```java
   private final boolean attachApplicationLocked(IApplicationThread thread,
   		int pid, int callingUid, long startSeq) {
   	thread.bindApplication(processName, appInfo, providers, null, profilerInfo,
   			null, null, null, testMode,
   			mBinderTransactionTrackingEnabled, enableTrackAllocation,
   			isRestrictedBackupMode || !normalMode, app.persistent,
   			new Configuration(getGlobalConfiguration()), app.compat,
   			getCommonServicesLocked(app.isolated),
   			mCoreSettingsObserver.getCoreSettingsLocked(),
   			buildSerial, isAutofillCompatEnabled);
   			
   	...
   	
   	// See if the top visible activity is waiting to run in this process...
   	if (normalMode) {
   		try {
               //根 Aactivity 在这里被启动
   			if (mStackSupervisor.attachApplicationLocked(app)) {
   				didSomething = true;
   			}
   		} catch (Exception e) {
   			Slog.wtf(TAG, "Exception thrown launching activities in " + app, e);
   			badApp = true;
   		}
   	}
   
   	// Find any services that should be running in this process...
   	if (!badApp) {
   		try {
   			didSomething |= mServices.attachApplicationLocked(app, processName);
   			checkTime(startTime, "attachApplicationLocked: after mServices.attachApplicationLocked");
   		} catch (Exception e) {
   			Slog.wtf(TAG, "Exception thrown starting services in " + app, e);
   			badApp = true;
   		}
   	}
   
   	// Check if a next-broadcast receiver is in this process...
   	if (!badApp && isPendingBroadcastProcessLocked(pid)) {
   		try {
   			didSomething |= sendPendingBroadcastsLocked(app);
   			checkTime(startTime, "attachApplicationLocked: after sendPendingBroadcastsLocked");
   		} catch (Exception e) {
   			// If the app died trying to launch the receiver we declare it 'bad'
   			Slog.wtf(TAG, "Exception thrown dispatching broadcasts in " + app, e);
   			badApp = true;
   		}
   	}
   }
   ```

   

7. ActivityStackSupervisor#attachApplicationLocked()--->realStartActivityLocked()

   API28 新增了管理类来控制生命周期变化。

   ```java
   //创建 activity 启动事务
   final ClientTransaction clientTransaction = ClientTransaction.obtain(app.thread,
   		r.appToken);
   clientTransaction.addCallback(LaunchActivityItem.obtain(new Intent(r.intent),
   		System.identityHashCode(r), r.info,
   		mergedConfiguration.getGlobalConfiguration(),
   		mergedConfiguration.getOverrideConfiguration(), r.compat,
   		r.launchedFromPackage, task.voiceInteractor, app.repProcState, r.icicle,
   		r.persistentState, results, newIntents, mService.isNextTransitionForward(),
   		profilerInfo));
   
   //设置最终期望达到的状态（resume状态）到事务中去
   final ActivityLifecycleItem lifecycleItem;
   if (andResume) {
   	lifecycleItem = ResumeActivityItem.obtain(mService.isNextTransitionForward());
   } else {
   	lifecycleItem = PauseActivityItem.obtain();
   }
   clientTransaction.setLifecycleStateRequest(lifecycleItem);
   
   //执行事务
   mService.getLifecycleManager().scheduleTransaction(clientTransaction);
   ```

   - ClientLifecycleManager#scheduleTransaction()

   - ClientTransaction#schedule()

   - IApplicationThread#scheduleTransaction()

     IApplicationThread 是 ApplicationThread 对象

   - ClientTransactionHandler#scheduleTransaction()

     - ActivityThread 继承自 ClientTransactionHandler
     - sendMessage(ActivityThread.H.EXECUTE_TRANSACTION, transaction);

   - ActivityTread 处理消息

     ```java
     final ClientTransaction transaction = (ClientTransaction) msg.obj;
     mTransactionExecutor.execute(transaction);
     if (isSystem()) {
         // Client transactions inside system process are recycled on the client side
         // instead of ClientLifecycleManager to avoid being cleared before this
         // message is handled.
         transaction.recycle();
     }
     ```

   - TransactionExecutor#exeecute()

     ```java
     public void execute(ClientTransaction transaction) {
         final IBinder token = transaction.getActivityToken();
         executeCallbacks(transaction);
         executeLifecycleState(transaction);
         mPendingActions.clear();
     }
     ```

   - TransactionExecutor#executeCallbacks()--->LaunchActivityItem#execute()

     ```java
     public void execute(ClientTransactionHandler client, IBinder token,
     		PendingTransactionActions pendingActions) {
     	ActivityClientRecord r = new ActivityClientRecord(token, mIntent, mIdent, mInfo, mOverrideConfig, mCompatInfo, mReferrer, mVoiceInteractor, mState, mPersistentState, mPendingResults, mPendingNewIntents, mIsForward, mProfilerInfo, client);
     	client.handleLaunchActivity(r, pendingActions, null /* customIntent */);
     }
     ```

   - ActivityThread#handleLaunchActivity()--->performLaunchActivity()

     - 使用 Instrumentation 通过反射创建 Activity；
     - 拿到之前创建的 Application，创建 ContextImpl；
     - activity#attach()，window 初始化在这里
     - 调用 Activity#onCreate()

   - 回到 TransactionExecutor#execute()

     - executeLifecycleState()

     - cycleToPath() 将起始状态和期望状态中间的状态通过计算得出然后放到一个数组中返回；

     - performLifecycleSequence() 调用数组里面的所有生命周期状态；

       ```java
       private void performLifecycleSequence(ActivityClientRecord r, IntArray path) {
       	final int size = path.size();
       	for (int i = 0, state; i < size; i++) {
       		state = path.get(i);
       		log("Transitioning to state: " + state);
       		switch (state) {
       			case ON_CREATE:
       				mTransactionHandler.handleLaunchActivity();
       				break;
       			case ON_START:
       				mTransactionHandler.handleStartActivity();
       				break;
       			case ON_RESUME:
       				mTransactionHandler.handleResumeActivity();
       				break;
       			case ON_PAUSE:
       				mTransactionHandler.handlePauseActivity();
       				break;
       			case ON_STOP:
       				mTransactionHandler.handleStopActivity();
       				break;
       			case ON_DESTROY:
       				mTransactionHandler.handleDestroyActivity();
       				break;
       			case ON_RESTART:
       				mTransactionHandler.performRestartActivity();
       				break;
       			default:
       				throw new IllegalArgumentException("Unexpected lifecycle state: " + state);
       		}
       	}
       }
       ```

       

8. 至此 Activity 被启动。

9. onCreate()--->onResume() 过程中还包含 Window 初始化、DecorView 创建、addView()、ViewRootImpl 创建、view 的绘制，最后 Activity#makeVisible() DecorView 可见，界面呈现在用户面前。



#### 三、相关类

| 类名                                   | 作用                                                         |
| :------------------------------------- | :----------------------------------------------------------- |
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



#### 四、AMS

##### 1、获取 AMS 代理

```java
public static IActivityManager getService() {
	return IActivityManagerSingleton.get();
}

private static final Singleton<IActivityManager> IActivityManagerSingleton =
		new Singleton<IActivityManager>() {
			@Override
			protected IActivityManager create() {
                //1
				final IBinder b = ServiceManager.getService(Context.ACTIVITY_SERVICE);
                //2
				final IActivityManager am = IActivityManager.Stub.asInterface(b);
				return am;
			}
		};
```

- 注释 1 处，得到名为 "activity" 的 Service 引用。
- 注释 2 处，转换成 IActivityManager 类型的对象，采用的是 AIDL，IActivityManager 是 aidl 文件，它对应的 Java 文件是由 AIDL 工具在编译时自动生成的。
- 要实现进程间通信，服务端即 AMS 只需要继承 IActivityManager.Stub 类并实现相应的方法就可以了。
- IActivityManager 是 AMS 在本地的代理。

##### 2、AMS 的启动

1. SystemServer#main()

2. SystemServer#run()

   - 创建 SystemServiceManager 实例，用以启动服务；
   - 启动引导服务，AMS 在这里
   - 启动核心服务
   - 启动其他服务

3. startBootstrapServices()

   ```java
   mActivityManagerService = mSystemServiceManager.startService(
       ActivityManagerService.Lifecycle.class).getService();
   mActivityManagerService.setSystemServiceManager(mSystemServiceManager);
   mActivityManagerService.setInstaller(installer);
   ```

4. SystemServiceManager#startService()

   ```java
   public void startService(@NonNull final SystemService service) {
   	// Register it.
   	mServices.add(service);
   	// Start it.
   	long time = SystemClock.elapsedRealtime();
   	try {
   		service.onStart();
   	} catch (RuntimeException ex) {
   		throw new RuntimeException();
   	}
   }
   ```

   - 将 service 对象添加到 mServices 中完成注册；
   - 调用 service#onStart() 启动 service 对象；

5. ActivityManagerService#LifeCycle

   ```java
   public static final class Lifecycle extends SystemService {
   	private final ActivityManagerService mService;
   
   	public Lifecycle(Context context) {
   		super(context);
           //创建 AMS 实例
   		mService = new ActivityManagerService(context);
   	}
   
   	@Override
   	public void onStart() {
           //调用 AMS start()
   		mService.start();
   	}
   
   	public ActivityManagerService getService() {
           //返回 AMS 实例
   		return mService;
   	}
   }
   ```



#### 3、AMS 与 应用程序进程的关系

- 启动应用程序时 AMS 会检查这个应用程序需要的应用程序进程是否存在。
- 如果需要的应用程序进程不存在，AMS 就会 请求 Zygote 进程创建需要的应用程序进程。



#### 4、AMS 重要的数据结构

##### 4.1 ActivityRecord

它内部会记录 Activity 的所有信息，因此它用来描述一个 Activity，它是在启动 Activity 时被创建的，具体是在 ActivityStarter#startActivity() 中。

它包含的信息有：

- AMS 的引用
- AndroidManifes 节点信息
- Activity 状态
- Activity 资源信息
- Activity 进程相关信息
- 其所在的 TaskRecord

##### 4.2 TaskRecord

用来描述一个 Activity 任务栈，重要的成员变量：

- taskId

  任务栈的唯一标识符

- affinity

  任务栈的倾向性

- intent

  启动这个任务栈的 Intent

- mActivitys

  按照历史顺序排列的 Activity 记录

- mStack

  当前归属的 ActivityStack

- mService

  AMS 的引用

##### 4.3 ActivityTask

ActivityTask 是一个管理类，用来管理系统所有 Activity，其内部维护了 Activity 的所有状态、特殊状态的 Activity 以及和 Activity 相关的列表等数据。

ActivityStack 是由 ActivityStackSupervisor 来进行管理的，ActivityStackSupervisor  在 AMS 的构造方法中被创建。

```java
public ActivityManagerService(Context systemContext) {
    mStackSupervisor = createStackSupervisor();
}
protected ActivityStackSupervisor createStackSupervisor() {
    final ActivityStackSupervisor supervisor = new ActivityStackSupervisor(this, mHandler.getLooper());
    supervisor.initialize();
    return supervisor;
}
```

ActivityStackSupervisor 中有多种 ActivityStack 实例：

- ActivityStack mHomeStack;

  用来存储 Launcher App 的所有 Activity

- ActivityStack mFocusedStack;

  表示当前正在接收输入或启动下一个 Activity 的所有 Activity

- private ActivityStack mLastFocusedStack;

  表示此前接收输入的所有 Activity



ActivityState 枚举了 Activity 的所有状态

```java
enum ActivityState {
	INITIALIZING,
	RESUMED,
	PAUSING,
	PAUSED,
	STOPPING,
	STOPPED,
	FINISHING,
	DESTROYING,
	DESTROYED
}
```



#### 5、Activity 栈管理

##### 5.1 Activity 任务栈模型

ActivityRecord 用来记录一个 Activity 的所有信息，TaskRecord 中包含一个或多个 ActivityRecord；

TaskRecord 用来表示 Activity 的任务栈，用来管理栈中的 ActivityRecord；

ActivityStack 又包含了一个或多个 TaskRecord，它是 TaskRecord 的管理者。



##### 5.2 Launch Mode



##### 5.3 Intent 的 FLAG

FLAG 也可以设定 Activity 的启动方式，如果 Launch Mode 和 FLAG 设定的 Activity 的启动方式有冲突，以 FLAG 为准。



##### 5.4 taskAffinity

在清单文件中指定 Activity 希望归属的栈































