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













