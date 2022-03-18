## LifeCycle

### 一、示例

```kotlin
class MainActivity : AppCompatActivity() {
    private var lifeListenter:LifeListenter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifeListenter= LifeListenter()
        lifecycle.addObserver(lifeListenter!!)
    }
}

open class LifeListenter : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(owner: LifecycleOwner) {
        Log.e("TAG-----CREATE", "CREATE")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(owner: LifecycleOwner) {
        Log.e("TAG-----START", "START")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(owner: LifecycleOwner) {
        Log.e("TAG-----RESUME", "RESUME")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        Log.e("TAG-----DESTROY", "DESTROY")
    }
}
```



<font color='red'>为什么实现 LifecycleObserver 后能通过注解感知到 Activity 的生命周期？</font>

原理在 addObserver() 里，里面通过反射处理了 OnLifecycleEvent 注解和事件的分发。



### 二、关键类：

#### 1、Lifecycle

抽象类，继承该类的子类，表示本身是一个具有 Android 生命周期特性的对象。

<font color='red'>被观察者</font>

对外提供如下能力：

- Android生命周期 **事件的监听**
- Android生命周期 **状态的获取**

方法：

- addObserver()
- removeObserver()
- getCurrentState()

内部类：

- enum Event
- enum State



#### 2、LifecycleRegistry

系统中 Lifecycle 的唯一实现类。

<font color='red'>具体被观察者</font>

实现 Lifecycle 中的三个抽象方法，生命周期观察订阅、移除观察者、获取生命周期状态。还实现了生命周期变化时触发的自身状态处理和相关对观察者的订阅回调的逻辑。

> 简单来说，就是对生命周期持有者提供一组状态迁移方法，生命周期持有者在对应的生命周期方法中，增加对这组方法的调用。而方法调用后，`Lifecycle`则会执行具体的变换处理，及执行回调观察者的逻辑。



#### 3、LifecycleObserver

该接口的实现类表示为关注生命周期事件的观察者。

<font color='red'>观察者</font>



#### 4、LifecycleOwner

接口，该接口的实现类表示能够为外部提供 Lifecycle 实例，通过实现 getLicecycle() 方法。

系统框架中，该接口的实现类为 ComponentActivity、Fragment，两者提供的 Lifecycle 实例是 LifecycleRegistry。



#### 5、Lifecycling

系统框架实现中，对所有 LifecycleObserver 的包装适配器选择和相关处理都由本类完成。



#### 6、ReflectiveGenericLifecycleObserver

它存储了我们在 Observer 里注解的方法，并在生命周期发生改变的时候最终通过反射的方式调用对应的方法。



### 三、注册流程

Lifecycle#addObserver()，由 LifecycleRegistry 实现。

1. LifecycleRegistry#addObserver(observer)

   将传入的观察者封装为 ObserverWithState，通过 Map 保存起来。

2. ObserverWithState 构造函数

   通过 Lifecycling.lifecycleEventObserver(observer) 将 observer 封装成一个 LifecycleEventObserver，它可以接收任何生命周期更改并将其分派给接收方。

3. Lifecycling#lifecycleEventObserver()

   根据传入参数 observer 的类型，返回不同 LifecycleEventObserver 的子类。

   默认返回 ReflectiveGenericLifecycleObserver 实例。

4. ReflectiveGenericLifecycleObserver 构造函数

   ClassesInfoCache.sInstance.getInfo(mWrapped.getClass());

   根据传入的 observer 的 class 对象，构建一个 CallbackInfo 实例。

   - ClassesInfoCache#getInfo()
   - ClassesInfoCache#createInfo()
     - 其中通过反射，获取观察者 observer 中声明的方法，遍历查看是否有被 OnLifecycleEvent 注解的方法。
     - 没有的话，直接 return；有的话，获取注解的值，比如 Lifecycle.Event.ON_RESUME，用 map 保存起来。



关键类、方法：

```java
static class ObserverWithState {
    State mState;
    LifecycleEventObserver mLifecycleObserver;

    ObserverWithState(LifecycleObserver observer, State initialState) {
        mLifecycleObserver = Lifecycling.lifecycleEventObserver(observer);
        mState = initialState;
    }

    void dispatchEvent(LifecycleOwner owner, Event event) {
        State newState = getStateAfter(event);
        mState = min(mState, newState);
        mLifecycleObserver.onStateChanged(owner, event);
        mState = newState;
    }
}
```





### 四、Lifecycle 与生命周期组件的交互

以 API 28 ComponentActivity 为例

ComponentActivity 实现了 LifecycleOwner 接口并持有 LifecycleRegistry 实例，但是操作 LifecycleRegistry 生命周期变化的逻辑是在 ReportFragment 中。

```java
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mSavedStateRegistryController.performRestore(savedInstanceState);
    //通过添加Fragment，绑定Activity的生命周期，对生命周期变化进行监听处理
    ReportFragment.injectIfNeededIn(this);
    if (mContentLayoutId != 0) {
        setContentView(mContentLayoutId);
    }
}
```

1. ReportFragment#injectIfNeededIn(this)

   new 一个 ReportFragment 实例，add 到 ComponentActivity 中，用来感知 Activity 的生命周期。

2. 在 ReportFragment 中的各个生命周期中，通过 dispatch() 方法分发对应的生命周期，比如 dispatch(Lifecycle.Event.ON_CREATE);

3. 调用 LifecycleRegistry#handleLifecycleEvent() 处理

4. 最终调用到ObserverWithState#dispatch()

5. LifecycleObserver#onStateChanged()

   进入实现类 ReflectiveGenericLifecycleObserver

6. ReflectiveGenericLifecycleObserver#onStateChanged()

   通过注册观察者时，构建的 CallbackInfo，调用其 invokeCallbacks()，通过反射调用被 OnLifecycleEvent 注解标注的方法。

7. 最终执行到我们继承自 LifecycleObserver 的类，监听到 Activity 的生命周期，在对应的生命周期中执行自己的逻辑。





![img](https://mmbiz.qpic.cn/mmbiz_png/v1LbPPWiaSt5Ka9YOCib7aRDz0Ficvry5bwXhfJCSKC6xq8Xm92HAxGavuJ3C6PpibPcdGQ90bXs0uQm9ByueDaumA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)