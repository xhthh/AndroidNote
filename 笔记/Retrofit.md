## Retrofit

### 一、基本使用流程

#### 1、定义 HTTP API，用于描述请求

```java
public interface GitHubService {
     @GET("users/{user}/repos")
     Call<List<Repo>> listRepos(@Path("user") String user);
}
//注解表示请求的接口部分，返回类型是请求的返回值类型，方法的参数即是请求的参数
```

#### 2、创建Retrofit并生成API的实现

```java
// 1.Retrofit构建过程
Retrofit retrofit = new Retrofit.Builder()
.baseUrl("https://api.github.com/")
.build();

// 2.创建网络请求接口类实例过程
GitHubService service = retrofit.create(GitHubService.class);
```

#### 3、调用 API 方法，生成 Call，执行请求

```java
// 3.生成并执行请求过程
Call<List<Repo>> repos = service.listRepos("octocat");
repos.execute() or repos.enqueue()
```

#### 4、Retrofit 框架中涉及到的设计式

```
1.Retrofit构建过程 
建造者模式、工厂方法模式

2.创建网络请求接口实例过程
外观模式、代理模式、单例模式、策略模式、装饰模式（建造者模式）

3.生成并执行请求过程
适配器模式（代理模式、装饰模式）
```

### 二、Retrofit 构建过程

#### 1、Retrofit 核心对象解析

首先Retrofit中有一个全局变量非常关键，在V2.5之前的版本，使用的是LinkedHashMap()，它是一个网络请求配置对象，是由网络请求接口中方法注解进行解析后得到的。

```java
//Retrofit.java
//网络请求配置对象，存储网络请求相关的配置，如网络请求的方法、数据转换器、网络请求适配器、网络请求工厂、基地址等
private final Map<Method, ServiceMethod<?>> serviceMethodCache = new ConcurrentHashMap<>();
```

Retrofit使用了建造者模式通过内部类Builder类建立一个Retrofit实例，如下：

```java
public static final class Builder {
    // 平台类型对象（Platform -> Android)
    private final Platform platform;
    // 网络请求工厂，默认使用OkHttpCall（工厂方法模式）
    private @Nullable okhttp3.Call.Factory callFactory;
    // 网络请求的url地址
    private @Nullable HttpUrl baseUrl;
    // 数据转换器工厂的集合
    private final List<Converter.Factory> converterFactories = new ArrayList<>();
    // 网络请求适配器工厂的集合，默认是ExecutorCallAdapterFactory
    private final List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
    // 回调方法执行器，在 Android 上默认是封装了 handler 的 MainThreadExecutor, 默认作用是：切换线程（子线程 -> 主线程）
    private @Nullable Executor callbackExecutor;
    // 一个开关，为true则会缓存创建的ServiceMethod
    private boolean validateEagerly;
}
```

#### 2、Retrofit 内部构造

在Builder内部构造时设置了默认Platform、callAdapterFactories和callbackExecutor。

> Platform 里有个内部类 MainThreadExecutor，其中使用 Handler 切换线程，将子线程中获取到的数据返回 UI 线程；

#### 3、添加 baseUrl

就是将String类型的url转换为OkHttp的HttpUrl，如下：

```java
//Retrofit.java
/**
 * Set the API base URL.
 *
 * @see #baseUrl(HttpUrl)
 */
public Builder baseUrl(String baseUrl) {
    Objects.requireNonNull(baseUrl, "baseUrl == null");
    return baseUrl(HttpUrl.get(baseUrl));
}

public Builder baseUrl(HttpUrl baseUrl) {
    Objects.requireNonNull(baseUrl, "baseUrl == null");
    List<String> pathSegments = baseUrl.pathSegments();
    if (!"".equals(pathSegments.get(pathSegments.size() - 1))) {
        throw new IllegalArgumentException("baseUrl must end in /: " + baseUrl);
    }
    this.baseUrl = baseUrl;
    return this;
}
```

#### 4、添加GsonConverterFactory

- GsonConverterFactory.create() 中创建了一个 Gson 实例传给 GsonConverterFactory 的构造方法，create() 方法在构建 Retrofit 实例时调用 Builder#addConvertFactory() 传入；
- addConvertFactory() 这一步是将一个含有Gson对象实例的GsonConverterFactory放入到了数据转换器工厂converterFactories里。



#### 5、build 过程

Retrofit#Builder#build() 方法中创建了 Retrofit 实例，并将六大核心对象配置到 Retrofit 对象中；

```java
final okhttp3.Call.Factory callFactory;
final HttpUrl baseUrl;
final List<Converter.Factory> converterFactories;
final List<CallAdapter.Factory> callAdapterFactories;
final @Nullable Executor callbackExecutor;
final boolean validateEagerly;
```

### 三、创建网络请求接口实例过程

```java
GitHubService service = retrofit.create(GitHubService.class);
```

#### 1、Retrofit#create()

```java
public <T> T create(final Class<T> service) {
  //判断是否需要提前缓存ServiceMethod对象
  validateServiceInterface(service);
  //使用动态代理拿到请求接口所有注解配置后，创建网络请求接口实例
  return (T)
	  Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] {service},
		  new InvocationHandler() {
		    private final Platform platform = Platform.get();
		    private final Object[] emptyArgs = new Object[0];

		    @Override
		    public @Nullable Object invoke(Object proxy, Method method, @Nullable Object[] args)
			  throws Throwable {
			  // If the method is a method from Object then defer to normal invocation.
			  if (method.getDeclaringClass() == Object.class) {
			    return method.invoke(this, args);
			  }
			  args = args != null ? args : emptyArgs;
			  return platform.isDefaultMethod(method)
				  ? platform.invokeDefaultMethod(method, service, proxy, args)
				  : loadServiceMethod(method).invoke(args);
		  	}
		  });
}


ServiceMethod<?> loadServiceMethod(Method method) {
    ServiceMethod<?> result = serviceMethodCache.get(method);
    if (result != null) return result;

    synchronized (serviceMethodCache) {
      result = serviceMethodCache.get(method);
      if (result == null) {
        //解析注解配置得到了ServiceMethod
        result = ServiceMethod.parseAnnotations(this, method);
        //最终加入到ConcurrentHashMap缓存中
        serviceMethodCache.put(method, result);
      }
    }
    return result;
}
```

动态代理

> ```
> Proxy.newProxyInstance最终会调用一个 native 方法，动态生成一个 ApiService（我们前面声明的接口）的实现类；
> newProxyInstance 方法中会传入一个 InvocationHandler 对象，即源码中的 new InvocationHandler() {...}
> 在Java动态生成的ApiService的实现类中，有我们定义的各种api调用方法，其中，会调用 super.h.invoke(this,m3,null)，
> 就是调用父类的h的invoke()，它的父类是Proxy，h即上面我们传入的 InvocationHandler对象。
> 
> 最后就是源码中 invoke()中的代码，serviceMethod之类获取注解信息，方法参数等，然后构建一个OkHttpCall
> ```
>
> ![img](https://upload-images.jianshu.io/upload_images/782269-fb29059f8e16d0fe.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/646/format/webp)





#### 2、ServiceMethod

```java
abstract class ServiceMethod<T> {
  static <T> ServiceMethod<T> parseAnnotations(Retrofit retrofit, Method method) {
    //通过RequestFactory解析注解配置（工厂模式、内部使用了建造者模式）
    RequestFactory requestFactory = RequestFactory.parseAnnotations(retrofit, method);

    Type returnType = method.getGenericReturnType();
    //最终是通过HttpServiceMethod构建的请求方法
    return HttpServiceMethod.parseAnnotations(retrofit, method, requestFactory);
  }

  abstract @Nullable T invoke(Object[] args);
}
```

#### 3、请求构造核心流程

根据RequestFactory#Builder构造方法和parseAnnotations方法的源码，可知的它的作用就是用来解析注解配置的。

```java
public interface GitHubService {
     @GET("users/{user}/repos")
     Call<List<Repo>> listRepos(@Path("user") String user);
}

//RequestFactory#Builder
Builder(Retrofit retrofit, Method method) {
    this.retrofit = retrofit;
    this.method = method;
    //获取网络请求接口方法里的注解，比如 @GET("users/{user}/repos")
    this.methodAnnotations = method.getAnnotations();
    //获取网络请求接口方法里的参数类型，比如 List<Repo>
    this.parameterTypes = method.getGenericParameterTypes();
    //获取网络请求接口方法里的注解内容，比如 @Path("user")
    this.parameterAnnotationsArray = method.getParameterAnnotations();
}
```

##### 3.1 HttpServiceMethod.parseAnnotations() 内部流程

```java
static <ResponseT, ReturnT> HttpServiceMethod<ResponseT, ReturnT> parseAnnotations(
      Retrofit retrofit, Method method, RequestFactory requestFactory) {
    //接口方法中的注解，比如 @GET
    Annotation[] annotations = method.getAnnotations();
    
    //网络请求接口里方法的返回值类型 Call<List<Repo>>
    Type adapterType;
    if (isKotlinSuspendFunction) {
      //....
    } else {
      adapterType = method.getGenericReturnType();
    }

    //DefaultCallAdapterFactory，其中 executor 为 MainThreadExecutor
    CallAdapter<ResponseT, ReturnT> callAdapter =
        createCallAdapter(retrofit, method, adapterType, annotations);
    
    //返回类型，就是接口方法中的泛型类，即接口实体，List<Repo>
    Type responseType = callAdapter.responseType();
    
    //根据网络请求接口方法的返回值和注解类型从Retrofit对象中获取对应的数据转换器
	//GsonResponseBodyConvert
    Converter<ResponseBody, ResponseT> responseConverter =
        createResponseConverter(retrofit, method, responseType);

    //OkhttpClient，在构建 Retrofit 中可以配置，比如自定义 OkHttpClient 配置拦截器等
    okhttp3.Call.Factory callFactory = retrofit.callFactory;
    
    //CallAdapter 继承自 HttpServiceMethod
    if (!isKotlinSuspendFunction) {
      return new CallAdapted<>(requestFactory, callFactory, responseConverter, callAdapter);
    }
}
```

- parseAnnotations() 返回的 CallAdapted 继承自 HttpServiceMethod，此处用了代理模式
- 当 Retrofit.create() 中的

##### 3.2  createCallAdapter(retrofit, method, returnType, annotations)

```java
//HttpServiceMethod.java
private static <ResponseT, ReturnT> CallAdapter<ResponseT, ReturnT> createCallAdapter(
      Retrofit retrofit, Method method, Type returnType, Annotation[] annotations) {
  //returnType 即网络请求接口里方法的返回值类型，Call<List<Repo>>
  //annotations 即网络请求接口方法的注解，@GET("")
  return (CallAdapter<ResponseT, ReturnT>) retrofit.callAdapter(returnType, annotations);
}

//Retrofit.java
public CallAdapter<?, ?> callAdapter(Type returnType, Annotation[] annotations) {
  return nextCallAdapter(null, returnType, annotations);
}

public CallAdapter<?, ?> nextCallAdapter(
    @Nullable CallAdapter.Factory skipPast, Type returnType, Annotation[] annotations) {
    Objects.requireNonNull(returnType, "returnType == null");
    Objects.requireNonNull(annotations, "annotations == null");

    int start = callAdapterFactories.indexOf(skipPast) + 1;
    //遍历 CallAdapter.Factory 集合寻找合适的工厂
    //通过方法的返回值类型与注解信息来找到匹配的CallAdapter
    for (int i = start, count = callAdapterFactories.size(); i < count; i++) {
      CallAdapter<?, ?> adapter = callAdapterFactories.get(i).get(returnType, annotations, this);
      if (adapter != null) {
        return adapter;
      }
    }
}
```



```java
//DefaultCallAdapterFactory.java
@Override
public @Nullable CallAdapter<?, ?> get(
    Type returnType, Annotation[] annotations, Retrofit retrofit) {
    if (getRawType(returnType) != Call.class) {
        return null;
    }
    if (!(returnType instanceof ParameterizedType)) {
        throw new IllegalArgumentException(
            "Call return type must be parameterized as Call<Foo> or Call<? extends Foo>");
    }
    final Type responseType = Utils.getParameterUpperBound(0, (ParameterizedType) returnType);

    final Executor executor =
        Utils.isAnnotationPresent(annotations, SkipCallbackExecutor.class)
        ? null
        : callbackExecutor;

    return new CallAdapter<Object, Call<?>>() {
        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public Call<Object> adapt(Call<Object> call) {
            return executor == null ? call : new ExecutorCallbackCall<>(executor, call);
        }
    };
}
```



##### 3.3 createResponseConverter(retrofit, method, responseType)

```java
//HttpServiceMethod.java
private static <ResponseT> Converter<ResponseBody, ResponseT> createResponseConverter(
   Retrofit retrofit, Method method, Type responseType) {
  Annotation[] annotations = method.getAnnotations();
  try {
    return retrofit.responseBodyConverter(responseType, annotations);
  }
}

//Retrofit.java
public <T> Converter<ResponseBody, T> responseBodyConverter(Type type, Annotation[] annotations) {
    return nextResponseBodyConverter(null, type, annotations);
}

public <T> Converter<ResponseBody, T> nextResponseBodyConverter(
      @Nullable Converter.Factory skipPast, Type type, Annotation[] annotations) {
    Objects.requireNonNull(type, "type == null");
    Objects.requireNonNull(annotations, "annotations == null");

    int start = converterFactories.indexOf(skipPast) + 1;
    //遍历 Converter.Factory 集合并寻找合适的工厂, 这里是GsonResponseBodyConverter
    //通过转换类型与注解信息，遍历converterFactories列表，找到匹配的Converter，如果找不到则抛出IllegalArgumentException异常。
    for (int i = start, count = converterFactories.size(); i < count; i++) {
      Converter<ResponseBody, ?> converter =
          converterFactories.get(i).responseBodyConverter(type, annotations, this);
      if (converter != null) {
        //noinspection unchecked
        return (Converter<ResponseBody, T>) converter;
      }
    }
}
```

> 当 serviceMethod.invoke() 方法调用时，在 HttpServiceMethod#invoke() 中将 `responseConverter` 作为构建 OkHttpCall 的参数传入，在 OkHttpCall#parseResponse() 中 通过 `responseConverter` 将数据转换成用户定义的响应类型。

##### 3.4 最终执行 httpServiceMethod.invoke() 方法

- Retrofit#create() 中 InvocationHandler 回调的 invoke() 方法中，loadServiceMethod(method) 最终返回了 CallAdapted，其继承自 HttpServiceMethod；

- 调用 serviceMethod.invoke()

  ```java
  //HttpServiceMethod.java
  @Override
  final @Nullable ReturnT invoke(Object[] args) {
      Call<ResponseT> call = new OkHttpCall<>(requestFactory, args, callFactory, responseConverter);
      return adapt(call, args);
  }
  protected abstract @Nullable ReturnT adapt(Call<ResponseT> call, Object[] args);
  
  //HttpServiceMethod#CallAdapted.java
  
  static final class CallAdapted<ResponseT, ReturnT> extends HttpServiceMethod<ResponseT, ReturnT> {
    private final CallAdapter<ResponseT, ReturnT> callAdapter;
  
    CallAdapted(
        RequestFactory requestFactory,
        okhttp3.Call.Factory callFactory,
        Converter<ResponseBody, ResponseT> responseConverter,
        CallAdapter<ResponseT, ReturnT> callAdapter) {
      super(requestFactory, callFactory, responseConverter);
      this.callAdapter = callAdapter;
    }
    @Override
    protected ReturnT adapt(Call<ResponseT> call, Object[] args) {
      //这里的 callAdapter 即网络请求适配器，默认的是 DefaultCallAdapterFactory
      return callAdapter.adapt(call);
    }
  }
  ```

- 走到了 CallAdapter#adapt(call)

  默认 DefaultCallAdapterFactory

  ```java
  @Override
  public Call<Object> adapt(Call<Object> call) {
    return executor == null ? call : new ExecutorCallbackCall<>(executor, call);
  }
  ```

- 上边 adapt() 返回了 ExecutorCallbackCall，它是一个装饰者，继承自 Call，持有两个变量：

  - callbackExecutor

    用于将 OkHttp 请求返回的数据切回 UI 线程；

  - delegate

    真正去做网络请求的 okHttpCall

- 此时，调用 Retrofit.create() 通过动态代理得到了 ApiService，即网络请求接口实例；

- 调用 apiService.getXXX() 得到一个 Call，调用其 enqueue()，异步请求，会走到 delegate.enqueu() 中，当 OkHttp 请求完成，得到响应后，通过 callbackExecutor 将数据切回 UI 线程，最终走到我们请求接口的回调当中；

```java
public interface CallAdapter<R, T> {
  //接口实体，即 Call<Repo> 中的 Repo
  Type responseType();

  //返回一个委托调用的实例，比如 DefaultCallAdapterFactory 中的 ExecutorCallbackCall，它持有 MainThreadExecutor 和 delegate，MainThreadExecutor 是用来切换线程的，delegate 是 真正用来请求的 OkHttpCall
  T adapt(Call<R> call);

  
  abstract class Factory {
    //创建并返回 CallAdapter 实例的方法
    public abstract @Nullable CallAdapter<?, ?> get(
        Type returnType, Annotation[] annotations, Retrofit retrofit);
  }
}
```



#### 四、创建网络请求接口实例，并执行请求过程

#### 1、service.listRepos()

```java
Call<List<Repo>> repos = service.listRepos("octocat");
```

service对象是动态代理对象Proxy.newProxyInstance()，当调用getCall()时会被它拦截，然后调用自身的InvocationHandler#invoke()，得到最终的Call对象。

#### 2、同步执行流程 repos.execute()



#### 3、异步执行流程 repos.enqueue()

- 使用静态代理 delegate进行异步请求
  - 创建OkHttp的Request对象，再封装成OkHttp.call
  - 方法同发送同步请求
- 线程切换，在主线程显示结果



### 五、整体流程

![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/7ab58d6242cc4fae96cd73eb198e325a~tplv-k3u1fbpfcp-zoom-in-crop-mark:1304:0:0:0.awebp)

Retrofit 充当一个适配器的角色，将Java接口翻译成http请求，然后通过OkHttp去发送这个请求：

- 定义一个接口，里面是各种通过注解和参数描述的API调用方法。

- Retrofit#create()

  - Proxy.newProxyInstance最终会调用一个 native 方法，动态生成一个 ApiService（我们前面声明的接口）的实现类；

  - newProxyInstance 方法中会传入一个 InvocationHandler 对象，即源码中的 new InvocationHandler() {...}；

  - 在Java动态生成的ApiService的实现类中，有我们定义的各种api调用方法，其中，会调用 super.h.invoke(this,m3,null)，就是调用父类的h的invoke()，它的父类是Proxy，h即上面我们传入的 InvocationHandler对象；

  - 最后就是源码中 invoke()中的代码，serviceMethod 获取注解信息，方法参数等，然后构建一个OkHttpCall，用于请求网络，其中还有 mainThreadExecutor 用于将数据切回主线程；

  - create方法最终会返回一个Proxy.newProxyInstance代理出来的对象，被代理者就是我们传进去的泛型，在这里就是我们定义的接口 ApiService。里面的InvocationHandler的invoke方法为我们返回一个Call对象，如果设置的是 RxJavaCallAdapterFactory 的话，返回的就是 Observable 对象。

    程序执行到这里的时候，还不会去回调invoke方法。

  - 当使用接口代理对象调用接口中定义的方法时，才会执行 InvokecationHandler 中的 invoke()，为我们生成了具有Call接口类型的具体对象。



### 六、其他

#### 1、Retrofit 优势

**OkHttp**：

- OkHttp主要负责socket部分的优化，比如多路复用，buffer缓存，数据压缩等等；
- okHttp消息回来需要切回主线程，需要自己处理；
- 调用比较复杂，需要自己进行封装；

**Retrofit**：

- Retrofit主要负责应用层面的封装，就是说主要面向开发者，方便使用，比如请求参数，响应数据的处理，错误处理等等；
- 支持同步、异步、RxJava；
- 超级解耦；
- 可以配置不同的反序列化工具来解析数据，如json、XML；

#### 2、Retrofit 怎样将 Call 转换成 Observable

- 构建 Retrofit 时，通过 builder 设置 addCallAdapterFactory(RxJavaCallAdapterFactory.create())
- RxJavaCallAdapterFactory 继承自 CallAdapter.Factory，在 loadServiceMethod() 中会 createCallAdapter()，这里调用 callAdapterFactories.get(i).get() 方法中创建了 RxJavaCallAdapter 实例；
- 在调用 serviceMethod.adapt() 时，RxJavaCallAdapter 中的 adapt() 中返回了 Observable 类型；



> CallAdapter 作用是数据转换；
>
> CallAdapter接口的设计，使得我们在使用Retrofit的时候可以自定义我们想要的返回类型。此接口的设计也为RxJava的扩展使用做了很好的基础！！！



#### 3、Retrofit如果设置baseUrl，然后API接口中定义的方法注解中传入了完整的url，哪个生效？

- 如果你在注解中提供的url是完整的url，则url将作为请求的url；
- 如果你在注解中提供的url是不完整的url，且不以 / 开头，则请求的url为baseUrl+注解中提供的值；
- 如果你在注解中提供的url是不完整的url，且以 / 开头，则请求的url为baseUrl的主机部分+注解中提供的值；



#### 4、Retrofit怎样切换线程？

在Retrofit中的builder类中，会调用Platform.get()方法获取platform，Android平台下，默认defaultCallbackExecutor是一个 MainThreadExecutor，在这里面获取主线程的looper，构建handler，通过handler.post()切换到主线程。



#### 5、用到的设计模式

- **动态代理模式**

  `Retrofit`内部通过**动态代理+反射**来拿到用户定义在接口中的请求参数，从而来构建实际请求。

  <font color='red'>为什么要使用动态代理来获取API方法？</font>

  `Retrofit`按照`RESTful`风格设计并通过注解来定义`API方法`的请求参数，并将这些`API方法`放到`interface`中。因为`interface`是不能被实例化的，所以这里采用动态代理在运行期间实例化`API接口`，获取到方法的请求参数。

  再进一步：

  **解耦**，将实际业务与`Retrofit`隔离开来。用户只需通过注解方法来定义请求参数，而实际请求的构建则通过`Retrofit`内部来实现。

- **策略模式**

  当针对同一类型问题有多种处理方式，仅仅是具体行为有差别时，就可以使用策略模式。

  例如：`Retrofit`中的请求适配器

  - 抽象策略：`CallAdapter`。
  - 策略具体实现：`DefaultCallAdapterFactory.get()`、`RxJava2CallAdapter`。

  即提供默认的请求适配器，也支持用户自定义，符合开闭原则，达到很好的可扩展性。

- **适配器模式**

  `Retrofit`会帮我们构建实际请求，内部通过默认的`DefaultCallAdapterFactory`来将请求转换成`Call<Object>`，同时`Retrofit`也支持其它平台，比如为了适配`RxJava`特性，将请求转换成`Observable<Object>`。

  - Target(目标角色): `Call<Object>`, `Observable<Object>`。
  - adaptee(需要适配的对象): `OkHttpCall`。
  - adapter(适配器)：`DefaultCallAdapterFactory.get()`、`RxJava2CallAdapter`。

- **工厂方法模式**

  我们以`Converter`来举例。

  - 抽象工厂：`Converter.Factory`。
  - 具体工厂：`GsonConverterFactory`、`BuiltInConverters`等等。
  - 抽象产品：`Converter`。
  - 具体产品：`GsonResponseBodyConverter`、`GsonRequestBodyConverter`、`ToStringConverter`等等。

- **建造者模式**

  构建复杂对象时使用，在构建`Retrofit`实例的时候，就用到了建造者模式。



