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



#### 四、WebView 内存泄漏

如何避免 WebView 内存泄漏

1. **不在xml 中定义 WebView，而是在需要的时候在 Activity 中创建，并且 Context 使用 getApplicationContext()。** 
2. **在 Activity 销毁（WebView）的时候，先让 WebView 加载 null 内容，然后移除 WebView，再销毁 WebView，最后置空。**



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

















