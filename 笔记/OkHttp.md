### OkHttp

参考资料：

https://juejin.cn/post/6844904133103747086

https://juejin.cn/post/6909445385266135048

https://juejin.cn/post/6881436122950402056

#### 一、请求的大致流程

##### 1、构建 OkHttpClient

##### 2、构建 Request

##### 3、Post 构建请求体 RequestBody

##### 4、构建 Call

RealCall#newRealCall()

##### 5、执行 Call

1. 同步执行 execute()

   - 通过 dispatcher 将请求 Call 加入一个双端队列

   - 通过 getResponseWithInterceptorChain() 获取响应

   - 通知 dispatcher 结束请求，将 Call 从队列中移除

     调用 Dispatcher#finished()，该方法中会将完成的任务从队列中移除，并将 readyAsyncCalls 等待队列中的请求移入正在执行的异步队列 runningAsyncCalls 中，交由线程池执行。

2. 异步执行 enqueue()



#### 二、分发器 Dispatcher

作用是对异步请求进行分发。

```java
Policy on when async requests are executed.
关于何时执行异步请求的策略。

public final class Dispatcher {
    private int maxRequests = 64;
    private int maxRequestsPerHost = 5;
    private @Nullable Runnable idleCallback;

    /** Executes calls. Created lazily. */
    private @Nullable ExecutorService executorService;

    /** Ready async calls in the order they'll be run. */
    private final Deque<AsyncCall> readyAsyncCalls = new ArrayDeque<>();

    /** Running asynchronous calls. Includes canceled calls that haven't finished yet. */
    private final Deque<AsyncCall> runningAsyncCalls = new ArrayDeque<>();

    /** Running synchronous calls. Includes canceled calls that haven't finished yet. */
    private final Deque<RealCall> runningSyncCalls = new ArrayDeque<>();
}
```



RealCall#enqueue()：

```java
client.dispatcher().enqueue(new AsyncCall(responseCallback));
```

AsyncCall 间接继承自 Runnable，在 Runnable 的 run() 中执行 AsyncCall 的 execute()

```java
synchronized void enqueue(AsyncCall call) {
    if (runningAsyncCalls.size() < maxRequests && runningCallsForHost(call) < maxRequestsPerHost) {
        runningAsyncCalls.add(call);
        executorService().execute(call);//使用线程池执行该请求
    } else {
        readyAsyncCalls.add(call);
    }
}

//CacheThreadPool类型，线程数量不定，只有非核心线程，适合执行大量的耗时较少的任务。
public synchronized ExecutorService executorService() {
    if (executorService == null) {
        executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Dispatcher", false));
    }
    return executorService;
}
```



#### 三、责任链的执行过程

```java
public interface Interceptor {
  Response intercept(Chain chain) throws IOException;

  interface Chain {
    Response proceed(Request request) throws IOException;
  }
}
```

RealCall#getResponseWithInterceptorChain()

```java
Response getResponseWithInterceptorChain() throws IOException {
    // Build a full stack of interceptors.
	List<Interceptor> interceptors = new ArrayList<>();
    interceptors.addAll(client.interceptors());
    interceptors.add(retryAndFollowUpInterceptor);
    interceptors.add(new BridgeInterceptor(client.cookieJar()));
    interceptors.add(new CacheInterceptor(client.internalCache()));
    interceptors.add(new ConnectInterceptor(client));
    if (!forWebSocket) {
      interceptors.addAll(client.networkInterceptors());
    }
    interceptors.add(new CallServerInterceptor(forWebSocket));

    Interceptor.Chain chain = new RealInterceptorChain(interceptors, null, null, null, 0,
        originalRequest, this, eventListener, client.connectTimeoutMillis(),
        client.readTimeoutMillis(), client.writeTimeoutMillis());

    return chain.proceed(originalRequest);
}
```

注意点：

1. 是当创建一个责任链 `RealInterceptorChain` 的时候，我们传入的第 5 个参数是 0。该参数名为 `index`，会被赋值给 `RealInterceptorChain` 实例内部的同名全局变量。
2. 当启用责任链的时候，会调用它的 `proceed(Request)` 方法。



RealInterceptorChain#proceed()

```java
public Response proceed(Request request, StreamAllocation streamAllocation, HttpCodec httpCodec,
      RealConnection connection) throws IOException {
    if (index >= interceptors.size()) throw new AssertionError();
	// ...
    // Call the next interceptor in the chain.
    RealInterceptorChain next = new RealInterceptorChain(interceptors, streamAllocation, httpCodec, connection, index + 1, request, call, eventListener, connectTimeout, readTimeout, writeTimeout);
    Interceptor interceptor = interceptors.get(index);
    Response response = interceptor.intercept(next);
	// ...
    return response;
}
```



#### 四、拦截器

##### 1、RetryAndFollowUpInterceptor：重试和重定向

- RetryAndFollowUpInterceptor 主要用来当请求失败的时候进行重试，以及在需要的情况下进行重定向。如果在创建 OkHttpClient 时没有加入自定义拦截器，它就是责任链中最先被调用的拦截器。

- RetryAndFollowUpInterceptor#intercept() 中有一个 white 循环，根据服务器返回的 response 是否需要重定向或者重试返回一个新的 request，如果这个 request 为空，则直接返回 response，否则进行循环，再次调用 chain.proceed()。

- 在 intercept() 中，主要根据错误的信息做一些处理，会根据服务器返回的信息判断这个请求是否可以重定向，或者是否有必要进行重试。如果值得去重试就会新建或者复用之前的连接在下一次循环中进行请求重试，否则就将得到的请求包装之后返回给用户。

- StreamAllocation，它相当于一个管理类，维护了服务器连接、并发流和请求之间的关系，该类还会初始化一个 Socket 连接对象，获取输入/输出流对象。

- 创建 StreamAllocation 对象时，传入了一个连接池对象 ConnectionPool。这里只是初始化这些类，目的是传递给下面的拦截器来从服务器中获取请求的响应。

  ```java
  StreamAllocation streamAllocation = new StreamAllocation(client.connectionPool(),
          createAddress(request.url()), call, eventListener, callStackTrace);
  ```



##### 2、BridgeInterceptor

桥拦截器，用于从用户的请求中构建网络请求，然后使用该请求访问网络，最后将网络响应转换成用户友好的响应。

在Request阶段配置用户信息，并添加一些请求头。在Response阶段，进行gzip解压。



##### 3、CacheInterceptor

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



##### 4、ConnectInterceptor

```java
@Override public Response intercept(Chain chain) throws IOException {
	RealInterceptorChain realChain = (RealInterceptorChain) chain;
	Request request = realChain.request();
	StreamAllocation streamAllocation = realChain.streamAllocation();

	// We need the network to satisfy this request. Possibly for validating a conditional GET.
	boolean doExtensiveHealthChecks = !request.method().equals("GET");
    //HttpCodec 用来编码请求并解码响应，RealConnection 用来向服务器发起连接。它们会在下一个拦截器中被用来从服务器中获取响应信息。
	HttpCodec httpCodec = streamAllocation.newStream(client, chain, doExtensiveHealthChecks);
	RealConnection connection = streamAllocation.connection();

	return realChain.proceed(request, streamAllocation, httpCodec, connection);
}
```

用来打开到指定服务器的网络连接，交给下一个拦截器处理。

在获取连接对象的时候，使用了连接池 ConnectionPool 来复用连接。

获取连接对象：

1. StreamAllocation#newStream() 返回一个 HttpCodec 对象

2. findHealthyConnection()

3. findConnection() ，这个方法在一个 white 循环中不停调用直到得到一个可用的连接

   返回一个 RealConnection 连接对象：

   - 判断当前的连接是否可以使用：流是否已经被关闭，并且已经被限制创建新的流；
   - 如果当前的连接无法使用，就从连接池中获取一个连接；
   - 连接池中也没有发现可用的连接，创建一个新的连接，并进行握手，然后将其放到连接池中。

   连接复用的一个好处就是：

   ​	<font color='red'>省去了进行 TCP 和 TLS 握手的一个过程。</font>因为建立连接本身也是需要消耗一些时间的。

4. acquire() 赋值给成员变量 connection

   从 ConnectionPool 连接池中获取 connection 后，也会调用此方法进行赋值



<font color='red'>RealConnection 是 OkHttp 实际建立连接的地方，在 connect() 中追踪代码，最终是通过 socket 与服务器建立连接的。</font>



##### 5、CallServerInterceptor

服务器请求拦截器 CallServerInterceptor 用来向服务器发起请求并获取数据。这是整个责任链的最后一个拦截器，拿到响应后直接返回给上一级的拦截器，不用再调用 proceed() 方法。

这个拦截器里会进行 IO 操作与服务器交互，底层使用了 OKIO 来进行 IO 操作。

1. 根据 Request 的配置写入请求头
2. 根据 Method 判断是否支持请求体，如果支持则尝试写入请求体并发送请求报文，否则直接发送
3. 读取响应头，构建 Response
4. 读取响应体，为 Response 写入 ResponseBody
5. 判断是否要关闭连接
6. 返回 Response



##### 6、ConnectionPool 连接池

OkHttp 的连接池使用一个双端队列来缓存已经创建的连接：

```java
private final Deque<RealConnection> connections = new ArrayDeque<>();

//这个方法在 StreamAllocation#findConnection() 中创建一个连接后，存入连接池
void put(RealConnection connection) {
    assert (Thread.holdsLock(this));
    if (!cleanupRunning) {
        cleanupRunning = true;
        //使用线程池执行清理任务
        executor.execute(cleanupRunnable);
    }
    //将新建的连接插入到双端队列中
    connections.add(connection);
}

private static final Executor executor = new ThreadPoolExecutor(0 /* corePoolSize */,
      Integer.MAX_VALUE /* maximumPoolSize */, 60L /* keepAliveTime */, TimeUnit.SECONDS,
      new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp ConnectionPool", true));
```

OkHttp 的缓存管理分成两个步骤：

1. 当我们创建了一个新的连接的时候，通过调用双端队列的 add() 方法，将其加入到队列中；

2. 清理连接缓存的操作由线程池来定时执行；

   每当向连接池插入一个连接之前都会调用线程池执行清理缓存的任务 executor.execute(cleanupRunnable)，cleanupRunnable 是一个 Runnable 实例，它在内部会调用 cleanup() 方法来清理无效的连接。

   <font color='red'>cleanup() 寻找一个闲置时间最长的连接：</font>

   遍历所有连接，寻找一个闲置时间最长的连接，然后根据该连接的闲置时长和最大允许的连接数量等参数来决定是否应该清理该连接。

   在从缓存的连接中取出连接来判断是否应该将其释放的时候使用到了两个变量：

   - maxIdleConnections 最大允许的闲置的连接数量，默认 5个；keepAliveDurationNs 连接允许存活的最长的时间，默认 5分钟；



##### 7、addInterceptor() 和 addNetworkInterceptor() 的区别？

从 RealCall#getResponseWithInterceptorChain() 中可以看出，interceptors 处于第一位，而 networkInterceptors 处于倒数第二位。

调用的时机不同：

1、interceptors 是用于在请求发送前和网络响应后的拦截器；

它只关心发送出去的请求和最终的响应结果，其他的一概不关心。

2、networkInterceptors 中可以获取到最终发送的请求 request，其中也包括重定向的数据，也可以获取到真正发生网络请求回来的 response 响应，从而修改对应的请求或者相应数据。

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





##### 8、超时

```kotlin
internal var connectTimeout = 10_000
internal var readTimeout = 10_000
internal var writeTimeout = 10_000
```



- connectTimeout 最终设置给了socket (确切的说应该是rawSocket)
- readTimeout 最终设置给了rawSocket 以及 在socket基础上创建的BufferedSource
- writeTimeout 最终设置给了在socket基础上创建的BufferedSink

> okhttp 底层基于 socket，所以 Timeout 自然也是设置给 Socket 的 connect/read/write。而 socket 是对于传输层的抽象，这里讨论的是 http，所以对 socket 设置各种 timeout 其实也就是对于 TCP 的 timeout 进行设置。





##### 9、OkHttp的优势？

- HTTP/2 支持允许所有访问同一主机的请求共享一个socket
- 利用连接池减少请求延迟（如果HTTP/2不可用）
- 支持GZIP压缩
- 响应缓存减少重复请求



##### 10、网络优化

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