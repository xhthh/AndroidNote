## Android 版本适配

#### Android M 6.0

- 运行时权限动态申请；



#### Android N 7.0

- 在应用间共享文件

  对于面向 Android 7.0 的应用，Android 框架执行的 `StrictMode` API 政策禁止在您的应用外部公开 `file://` URI。如果一项包含文件 URI 的 intent 离开您的应用，则应用出现故障，并出现 `FileUriExposedException` 异常。

  要在应用间共享文件，您应发送一项 `content://` URI，并授予 URI 临时访问权限。进行此授权的最简单方式是使用 `FileProvider` 类。

- APK signature scheme v2

  - 只勾选V1签名就是传统方案签署，但是在 Android 7.0 上不会使用V2安全的验证方式。
  - 只勾选V2签名7.0以下会显示未安装，Android 7.0 上则会使用了V2安全的验证方式。
  - 同时勾选V1和V2则所有版本都没问题。

- Android 7.0 移除了三项隐式广播，以帮助优化内存使用和电量消耗。

  - CONNECTIVITY_CHANGE 广播

    > 在后台时不能接受到 CONNECTIVITY_CHANGE 广播，前台不影响。

  - ACTION_NEW_PICTURE 和 ACTION_NEW_VIDEO 广播

    > 不能发送或是接收新增图片（ACTION_NEW_PICTURE）和新增视频（ACTION_NEW_VIDEO）的广播。

- 等等



#### Android 8.0 O

- 通知权限

  > Android 8.0 之后通知权限默认都是关闭的，无法默认开启以及通过程序主动开启，需要程序员读取权限开启情况，然后提示用户去开启。

- 通知适配

  > Android 8.0 修改了通知，比如通知渠道、通知标志、通知超时、背景颜色。比较重要的是通知渠道，其允许您为要显示的每种通知类型创建用户可自定义的渠道。好处就是对于某个应用可以把权限分成很多类，用户开控制是否显示哪些类别的通知。而开发者要做的就是必须设置这个渠道id。

- 自适应启动图标

  从Android 8.0系统开始，应用程序的图标被分为了两层：前景层和背景层。

- 修改运行时权限问题，用什么权限去申请什么权限

  > 在 Android 8.0 之前，如果应用在运行时请求权限并且被授予该权限，系统会错误地将属于同一权限组并且在清单中注册的其他权限也一起授予应用。
  >
  > 对于针对 Android 8.0 的应用，此行为已被纠正。系统只会授予应用明确请求的权限。然而，一旦用户为应用授予某个权限，则所有后续对该权限组中权限的请求都将被自动批准。

- 安装apk

  > Android 8.0 去除了“允许未知来源”选项，如果我们的App具备安装App的功能，那么AndroidManifest文件需要包含 REQUEST_INSTALL_PACKAGES 权限，未声明此权限的应用将无法安装其他应用。

- 静态广播无法正常接收

  > 从 Android 8.0 开始，对清单文件中静态注册广播接收者增加了限制，建议改为动态注册。如果还是想用静态注册的方式，在 Intent 里添加 Component 参数可实现。

- 等等



#### Android 9.0 P

- 限制明文流量的网络请求，非加密的流量请求（http）都会被系统禁止掉。

  - 将 http 请求改为 https；
  - 添加网络安全设置 android:networkSecurityConfig

  > 不作处理访问 http 会失败 `java.net.UnknownServiceException: CLEARTEXT communication to xxx not permitted by network security policy`

- 移除 Apache HTTP 客户端

- 前台服务需要添加权限

  > Android 9.0 之后，比如要授予 FOREGROUND_SERVICE 权限，才能够使用前台服务 startForegroundService，否则会抛出异常。这是个普通权限，不需要动态申请。

- 全面限制静态广播接收

  > 升级 Android 9.0 后，隐式广播江湖被全面禁止，在 AndroidManifest 中注册的 Receiver 将不能够生效，需要进行动态注册。

- 非 SDK 接口访问限制

- 强制执行 FLAG_ACTIVITY_NEW_TASK 要求

  > Android 9.0 中 不能从非 Activity 环境中启动 Activity，除非 intent 加上 FLAG_ACTIVITY_NEW_TASK。
  >
  > Android 7.0 以下 和 Android 9.0 及以上 都需要添加 FLAG_ACTIVITY_NEW_TASK。Android 7.0 因为一个错误会临时组织实施标志要求。

- 等等



#### Android 10 Q

https://juejin.cn/post/6844904073024503822

- 分区存储

  - 特定目录（App-specific），使用 getExternalFilesDir(String type) 或 getExternalCacheDir() 方法访问。无需权限，且卸载应用时会自动删除。
  - 照片、视频、音频这类媒体文件。使用 MediaStore 访问，访问其他应用的媒体文件时需要 READ_EXTERNAL_STORAGE 权限。
  - 其他目录，使用存储访问框架 SAF（Storage Access Framwork）

  > 解决方案：
  >
  > - manifest 里面加属性 requestLegacyExternalStorage
  > - 适配
  >
  > App-specific 特定目录下的文件数据会随着应用卸载删除，但是可以在 AndroidManifest 中 通过 `android:hasFragileUserData="true"` 来让用户选择是否保留。

- 在后台运行时访问设备位置信息需要权限

  > Android 10 引入了 ACCESS_BACKGROUND_LOCATION 权限（危险权限）。该权限允许应用程序在后台访问位置。如果请求此权限，则还必须请求 ACCESS_FINE_LOCATION 或 ACCESS_COARSE_LOCATION 权限。只请求此权限无效果。官方推荐使用前台服务来实现，在前台服务中获取位置信息。

- 应用处于后台时，不能启动 Activity。

- 对不可重置的设备标识符实施了限制

  > 从 Android 10 开始，应用必须具有 READ_PRIVILEGED_PHONE_STATE 特许权限才能访问设备的不可重置标识符（包含 IMEI 和序列号）。

- 深色主题





#### Android 11.0 R

https://juejin.cn/post/6948211914455384072

- 分区存储强制执行

  targetSdkVersion等于30，Scoped Storage就会被强制启用，requestLegacyExternalStorage标记将会被忽略。

  但是可以覆盖安装，通过 `android:preserveLegacyExternalStorage="true"` 暂时关闭分区存储，让开发者完成数据迁移的工作。如果卸载重装的话就会失效。

  > - sdk 29以下，可以使用以前的存储方式；
  > - sdk 29 可以使用作用域存储，区别是会增大App占用空间，且随着App卸载而删除；也可以在 AndroidManifest 中配置 android:requestLegacyExternalStorage 关闭作用域存储；
  > - sdk 30，强制执行作用域存储，但是也可以操作 sdcard 文件，需要申请 MANAGE_EXTERNAL_STORAGE 权限，使用 intent 跳转指定授权页面，让用户手动进行授权。

- 存储访问框架（SAF）变更

  - 使用 `ACTION_OPEN_DOCUMENT_TREE` 或 `ACTION_OPEN_DOCUMENT`，无法浏览到`Android/data/` 和 `Android/obb/ `目录及其所有子目录。
  - 使用 `ACTION_OPEN_DOCUMENT_TREE`无法授权访问存储根目录、Download文件夹。

- 媒体文件访问权限

  - 执行批量操作

    > sdk 30 中 Batch operations 特性，修改其他应用程序锁贡献的数据，比如美图秀秀进行修图，通过新增的 API，如 MediaStore.createWriteRequest 等，就可以对文件进行批量处理。

  - 使用直接文件路径和原生库访问文件

- 读取电话号码需要权限 READ_PHONE_NUMBERS

- 自定义 toast 不允许在后台显示

- 必须加上 V2 签名

- 获取其他应用信息，如包名等

  - 必须在清单文件中添加`<queries>`元素，告知系统你要获取哪些应用信息或者哪一类应用；
  - 添加 `QUERY_ALL_PACKAGES` 权限

- 请求的权限数月未使用，系统会通过自动重置用户已授予应用的运行时敏感权限；

  > 可以引导用户去设置页面关闭自动重置权限功能；

- 后台位置信息访问权限

- 状态栏高度需要适配

  > 系统为Android 11的手机上`targetSdkVersion` 是30时获取状态栏高度为0，低于30获取值正常。。。因此需要使用`WindowMetrics `适配一下



#### Android 12 S

https://juejin.cn/post/7093787313095999502

- 应用启动画面

- android:exported

  > 如果 targetSdkVersion 31 时，Activity、Service 或者 BroadcastReveiver 中使用 intent-filter，并且没有显示的声明 android:exported 的值，则 App 将无法安装。
  >
  > https://blog.csdn.net/z936689039/article/details/128649726
  
- 待处理 intent 可变性

  > 以Android 12为目标平台的App，在构建PendingIntent时，需要指定Flag为FLAG_IMMUTABLE(建议)或FLAG_MUTABLE二者之一，否则App将崩溃

- 前台服务启动限制

  > 以Android 12为目标平台的App，如果尝试在后台运行时启动前台服务(startForegroundService)，则会引发ForegroundServiceStartNotAllowedException异常

- 精确闹钟的权限

  > 以Android 12为目标平台的App，如果使用到了AlarmManager来设置定时任务，并且设置的是精准的闹钟(使用了setAlarmClock()、setExact()、setExactAndAllowWhileIdle()这几种方法)，则需要确保SCHEDULE_EXACT_ALARM权限声明且打开，否则App将崩溃

- 通知 trampoline 限制

  > 之前在配置通知(Notification)的点按行为时，可能会通过PendingIntent来启动一个Service或BrocastReceiver。而以Android 12为目标平台的App，如果尝试在Service或BrocastReceiver中内调用 startActivity()，系统会阻止该Activity启动，并在 Logcat 中显示：
  >
  > Indirect notification activity start (trampoline) from PACKAGE_NAME，this should be avoided for performance reasons .

- 自定义通知

  > 如果之前App中的通知(Notification)中使用到了自定义内容视图，并且该视图是填满整个通知区域的。那么当App以Android 12为目标平台后，视图将不再能填充整个区域，而是会被缩小到某个固定范围



#### Android 13 T

- 媒体权限细分

  > 通过 `Intent(MediaStore.ACTION_PICK_IMAGES);` 就可以打开内置的图片选择器，支持视频、音频、图片分类，支持多选和单选；
  >
  > READ_EXTERNAL_STORAGE 权限细分为：
  >
  > - READ_MEDIA_IMAGES
  > - READ_MEDIA_AUDIO
  > - READ_MEDIA_VIDEO

- 运行时通知权限 POST_NOTIFICATIONS

- 附近WiFi设备权限

  > 以 Android 13(33) 为目标平台的应用程序，访问附近的 WI-FI 设备。除特例API需要申请ACCESS_FINE_LOCATION外，其他需要申请 android.permission.NEARBY_WIFI_DEVICES 运行时权限；

- 闹钟权限USE_EXACT_ALARM

  > 除非App属于闹钟、计时器、日历等类型的应用或者在已被列入到应用市场的白名单里

- intent 过滤器会屏蔽不匹配的 intent

- 动态注册的广播需要申明 Export行为

  > Android13允许用户指定是否接受外部应用的广播，前提是启用了`DYNAMIC_RECEIVER_EXPLICIT_EXPORT_REQUIRED` 兼容性框架（默认是关闭的）





#### Android 14

##### 1、行为变更：以 Android 14 或更高版本为目标平台的应用

- 前台服务类型是必填项

  > 如果您的应用以 Android 14 为目标平台，则必须为应用中的每个前台服务至少指定一项前台服务类型、

- 对隐式 intent 和待处理 intent 的限制

  > 对于以 Android 14 为目标平台的应用，Android 会通过以下方式限制应用向内部应用组件发送隐式 intent：
  >
  > - 隐式 intent 只能传送到导出的组件。应用必须使用显式 intent 传送到未导出的组件，或将该组件标记为已导出。
  > - 如果应用通过未指定组件或软件包的 intent 创建可变待处理 intent，系统现在会抛出异常。

- 在运行时注册的广播接收器必须指定导出行为

  > 以 Android 14 为目标平台并使用[上下文注册的接收器](https://developer.android.google.cn/guide/components/broadcasts?hl=zh-cn#context-registered-receivers)的应用和服务必须指定以下标志，以指明接收器是否应导出到设备上的所有其他应用：`RECEIVER_EXPORTED` 或 `RECEIVER_NOT_EXPORTED`。
  >
  > 如果您的应用仅通过 `Context#registerReceiver` 方法（例如 [`Context#registerReceiver()`](https://developer.android.google.cn/reference/android/content/Context?hl=zh-cn#registerReceiver(android.content.BroadcastReceiver, android.content.IntentFilter))）针对[系统广播](https://developer.android.google.cn/guide/components/broadcasts?hl=zh-cn#system-broadcasts)注册接收器，那么它在注册接收器时不应指定标志。

- 更安全的动态代码加载

  > 如果必须动态加载代码，请使用以下方法将动态加载的文件（例如 DEX、JAR 或 APK 文件）在文件打开后和写入任何内容之前立即设置为只读。
  >
  > jar.setReadOnly()

- 压缩路径遍历

  > 对于以 Android 14 为目标平台的应用，Android 会通过以下方式防止 Zip 路径遍历漏洞：如果 Zip 文件条目名称包含“..”或以“/”开头，[`ZipFile(String)`](https://developer.android.google.cn/reference/java/util/zip/ZipFile?hl=zh-cn#public-constructors) 和 [`ZipInputStream.getNextEntry()`](https://developer.android.google.cn/reference/java/util/zip/ZipInputStream?hl=zh-cn#getNextEntry()) 会抛出 [`ZipException`](https://developer.android.google.cn/reference/java/util/zip/ZipException?hl=zh-cn)。
  >
  > 应用可以通过调用 [`dalvik.system.ZipPathValidator.clearCallback()`](https://developer.android.google.cn/reference/dalvik/system/ZipPathValidator?hl=zh-cn#clearCallback()) 选择停用此验证。

- 针对从后台启动 activity 的其他限制

- OpenJDK17

  > - 对正则表达式的更改：现在不允许无效的组引用
  > - UUID 处理：[`java.util.UUID.fromString()`](https://link.juejin.cn?target=https%3A%2F%2Fdeveloper.android.com%2Freference%2Fjava%2Futil%2FUUID%23fromString(java.lang.String)) 方法现在在验证输入参数时会进行更严格的检查
  > - ProGuard 问题：在某些情况下，如果使用 ProGuard 缩小、混淆和优化应用，添加 [`java.lang.ClassValue`](https://link.juejin.cn?target=https%3A%2F%2Fdeveloper.android.com%2Freference%2Fjava%2Flang%2FClassValue) 会导致出现问题，问题源于 Kotlin 库，库会根据是否 `Class.forName("java.lang.ClassValue")` 返回类来更改运行时行为。

##### 2、行为变更：所有应用

- 默认拒绝设定精确的闹钟

- 当应用进入缓存时，上下文注册的广播将加入队列

- 应用只能终止自己的后台进程

- 最低可安装的目标 API 级别

  > 从 Android 14 开始，[`targetSdkVersion`](https://developer.android.google.cn/guide/topics/manifest/uses-sdk-element?hl=zh-cn) 低于 23 的应用无法安装。

- 授予对照片和视频的部分访问权限

  > 如果你的应用已经使用了[系统照片选择器](https://link.juejin.cn/?target=https%3A%2F%2Fdeveloper.android.com%2Ftraining%2Fdata-storage%2Fshared%2Fphotopicker)（photopicker），那么无需进行任何改动。
  >
  > 在 Android 14 上，当应用请求 Android 13（API 级别 33）中引入的任何 `READ_MEDIA_IMAGES`或 `READ_MEDIA_VIDEO` 媒体权限时，用户可以授予对其照片和视频的部分访问权限。
  >
  > 新对话框显示以下权限选项：
  >
  > - **选择照片和视频：** Android 14 中的新功能，用户选择他们想要提供给应用的特定照片和视频。
  > - **全部允许**：用户授予对设备上所有照片和视频的完整库访问权限。
  > - **不允许**：用户拒绝所有访问。

- 安全的全屏 Intent 通知

  > 全屏 intent 通知适用于需要用户立即注意的极高优先级通知，例如用户来电或用户配置的闹钟设置。从 Android 14 开始，获准使用此权限的应用仅限于提供通话和闹钟的应用。

- 关于不可关闭通知用户体验方式的变更

  > 调用 `Notification.Builder#setOngoing(true)` 或 `NotificationCompat.Builder#setOngoing(true)`  方法可以防止使用者关闭前台通知，从 Android 14 开始允许使用者关闭这类型的通知，但是但在下列情況下，使用者无法关闭通知：
  >
  > - 手机锁定时
  > - 点击全部清除通知的按钮（避免不慎关闭通知）
  >
  > 链接：https://juejin.cn/post/7233208763921956920













> ### Android 12
>
> https://juejin.cn/post/7037105000480243748
>
> #### 一、android:exported
>
> 它主要是设置 Activity、Service、BroadcastReceiver 是否可由其他应用组件启动，`true` 表示可以，`false` 表示不可以。
>
> > 若为 `false`，则 `Activity` 只能由同一应用的组件或使用同一用户 ID 的不同应用启动。
>
> **exported 的使用**：
>
> - 一般情况下如果使用了 `intent-filter`，则不能将 `exported` 设置为 `false`，不然在 `Activity` 被调用时系统会抛出 `ActivityNotFoundExeception` 异常。
> - 如果没有 `intent-filter`，那就不应该把 `Activity` 的 `exported` 设置为 `true`，**这可能会在安全扫描时被定义为安全漏洞**。
>
> 在 Android 12 的平台上，也就是使用 `targetSdkVersion 31` 时，需要注意：
>
> **如果 `Activity`、`Service` 或者 `Receiver` 使用 `intent-filter`，并且未显示声明 `android:exported` 的值，App 将会无法安装。**
>
> > 可以使用 gradle 脚本，在打包过程中检索所有没有设置 `exported` 的组件，给他们动态设置上 `exported`。如果有需要，还可以自己增加判断设置了 `"intent-filter"` 的才配置 `exported` 。
>
> 
>
> #### 二、SplashScreen
>
> Android 12 新增加了 `SplashScreen` 的 API，它包括启动时进入应用的动作、显示应用的图标动画，以及展示应用本身的过渡效果。
>
> 大概由 4 个部分组成：
>
> - 最好是矢量的可绘制对象，当然可以是静态或动画形式；
> - 图标的背景（可选）；
> - 与自适应图标一样，前景的三分之一被遮盖；
> - 窗口背景；
>
> > **不管 targetSdkVersion 是什么版本，当运行到 Android 12 的手机上时，所有的 App 都会增加 `SplashScreen` 的功能。**
> >
> > 如果什么都不做，那 App 的 Launcher 图标会变成 `SplashScreen` 界面的那个图标，而对应的原主题下 `windowBackground` 属性指定的颜色，就会成为 `SplashScreen` 界面的背景颜色。**这个启动效果在所有应用的冷启动和热启动期间都会出现。**
> >
> > - 如果原本使用了 `android:windowBackground` 实现了启动页，你的实现会被默认的启动样式替换；
> > - 如果你使用了一个额外的 Activity 作为启动页，则会先弹出系统默认启动页，再弹出你实现的启动页，用户会看到两次闪屏。
>
> **适配**：
>
> 1. 升级 sdk
> 2. 添加 splashscreen 依赖
> 3. 增加 values-v31 目录
> 4. 添加 styles.xml 对应的主题
> 5. 给启动 Activity 添加这个主题，不同目录下使用不同主题来达到适配效果
>
> 
>
> #### 三、其他
>
> ##### 1、 通知中心
>
> Android 12 更改了可以完全自定义通知外观和行为，以前自定义通知能够使用整个通知区域并提供自己的布局和样式，现在它行为变了。
>
> 使用 targetSdkVersion 31 的 App，包含自定义内容视图的通知将不再使用完整通知区域，而是使用系统标准模板。
>
> 此模板可确保自定义通知在所有状态下都与其他通知长得一模一样，例如在收起状态下的通知图标和展开功能，以及在展开状态下的通知图标、应用名称和收起功能，与 Notification.DecoratedCustomViewStyle 的行为几乎完全相同。
>
> 
>
> ##### 2、 Android App Links 验证
>
> Android App Links 是一种特殊类型的 DeepLink，用于让 Web 直接在 Android 应用中打开相应 App 的特定内容而无需用户选择应用。
>
> 使用 targetSdkVersion 为 31 的 App，系统对 Android App Links 的验证方式进行了一些调整，这些调整会提升应用链接的可靠性。
>
> > 如果你的 App 是依靠 Android App Links 验证在应用中打开网页链接，那么在为 Android App Links 验证添加 intent 过滤器时，请确保使用正确的格式，**尤其需要注意的是确保这些 `intent-filter` 包含 BROWSABLE 类别并支持 `https` 方案**。
>
> 
>
> ##### 3、 安全和隐私设置
>
> - **大致位置**
>
>   使用 targetSdkVersion 31 的 App，<font color='red'>应用只能访问大致位置信息</font>？
>
>   > 如果 App 请求 `ACCESS_COARSE_LOCATION` 但未请求 `ACCESS_FINE_LOCATION` 那么不会有任何影响。
>
>   targetSdkVersion 31 的 App 请求 `ACCESS_FINE_LOCATION` 运行时权限，还必须请求 `ACCESS_COARSE_LOCATION` 权限。
>
> - **SameSite Cookie**
>
> 
>
> ##### 4、 应用休眠
>
> Android 12 在 Android 11 （API 30）中引入的**自动重置权限行为**的基础上进行了扩展。
>
> 如果 targetSdkVersion 31 的 App 用户几个月不打开，则系统会自动重置授予的所有权限并将 App 置于休眠状态。
>
> 
>
> ### Android 11 适配
>
> https://juejin.cn/post/6860370635664261128
>
> #### 一、适配 targetSdkVersion 30
>
> ##### 1、分区存储强制执行⭐
>

