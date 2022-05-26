### OkHttp

参考资料：

https://juejin.cn/post/6844904133103747086

https://juejin.cn/post/6909445385266135048

https://juejin.cn/post/6881436122950402056

#### 一、请求的大致流程

##### 1、构建 OkHttpClient

通过构建者模式创建，Builder.build() 中真正创建了 OkHttpClient，并传入 Builder 对象；

##### 2、构建 Request

一样是构建者模式，主要参数有 httpUrl、method、headers、requestBody

##### 3、Post 构建请求体 RequestBody

##### 4、构建 Call

Call 是一个接口，真正实现在 RealCall

```kotlin
//OkHttpClient.kt
override fun newCall(request: Request): Call = RealCall(this, request, forWebSocket = false)

```

##### 5、执行 Call

> 同步执行 execute()
>
> - 通过 dispatcher 将请求 Call 加入一个双端队列
>
> - 通过 getResponseWithInterceptorChain() 获取响应
>
> - 通知 dispatcher 结束请求，将 Call 从队列中移除
>
>   调用 Dispatcher#finished()，该方法中会将完成的任务从队列中移除，并将 readyAsyncCalls 等待队列中的请求移入正在执行的异步队列 runningAsyncCalls 中，交由线程池执行。

1. 异步执行 enqueue()

   ```kotlin
   //RealCall.kt
   override fun enqueue(responseCallback: Callback) {
     //检查是否执行了两遍，也就是说你是不是同一个快递想要发两次，这当然是不行的。
     check(executed.compareAndSet(false, true)) { "Already Executed" }
     //请求后会立即调用，相当于监听请求的开始事件
     callStart()
     //将请求交给调度器来决定什么时候请求
     client.dispatcher.enqueue(AsyncCall(responseCallback))
   }
   ```

   > AsyncCall 继承自 Runnable

2. Dispatcher

   ```kotlin
   class Dispatcher constructor() {
    //最大请求数，也就是最多只有64辆发送快递的车，用完了就没了
     @get:Synchronized var maxRequests = 64  
   	//同一主机最大请求数量，加入你退货的目的地是北京，发往北京的快递车最多只有5辆
     @get:Synchronized var maxRequestsPerHost = 5
   
   	//线程安全的单例模式，线程池的获取用于线程调度。
     @get:Synchronized
     @get:JvmName("executorService") val executorService: ExecutorService
       get() {
         if (executorServiceOrNull == null) {
           executorServiceOrNull = ThreadPoolExecutor(0, Int.MAX_VALUE, 60, TimeUnit.SECONDS,
               SynchronousQueue(), threadFactory("$okHttpName Dispatcher", false))
         }
         return executorServiceOrNull!!
   }
   
    //准备的队列，也就是进入了打包的车
     private val readyAsyncCalls = ArrayDeque<AsyncCall>()
     //异步发送队列，也就是进入了准备出发的车
     private val runningAsyncCalls = ArrayDeque<AsyncCall>()
     //同步发送队列，同样是进入了准备出发的车
     private val runningSyncCalls = ArrayDeque<RealCall>()
    	...
   }
   ```

   > 线程池核心线程数为 0，最大线程数不限制，为 CachedThreadPool 类型，适合执行大量的耗时较少的任务；
   >
   > 超时时间为 60 秒；
   >
   > 缓存线程池，具有缓存功能的，如果一个线程工作完了并且60s之内又有请求过来，就复用刚才那个线程，这提高了性能。
   >
   > 阻塞队列用的SynchronousQueue，它的特点是不存储数据，当添加一个元素时，必须等待一个消费线程取出它，否则一直阻塞，如果当前有空闲线程则直接在这个空闲线程执行，如果没有则新启动一个线程执行任务。通常用于需要快速响应任务的场景，在网络请求要求低延迟的大背景下比较合适。

3. Dispatcher.enqueue

   ```kotlin
   //Dispatcher.kt
   internal fun enqueue(call: AsyncCall) {
     //确保线程安全
     synchronized(this) {
       //把快递放到打包车中
       readyAsyncCalls.add(call)
       //如果不是websocket，前面可知forWebSocket为false
       if (!call.call.forWebSocket) {
         //查询有没有退货到北京的车
         val existingCall = findExistingCallWithHost(call.host)
         //复用退货到北京的计数器callsPerHost，用于统计发往北京快递车数量
         if (existingCall != null) call.reuseCallsPerHostFrom(existingCall)
       }
     }
     //准备发送快递
     promoteAndExecute()
   }
   ```

   首先将 call 放入请求队列中，然后检查同一个主机已经有了几个请求，如果同主机下已经有请求，用它的计数器 callsPerHost 类型 AtomicInteger

4. Dispatcher.promoteAndExecute

   ```kotlin
   private fun promoteAndExecute(): Boolean {
     this.assertThreadDoesntHoldLock()
     //收集所有要发出去的快递，也就是要执行的请求
     val executableCalls = mutableListOf<AsyncCall>()
     val isRunning: Boolean
     synchronized(this) {
       val i = readyAsyncCalls.iterator()//得到迭代器对象用于遍历
       while (i.hasNext()) {//遍历
         val asyncCall = i.next()//得到下一个快递
         //很重要的判断，如果64个快递车都出发了，也就没快递车了，那么将无法请求
         if (runningAsyncCalls.size >= this.maxRequests) break 
         //发往北京的5辆快递车也都走了，那么继续下一个快递
         if (asyncCall.callsPerHost.get() >= this.maxRequestsPerHost) continue // Host max capacity.
         //上面都通过了，说明有车，并且发往北京的快递车也还有，那么就离开打包车
         i.remove()
         //把去往北京的快递车数量+1
         asyncCall.callsPerHost.incrementAndGet()
         //把快递保存起来
         executableCalls.add(asyncCall)
         //将快递放到即将出发的车中
         runningAsyncCalls.add(asyncCall)
       }
       isRunning = runningCallsCount() > 0
     }
     //循环遍历，将所有的快递寄出去
     for (i in 0 until executableCalls.size) {
       val asyncCall = executableCalls[i]
       asyncCall.executeOn(executorService)
     }
     return isRunning
   }
   ```

   - 首先遍历 readyAsyncCalls，判断还要没有要执行的 call；
   - 有的话取出来，判断 runningAsyncCalls 是否超过最大 64 个的限制，超过跳出循环；
   - 判断相同 host 下的请求个数是否超过 5 个的限制，是的话，取下一个 call 进行判断；
   - 通过判断，将相同 host 下的请求个数 callsPerHost 加1；
   - 将 call 添加到 executableCalls 和 runningAsyncCalls中；
   - 遍历，从 executableCalls 中取出 call，asyncCall.executeOn(executorService) 执行；

   > AsyncCall 继承自 Runnable，构造函数中有一个 responseCallback，即请求执行完成功和失败的回调；
   >
   > 用于异步请求，通过线程池执行，回调到 AsyncCall#run() 中；

5. asyncCall.executeOn

   ```kotlin
   fun executeOn(executorService: ExecutorService) {
     client.dispatcher.assertThreadDoesntHoldLock()
     //暂时定义发送没成功
     var success = false
     try {
       //使用线程池来执行自身
       executorService.execute(this)
       //发送成功了
       success = true
     } catch (e: RejectedExecutionException) {
       val ioException = InterruptedIOException("executor rejected")
       ioException.initCause(e)
       noMoreExchanges(ioException)
       //失败回调
       responseCallback.onFailure(this@RealCall, ioException)
     } finally {
       if (!success) {
         //结束
         client.dispatcher.finished(this) 
       }
     }
   }
   ```

   使用线程池来执行，回调到 run()；

6. AsyncCall#run()

   - 通过 getResponseWithInterceptorChain() 得到最终响应，回调 onResponse(response)；

   - 失败回调 onFailure()；

   - finally 执行 client.dispatcher.finished(this)

     - callsPerHost 减 1；

     - 将执行完的 call，从 runningAsyncCalls 中移除；

     - 再次执行 promoteAndExecute()；

       > 因为 Dispatcher#enqueue() 中先将要执行的 call，添加到了 readyAsyncCalls，在 promoteAndExecute() 中如果请求超过两个限制，没有执行的话，在请求执行完后 finished() 中，会再次执行 promoteAndExecute()，把之前未执行的 call 从 readyAsyncCalls 中取出来，判断执行。
       >
       > readyAsyncCalls 是个队列，应该是按顺序取出；



#### 二、责任链

```kotlin
@Throws(IOException::class)
  internal fun getResponseWithInterceptorChain(): Response {
    // 构建完整的拦截器栈.
    val interceptors = mutableListOf<Interceptor>()
    interceptors += client.interceptors////用户自定义的拦截器
    //添加默认拦截器
    interceptors += RetryAndFollowUpInterceptor(client)
    interceptors += BridgeInterceptor(client.cookieJar)
    interceptors += CacheInterceptor(client.cache)
    interceptors += ConnectInterceptor
    if (!forWebSocket) {
      //添加用户定义的网络拦截器
      interceptors += client.networkInterceptors
    }
    interceptors += CallServerInterceptor(forWebSocket)

    val chain = RealInterceptorChain(
        call = this,
        interceptors = interceptors,
        index = 0,
        exchange = null,
        request = originalRequest,
        connectTimeoutMillis = client.connectTimeoutMillis,
        readTimeoutMillis = client.readTimeoutMillis,
        writeTimeoutMillis = client.writeTimeoutMillis
    )
    try {
      //责任链的精髓所在，这一行代码就开启责任链的传递，然后责任链中的所有拦截器层层调用，然后层层返回
      val response = chain.proceed(originalRequest)
      //...
      return response
    } catch (e: IOException) {
      //...
    } finally {
      //...
    }
  }
```

当调用了chain.proceed方法以后即进入下一个拦截器的方法，下一个拦截器依然调用chain.proceed方法，重复不断的进入下一个拦截器。这就是请求层层被拦截，响应传回来的时候会被最后一个拦截器拦截，然后在层层的传回来。

![img](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/fb548dc0b3ba466c9adeadeb764503f6~tplv-k3u1fbpfcp-zoom-in-crop-mark:1304:0:0:0.awebp)

```kotlin
fun interface Interceptor {
  @Throws(IOException::class)
  fun intercept(chain: Chain): Response

  interface Chain {
    //...
    @Throws(IOException::class)
    fun proceed(request: Request): Response
    //...
  }
}
```

```kotlin
//RealInterceptorChain.kt
@Throws(IOException::class)
override fun proceed(request: Request): Response {
    check(index < interceptors.size)
    calls++
    // Call the next interceptor in the chain.
    val next = copy(index = index + 1, request = request)
    val interceptor = interceptors[index]

    @Suppress("USELESS_ELVIS")
    val response = interceptor.intercept(next) ?: throw NullPointerException(
        "interceptor $interceptor returned null")
    return response
}
```

> 注意点：
>
> - 当创建一个责任链 `RealInterceptorChain` 的时候，我们传入的第 5 个参数是 0。该参数名为 `index`，会被赋值给 `RealInterceptorChain` 实例内部的同名全局变量。
> - 假设没有自定义 interceptor，即默认第一个为 RetryAndFollowUpInterceptor，index 为 0，RealInterceptorChain 中 copy(index+1) 获取责任链，并取出 RetryAndFollowUpInterceptor，调用其 intercept(next) 传入责任链，在其 intercept() 中处理完自身逻辑后，继续调用 chain.proceed(request)
> - 责任链不同节点对于proceed的调用次数有不同的限制，ConnectInterceptor拦截器及其之后的拦截器能且只能调用一次，因为网络握手、连接、发送请求的工作发生在这些拦截器内，表示正式发出了一次网络请求；而在这之前的拦截器可以执行多次proceed，比如错误重试。



#### 三、拦截器

- **RetryAndFollowUpInterceptor**

  总的来说RetryAndFollowUpInterceptor就是负责重试和请求重定向的一个拦截器，它还额外做了一个工作就是创建了一个ExchangeFinder对象，这个对象就是用来管理连接池为后来的连接做准备的。

- **BridgeInterceptor**

  桥接，负责把应用请求转换成网络请求，把网络响应转换成应用响应，就是添加各种响应头信息的。负责把用户构造的请求转换为发送给服务器的请求，把服务器返回的响应转换为对用户友好的响应。 在Request阶段配置用户信息，并添加一些请求头。在Response阶段，进行gzip解压。

  有以下几点需要注意：

  - 开发者没有添加Accept-Encoding时，自动添加Accept-Encoding: gzip
  - 自动添加Accept-Encoding，会对request，response进行自动解压
  - 手动添加Accept-Encoding，不负责解压缩
  - 自动解压时移除Content-Length，所以上层Java代码想要contentLength时为-1
  - 自动解压时移除 Content-Encoding

- **CacheInterceptor**

  用来负责读取缓存以及更新缓存的。它内部的实现是使用的OKIO是进行读取和写入的。

  整个方法的流程如下：

  1. 读取候选缓存，
  2. 创建缓存策略，强制缓存、对比缓存等
  3. 根据策略，不使用网络，又没有缓存的直接报错，并返回错误码504。
  4. 根据策略，不使用网络，有缓存的直接返回。
  5. 前面两个都没有返回，继续执行下一个Interceptor，即ConnectInterceptor。
  6. 接收到网络结果，如果响应code式304，则使用缓存，返回缓存结果。
  7. 读取网络结果。
  8. 对数据进行缓存。
  9. 返回网络读取的结果。

  <font color='red'>Okhttp的缓存是根据服务器header自动的完成的，整个流程也是根据RFC文档写死的，客户端不必要进行手动控制。</font>

- **ConnectInterceptor**

  总的来说就是做了这么几个工作:

  1. RetryAndFollowUpInterceptor中定义的ExchangeFinder对象，它里面包含了一个连接池，用于在连接池中取得连接对象。
  2. 首先查找是否有可用的连接，没有的话就尝试是否有能重用的连接，没有的话就去连接池中找，连接池中也没有就创建新的连接对象RealConnection。
  3. 然后调用连接对象RealConnection的connect方法，最终创建了Socket对象用于真正的连接，然后使用了OkIO的的输入输出流，为输入输出做准备。
  4. 最终返回Exchange对象，它是负责发送请求和接受响应的，而真正具体干活的是ExchangeCodec对象。
  5. 将Exchange对象放到拦截链中，让下一个拦截器进行真正的请求和响应。

  <font color='red'>RealConnection 是 OkHttp 实际建立连接的地方，在 connect() 中追踪代码，最终是通过 socket 与服务器建立连接的。</font>

  <font color='red'>Okhttp的磁盘缓存机制是基于DiskLruCache做的，即最近最少使用算法来进行缓存的，使用okio作为输入输出。</font>

- **CallServerInterceptor**

  服务器请求拦截器 CallServerInterceptor 用来向服务器发起请求并获取数据。这是整个责任链的最后一个拦截器，拿到响应后直接返回给上一级的拦截器，不用再调用 proceed() 方法。

  主要就是创建了Socket对象，并且使用Socket对象建立了连接，然后使用OKio中的接口获得输入/输出流。

  流程大致为

  1. 先写入请求头，如果是GET请求的话就已经请求完毕，POST请求的话是先发送请求头再发送请求体，会发送两个TCP包
  2. 然后读取响应头，接着判断过后，读取响应体。
  3. 最终将响应的结果返回，这个结果会层层的向上传递，经过上面所有的拦截器。
  4. 最终走到了我们自定义的回调处。

  > 这个拦截器里会进行 IO 操作与服务器交互，底层使用了 OKIO 来进行 IO 操作。
  >
  > 1. 根据 Request 的配置写入请求头
  > 2. 根据 Method 判断是否支持请求体，如果支持则尝试写入请求体并发送请求报文，否则直接发送
  > 3. 读取响应头，构建 Response
  > 4. 读取响应体，为 Response 写入 ResponseBody
  > 5. 判断是否要关闭连接
  > 6. 返回 Response



**拦截器总结**

拦截器采用了责任链模式，层层向下请求，请求后的结果层层向上传递

- 首先经过了RetryAndFollowUpInterceptor拦截器，这个拦截器负责重试和重定向，最大重试次数为20次。并且在这个对象中创建了ExchangeFinder对象，用于管理连接池等，为随后的链接做准备；
- 经过BridgeInterceptor拦截器，这个拦截器主要就是帮我们添加一些请求头的和压缩/解压缩的。在这个拦截器中表明了，如果用户自定义了gzip请求头，需要自行解压缩，OkHttp则不再负责解压缩；
- CacheInterceptor是负责缓存的，并且内部使用的是OKio进行的缓存；
- ConnectInterceptor拦截器是负责建立连接的，最终是通过RealConnection对象建立的Socket连接，并且获得了输入输出流为下一步读写做准备。RealConnection对象的获取是优先复用的，没有复用的就从连接池里取，连接池也没的话在创建新的，并加入连接池；
- CallServerInterceptor拦截器就是最终的拦截器了，它将负责数据真正的读取和写入。



#### 四、缓存策略

HTTP的缓存机制也是依赖于请求和响应header里的参数类实现的。缓存分为两种，一种是强制缓存，一种是对比缓存

##### 1、强制缓存

![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/2a86833457f344aa9e7753997d49f77a~tplv-k3u1fbpfcp-zoom-in-crop-mark:1304:0:0:0.awebp)

客户端先看有没有缓存，有缓存直接拿缓存，如果没缓存的话就请求服务器然后将结果缓存，以备下次请求。

强制缓存使用的的两个标识：

- Expires：Expires的值为服务端返回的到期时间，即下一次请求时，请求时间小于服务端返回的到期时间，直接使用缓存数据。到期时间是服务端生成的，客户端和服务端的时间可能有误差。
- Cache-Control：Expires有个时间校验的问题，所有HTTP1.1采用Cache-Control替代Expires。
  - private: 客户端可以缓存。
  - public: 客户端和代理服务器都可缓存。
  - max-age=xxx: 缓存的内容将在 xxx 秒后失效
  - no-cache: 需要使用对比缓存来验证缓存数据。
  - no-store: 所有内容都不会缓存，强制缓存，对比缓存都不会触发。



##### 2、对比缓存

![img](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/caf9ee4b11dc4291bc6343198e30c388~tplv-k3u1fbpfcp-zoom-in-crop-mark:1304:0:0:0.awebp)

对比缓存需要服务端参与判断是否继续使用缓存，当客户端第一次请求数据时，服务端会将缓存标识（Last-Modified/If-Modified-Since与Etag/If-None-Match）与数据一起返回给客户端，客户端将两者都备份到缓存中 ，再次请求数据时，客户端将上次备份的缓存 标识发送给服务端，服务端根据缓存标识进行判断，如果返回304，则表示通知客户端可以继续使用缓存。

- **Last-Modified/If-Modified-Since**

  Last-Modified 表示资源上次修改的时间。

  当客户端发送第一次请求时，服务端返回资源上次修改的时间：

  ```
  Last-Modified: Tue, 12 Jan 2016 09:31:27 GMT
  ```

  客户端再次发送，会在header里携带If-Modified-Since。将上次服务端返回的资源时间上传给服务端。

  ```
  If-Modified-Since: Tue, 12 Jan 2016 09:31:27 GMT
  ```

  服务端接收到客户端发来的资源修改时间，与自己当前的资源修改时间进行对比，如果自己的资源修改时间大于客户端发来的资源修改时间，则说明资源做过修改， 则返回200表示需要重新请求资源，否则返回304表示资源没有被修改，可以继续使用缓存。

- **Etag/If-None-Match**

  如果标识码发生改变，则说明资源已经被修改，ETag优先级高于Last-Modified。

  ETag是资源文件的一种标识码，当客户端发送第一次请求时，服务端会返回当前资源的标识码：

  ```
  ETag: "5694c7ef-24dc"
  ```

  客户端再次发送，会在header里携带上次服务端返回的资源标识码：

  ```
  If-None-Match:"5694c7ef-24dc"
  ```

  服务端接收到客户端发来的资源标识码，则会与自己当前的资源吗进行比较，如果不同，则说明资源已经被修改，则返回200，如果相同则说明资源没有被修改，返回 304，客户端可以继续使用缓存。

##### 3、流程图

![img](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/9fcfc775c3ed4f0fa00dd32967f32b48~tplv-k3u1fbpfcp-zoom-in-crop-mark:1304:0:0:0.awebp)

##### 4、OkHttp 缓存

Okhttp的缓存策略就是根据上述流程图实现的，具体的实现类是CacheStrategy，CacheStrategy的构造函数里有两个参数

```kotlin
class CacheStrategy internal constructor(
  val networkRequest: Request?,
  val cacheResponse: Response?
)
```

这两个参数参数的含义如下：

- networkRequest：网络请求。
- cacheResponse：缓存响应，基于DiskLruCache实现的文件缓存，key是请求中url的md5，value是文件中查询到的缓存

CacheStrategy 根据之前缓存结果与当前将要发生的 request 的Header 计算缓存策略。规则如下

| networkRequest | cacheResponse | CacheStrategy                                                |
| -------------- | ------------- | ------------------------------------------------------------ |
| null           | null          | only-if-cached(表明不进行网络请求，且缓存不存在或者过期，一定会返回 503 错误) |
| null           | non-null      | 不进行网络请求，而且缓存可以使用，直接返回缓存，不用请求网络 |
| non-null       | null          | 需要进行网络请求，而且缓存不存在或者过期，直接访问网络。     |
| non-null       | not-null      | Header 中含有 ETag/Last-Modified 标识，需要在条件请求下使用，还是需要访问网络。 |

具体流程如下所示：

- 读取候选缓存。
- 根据候选缓存创建缓存策略。
- 根据缓存策略，如果不进行网络请求，而且没有缓存数据时，报错返回错误码 504。
- 根据缓存策略，如果不进行网络请求，缓存数据可用，则直接返回缓存数据。
- 缓存无效，则继续执行网络请求。
- 通过服务端校验后，缓存数据可以使用（返回 304），则直接返回缓存数据，并且更新缓存。
- 读取网络结果，构造 response，对数据进行缓存。

整个流程就是这样，另外说一点，Okhttp的缓存是根据服务器header自动的完成的，整个流程也是根据RFC文档写死的，客户端不必要进行手动控制。

Okhttp的磁盘缓存机制是基于DiskLruCache做的，即最近最少使用算法来进行缓存的，使用okio作为输入输出。



#### 五、连接池

因为HTTP是基于TCP，TCP连接时需要经过三次握手，为了加快网络访问速度，我们可以Reuqst的header中将Connection设置为keepalive来复用连接。

Okhttp支持5个并发KeepAlive，默认链路生命为5分钟(链路空闲后，保持存活的时间)，连接池有ConectionPool实现，对连接进行回收和管理。

连接池真正的实现类是RealConnectionPool：

```kotlin
class RealConnectionPool(
  taskRunner: TaskRunner,
  /** The maximum number of idle connections for each address. */
  private val maxIdleConnections: Int,
  keepAliveDuration: Long,
  timeUnit: TimeUnit
) {
  //taskRunner 中有 ThreadPoolExecutor
  private val cleanupQueue: TaskQueue = taskRunner.newQueue()
  private val cleanupTask = object : Task("$okHttpName ConnectionPool") {
    override fun runOnce() = cleanup(System.nanoTime())//清理任务的具体操作在 cleanup()中
  }
  private val connections = ConcurrentLinkedQueue<RealConnection>()
    
  fun put(connection: RealConnection) {
    connection.assertThreadHoldsLock()

    connections.add(connection)//将新建的连接插入到连接池
    cleanupQueue.schedule(cleanupTask)//使用线程池执行清理闲置连接的任务
  }
}
```

> ConcurrentLinkedQueue是一个基于链接节点的无界线程安全队列。该队列的元素遵循先进先出的原则。头是最先加入的，尾是最近加入的。和其他并发集合一样。把一个元素放入到队列的线程的优先级高与对元素的访问和移除的线程。

OkHttp 的连接管理管理分成两个步骤：

1. 当我们创建了一个新的连接的时候，通过调用双端队列的 add() 方法，将其加入到队列中；

2. 清理连接缓存的操作由线程池来定时执行；

   每当向连接池插入一个连接之前都会调用线程池执行清理缓存的任务 executor.execute(cleanupRunnable)，cleanupRunnable 是一个 Runnable 实例，它在内部会调用 cleanup() 方法来清理无效的连接。

   <font color='red'>cleanup() 寻找一个闲置时间最长的连接：</font>

   遍历所有连接，寻找一个闲置时间最长的连接，然后根据该连接的闲置时长和最大允许的连接数量等参数来决定是否应该清理该连接。

   在从缓存的连接中取出连接来判断是否应该将其释放的时候使用到了两个变量：

   - maxIdleConnections 最大允许的闲置的连接数量，默认 5个；
   - keepAliveDurationNs 连接允许存活的最长的时间，默认 5分钟；



#### 六、其他

##### 1、addInterceptor() 和 addNetworkInterceptor() 的区别？

从 RealCall#getResponseWithInterceptorChain() 中可以看出，interceptors 处于第一位，而 networkInterceptors 处于倒数第二位。

调用的时机不同：

1. **interceptors** 是用于在**请求发送前**和**网络响应后**的拦截器；

   它只关心发送出去的请求和最终的响应结果，其他的一概不关心。

2. **networkInterceptors** 中可以获取到最终发送的请求 request，其中也包括重定向的数据，也可以获取到真正发生网络请求回来的 response 响应，从而修改对应的请求或者响应数据。

3. 除了CallServerInterceptor，每个拦截器都应该至少调用一次realChain.proceed方法。实际上在应用拦截器这层可以多次调用proceed方法（本地异常重试）或者不调用proceed方法（中断），但是网络拦截器这层连接已经准备好，可且仅可调用一次proceed方法。

4. 从使用场景看，应用拦截器因为只会调用一次，通常用于统计客户端的网络请求发起情况；而网络拦截器一次调用代表了一定会发起一次网络通信，因此通常可用于统计网络链路上传输的数据。



**Application interceptors**

- Don't need to worry about intermediate responses like redirects and retries. 不需要去关心中发生的重定向和重试操作。因为它处于第一个拦截器，会获取到最终的响应 response 。
- Are always invoked once, even if the HTTP response is served from the cache. 只会被调用一次，即使这个响应是从缓存中获取的。
- Observe the application's original intent. Unconcerned with -OkHttp-injected headers like If-None-Match. 只关注最原始的请求，不去关系请求的资源是否发生了改变，我只关注最后的 response 结果而已。
- Permitted to short-circuit and not call Chain.proceed(). 因为是第一个被执行的拦截器，因此它有权决定了是否要调用其他拦截，也就是 Chain.proceed() 方法是否要被执行。
- Permitted to retry and make multiple calls to Chain.proceed() 因为是第一个被执行的拦截器，因此它有可以多次调用 Chain.proceed() 方法，其实也就是相当与重新请求的作用了。

**Network Interceptors**

- Able to operate on intermediate responses like redirects and retries. 因为 NetworkInterceptor 是排在第 6 个拦截器中，因此可以操作经过 RetryAndFollowup 进行失败重试或者重定向之后得到的resposne。
- Not invoked for cached responses that short-circuit the network. 对于从缓存获取的 response 则不会去触发 NetworkInterceptor 。因为响应直接从 CacheInterceptor 返回了。
- Observe the data just as it will be transmitted over the network. 观察将在网络上传输的数据
- Access to the Connection that carries the request. 访问携带请求的连接





##### 2、超时

```kotlin
//OkHttpClient.kt
internal var callTimeout = 0//总超时时间
internal var connectTimeout = 10_000//连接超时
internal var readTimeout = 10_000//读取超时
internal var writeTimeout = 10_000//写入超时

private fun test() {
    val client: OkHttpClient = Builder()
            .connectTimeout((60 * 1000).toLong(), TimeUnit.MILLISECONDS)
            .readTimeout((5 * 60 * 1000).toLong(), TimeUnit.MILLISECONDS)
            .writeTimeout((5 * 60 * 1000).toLong(), TimeUnit.MILLISECONDS)
            .build()
}
```

- **CONNECT_TIMEOUT**：

  三次握手 + SSL建立耗时

  超时时间：默认10s

  测试方法：请求 www.facebook.com 触发 SocketTimeoutException 

- **READ_TIMEOUT**：
  连接建立后，从远端获取数据。TCP 传输

- **WRITE_TIMEOUT**：
  连接建立后，向远端发送数据。TCP 传输

- **CALL_TIMEOUT**：
  从发起到结束的总时长。其中不包括 UnknownHostException 情况

  UnknownHostException：

  测试方法：拔掉路由外部网线，但是保持设备与路由的连接。
  超时时间：20s



**耗时段介绍「从上往下」**

| 耗时操作     | 调用位置                                                     |
| ------------ | ------------------------------------------------------------ |
| DNS解析      | 「ConnectInterceptor」streamAllocation.newStream<br/>「RouteSelector」address.dns().lookup() |
| 连接时间     | 「ConnectInterceptor」streamAllocation.newStream<br/>「Platform」socket.connect() |
| 写入request  | 「CallServerInterceptor」httpCodec.writeRequestHeaders       |
| 服务器响应   | 「ConnectInterceptor」streamAllocation.newStream<br/>「RealConnection」socket.connect() |
| 读取response | 「CallServerInterceptor」httpCodec.readResponseHeaders       |



**4个超时设置「OkHttpClient.Builder」**

| api              | 简介                   | 生效机制                                                     |
| ---------------- | ---------------------- | ------------------------------------------------------------ |
| callTimeout()    | 整个流程耗费的超时时间 | RealCall.execute方法，设置进入<br/>AsyncTimeout + WatchDog实现 |
| connectTimeout() | 三次握手 + SSL建立耗时 | socket.connect(address, connectTimeout)                      |
| readTimeout()    | source读取耗时         | source.timeout(readTimeout)<br/>AsyncTimeout + WatchDog实现  |
| readTimeout()    | rawSocket读取耗时      | rawSocket.setSoTimeout(readTimeout)                          |
| writeTimeout     | sink写入耗时           | sink.timeout(writeTimeout)<br/>AsyncTimeout + WatchDog实现   |

**耗时操作之间的关联**

| route.requiresTunnel() | callTimeout = dns + connection + readTimeout + readTimeout + writeTimeout + 其它 |
| ---------------------- | ------------------------------------------------------------ |
| 无                     | callTimeout = dns + connectTime + readTimeout + 其它         |

- 基本耗时：dns + 三次握手耗时 + 服务器响应耗时
- 若有渠道，则增加 source.timeout().(readTimeout) + sink.timeout.(writeTimeout)





- connectTimeout 最终设置给了socket (确切的说应该是rawSocket)
- readTimeout 最终设置给了rawSocket 以及 在socket基础上创建的BufferedSource
- writeTimeout 最终设置给了在socket基础上创建的BufferedSink

> okhttp 底层基于 socket，所以 Timeout 自然也是设置给 Socket 的 connect/read/write。而 socket 是对于传输层的抽象，这里讨论的是 http，所以对 socket 设置各种 timeout 其实也就是对于 TCP 的 timeout 进行设置。





##### 3、OkHttp的优势？

- HTTP/2 支持允许所有访问同一主机的请求共享一个socket
- 支持Http1、Http2、Quic以及WebSocket；
- 连接池复用底层TCP(Socket)，减少请求延时；
- 无缝的支持GZIP减少数据流量；
- 缓存响应数据减少重复的网络请求；
- 请求失败自动重试主机的其他ip，自动重定向；



**http1 和 http2 的区别**：

HTTP/1.1的缺陷
1.高延迟--带来页面加载速度的降低
    网络延迟问题主要由于队头阻塞(Head-Of-Line Blocking),导致带宽无法被充分利用。

    队头阻塞是指当顺序发送的请求序列中的一个请求因为某种原因被阻塞时，在后面排队的所有请求也一并被阻塞，会导致客户端迟迟收不到数据。

2.无状态特性--带来的巨大HTTP头部
    由于报文Header一般会携带"User Agent""Cookie""Accept""Server"等许多固定的头字段（如下图），多达几百字节甚至上千字节，但Body却经常只有几十字节（比如GET请求、
    204/301/304响应），成了不折不扣的“大头儿子”。Header里携带的内容过大，在一定程度上增加了传输的成本。更要命的是，成千上万的请求响应报文里有很多字段值都是重复的，非常浪费。

3.明文传输--带来的不安全性
     HTTP/1.1在传输数据时，所有传输的内容都是明文，客户端和服务器端都无法验证对方的身份，这在一定程度上无法保证数据的安全性。

4.不支持服务器推送消息


HTTP/2 新特性
1.二进制传输
HTTP/2传输数据量的大幅减少,主要有两个原因:以二进制方式传输和Header 压缩。我们先来介绍二进制传输,HTTP/2 采用二进制格式传输数据，而非HTTP/1.x 里纯文本形式的报文 ，二进制协议解析起来更高效。 HTTP/2 将请求和响应数据分割为更小的帧，并且它们采用二进制编码。

2.Header 压缩
HTTP/2并没有使用传统的压缩算法，而是开发了专门的"HPACK”算法，在客户端和服务器两端建立“字典”，用索引号表示重复的字符串，还采用哈夫曼编码来压缩整数和字符串，可以达到50%~90%的高压缩率。

3.多路复用
在 HTTP/2 中引入了多路复用的技术。多路复用很好的解决了浏览器限制同一个域名下的请求数量的问题，同时也接更容易实现全速传输，毕竟新开一个 TCP 连接都需要慢慢提升传输速度。

4.Server Push
HTTP2还在一定程度上改变了传统的“请求-应答”工作模式，服务器不再是完全被动地响应请求，也可以新建“流”主动向客户端发送消息。比如，在浏览器刚请求HTML的时候就提前把可能会用到的JS、CSS文件发给客户端，减少等待的延迟，这被称为"服务器推送"（ Server Push，也叫 Cache push）

5.提高安全性
出于兼容的考虑，HTTP/2延续了HTTP/1的“明文”特点，可以像以前一样使用明文传输数据，不强制使用加密通信，不过格式还是二进制，只是不需要解密。



##### 4、网络优化

1. DNS 解析优化，分安全性和速度提升两方面。

   - 安全性

     防劫持

   - 速度

     - IP直连
     - DNS解析超时

2. 网络请求数据缓存，对于请求返回的数据需要缓存到本地数据库中。实际上，在某些场景中对于请求对象 Request 自身也需要做缓存操作。比如“发送埋点”的请求，这样请求失败就将其保存到本地数据库中，当 App 重启或者重新接收到连接网络的时候，重新尝试发送之前失败的请求。

3. 幂等性

   HTTP 方法的幂等性是指一次和多次请求某一个资源应该具有同样的副作用。举一个例子：当我们点外卖付款时，服务端扣款成功后发送给客户端一条扣款成功的消息，但是如果此时由于网络问题，客户端并没有成功接收到此消息，用户就有可能认为没有付款成功，甚至是尝试再次付款。

   幂等性就是为了解决这种问题，但是它属于代码设计层面的技巧，并不是一个实体方法或者开源库。



##### 5、断点续传

指的是在上传/下载时，将任务（一个文件或压缩包）人为的划分为几个部分，每一个部分采用一个线程进行上传/下载，如果碰到网络故障，可以从已经上传/下载的部分开始继续上传/下载未完成的部分，而没有必要从头开始上传/下载。可以节省时间，提高速度。

###### 5.1 Http 怎么支持断点续传的？

Http 1.1 协议中默认支持获取文件的部分内容，这其中主要是通过头部的两个参数：Range 和 Content Range 来实现的。客户端发请求时对应的是 Range ，服务器端响应时对应的是 Content-Range。

- **Range**

  客户端想要获取文件的部分内容，那么它就需要请求头部中的 Range 参数中指定获取内容的起始字节的位置和终止字节的位置，它的格式一般为：

  ```
  Range:(unit=first byte pos)-[last byte pos]
  
  Range: bytes=0-499      表示第 0-499 字节范围的内容 
  Range: bytes=500-999    表示第 500-999 字节范围的内容 
  Range: bytes=-500       表示最后 500 字节的内容 
  Range: bytes=500-       表示从第 500 字节开始到文件结束部分的内容 
  Range: bytes=0-0,-1     表示第一个和最后一个字节 
  Range: bytes=500-600,601-999 同时指定几个范围
  ```

- **Content-Range**

  在收到客户端中携带 Range 的请求后，服务器会在响应的头部中添加 Content Range 参数，返回可接受的文件字节范围及其文件的总大小。它的格式如下：

  ```
  Content-Range: bytes (unit first byte pos) - [last byte pos]/[entity legth]
  
  // 0－499 是指当前发送的数据的范围，而 22400 则是文件的总大小。
  Content-Range: bytes 0-499/22400
  ```

###### 5.2 使用断点续传和不使用断点续传的响应内容区别

- 不使用断点续传

```
HTTP/1.1 200 Ok
```

- 使用断点续传

```
HTTP/1.1 206 Partial Content
```



###### 5.3 处理请求资源发生改变的问题

在现实的场景中，服务器中的文件是会有发生变化的情况的，那么我们发起续传的请求肯定是失败的，那么为了处理这种服务器文件资源发生改变的问题，在 RFC2616 中定义了 **Last-Modified** 和  **Etag** 来判断续传文件资源是否发生改变。

- **Last-Modified & If-Modified-Since（文件最后修改时间）**

  **Last-Modified**：记录 Http 页面最后修改时间的 Http 头部参数，Last-Modified 是由服务端发送给客户端的

  **If-Modified-Since**：记录 Http 页面最后修改时间的 Http 头部参数，If-Modified-Since 是有客户端发送给服务端的

  验证过程

  - step 1：客户端缓存从服务端获取的页面
  - step 1：客户端访问相同页面时，客户端将服务器发送过来的  Last-Modified 通过 If-Modified-Since 发送给服务器
  - step 2：服务器通过客户端发送过来的 If-Modified-Since 进行判断客户端当前的缓存的页面是否为最新的
    - 如果不是最新的，那么就发送最新的页面给客户端
    - 如果是最新的，那么就发送 304 告诉客户端它本地缓存的页面是最新的

- **Etag & if-Range（文件唯一标志）**

  - Etag：作为文件的唯一标志，这个标志可以是文件的 hash 值或者是一个版本
  - if-Range：用于判断实体是否发生改变，如果实体未改变，服务器发送客户端丢失的部分，否则发送整个实体。一般格式：

  ```
  If-Range: Etag | HTTP-Date
  复制代码
  ```

  If-Range 可以使用 Etag 或者 Last-Modified 返回的值。当没有 ETage 却有 Last-modified 时，可以把 Last-modified 作为 If-Range 字段的值

  - 验证过程
    - step 1：客户端发起续传请求，头部包含 Range 和 if-Range 参数
    - step 2：服务器中收到客户端的请求之后，将客户端和服务器的 Etag 进行比对
      - 相等：请求文件资源没有发生变化，应答报文为 206
      - 不相等：请求文件资源发生变化，应答报文为 200



###### 5.4 检查服务器是否支持断点续传

几个关键信息：

- HTTP/1.1 206 Partial Content
- Content-Range: bytes 10-222/7877
- Etag: "1ec5-502264e2ae4c0"
- Last-Modified: Wed, 03 Sep 2014 10:00:27 GMT



###### 5.5 OkHttp 断点下载

**断点下载思路**

- step 1：判断检查本地是否有下载文件，若存在，则获取已下载的文件大小 downloadLength，若不存在，那么本地已下载文件的长度为 0
- step 2：获取将要下载的文件总大小（HTTP 响应头部的 content-Length)
- step 3：比对已下载文件大小和将要下载的文件总大小（contentLength），判断要下载的长度
- step 4：再即将发起下载请求的 HTTP 头部中添加即将下载的文件大小范围（Range: bytes = downloadLength - contentLength)
- setp5：得到响应后，通过 RandomAccessFile.seek(downloadLength)，跳过已下载的部分，进行写入；





##### 6、拦截器使用场景

- Log输出
- 增加公共请求参数
- 修改请求头
- 加密请求参数
- 服务器端错误码处理（时间戳异常为例）



##### 7、取消请求

- 可以通过对request的处理，设置tag，传入当前activity
- 然后在页面销毁时，进行取消请求
- 然后通过Dispatcher获取正在执行和等待执行的请求队列，如果tag相符，则call.cancel()



> 如果项目没有用mvp和[mvvm](https://so.csdn.net/so/search?q=mvvm&spm=1001.2101.3001.7020)这种框架自带解决网络请求内存泄漏，用的mvc处理内存泄漏的时候可以考虑rxjava解绑或直接取消okhttp的请求。

```java
//tag直接用LifecycleOwner最方便  
if (tag instanceof  LifecycleOwner){
    LifecycleOwner lifecycleOwner = (LifecycleOwner) tag;
    lifecycleOwner.getLifecycle().addObserver(new LifecycleEventObserver() {
        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_DESTROY){
                cancelTag(tag);
                lifecycleOwner.getLifecycle().removeObserver(this);
            }
        }
    });
}
 
//请求的时候记得传tag
Request request = new Request.Builder()
    .url(url)
    .post(RequestBody.create(mediaType, requestBody))
    .tag(tag)
    .build();
OKHttpClient.client().newCall(request).enqueue(new Callback() {
 
//页面destroy的时候调用
public void cancelTag(Object tag) {
    if (tag == null)
        return;
    for (Call call : OKHttpClient.client().dispatcher().queuedCalls()) {
        if (tag.equals(call.request().tag())) {
            call.cancel();
        }
    }
    for (Call call : OKHttpClient.client().dispatcher().runningCalls()) {
        if (tag.equals(call.request().tag())) {
            call.cancel();
        }
    }
}
```

##### 8、HTTPDNS

###### 8.1 DNS

**DNS（Domain Name System），它的作用就是根据域名，查出对应的 IP 地址**，它是 HTTP 协议的前提。只有将域名正确的解析成 IP 地址后，后面的 HTTP 流程才可以继续进行下去。

DNS 服务器的要求，一定是高可用、高并发和分布式的服务器。它被分为多个层次结构。

- 根 DNS 服务器：返回顶级域 DNS 服务器的 IP 地址。
- 顶级域 DNS 服务器：返回权威 DNS 服务器的 IP 地址。
- 权威 DNS 服务器：返回相应主机的 IP 地址。

当开始 DNS 解析的时候，如果 LocalDNS 没有缓存，那就会向 LocalDNS 服务器请求（通常就是运营商），如果还是没有，就会一级一级的，从根域名查对应的顶级域名，再从顶级域名查权威域名服务器，最后通过权威域名服务器，获取具体域名对应的 IP 地址。

DNS 在提供域名和 IP 地址映射的过程中，其实提供了很多基于域名的功能，例如服务器的负载均衡，但是它也带来了一些问题。

![img](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2019/3/25/169b2d9e6e29ed1c~tplv-t2oaga2asx-zoom-in-crop-mark:1304:0:0:0.awebp)

###### 8.2 DNS问题

- **不稳定**

  DNS劫持或者故障，导致服务不可用；

- **不准确**

  **LocalDNS 调度，并不一定是就近原则**，某些小运营商没有 DNS 服务器，直接调用其他运营商的 DNS 服务器，最终直接跨网传输。例如：用户侧是移动运营商，调度到了电信的 IP，造成访问慢，甚至访问受限等问题。

- **不及时**

  运营商可能会修改 DNS 的 TTL(Time-To-Live，DNS 缓存时间)，导致 DNS 的修改，延迟生效。

  还有运营商为了保证网内用户的访问质量，同时减少跨网结算，运营商会在网内搭建内容缓存服务器，通过把域名强行指向内容缓存服务器的地址，来实现本地本网流量完全留在本地的目的。

  ![img](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2019/3/25/169b2d9e6e37434e~tplv-t2oaga2asx-zoom-in-crop-mark:1304:0:0:0.awebp)

###### 8.3 HTTPDNS 的解决方案

DNS 不仅支持 UDP，它还支持 TCP，但是大部分标准的 DNS 都是基于 UDP 与 DNS 服务器的 53 端口进行交互。

HTTPDNS 则不同，顾名思义它是利用 HTTP 协议与 DNS 服务器进行交互。不走传统的 DNS 解析，从而绕过运营商的 LocalDNS 服务器，有效的防止了域名劫持，提高域名解析的效率。

![img](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2019/3/25/169b2d9e6e6e0269~tplv-t2oaga2asx-zoom-in-crop-mark:1304:0:0:0.awebp)

这就相当于，每家各自基于 HTTP 协议，自己实现了一套域名解析，自己去维护了一份域名与 IP 的地址簿，而不是使用同一的地址簿（DNS服务器）。



###### 8.4 OkHttp 接入 HTTPDNS

在 OkHttp 中使用 HTTPDNS，有两种方式。

1. 通过拦截器，在发送请求之前，将域名替换为 IP 地址。

   - 问题：

     HTTPS 下 IP 直连的证书问题、代理的问题、Cookie 的问题等等。

     其中最严重的问题是，此方案（拦截器+HTTPDNS）遇到 https 时，如果存在一台服务器支持多个域名，可能导致证书无法匹配的问题。

   - 解决方案：

     针对 "domain 不匹配" 的问题，可以通过 hook 证书验证过程中的第二步，将 IP 直接替换成原来的域名，再执行证书验证。

     而 HttpURLConnect，提供了一个 HostnameVerifier 接口，实现它即可完成替换。

2. 通过 OkHttp 提供的 `.dns()` 接口，配置 HTTPDNS。

   这样做的好处在于：

   - 还是用域名进行访问，只是底层 DNS 解析换成了 HTTPDNS，以确保解析的 IP 地址符合预期。
   - HTTPS 下的问题也得到解决，证书依然使用域名进行校验。