### Android启动相关

#### 一、系统启动

##### 1、启动电源以及系统启动

当电源按下时引导芯片代码从预定义的地方（固化在 ROM）开始执行。加载引导程序 BootLoader 到 RAM，然后执行。

##### 2、引导程序 BootLoader

引导程序 BootLoader 是在 Android 操作系统开始运行前的一个小程序，它的主要作用是把系统 OS 拉起来并运行。

##### 3、Linux 内核启动

当内核启动时，设置缓存、被保护存储器、计划列表、加载驱动。当完成系统设置时，它首先在系统文件中寻找 init.rc 文件，并启动 init 进程。

##### 4、init 进程启动

初始化和启动属性服务，并且启动 Zygote 进程。

##### 5、Zygote 进程启动

创建 Java 虚拟机并为 Java 虚拟机注册 JNI 方法，创建服务器端 Socket，启动 SystemServer 进程。

通过 JNI 调用 ZygoteInit#main() 后，Zygote 便进入了 Java 框架层。

```java
public static void main(String[] argv[]) {
    ...
    try {
        //创建一个 Server 端的 Socket
        zygoteServer.registerServerSocketFromEnv(socketName);
            
        if (!enableLazyPreload) {
            //预加载类和资源
            preload(bootTimingsTraceLog);
        }
        ...

        if (startSystemServer) {
            //启动 SystemServer 进程
            Runnable r = forkSystemServer(abiList, socketName, zygoteServer);

            if (r != null) {
                r.run();
                return;
            }
        }
        //等待 AMS 请求
        caller = zygoteServer.runSelectLoop(abiList);
    }
}
```

- 创建一个 Server 端的 Socket，用于 AMS 请求 Zygote 创建新的应用程序进程
- 预加载类和资源
- 启动 SystemServer 进程
- 等待 AMS 请求创建新的应用程序进程

##### 6、SystemServer 进程启动

ZygoteInit#main()--->forSystemServer()--->handleSystemServerProcess()：

- 创建了 PathClassLoader

- 调用 zygoteInit()--->nativeZygoteInit() Native 层代码，用来启动 Binder 线程池，这样 SystemServer 进程就可以使用 Binder 与其他进程进行通信了。

  ```java
  public static final Runnable zygoteInit(int targetSdkVersion, String[] argv, ClassLoader classLoader) {
      //启动 Binder 线程池
      ZygoteInit.nativeZygoteInit();
      //进入 SystemServer 的 main()，通过反射调用的 main()
      return RuntimeInit.applicationInit(targetSdkVersion, argv, classLoader);
  }
  ```

- System#main()

  - 创建 SystemServiceManager，其用于对系统的服务进行创建、启动和生命周期管理；
  - 启动各种服务；
  
  ```java
  public static void main(String[] args) {
      new SystemServer().run();
  }
  
  private void run() {
      try {
          ...
          //创建消息 Looper
          Looper.prepareMainLooper();
          // Initialize native services.加载动态库
          System.loadLibrary("android_servers");
          performPendingShutdown();
          //创建系统的 Context
          createSystemContext();
          // Create the system service manager.
          mSystemServiceManager = new SystemServiceManager(mSystemContext);
          ...
      }
      // Start services.
      try {
          //启动引导服务
          startBootstrapServices();//ActivityManagerService 启动
          //启动核心服务
          startCoreServices();
          //启动其他服务
          startOtherServices();
      }
  	...
    Looper.loop();
  }
  ```
```
  
引导服务
  
- ActivityManagerService
  
  负责四大组件的启动、切换、调度
  
- PackageManagerService 用来对 apk 进行安装、解析、删除、卸载等操作
  
核心服务
  
  其他服务
  
  - WindowManagerService 窗口管理服务
  
- 

##### 7、Launcher 启动过程

系统启动的最后一步是启动一个应用程序（Launcher）用来显示系统中已经安装的应用程序。Launcher 在启动过程中会请求 PackageManagerService 返回系统中已经安装的应用程序的信息，并将这些信息封装成一个快捷图标列表显示在系统屏幕上。

- SystemServer#startOtherServices()

- ActivityManagerService#systemReady()--->最终走到 startHomeActivityLocked() 中

  ```java
  boolean startHomeActivityLocked(int userId, String reason) {
      //判断工厂模式和 mTopAction 的值，符合要求就继续执行下去
      //mTopAction 用来描述第一个被启动的 Activity 的 Action，默认是 Intent.ACTION_MAIN
      if (mFactoryTest == FactoryTest.FACTORY_TEST_LOW_LEVEL && mTopAction ==null) {
          return false;
      }
      //创建 Launcher 启动所需的 Intent
      Intent intent = getHomeIntent();
      ActivityInfo aInfo = resolveActivityInfo(intent, STOCK_PM_FLAGS, userId);
      if (aInfo != null) {
          ...
          ProcessRecord app = getProcessRecordLocked(aInfo.processName,
                  aInfo.applicationInfo.uid, true);
          if (app == null || app.instr == null) {
              //启动 Launcher
              mActivityStartController.startHomeActivity(intent, aInfo, myReason);
          }
      }
      return true;
  }
```

- ActivityStartController#startHomeActivity()

  ```java
  void startHomeActivity(Intent intent, ActivityInfo aInfo, String reason) {
      //将 Launcher 放入 HomeStack
      mSupervisor.moveHomeStackTaskToTop(reason);
  
      //后面的过程和 Activity 启动过程一样
      mLastHomeActivityStartResult = obtainStarter(intent, "startHomeActivity: " + reason)
          .setOutActivity(tmpOutRecord)
          .setCallingUid(0)
          .setActivityInfo(aInfo)
          .execute();
      mLastHomeActivityStartRecord = tmpOutRecord[0];
      if (mSupervisor.inResumeTopActivity) {
          mSupervisor.scheduleResumeTopActivities();
      }
  }
  ```

  #### 二、应用程序进程启动

  ##### 1、AMS 发送启动应用程序进程请求

  AMS 通过 startProcessLocked() 向 Zygote 进程发送请求：

  - 创建应用程序进程的用户 ID
  - 对用户组 ID 进行创建和赋值
  - 如果 entryPoint 为 ActivityThread，即应用程序进程主线程的类名
  - 启动应用程序进程，将用户 ID 和用户组 ID 传进去

  ```java
  private final boolean startProcessLocked(ProcessRecord app, String hostingType,
          String hostingNameStr, boolean disableHiddenApiChecks, String abiOverride) {
  	try {
  		int uid = app.uid;//获取要创建的应用程序进程的用户 ID
  		int[] gids = null;
  		int mountExternal = Zygote.MOUNT_EXTERNAL_NONE;
  		if (!app.isolated) {
  			if (ArrayUtils.isEmpty(permGids)) {
  				gids = new int[3];
  			} else {
  				gids = new int[permGids.length + 3];
  				System.arraycopy(permGids, 0, gids, 3, permGids.length);
  			}
              //对 gid 进程创建和赋值
  			gids[0] = UserHandle.getSharedAppGid(UserHandle.getAppId(uid));
  			gids[1] = UserHandle.getCacheAppGid(UserHandle.getAppId(uid));
  			gids[2] = UserHandle.getUserGid(UserHandle.getUserId(uid));
  		}
  		
  		final String entryPoint = "android.app.ActivityThread";
  		//启动应用程序进程
  		return startProcessLocked();
  	}
  }
  
  private boolean startProcessLocked() {
      final ProcessStartResult startResult = startProcess();
  	handleProcessStartedLocked();
  }
  
  private ProcessStartResult startProcess() {
      startResult = Process.start();//调用 ZygoteProcess#start()
  }
  
  class ZygoteProcess {
      public final Process.ProcessStartResult start(){
          return startViaZygote();
      }
      
      private Process.ProcessStartResult startViaZygote() {
          //创建字符串列表 argsForZygote，并将应用进程的启动参数保存在 argsForZygote 中
          
          synchronized(mLock) {
              return zygoteSendArgsAndGetResult(openZygoteSocketIfNeeded(abi), argsForZygote);
          }
      }
      
      private ZygoteState openZygoteSocketIfNeeded(String abi) {
          if (primaryZygoteState == null || primaryZygoteState.isClosed()) {
              try {
                  primaryZygoteState = ZygoteState.connect(mSocket);
              }
          }
      }
  }
  ```

  Zygote 的 main() 中会创建 Server 端的 Socket，最终调用 connect() ，建立 Socket 连接。

  创建字符串列表 argsForZygote，并将应用进程的启动参数保存在 argsForZygote 中，最终通过 Socket 进行传输通信。

  ##### 2、Zygote 接收请求并创建应用程序进程

  Socket 连接成功并匹配 ABI 后会返回 ZygoteState 类型对象，上面会将应用进程的启动参数 argsForZygote 写入 ZygoteState 中，这样 Zygote 进程就会收到一个创建新的应用程序进程的请求。

  - ZygoteInit#main()

    - 创建一个 Server 端的 Socket
    - 预加载类和资源
    - 启动 SystemServer 进程
    - 调用 runSelectLoop() 等待 AMS 请求

  - ZygoteInit#runSelectLoop()

    ```java
    Runnable runSelectLoop(String abiList) {
        ZygoteConnection connection = peers.get(i);
        final Runnable command = connection.processOneCommand(this);
    }
    
    Runnable processOneCommand(ZygoteServer zygoteServer) {
    	String args[];
    	Arguments parsedArgs = null;
    	FileDescriptor[] descriptors;
    
    	try {
            //获取应用程序进程的启动参数
    		args = readArgumentList();
    		descriptors = mSocket.getAncillaryFileDescriptors();
    	} catch (IOException ex) {
    		throw new IllegalStateException("IOException on command socket", ex);
    	}
    
    	parsedArgs = new Arguments(args);
        //创建应用程序进程
    	pid = Zygote.forkAndSpecialize();
        
        try {
            if (pid == 0) {
                // in child
                zygoteServer.setForkChild();
    
                zygoteServer.closeServerSocket();
                IoUtils.closeQuietly(serverPipeFd);
                serverPipeFd = null;
    
                return handleChildProc(parsedArgs, descriptors, childPipeFd,
                                       parsedArgs.startChildZygote);
            }
        }
    }
    
    private Runnable handleChildProc(Arguments parsedArgs, FileDescriptor[] descriptors,
    		FileDescriptor pipeFd, boolean isZygote) {
    	if (parsedArgs.invokeWith != null) {
    		// Should not get here.
    		throw new IllegalStateException();
    	} else {
    		if (!isZygote) {
    			return ZygoteInit.zygoteInit();
    		} else {
    			return ZygoteInit.childZygoteInit();
    		}
    	}
    }
    
    ```

    - 当有 AMS 的请求数据到来时，处理请求数据

    - 获取应用程序进程的启动参数

    - 创建应用程序进程，返回 pid

      - pid == 0，即当前代码逻辑运行在子进程中，处理应用程序进程，调用 ZygoteInit#zygoteInit()

        ```java
        public static final Runnable zygoteInit(int targetSdkVersion, String[] argv, ClassLoader classLoader) {
            //启动 Binder 线程池
            ZygoteInit.nativeZygoteInit();
            //进入 ActivityThread，通过反射调用的 main()
            return RuntimeInit.applicationInit(targetSdkVersion, argv, classLoader);
        }
        
        protected static Runnable findStaticMain(String className, String[] argv,
        		ClassLoader classLoader) {
        	Class<?> cl;
        
        	try {
        		cl = Class.forName(className, true, classLoader);
        	}
        
        	Method m;
        	try {
                //执行 ActivityThread 的 main()
        		m = cl.getMethod("main", new Class[] { String[].class });
        	}	
        	return new MethodAndArgsCaller(m, argv);//传入方法
        }
        ```

        最终执行到 ActivityThread#main()，这里为何采用了抛出异常而不是直接调用 ActivityThread#main()，因为这种抛出异常的处理会清除所有的设置过程需要的堆栈帧，并让 ActivityThread 的 main() 看起来像是应用程序进程的入口方法。

        

        MethodAndArgsCaller 是一个 Runnable，其 run() 中调用 invoke()，ActivityThread 的 main() 就会被动态调用，应用程序就进入了  ActivityThread#main()。

        ```java
        public void run() {
            try {
                //这里的 mMethod 就是 ActivityThread 的 main()
                mMethod.invoke(null, new Object[] { mArgs });
            }
        }
        ```

        MethodAndArgsCaller 这个 Runnebale 最终被调用的地方是 ZygoteInit#main() 中

        ```java
        public static void main(String argv[]) {
        	final Runnable caller;//MethodAndArgsCaller 对象
        	try {
        		caller = zygoteServer.runSelectLoop(abiList);
        	}
        
        	// We're in the child process and have exited the select loop. Proceed to execute the command.
        	if (caller != null) {
        		caller.run();
        	}
        }
        ```

        

























