## Android 版本适配

> **Android M 6.0 适配**
>
> - 运行时权限动态申请；
>
> **Android N 7.0 适配**
>
> - 在应用间共享文件
>
>   对于面向 Android 7.0 的应用，Android 框架执行的 `StrictMode` API 政策禁止在您的应用外部公开 `file://` URI。如果一项包含文件 URI 的 intent 离开您的应用，则应用出现故障，并出现 `FileUriExposedException` 异常。
>
>   要在应用间共享文件，您应发送一项 `content://` URI，并授予 URI 临时访问权限。进行此授权的最简单方式是使用 `FileProvider` 类。
>
> - APK signature scheme v2
>
>   - 只勾选V1签名就是传统方案签署，但是在 Android 7.0 上不会使用V2安全的验证方式。
>   - 只勾选V2签名7.0以下会显示未安装，Android 7.0 上则会使用了V2安全的验证方式。
>   - 同时勾选V1和V2则所有版本都没问题。
>
> - Android 7.0 移除了三项隐式广播，以帮助优化内存使用和电量消耗。
>
>   - CONNECTIVITY_CHANGE 广播
>
>     > 在后台时不能接受到 CONNECTIVITY_CHANGE 广播，前台不影响。
>
>   - ACTION_NEW_PICTURE 和 ACTION_NEW_VIDEO 广播
>
>     > 不能发送或是接收新增图片（ACTION_NEW_PICTURE）和新增视频（ACTION_NEW_VIDEO）的广播。
>
> - 等等
>
> **Android 8.0 O 适配**
>
> - 通知权限
>
>   > Android 8.0 之后通知权限默认都是关闭的，无法默认开启以及通过程序主动开启，需要程序员读取权限开启情况，然后提示用户去开启。
>
> - 通知适配
>
>   > Android 8.0 修改了通知，比如通知渠道、通知标志、通知超时、背景颜色。比较重要的是通知渠道，其允许您为要显示的每种通知类型创建用户可自定义的渠道。好处就是对于某个应用可以把权限分成很多类，用户开控制是否显示哪些类别的通知。而开发者要做的就是必须设置这个渠道id。
>
> - 自适应启动图标
>
>   从Android 8.0系统开始，应用程序的图标被分为了两层：前景层和背景层。
>
> - 修改运行时权限问题，用什么权限去申请什么权限
>
>   > 在 Android 8.0 之前，如果应用在运行时请求权限并且被授予该权限，系统会错误地将属于同一权限组并且在清单中注册的其他权限也一起授予应用。
>   >
>   > 对于针对 Android 8.0 的应用，此行为已被纠正。系统只会授予应用明确请求的权限。然而，一旦用户为应用授予某个权限，则所有后续对该权限组中权限的请求都将被自动批准。
>
> - 安装apk
>
>   > Android 8.0 去除了“允许未知来源”选项，如果我们的App具备安装App的功能，那么AndroidManifest文件需要包含 REQUEST_INSTALL_PACKAGES 权限，未声明此权限的应用将无法安装其他应用。
>
> - 静态广播无法正常接收
>
>   > 从 Android 8.0 开始，对清单文件中静态注册广播接收者增加了限制，建议改为动态注册。如果还是想用静态注册的方式，在 Intent 里添加 Component 参数可实现。
>
> - 等等
>
> **Android 9.0 P**
>
> - 限制明文流量的网络请求，非加密的流量请求（http）都会被系统禁止掉。
>
>   - 将 http 请求改为 https；
>   - 添加网络安全设置 android:networkSecurityConfig
>
>   > 不作处理访问 http 会失败 `java.net.UnknownServiceException: CLEARTEXT communication to xxx not permitted by network security policy`
>
> - 移除 Apache HTTP 客户端
>
> - 前台服务需要添加权限
>
>   > Android 9.0 之后，比如要授予 FOREGROUND_SERVICE 权限，才能够使用前台服务 startForegroundService，否则会抛出异常。这是个普通权限，不需要动态申请。
>
> - 全面限制静态广播接收
>
>   > 升级 Android 9.0 后，隐式广播江湖被全面禁止，在 AndroidManifest 中注册的 Receiver 将不能够生效，需要进行动态注册。
>
> - 非 SDK 接口访问限制
>
> - 强制执行 FLAG_ACTIVITY_NEW_TASK 要求
>
>   > Android 9.0 中 不能从非 Activity 环境中启动 Activity，除非 intent 加上 FLAG_ACTIVITY_NEW_TASK。
>   >
>   > Android 7.0 以下 和 Android 9.0 及以上 都需要添加 FLAG_ACTIVITY_NEW_TASK。Android 7.0 因为一个错误会临时组织实施标志要求。
>
> - 等等
>
> **Android 10 Q**
>
> - 分区存储
>
>   - 特定目录（App-specific），使用 getExternalFilesDir(String type) 或 getExternalCacheDir() 方法访问。无需权限，且卸载应用时会自动删除。
>   - 照片、视频、音频这类媒体文件。使用 MediaStore 访问，访问其他应用的媒体文件时需要 READ_EXTERNAL_STORAGE 权限。
>   - 其他目录，使用存储访问框架 SAF（Storage Access Framwork）
>
>   > 解决方案：
>   >
>   > - manifest 里面加属性 requestLegacyExternalStorage
>   > - 适配
>   >
>   > App-specific 特定目录下的文件数据会随着应用卸载删除，但是可以在 AndroidManifest 中 通过 `android:hasFragileUserData="true"` 来让用户选择是否保留。
>
> - 在后台运行时访问设备位置信息需要权限
>
>   > Android 10 引入了 ACCESS_BACKGROUND_LOCATION 权限（危险权限）。该权限允许应用程序在后台访问位置。如果请求此权限，则还必须请求 ACCESS_FINE_LOCATION 或 ACCESS_COARSE_LOCATION 权限。只请求此权限无效果。官方推荐使用前台服务来实现，在前台服务中获取位置信息。
>
> - 应用处于后台时，不能启动 Activity。
>
> - 对不可重置的设备标识符实施了限制
>
>   > 从 Android 10 开始，应用必须具有 READ_PRIVILEGED_PHONE_STATE 特许权限才能访问设备的不可重置标识符（包含 IMEI 和序列号）。
>
> - 深色主题
>
> **Android 11.0 R**
>
> - 分区存储强制执行
>
>   targetSdkVersion等于30，Scoped Storage就会被强制启用，requestLegacyExternalStorage标记将会被忽略。
>
>   但是可以覆盖安装，通过 `android:preserveLegacyExternalStorage="true"` 暂时关闭分区存储，让开发者完成数据迁移的工作。如果卸载重装的话就会失效。
>
>   > - sdk 29以下，可以使用以前的存储方式；
>   > - sdk 29 可以使用作用域存储，区别是会增大App占用空间，且随着App卸载而删除；也可以在 AndroidManifest 中配置 android:requestLegacyExternalStorage 关闭作用域存储；
>   > - sdk 30，强制执行作用域存储，但是也可以操作 sdcard 文件，需要申请 MANAGE_EXTERNAL_STORAGE 权限，使用 intent 跳转指定授权页面，让用户手动进行授权。
>
> - 媒体文件访问权限
>
>   - 执行批量操作
>
>     > sdk 30 中 Batch operations 特性，修改其他应用程序锁贡献的数据，比如美图秀秀进行修图，通过新增的 API，如 MediaStore.createWriteRequest 等，就可以对文件进行批量处理。
>
>   - 使用直接文件路径和原生库访问文件
>
> - 读取电话号码需要权限 READ_PHONE_NUMBERS
>
> - 自定义 toast 不允许在后台显示
>
> - 必须加上 V2 签名
>
> - 获取其他应用信息，如包名等
>
>   - 必须在清单文件中添加`<queries>`元素，告知系统你要获取哪些应用信息或者哪一类应用；
>   - 添加 `QUERY_ALL_PACKAGES` 权限
>
> - 请求的权限数月未使用，系统会通过自动重置用户已授予应用的运行时敏感权限；
>
>   > 可以引导用户去设置页面关闭自动重置权限功能；
>
> - 后台位置信息访问权限
>
> **Android 12**
>
> - android:exported
>
>   > 如果 targetSdkVersion 31 时，Activity、Service 或者 BroadcastReveiver 中使用 intent-filter，并且没有显示的声明 android:exported 的值，则 App 将无法安装。



### Android 12

https://juejin.cn/post/7037105000480243748

#### 一、android:exported

它主要是设置 Activity、Service、BroadcastReceiver 是否可由其他应用组件启动，`true` 表示可以，`false` 表示不可以。

> 若为 `false`，则 `Activity` 只能由同一应用的组件或使用同一用户 ID 的不同应用启动。

**exported 的使用**：

- 一般情况下如果使用了 `intent-filter`，则不能将 `exported` 设置为 `false`，不然在 `Activity` 被调用时系统会抛出 `ActivityNotFoundExeception` 异常。
- 如果没有 `intent-filter`，那就不应该把 `Activity` 的 `exported` 设置为 `true`，**这可能会在安全扫描时被定义为安全漏洞**。

在 Android 12 的平台上，也就是使用 `targetSdkVersion 31` 时，需要注意：

**如果 `Activity`、`Service` 或者 `Receiver` 使用 `intent-filter`，并且未显示声明 `android:exported` 的值，App 将会无法安装。**

> 可以使用 gradle 脚本，在打包过程中检索所有没有设置 `exported` 的组件，给他们动态设置上 `exported`。如果有需要，还可以自己增加判断设置了 `"intent-filter"` 的才配置 `exported` 。



#### 二、SplashScreen

Android 12 新增加了 `SplashScreen` 的 API，它包括启动时进入应用的动作、显示应用的图标动画，以及展示应用本身的过渡效果。

大概由 4 个部分组成：

- 最好是矢量的可绘制对象，当然可以是静态或动画形式；
- 图标的背景（可选）；
- 与自适应图标一样，前景的三分之一被遮盖；
- 窗口背景；

> **不管 targetSdkVersion 是什么版本，当运行到 Android 12 的手机上时，所有的 App 都会增加 `SplashScreen` 的功能。**
>
> 如果什么都不做，那 App 的 Launcher 图标会变成 `SplashScreen` 界面的那个图标，而对应的原主题下 `windowBackground` 属性指定的颜色，就会成为 `SplashScreen` 界面的背景颜色。**这个启动效果在所有应用的冷启动和热启动期间都会出现。**
>
> - 如果原本使用了 `android:windowBackground` 实现了启动页，你的实现会被默认的启动样式替换；
> - 如果你使用了一个额外的 Activity 作为启动页，则会先弹出系统默认启动页，再弹出你实现的启动页，用户会看到两次闪屏。

**适配**：

1. 升级 sdk
2. 添加 splashscreen 依赖
3. 增加 values-v31 目录
4. 添加 styles.xml 对应的主题
5. 给启动 Activity 添加这个主题，不同目录下使用不同主题来达到适配效果



#### 三、其他

##### 1、 通知中心

Android 12 更改了可以完全自定义通知外观和行为，以前自定义通知能够使用整个通知区域并提供自己的布局和样式，现在它行为变了。

使用 targetSdkVersion 31 的 App，包含自定义内容视图的通知将不再使用完整通知区域，而是使用系统标准模板。

此模板可确保自定义通知在所有状态下都与其他通知长得一模一样，例如在收起状态下的通知图标和展开功能，以及在展开状态下的通知图标、应用名称和收起功能，与 Notification.DecoratedCustomViewStyle 的行为几乎完全相同。



##### 2、 Android App Links 验证

Android App Links 是一种特殊类型的 DeepLink，用于让 Web 直接在 Android 应用中打开相应 App 的特定内容而无需用户选择应用。

使用 targetSdkVersion 为 31 的 App，系统对 Android App Links 的验证方式进行了一些调整，这些调整会提升应用链接的可靠性。

> 如果你的 App 是依靠 Android App Links 验证在应用中打开网页链接，那么在为 Android App Links 验证添加 intent 过滤器时，请确保使用正确的格式，**尤其需要注意的是确保这些 `intent-filter` 包含 BROWSABLE 类别并支持 `https` 方案**。



##### 3、 安全和隐私设置

- **大致位置**

  使用 targetSdkVersion 31 的 App，<font color='red'>应用只能访问大致位置信息</font>？

  > 如果 App 请求 `ACCESS_COARSE_LOCATION` 但未请求 `ACCESS_FINE_LOCATION` 那么不会有任何影响。

  targetSdkVersion 31 的 App 请求 `ACCESS_FINE_LOCATION` 运行时权限，还必须请求 `ACCESS_COARSE_LOCATION` 权限。

- **SameSite Cookie**



##### 4、 应用休眠

Android 12 在 Android 11 （API 30）中引入的**自动重置权限行为**的基础上进行了扩展。

如果 targetSdkVersion 31 的 App 用户几个月不打开，则系统会自动重置授予的所有权限并将 App 置于休眠状态。



### Android 11 适配

https://juejin.cn/post/6860370635664261128

#### 一、适配 targetSdkVersion 30

##### 1、分区存储强制执行⭐

