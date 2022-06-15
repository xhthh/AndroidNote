### WebView 和 Js 交互

参考：

https://www.jianshu.com/p/910e058a1d63

https://www.jianshu.com/p/345f4d8a5cfa/



#### 一、交互方式总结

Android 与 JS 通过 WebView 互相调用方法，实际上是：

- Android 去调用 JS 的代码
  - 通过 WebView 的 loadUrl()
  - 通过 WebView 的 evaluateJavascript()

- JS 去调用 Android 的代码
  - 通过 WebView 的 addJavascriptInterface() 进行对象映射
  - 通过 WebViewClient 的 shouldOverrideUrlLoading() 回调拦截 url
  - 通过 WebChromeClient 的 onJsAlert()、onJsConfirm()、onJsPrompt() 方法回调拦截 JS 对话框 alert()、confirm()、prompt() 消息

总结：

![img](https://upload-images.jianshu.io/upload_images/944365-613b57c93dff2eb8.png)

> 1. 常用的拦截是：拦截 JS的输入框（即`prompt（）`方法）
>
> 2. 因为只有`prompt（）`可以返回任意类型的值，操作最全面方便、更加灵活；而alert（）对话框没有返回值；confirm（）对话框只能返回两种状态（确定 / 取消）两个值
>
>    **这是 JS 调用 Android 方法，所以需要 JS 主动调用 prompt()，这样才会走 WebChromeClient#onJsPrompt() 方法，我们在 onJsPrompt() 中根据协议处理逻辑，将 result 返回给 JS**；



<font color='red'>带回调的调用</font>：

在一端调用的时候在参数中加一个 callbackId 标记对应的回调，对端接收到调用请求后，进行实际操作，如果带有 callbackId，对端再进行一次调用，将结果、callbackId 回传回来，这端根据 callbackId 匹配相应的回调，将结果传入执行就可以了。



**关于 shouldOverrideUrlLoading()**：

- shouldOverrideUrlLoading() 接口，主要是给WebView提供时机，让其选择是否对UrlLoading进行拦截。
- 关于该接口的返回值，True（拦截WebView加载Url），False（允许WebView加载Url）
- shouldOverrideUrlLoading() 调用时机：
  - 当我们点击页面中的一个link时，先调用shouldOverrideUrlLoading再调用onPageStarted。
  - 当我们通过loadUrl的方式加载一个页面时，先调用onPageStarted再调用shouldOverrideUrlLoading。
  - 不过shouldOverrideUrlLoading不一定每次都被调用，只有需要的时候才会被调用。比如，一开始页面加载时（没有重定向）不调用，reload不调用，返回上一页面不调用。



#### 二、WebView 漏洞

参考：https://www.jianshu.com/p/3a345d27cd42

##### 1、漏洞类型

- 任意代码执行漏洞
- 密码明文存储漏洞
- 域控制不严格漏洞

##### 2、具体分析

###### 2.1 任意代码执行漏洞

JS 调用 Android 的其中一个方式是通过 `addJavascriptInterface` 接口进行对象映射：

```java
 webView.addJavascriptInterface(new JSObject(), "myObj");
// 参数1：Android的本地对象
// 参数2：JS的对象
// 通过对象映射将Android中的本地对象和JS中的对象进行关联，从而实现JS调用Android的对象和方法
```

<font color='red'>当 JS 拿到 Android 这个对象后，就可以调用这个 Android 对象中所有的方法，包括系统类（java.lang.Runtime 类），从而进行任意代码执行。</font>

> 比如可以执行命令获取本地设备的 SD 卡中的文件等信息从而造成信息泄露。



<font color='red'>解决方案</font>：

1. Android 4.2 之前

   在Android 4.2版本之前采用**拦截prompt（）**进行漏洞修复。

   - 继承 WebView，重写 `addJavascriptInterface` 方法，维护一个对象映射关系的 map，将需要添加的 JS 接口放入该 map；
   - 每次当 WebView 加载页面前加载一段本地的 js 代码
     - 让 JS 调用 JavaScript 方法，该方法是通过调用 prompt() 把 JS 中的信息传给 Android 端；
     - 在 Android onJsPrompt() 中，解析传递过来的信息，再通过反射机制调用 Java 对象的方法；

2. Android 4.2 之后

   Google 在Android 4.2 版本中规定对被调用的函数以 `@JavascriptInterface`进行注解从而避免漏洞攻击。



###### 2.2  密码明文存储漏洞

关闭密码保存提醒

```java
WebSettings.setSavePassword(false) 
```

###### 2.3 域控制不严格漏洞

<font color='red'>问题分析</font>：

A 应用可以通过 B 应用导出的 Activity 让 B 应用加载一个恶意的 file 协议的 url，从而可以获取 B 应用的内部私有文件，从而带来数据泄露威胁。

<font color='red'>getSettings类中有安全影响的设置</font>：

- setAllowFileAccess()
- setAllowFileAccessFromFileURLs()
- setAllowUniversalAccessFromFileURLs()

<font color='red'>解决方案</font>：

- 对于不需要使用 file 协议的应用，禁用 file 协议

  ```java
  // 禁用 file 协议；
  setAllowFileAccess(false); 
  setAllowFileAccessFromFileURLs(false);
  setAllowUniversalAccessFromFileURLs(false);
  ```

- 对于需要使用 file 协议的应用，禁止 file 协议加载 JavaScript

  ```java
  // 需要使用 file 协议
  setAllowFileAccess(true); 
  setAllowFileAccessFromFileURLs(false);
  setAllowUniversalAccessFromFileURLs(false);
  
  // 禁止 file 协议加载 JavaScript
  if (url.startsWith("file://") {
      setJavaScriptEnabled(false);
  } else {
      setJavaScriptEnabled(true);
  }
  ```

  

#### 三、JsBridge 开源库

https://github.com/lzyzsd/JsBridge

##### 1、库的结构

1. 一个用来注入的 js 文件；
2. 一个自定义 WebView（包括 WebViewClient）；
3. 作为载体的 BridgeHandler；



##### 2、JsBridge 的 核心

1. 拦截 url

   js 调用 native 的方法

   - 向 body 中添加一个不可见的 iframe 元素。通过拦截 url 的方法来执行相应的操作，但是页面本身不能跳转，所以可以改变一个不可见的 iframe 的 src 就可以让 webview 拦截到 url，而用户是无感知的。
   - 拦截 url。通过 shouldOverrideUrlLoading() 来拦截约定规则的 url，再做具体操作。
   - <font color='red'>返回给 JS 的回调是通过 loadurl() 来实现的。</font>

2. loadurl("javascript:js_method")

   native 调用 js 的方法

##### 3、框架存在的问题

<font color='red'>问题：</font>

如果 native 在调用 regishandler() 的时候，如果注册没有结束，h5 就调用该方法会导致桥不通的问题。

<font color='red'>解决：</font>

本地 js 加载时机不对，如果打开界面就需要进行接口调用，可能存在找不到 callback。

采用 h5 端加载的方式解决了这个问题。或者是让 h5 端不断循环找对象，找到对象之后再发消息。



![img](https://upload-images.jianshu.io/upload_images/6456061-3d41d9295cac7c00.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

##### 4、Web 发送 URL 请求的方法

- a标签

  需要用户操作

- location.href

  可能会引起页面的跳转丢失调用

- 使用 iframe.src

  安卓提供了`shouldOverrideUrlLoading`方法拦截

- 发送 ajax 请求

  Android 没有相应的拦截方法

##### 5、Java 代码调 JS

![img](https://upload-images.jianshu.io/upload_images/3881353-011e5c02531b69e8.png?imageMogr2/auto-orient/strip|imageView2/2/w/794/format/webp)

- callHandler(String handlerName, String data, CallBackFunction callBack)

  - 方法名
  - 一般为和服务器约定的gson对象
  - 回调函数

- doSend()

  - 组装 message 对象
  - 将回调函数保存在 responseCallbacks 中

- queueMessage()

  将消息加入消息队列，如果队列为空，则直接分发运行

  > startupMessage队列主要是用来在JsBridge的js库注入之前，保存Java调用JS的消息，避免消息的丢失或失效。待页面加载完成后，后续CallHandler的调用，可直接使用loadUrl方法而不需入队。究其根本，是因为Js代码库必须在onPageFinished（页面加载完成）中才能注入导致的。
  >
  > **必须在主线程中调用CallHandler方法**。

- dispatchMessage()

  - 将 message 对象转为 JS 语句
  - 通过loadUrl执行JS代码，触发lib库中的_handleMessageFromNative方法（即 assets 中的 js 文件）

- _handleMessageFromNative() 

- _dispatchMessageFromNative()

  - js 文件中有个 messageHandlers，用来保存 js 代码端注册的方法，可以根据 handlerName 方法名查找；
  - 调用 js 相应方法，处理回调

##### 6、JS 代码调 Java

![img](https://upload-images.jianshu.io/upload_images/3881353-f7e52c151ade2032.png?imageMogr2/auto-orient/strip|imageView2/2/w/901/format/webp)

> 实现原理：利用js的iFrame（不显示）的src动态变化，触发java层WebViewClient的shouldOverrideUrlLoading方法，然后让本地去调用javasript。
>  JS代码执行完成后，最终调用_doSend方法处理回调。

- callHandler(handlerName, data, responseCallback)

- WebViewJavascriptBridge.js#_doSend()

  该方法中修改一个不可见的 iframe 的 src 用于触发  Android 端的 shouldOverrideUrlLoading()

- BridgeWebViewClient#shouldOverrideUrlLoading()

  - iFrame变更后，java部分触发shouldOverrideUrlLoading方法；
  - 根据scheme不同，进入webview的flushMessageQueue方法，该方法最终调用JS的_fetchQueue方法。
  - BridgeWebview#flushMessageQueue() 中在主线程中调用了 loadUrl(url, returnCallback)，将回调保存到 responseCallbacks 这个 Map 中；

- _fetchQueue()

  获取sendMessageQueue返回给native,由于android不能直接获取返回的内容,所以使用url shouldOverrideUrlLoading 的方式返回内容；

- BridgeWebViewClient#shouldOverrideUrlLoading()

  拦截 url，scheme 为 “yy://return“

- BridgeWebView#handlerReturnData(url)

  - 从 url 中获取方法名，根据方法名从 responseCallbacks 里获取对应的 callbackFunction 回调；
  - 从 url 中获取 js 传来的 data，作为参数执行其回调 onCallback(data)，即 flushMessageQueue() 中 loadUrl() 参数中的 onCallBack()；

- 回到 BridgeWebView#flushMessageQueue() 中

  1. 将 js 传来的数据解析为 Message 集合，遍历；
  2. 根据 js 传递的 callbackId，构建一个 responseFunction 回调；
  3. 通过 handlerName 即方法名，获取到对应的 handler 调用其 handler() 方法，将 data 和 responseFunction 回调传过去，最终执行到 Android 端注册本地方法的回调；
  4. 注册本地方法的回调中，我们进行原生方法的执行，并将 js 需要的回调数据通过 responseFunction 返回；
  5. 这里返回到的地方即 步骤2 中构建的 responseFunction 的 onCallback() 回调中，这里我们构建 responseMsg，设置 js 传给我们的 callbackId，将要返回的数据设置进去，通过 queueMessage(responseMsg) 
  6. queueMessage(responseMsg) 即是通过Android 调 Js，即 loadUrl() 最终将数据返回给 JS；

  



#### 四、WebView 内存泄漏

如何避免 WebView 内存泄漏

1. **不在xml 中定义 WebView，而是在需要的时候在 Activity 中创建，并且 Context 使用 getApplicationContext()。** 
2. **在 Activity 销毁（WebView）的时候，先让 WebView 加载 null 内容，然后移除 WebView，再销毁 WebView，最后置空。**
3. 动态添加webview，通过布局的viewgroup，使用viewgroup.add方式添加webview，销毁的时候先通过viewgroup.removeView(webview)删除webview，然后webview.destroy；另外传入webview的Context采用弱引用的方式。

> 内存泄漏的主要原因是引用了 Activity/Fragment 的 Context，导致 Activity/Fragment 无法被释放。



#### 五、WebView 缓存机制和资源预加载方案

##### 1、WebView 存在的性能问题

###### 1.1 H5 页面加载速度慢

- **渲染速度慢** 

  - JS 解析效率

    JS 本身的解析过程复杂、解析速度不快 & 前端页面涉及较多 JS 代码文件，所以叠加起来会导致 JS 解析效率非常低。

  - 手机硬件设备的性能

- **页面资源加载慢**

  - H5 页面一般会比较多
  - 每加载一个 H5 页面，都会产生较多网络请求
    - HTML 主 URL 自身的请求；
    - HTML 外部引用的 JS、CSS、字体文件，图片也是一个独立的 HTTP 请求；

###### 1.2 耗费流量



##### 2、解决方案

- 前端 H5 的缓存机制（WebView 自带）
- 资源预加载
- 资源拦截

###### 2.1 前端 H5 的缓存机制

![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWdjb252ZXJ0LmNzZG5pbWcuY24vYUhSMGNITTZMeTkxYzJWeUxXZHZiR1F0WTJSdUxuaHBkSFV1YVc4dk1qQXhPUzgyTHpFeEx6RTJZalEyTjJGaU9EY3dPVEk1TTJV)

###### 2.2 资源预加载

- 预加载 WebView 对象
- 预加载 H5 资源



###### 2.3 资源拦截

![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWdjb252ZXJ0LmNzZG5pbWcuY24vYUhSMGNITTZMeTkxYzJWeUxXZHZiR1F0WTJSdUxuaHBkSFV1YVc4dk1qQXhPUzgyTHpFeEx6RTJZalEyTjJGaVkySmhaalpqTlRj)

















