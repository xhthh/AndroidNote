### Android版本适配记录

> 相关知识点：
>
> - gradle 插件版本、gradle版本 和buildToolsVersion之间的对应关系
>
>   https://blog.csdn.net/u013620306/article/details/107858433
>
> - compileSdkVersion、targetSdkVersion、minSdkVersion作用与区别
>
>   https://blog.csdn.net/cpcpcp123/article/details/114754529

#### Android 13

##### 1、APG 3.0.0 之后，不需要声明 buildToolsVersion，升级 compileSdkVersion 和 targetSdkVersion 至 33 后，升级 buildToolsVersion 为 33.0.1 报错

https://blog.csdn.net/qq_38122220/article/details/124057020

版本不匹配



##### 2、更安全地导出上下文注册的接收器

以 Android 13(33) 或更高版本为目标平台的应用，必须为每个广播接收器指定 `RECEIVER_EXPORTED` 或 `RECEIVER_NOT_EXPORTED`，否则当 App 尝试注册广播接收器时，系统会抛出 `SecurityException`





> - idcard_quality 旷视身份证这个库 升级33后会不会有问题，验证一下
>
> - android12 后 activity/service/receiver 有 intent-filter 的需要显示声明 exported
>
>   - 极光厂商通道中的PluginHuaweiPlatformsReceiver 没有声明，升级极光sdk版本
>
>     https://docs.jiguang.cn/jpush/client/Android/android_3rd_guide
>
>     *JPush Android SDK 4.6.0 以上版本更新华为 HMS SDK 版本为:6.3.0.304 及以上版本,接入要求：Android Studio 3.6.1 或更高版本、Android Gradle 插件 3.5.4 或更高版本。*
>
>     - 当前 gradle 插件版本为 3.4.3，不升级可以使用哪个版本的sdk，有适配Android12
>   - 看极光文档，v4.2.2，适配了 Android12，这个需要的gradle插件版本是多少，配合 Jcore V2.9.0 级以上版本使用
>   
>   - Installation failed due to: 'INSTALL_PARSE_FAILED_MANIFEST_MALFORMED: Failed parse during installPackageLI: /data/app/vmdl178988413.tmp/base.apk (at Binary XML file line #415): cn.sharesdk.tencent.qq.ReceiveActivity: Targeting S+ (version 31 and above) requires that an explicit value for android:exported be defined when intent filters are present'
>   
>     sharesdk epxorted 问题，需要升级版本
>   
>     ```
>     #gradle.properties
>     MobSDK.spEdition=FP//更新最新版本
>     
>     
>     classpath "com.mob.sdk:MobSDK2:+"
>     
>     A problem occurred configuring root project 'jiufuwanka_android'.
>     > Could not resolve all artifacts for configuration ':classpath'.
>        > Could not find any matches for com.mob.sdk:MobSDK2:+ as no versions of com.mob.sdk:MobSDK2 are available.
>          Searched in the following locations:
>            - https://maven.aliyun.com/repository/public/com/mob/sdk/MobSDK2/maven-metadata.xml
>            - https://maven.aliyun.com/repository/public/com/mob/sdk/MobSDK2/
>            - https://maven.aliyun.com/repository/google/com/mob/sdk/MobSDK2/maven-metadata.xml
>            - https://maven.aliyun.com/repository/google/com/mob/sdk/MobSDK2/
>            - file:/C:/Users/E480/.m2/repository/com/mob/sdk/MobSDK2/maven-metadata.xml
>            - file:/C:/Users/E480/.m2/repository/com/mob/sdk/MobSDK2/
>            - https://repo.maven.apache.org/maven2/com/mob/sdk/MobSDK2/maven-metadata.xml
>            - https://repo.maven.apache.org/maven2/com/mob/sdk/MobSDK2/
>          Required by:
>              project :
>     
>     Possible solution:
>      - Declare repository providing the artifact, see the documentation at https://docs.gradle.org/current/userguide/declaring_repositories.html
>     
>     
>     ```
>   
> - 运行报错
>
>   ```
>   Installation did not succeed.
>   The application could not be installed: INSTALL_PARSE_FAILED_RESOURCES_ARSC_COMPRESSED
>   
>   List of apks:
>   [0] 'D:\WorkSpace\9F\jiufuwanka_android\app\build\outputs\apk\360store_2\debug\app-360store_2-debug.apk'
>   Installation failed due to: '-124: Failed parse during installPackageLI: Targeting R+ (version 30 and above) requires the resources.arsc of installed APKs to be stored uncompressed and aligned on a 4-byte boundary'
>   Retry
>   Failed to launch an application on all devices
>   ```
>
>   滴滴 booster 压缩task的问题
>
> - 听云问题
>
>   ```
>   1: Task failed with an exception.
>   -----------
>   * What went wrong:
>   Execution failed for task ':app:networkBenchNewLensInstrumentTask'.
>   > java.io.IOException: Can not attach to current VM
>   ```
>
>   https://blog.csdn.net/NakajimaFN/article/details/127281084
>   
>   听云文档
>   
>   http://wukongdoc.tingyun.com/app/sdk_deploy/Android/Android_Gradle.html
>   
>   - Gradle接入方式，升级听云SDK至 2.17.1.10
>   
>     - gradle插件 4.2.2
>     - targetSdkVersion 33
>   
>   - 写了个bug，后台查不到crash信息
>   
>     小米手机 Android13系统
>   
>     ```
>     I/NBSAgent: NBSAgent start.
>     I/NBSAgent: NBSAgent enabled.
>     I/NBSAgent: NBSAgent V2.17.1.10
>     I/NBSAgent: Android app is debugMode !
>     I/NBSAgent: connect success
>     ```
>   
>     华为mate40pro
>   
>     ```
>     I/NBSAgent: NBSAgent start.
>     I/NBSAgent: NBSAgent enabled.
>     I/NBSAgent: NBSAgent V2.17.1.10
>     I/NBSAgent: Android app is debugMode !
>     I/NBSAgent: errorCode:460, Invalid key(cea9fe7d00c0472d833a110e10c8b6a2)
>     
>     
>     使用之前的V2.15.0.7 jar包， 连接成功，崩溃信息，后台能看到
>     I/NBSAgent: NBSAgent start.
>     I/NBSAgent: NBSAgent enabled.
>     I/NBSAgent: NBSAgent V2.15.0.7
>     I/NBSAgent: connect success
>     ```
>   
>     > 会不会是 初始化的问题，start()







#### Android 12

##### 1、exported

https://blog.csdn.net/u013208026/article/details/126241775







#### 检测是否使用非SDK接口

> 在 Application#onCreate() 中
>
> ```java
> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
>             StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
>                     .detectNonSdkApiUsage()
>                     .penaltyListener(new ThreadPoolExecutor(
>                             CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
>                             new LinkedBlockingQueue<Runnable>()), new StrictMode.OnVmViolationListener() {
>                         @Override
>                         public void onVmViolation(Violation v) {
>                             //todo 没走到
>                             Log.e("xht", "---非SDK接口使用---" + v.getMessage());
>                         }
>                     })
>                     .build());
>         }
> ```
>
> 您还可以利用 `StrictMode` API 来测试您的应用是否使用非 SDK 接口。请使用 [`detectNonSdkApiUsage`](https://developer.android.com/reference/android/os/StrictMode.VmPolicy.Builder?hl=zh-cn#detectNonSdkApiUsage()) 方法来启用此 API。启用 `StrictMode` API 后，您可以使用 [`penaltyListener`](https://developer.android.com/reference/android/os/StrictMode.VmPolicy.Builder?hl=zh-cn#penaltyListener(java.util.concurrent.Executor, android.os.StrictMode.OnVmViolationListener)) 来接收每次使用非 SDK 接口时触发的回调，并且您可以在其中实现自定义处理。回调中提供的 `Violation` 对象派生自 `Throwable`，并且封闭式堆栈轨迹会提供相应使用行为的具体情境。
>
> 