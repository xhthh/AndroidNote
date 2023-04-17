### JetPack

```java
Lifecycle 解决了生命周期 同步问题
LiveData 实现了真正的状态驱动，主要为了 新手老手都能不假思索遵循 “通过唯一可信源分发消息” 标准化开发理念，以便快速开发过程中 “难追溯、难排查、不可预期” 问题发生概率降低到最低。
ViewModel 主要为了解决 “状态管理” 和 “页面通信” 问题，可以让 Fragment 通讯变得更优雅
DataBinding 让双向绑定成为了可能
Jetpack 只是让 MVVM 更简单、更安全
```

#### 一、LifeCycle

##### 1.1 相关类

- **LifecycleObserver 接口（Lifecycle观察者）**：实现该接口的类，通过注解的方式，可以通过被 LifecycleOwner 类的 addObserver() 方法注册，被注册后，LifecycleObserver 便可观察到 LifecycleOwner 的生命周期事件；
- **LifecycleOwner 接口（Lifecycle 持有者）**：实现该接口的类持有生命周期（Lifecycle对象），该接口的生命周期（Lifecycle对象）的改变会被其注册的观察者 LifecycleObserver 观察到并触发其对应的事件；
- **Lifecycle（生命周期）**：和 LifecycleOwner 不同的是，LifecycleOwner 本身持有 Lifecycle 对象，LifecycleOwner 接口中只有一个 getLifecycle() 的方法，用来获取 Lifeycycle 对象（实际上返回的是 **LifecycleRegistry** 对象，Lifecycle 的实现类，具体操作都在这里）；
- **State（当前生命周期所处状态）**
- **Event（当前生命周期改变对应的事件）**：当 Lifecycle 发生改变，如进入 onCreate()，会自动发出 ON_CREATE 事件；

##### 1.2 注册流程

- 注册
  - addObserver() 进行注册；
  - 通过反射，获取观察者 observer 中声明的方法，遍历查看是否有被 OnLifecycleEvent 注解的方法；
  - 最终通过 Map 存储；
  
- 分发处理
  - ComponentActivity 实现了 LifecycleOwner 接口并持有 LifecycleRegistry 实例，但是操作 LifecycleRegistry 生命周期变化的逻辑是在 ReportFragment 中（在API 29及以上 直接使用activity的registerActivityLifecycleCallbacks 注册了生命周期回调，29以下是通过 Fragment 感知生命周期）；

  - ReportFragment 通过感知 Activity 的生命周期，在各个生命周期方法中调用 handleLifecycleEvent() 进行分发；

    ```java
    public void handleLifecycleEvent(@NonNull Lifecycle.Event event) {
        moveToState(event.getTargetState());//移动到这个状态，
    }
    ```
  
  - 使用 event.getTargetState() 获取 event 发生之后将要处于的状态；
  
    - moveToState() 移动到这个状态；
  
  - 最后使用 sync() 把生命周期状态同步给所有的观察者；
  
    observer.dispatchEvent(lifeCycleOwner, event)
  
    - ObserverWithState#dispatchEvent() 中会回调 mLifecycleObserver.onStateChanged(owner, event)
    - mLifecycleObserver 的实现类是在 ObserverWithState 构造函数中，Lifecycling.lifecycleEventObserver(observer) 返回的 ReflectiveGenericLifecycleObserver；
  
  - 最终在实现类的回调中，通过反射，调用在注册时，保存的数据找到对应的方法，进行调用；
  
- 流程总结

  ![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/bfeed64e896c49ddb57b7ff1f1a099d1~tplv-k3u1fbpfcp-zoom-in-crop-mark:4536:0:0:0.awebp)



#### 二、LiveData

> LiveData 是一种可观察的数据存储器类。与常规的可观察类不同，LiveData 具有生命周期感知能力，意指它遵循其他应用组件（如 Activity/Fragment）的生命周期。这种感知能力可确保 LiveData 仅更新处于活跃生命周期状态的应用组件观察者。

即：

1. LiveData 是一个数据持有者，给源数据包装一层；
2. 源数据使用 LiveData 包装后，可以被 observer 观察，数据有更新时 observer 可感知；
3. 但 observer 的感知，只发生在（Activity/Fragment）活跃生命周期状态（STARTED、RESUMED）；

优势：

- **确保界面符合数据状态**，当生命周期状态变化时，LiveData 通知 Observer，可以在 observer 中更新界面。观察者可以在生命周期状态更改时刷新界面，而不是在每次数据变化时刷新界面；
- **不会发生内存泄漏**，observer 会在 LifecycleOwner 状态变为 DESTROYED 后自动 remove；
- **不会因 Activity 停止而导致崩溃**，如果 LifecycleOwner 生命周期处于非活跃状态，则它不会接收任何 LiveData 事件；
- **不需要手动解除观察**，LiveData 能感知生命周期状态变化，会自动管理这些操作；
- **数据始终保持最新状态**，数据更新时，若 LifecycleOwner 为非活跃状态，那么会在变为活跃时接收最新数据。例如，曾经在后台的 Activity 会在返回前台后，observer 立即接收最新的数据；



源码：

- 观察者模式，`liveData.observe(lifecycleOwner, observer)`，observe() 方法将 owner 和 observer 封装成一个 `LifecycleBoundObserver` 对象，放到一个 map 中保存，利用 Lifecycle.addObserver() 关联生命周期；

  - 判断是否是 DESTROYED 状态，是就忽略；
  - LifecycleBoundObserver 实现了 LifecycleEventObserver 接口，这个接口继承自 Lifecycle 中的 LifecycleObserver；

- 当生命周期发生变化时，Lifecycle 进行分发时，会调用 onStateChanged() 方法；

  - 当生命周期是活跃状态时，进行分发，最终走到 Observer#onChanged()；

  

#### 三、ViewModel

##### 3.1 简介

ViewModel，视图模型，即为界面准备数据的模型。

> ViewModel 以注重生命周期的方式存储和管理界面相关的数据。（作用）
>
> ViewModel 类让数据可在发生屏幕旋转等配置更改后继续留存。（特点）
>
> - 生命周期长于 Activity；
>
>   因系统配置变更 Activity 销毁重建，ViewModel 对象会保留并关联到新的 Activity。而 Activity 的正常销毁（系统不会重建Activity）时，ViewModel 对象是会清除的。
>
> - 不持有UI层的引用；
>
>   因为 ViewModel 的生命周期长于 Activity，所以它不能持有UI 层的引用，它是通过 LiveData 和 UI 层进行交互的。

##### 3.2 ViewModel 的使用

- 作用和 Presenter 类似，结合 LiveData，不需要持有UI的引用，即可用于获取数据和更新UI。

  - 创建 ViewModelProvider；
  - get() 方法获取 ViewModel 实例；
  - 观察 ViewModel 中的 LiveData；

- Fragment 间数据共享

  两个 Fragment 通过 ViewModelProvider 获取 ViewModel 时，传入的都是它们宿主 Activity。它们各自获取 ViewModelProvider 时，会收到相同的 ShareViewModel 实例（其范围限定为该 Activity）。



##### 3.3 源码

###### 3.3.1 关键类

- ViewModel

  - 一个抽象类，内部没有啥逻辑，有个 clear() 方法会在 ViewModel 被清除时调用；
  - ViewModel 实例的获取是通过 ViewModelProvider 类，即 ViewModel 提供者；

- ViewModelProvider

  ```java
  public ViewModelProvider(@NonNull ViewModelStoreOwner owner, @NonNull Factory factory) {
      this(owner.getViewModelStore(), factory);
  }
  public ViewModelProvider(@NonNull ViewModelStore store, @NonNull Factory factory) {
      mFactory = factory;
      mViewModelStore = store;
  }
  ```

  - ViewModelStoreOwner 是一个接口，ViewModel存储器拥有者；

    实现类有 Fragment、Activity，即 Activity/Fragment 都是 ViewModel 存储器的拥有者；

  - ViewModelStore，ViewModel存储器；

  - Factory，创建ViewModel实例的工厂；

    根据传入的 class 反射获取 ViewModel 实例

  - get() 方法获取 ViewModel 实例

    - 通过 key 从 ViewModelStore 中获取，得到就直接返回；
    - ViewModelStore 中获取不到，通过 class 反射创建，然后存入 ViewModelStore 中，返回；

- ViewModelStore

  - ViewModel存储器，将 ViewModel 作为 value 存储在 HashMap 中；
  - 有 put()、get() 方法和一个 clear() 方法；
  - 如果 ViewModelStore 的拥有者（Activity/Fragment）销毁后不会重建，就会调用 clear() 方法，在 Activity ON_DESTROY 之后；

- ViewModelStoreOwner

  - 一个接口，Activity、Fragment 是实现类，getViewModelStore() 获取实例;

  - ComponentActivity#NonConfigurationInstances 和配置无关的一个类，持有 ViewModelStore 的引用；

    - getViewModelStore() 时会先通过 getLastNonConfigurationInstance() 得到 NoConfigurationInstances 实例，将其持有的 ViewModelStore 赋值给 成员变量 mViewModelStore，如果为空，则 new 一个；

    - getLastNonConfigurationInstance() 方法返回的是 Activitry 中 成员变量 mLastNonConfigurationInstances 中的一个 object；

      ```java
      public Object getLastNonConfigurationInstance() {
          return mLastNonConfigurationInstances != null
                  ? mLastNonConfigurationInstances.activity : null;
      }
      ```

    - Activity#mLastNonConfigurationInstances 来自于 attach()，Activity#attach() 在 ActivityThread#performLaunchActivity() 中被调用，其值来自于 ActivityClientRecord#lastNonConfigurationInstances；

    - ActivityClientRecord#lastNonConfigurationInstances 的值在 performDestroyActivity() 中被赋值；

      ```java
      ActivityClientRecord performDestroyActivity(IBinder token, boolean finishing,
              int configChanges, boolean getNonConfigInstance, String reason) {
          ActivityClientRecord r = mActivities.get(token);
          r.lastNonConfigurationInstances
                     = r.activity.retainNonConfigurationInstances();
      }
      ```

    - Activity#retainNonConfigurationInstances() 中会调用 onRetainNonConfigurationInstance()，

      该方法会构建一个 NonConfigurationInstances 对象，中将 Activity 的成员变量 mViewModelStore 赋值给 nci.viewModelStore，然后返回 nci 对象；

    - 总结一下就是，ViewModelStore 对象创建后会赋值给 ComponentActivity 中的成员变量，在横竖屏切换时，Activity 会销毁，此时 ActivityThread 中会执行 Activity 中的 retainNonConfigurationInstances() 方法保存 lastNonConfigurationInstances，该方法中会保存 viewModelStore 对象；

      activityThread把这个ViewModeStore缓存到ActivityClientRecord对象中。而activityClientRecord对象是用户手动back键finish它，它才会销毁的，故viewmodel就能不会因为窗口配置改变或者系统回收内存而销毁。

      > ActivityThread 中的 ActivityClientRecord 不受 activity 重建的影响，那么 ActivityClientRecord 中的 lastNonConfigurationInstances 也不受影响，其中的 Object activity 也不受影响，那么 ComponentActivity 中通过 getLastNonConfigurationInstance() 方法获取到的 NonConfigurationInstances 实例及其持有的 ViewModelStore 也不受影响；

###### 3.3.2 总结

- ViewModel 是为界面准备数据的模型，分担 `Activity`/`Fragment` 的逻辑，同时会维护自己独立的生命周期，特点是配置更改界面销毁后依然存在、不持有UI的引用；
- 用途：
  - 可以和 LiveData 搭配使用，代替 MVP 中的 Presenter；
  - 也可用于 Fragment 间的数据共享；
- 源码：
  - ViewModel 的存储和获取：
    - 通过 ViewModelStore 进行存储和获取，保存在 HashMap 中；
    - 提供了 clear() 方法，在 Activity 真正销毁后，进行清除；
  - ViewModelStore 的存储和获取：
    - NonConfigurationInstances 与配置无关的类，通过它获取 ViewModelStore 的实例，追踪源码最终到 Activity#attach()--->ActivityThread#performLaunchActivity()--->ActivityClientRecord中的变量 lastNonConfigurationInstances；
    
    - 而 ActivityClientRecord 保存在 ActivityThread 的 mActivities 中；
    
    - Activity 的生命周期都是通过 ActivityThread 管理的, 重建Activity通过 `ActivityThread # handleRelaunchActivity` 实现；
    
      `handleRelaunchActivityInner()` 分为两步 ：
    
      - handleDestroyActivity()销毁当前的页面；
    
        在 performDestroyActivity() 在调用 mInstrumentation.callActivityOnDestroy(r.activity) 也就是销毁 Activity 之前, 会调用 activity.retainNonConfigurationInstances() 将返回的 NonConfigurationInstances 对象保存到 ActivityClientRecord 中；
    
        > //在销毁前保存 NonConfigurationInstances
        > r.lastNonConfigurationInstances = r.activity.retainNonConfigurationInstances();
    
      - handleLaunchActivity()重建新的页面；
    
        performLaunchActivity(r)，创建 activity，调用 attach(r.lastNonConfigurationInstances)；
  
  > ActivityClientRecord r 中保存了 Activity.NonConfigurationInstances.
  >
  > Activity.NonConfigurationInstances 中保存了 FragmentActivity.NonConfigurationInstances.
  >
  > FragmentActivity.NonConfigurationInstances 中保存了 ViewModelStore viewModelStore
  >
  > ViewModelStore viewModelStore 内部通过HashMap保存了 当前Activity的所有 ViewModel
  >
  > 销毁页面时, ViewModel 最终将被保存到 ActivityRecord 中.
  > 重建页面时, 使用了同一个 ActivityRecord 来进行数据的恢复, 从中可以获得销毁前页面上所有 ViewModel 的容器 ViewModelStore, 再次调用 ViewModelProviders.of(Activity.class).get(ViewModel.class) 根据类名从 HashMap 中获得已经创建过的 ViewModel.
  > 原文链接：https://blog.csdn.net/qijingwang/article/details/121521256



#### 四、MVVM

##### 1、概念

MVVM，职责分类：

- Model，模型层，即数据模型，用于获取和存储数据；
- View，视图，即 Activity/Fragment；
- ViewModel，视图模型，负责业务逻辑；

> MVVM 中的 ViewModel 就是一个名称，可以理解为 MVP 中的 Presenter。而 Jetpack ViewModel 组件是对 MVVM 的 ViewModel 的具体实现方案。

MVVM 的本质是 **数据驱动**，把解耦做的更彻底，ViewModel 不持有 View。

View 产生事件，使用 ViewModel 进行逻辑处理后，通知 Model 更新数据，Model 把更新的数据给 ViewModel，ViewModel **自动通知View更新界面，而不是主动调用View的方法**。



##### 2、Jetpack 中的 MVVM

- View层

  Activity、Fragment、布局文件等于界面相关的东西；

- ViewModel层

  - Jetpack ViewModel + Jetpack LiveData；
  - 用于持有和 UI 元素相关的数据，以保证这些数据在屏幕旋转时不会丢失，并且还要提供接口给 View 层调用以及和仓库层进行通信；

- Model层

  Repository 仓库，包含本地持久性数据和服务端数据；

##### 3、与 MVP 的区别

MVVM 采用双向绑定，数据的修改直接反应到 Model 上，View 的修改也会导致数据的变更，ViewModel 不持有 View 的引用；

MVP 中 Presenter 和 View 互相持有对方的引用；



> 双向绑定是 DataBinding 吧



#### 五、MVP

MVP 的缺点：

- 接口爆炸

  写一个Contract接口，然后把与MVP相关接口全部列入到里面去；

- 内存泄漏问题

  当用户关闭了View层，但这时Model层如果仍然在进行耗时操作，因为Presenter层也持有View层的引用，所以造成垃圾回收器无法对View层进行回收，这样一来，就造成了内存泄漏。

  解决方法：

  - 可以重写onDestroy()方法，在View销毁时强制回收掉Presenter；
  
  - 或是采用弱引用的方式；
  
    ```java
    //绑定解析View 以图和数据交互
    public class BasePresenter<V> {
        //用弱引用创建View
    
        //弱引用 ->解决MVP的内存泄露->OOM内存溢出
        //强 软 弱 虚
        WeakReference<V> mWeakReference;
        //请求方式 okHttp+retrofit+rxjava
        //每个请求 都会产生订阅->把订阅做一个统一的管理
    
        //将rxjava的 订阅 添加到mCompositeDisposable 通过mCompositeDisposable 对订阅的请求做统一的管理
        protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    
    //    通过mWeakReference创建View
        public void attachView(V view){
            mWeakReference = new WeakReference<V>(view);
        }
        //取出对应的view
        protected V getView(){
            return mWeakReference == null?null:mWeakReference.get();
        }
        //在咱们取出对应的view之前做这个判断
        protected boolean isViewAttached(){
            return mWeakReference != null && mWeakReference.get() !=null;
        }
    
        //在APP或者页面的生命周期结束 及其他需要的地方 释放View 取消订阅
    
        public void detachView(){
    //        释放View
            if (mWeakReference != null){
                mWeakReference.clear();
                mWeakReference = null;
            }
    //        取消订阅
            mCompositeDisposable.clear();
    
        }
    }
    ```
  
    
  
- 视图和Presenter的交互会过于频繁，使得他们的联系过于紧密。也就是说，一旦视图变更了，presenter也要变更。



#### 六、DataBinding

##### 1、定义概念

DataBinding，含义是 **数据绑定**，即 **布局中的控件** 和 **可观察的数据** 进行绑定。

> DataBinding 并非是将 UI 逻辑搬到 XML 中写而导致难以调试，只负责绑定数据，UI 控件与其需要的 `终态数据` （即 UI 控件直接需要的数据，而不是一段逻辑）进行绑定。

**优势**：

1. 无需多处调用控件，原本调用的地方只需要 set 数据即可；
2. 无需手动判空；
3. 不用在写 findViewById；
4. 引入 DataBinding 后，原本的 UI 逻辑无需改动，只需设置终态数据；

##### 2、自定义属性

BindingAdapter 能为控件提供自定义属性。

```xml
<ImageView
    android:layout_width="100dp"
    android:layout_height="100dp"
    app:imageUrl="@{user.avatar}"
    app:placeHolder="@{@drawable/dog}"/>
```

```java
@BindingAdapter({"app:imageUrl", "app:placeHolder"})
public static void loadImageFromUri(ImageView imageView, String imageUri, Drawable placeHolder){
    Glide.with(imageView.getContext())
        .load(imageUri)
        .placeholder(placeHolder)
        .into(imageView);
}
```

> 通常可以用 @BindingAdapter 方式，在模块内部来做一些公用逻辑。例如上面的图片加载，@BindingAdapter 注解的方法只要写一次，所有用到 ImageView 加载图片的地方，在xml 中都可以直接使用 app:imageUrl、app:placeHolder 直接绑定数据。

##### 3、与 LiveData 结合使用

DataBinding 如果要监听数据的变化，实时对 View 进行刷新，在 xml 中定义的数据，必须要继承 BaseObservable 或者使用 ObservableFiled，还要添加注解 @Bindable、调用 notifyPropertyChanged(BR.xxx)。

LiveData 可以代替 ObservableFiled，并且还具备生命周期管理。不用侵入式的修改数据实体类。

**DataBinding + LiveData + ViewModel**：

- 使用 LiveData 对象作为数据绑定来源，需要设置 LifecycleOwner；
- xml 中定义变量 ViewModel，并使用 ViewModel 中的 LiveData 绑定对应控件；
- binding 设置变量 ViewModel；

```java
//结合DataBinding使用的ViewModel
//1. 要使用LiveData对象作为数据绑定来源，需要设置LifecycleOwner
binding.setLifecycleOwner(this);

ViewModelProvider viewModelProvider = new ViewModelProvider(this);
mUserViewModel = viewModelProvider.get(UserViewModel.class);
//3. 设置变量ViewModel
binding.setVm(mUserViewModel);
```

```xml
<!-- 2. 定义ViewModel 并绑定-->
<variable
    name="vm"
    type="com.hfy.demo01.module.jetpack.databinding.UserViewModel" />
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@{vm.userLiveData.name}"/>
```

**Tips**：

特别需要注意的是，字符串的拼接不要用直接的文字表示，比如

```bash
android:text='@{"user的name:"+bean.name}'  
```

这种容易出问题。并且，DataBinding报错并不会指向错误源，事后很难排查。





##### 4、原理

DataBinding 使用了 apt 技术，在编译期生成对应的类，所有的layout都会生成一个Binding类，这个类继承ViewDataBinding，然后实现了execute*方法。

我们在activity中把 mUser对象传入了binding类，在每次对它进行set操作的时候都会触发notify， 之后DataBinding框架会回调execute方法， 框架通过注解拿到get方法，然后拿到和UI所对应的数据，之后结合layout中对应的标注去更新UI。

DataBinding通过布局中的tag将控件查找出来，然后根据生成的配置文件将V与M进行对应的同步操作，设置一个全局的布局变化监听来实时更新，M通过他的set方法进行同步。



#### 六、Navigation

单一职责

大致体系：

- **NavHostFragment**

  - 作为 Activity 导航界面的载体
  - 管理并控制导航的行为，处于单一职责原则，这个功能交给另一个类，NavController

- **NavController**

  控制导航行为，且将 NavController 的持有者抽象为一个接口。为了保证导航的 **安全**，NavHostFragment 在其 **作用域** 内，理应 **有且仅有一个NavController 的实例**。

  职责：

  - 对navigation资源文件夹下nav_graph.xml的 **解析**；
  - 通过解析xml，获取所有 **Destination**（目标点）的 **引用** 或者 **Class的引用**；
  - 记录当前栈中 **Fragment的顺序**；
  - 管理控制 **导航行为**；

- 设计 **NavController** 

   **NavController** 持有了一个 **NavInflater** ,并通过 **NavInflater** 解析xml文件。

  这之后，获取了所有 **Destination** 的 Class对象，并通过反射的方式，实例化对应的 **Destination**，通过一个队列保存：

  ```java
  private NavInflater mInflater;  //NavInflater
  private NavGraph mGraph;        //解析xml，得到NavGraph
  private int mGraphId;           //xml对应的id，比如 nav_graph_main
  //所有Destination的队列,用来处理回退栈
  private final Deque<NavDestination> mBackStack = new ArrayDeque<>();
  ```

  因为 Navigation 控件并非只为 Fragment 导航，且可为 Activity 导航，处于拓展性的考虑，将导航的 **Destination** 抽象出来，这个类叫做 **NavDestination** ——无论 **Fragment** 也好，**Activity** 也罢，只要实现了这个接口，对于**NavController** 来讲，他们都是 **Destination（目标点）**而已。

- **NavDestination 和 Navigator**

  如何根据不同的 **NavDestination** 进行不同的 **导航处理** 呢？

  抽象出一个类，**Navigator**

  ```java
  public abstract class Navigator<D extends NavDestination> {
      //省略很多代码,包括部分抽象方法，这里仅阐述设计的思路！
      //导航
      public abstract void navigate(@NonNull D destination, @Nullable Bundle args,
                                       @Nullable NavOptions navOptions);
      //实例化NavDestination（就是Fragment）
      public abstract D createDestination();
      //后退导航
      public abstract boolean popBackStack();
  }
  ```

  **Navigator**(导航者) 的职责很单纯：

  - 1.能够实例化对应的 **NavDestination**
  - 2.能够指定导航
  - 3.能够后退导航

  **NavController** 获取了所有 **NavDestination** 的Class对象，但是我不负责它 **如何实例化** ，也不负责 **如何导航** ，也不负责
  **如何后退** ——我仅仅持有向上的引用，然后调用它的接口方法，它的实现我不关心。

  简单说一下不同的 NavDestination 怎样实现，不同的 **Navigator** 对应不同的 **NavDestination**：

  - **FragmentNavigator** 
    - 负责 Fragment 的跳转、回退和 Destination 的创建；
    - navigate() 即负责导航跳转，其实里面还是用的 FragmentTransaction，且是用的 `replace()` 方法，所以有点问题，因为每次都会销毁重建；
    - popBackStack() 负责回退，在 Fragment 中就是通过回退栈处理，`mFragmentManager.popBackStack()`
  - **ActivityNavigator**
    - navigate() 负责跳转，就是通过 Destination 处理 Intent，然后 `startActivity(intent)`；
    - popBackStack() 回退，就是 finish；



