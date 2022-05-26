## LayoutInflater 布局解析

https://juejin.cn/post/6844903919286485000

### 一、概述

`LayoutInflater`，其整个流程中除了调用`inflate()`函数 **填充布局** 功能之外，还涉及到了 **应用启动**、**调用系统服务**（进程间通信）、**对应组件作用域内单例管理**、**额外功能扩展** 等等一系列复杂的逻辑。

![img](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2019/8/19/16ca911b1e5e7714~tplv-t2oaga2asx-zoom-in-crop-mark:1304:0:0:0.awebp)

### 二、整体思路

#### 1、创建流程

`LayoutInflater`的作用就是 **布局填充器** ，其行为本质是调用了`Android`本身提供的 **系统服务**。而在`Android`系统的设计中，获取系统服务的实现方式就是通过`ServiceManager`来取得和对应服务交互的`IBinder`对象，然后创建对应系统服务的代理。

`Android`应用层将系统服务注册相关的`API`放在了`SystemServiceRegistry`类中，而将注册服务行为的代码放在了`ContextImpl`类中，`ContextImpl`类实现了`Context`类下的所有抽象方法。

`Android`应用层还定义了一个`Context`的另外一个子类：`ContextWrapper`，`Activity`、`Service`等组件继承了`ContextWrapper`, 每个`ContextWrapper`的实例有且仅对应一个`ContextImpl`，形成一一对应的关系，该类是 **装饰器模式** 的体现：保证了`Context`类公共功能代码和不同功能代码的隔离。

> Activity 继承自 ContextThemeWrapper，Service 继承自 ContextWrapper；

此外，虽然`ContextImpl`类作为`Context`类公共`API`的实现者，`LayoutInlater`的获取则交给了`ContextThemeWrapper`类，该类中将`LayoutInlater`的获取交给了一个成员变量，保证了单个组件 **作用域内的单例**。

```java
//ContextThemeWrapper
@Override
public Object getSystemService(String name) {
    if (LAYOUT_INFLATER_SERVICE.equals(name)) {//layout_inflater
        if (mInflater == null) {
            mInflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
        }
        return mInflater;
    }
    return getBaseContext().getSystemService(name);
}
```



#### 2、布局填充流程

`LayoutInflater#inflate()`函数对布局进行填充，该函数作用是对`xml`文件中标签的解析，并根据参数决定是否直接将新创建的`View`配置在指定的`ViewGroup`中。

一般来说，一个`View`的实例化依赖`Context`上下文对象和`attr`的属性集，而设计者正是通过将上下文对象和属性集作为参数，通过 **反射** 注入到`View`的构造器中对`View`进行创建。

考虑到 **性能优化** 和 **可扩展性**，设计者为`LayoutInflater`设计了一个`LayoutInflater.Factory2`接口：在`xml`解析过程中，开发者可以通过配置该接口对`View`的创建过程进行拦截：**通过new的方式创建控件以避免大量地使用反射**，亦或者 **额外配置特殊标签的解析逻辑以创建特殊组件**（比如`Fragment`）。

> `LayoutInflater.Factory2`接口在`Android SDK`中的应用非常普遍，`AppCompatActivity`和`FragmentManager`就是最有力的体现。



### 三、创建流程

#### 1、Context 系统服务的提供者

`LayoutInflater`作为系统服务之一，获取方式是通过`ServiceManager`来取得和对应服务交互的`IBinder`对象，然后创建对应系统服务的代理。

`Android`的设计者将获取系统服务的接口交给了`Context`类，意味着开发者可以通过任意一个`Context`的实现类获取系统服务，包括不限于`Activity`、`Service`、`Application`等等：

```java
public abstract class Context {
  // 获取系统服务
  public abstract Object getSystemService(String name);
  // ......
}
```

`Context`类地职责并非只针对 **系统服务** 进行提供，还包括诸如 **启动其它组件**、**获取SharedPerferences** 等等，其中大部分功能对于`Context`的子类而言都是公共的，因此没有必要每个子类都对其进行实现。

`Android`设计者并没有直接通过继承的方式将公共业务逻辑放入`Base`类供组件调用或者重写，而是借鉴了 **装饰器模式** 的思想：分别定义了`ContextImpl`和`ContextWrapper`两个子类：

![img](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2019/8/19/16ca911b1fd160ac~tplv-t2oaga2asx-zoom-in-crop-mark:1304:0:0:0.awebp)

#### 2、ContextImpl：Context 的公共API实现

`Context`的公共`API`的实现都交给了`ContextImpl`，以获取系统服务为例，`Android`应用层将系统服务注册相关的`API`放在了`SystemServiceRegistry`类中，而`ContextImpl`则是`SystemServiceRegistry#getSystemService`的唯一调用者：

```java
class ContextImpl extends Context {
    // 该成员即开发者使用的`Activity`等外部组件
    private Context mOuterContext;

    @Override
    public Object getSystemService(String name) {
        return SystemServiceRegistry.getSystemService(this, name);
    }
}
```

这种设计使得 **系统服务的注册**（`SystemServiceRegistry`类） 和 **系统服务的获取**（`ContextImpl`类） 在代码中只有一处声明和调用，大幅降低了模块之间的耦合。

#### 3、ContextWrapper：Context的装饰器

`ContextWrapper`则是`Context`的装饰器，当组件需要获取系统服务时交给`ContextImpl`成员处理，伪代码实现如下：

```java
// class Activity extends ContextWrapper
class ContextWrapper extends Context {
    // 1.将 ContextImpl 作为成员进行存储
    public ContextWrapper(ContextImpl base) {
        mBase = base;
    }

    ContextImpl mBase;

    // 2.系统服务的获取统一交给了ContextImpl
    @Override
    public Object getSystemService(String name) {
      return mBase.getSystemService(name);
    }
}
```

`ContextWrapper`装饰器的初始化如何实现呢？每当一个`ContextWrapper`组件（如`Activity`）被创建时，都为其创建一个对应的`ContextImpl`实例，伪代码实现如下:

```java
public final class ActivityThread {

  // 每当`Activity`被创建
  private Activity performLaunchActivity() {
      // ....
      // 1.实例化 ContextImpl
      ContextImpl appContext = new ContextImpl();
      // 2.将 activity 注入 ContextImpl
      appContext.setOuterContext(activity);
      // 3.将 ContextImpl 也注入到 activity中
      activity.attach(appContext, ....);
      // ....
  }
}
```

> 在 Activity、Service 和 Application 通过反射创建后均调用了各自的 attach() 方法，其中调用了 ContextWrapper 的 attachBaseContext(context) 方法，对 mBase 进行赋值，传入的均为 ContextImpl 实例；

#### 4、组件的局部单例

对于单个`Activity`而言，多次调用`activity.getLayoutInflater()`或者`LayoutInflater.from(activity)`，获取到的`LayoutInflater`对象都是单例的——对于涉及到了跨进程通信的系统服务而言，通过作用域内的单例模式保证以节省性能是完全可以理解的。

设计者将对应的代码放在了`ContextWrapper`的子类`ContextThemeWrapper`中，该类用于方便开发者为`Activity`配置自定义的主题，除此之外还通过一个成员持有了一个`LayoutInflater`对象：

```java
// class Activity extends ContextThemeWrapper
public class ContextThemeWrapper extends ContextWrapper {
  private Resources.Theme mTheme;
  private LayoutInflater mInflater;

  @Override
  public Object getSystemService(String name) {
      // 保证 LayoutInflater 的局部单例
      if (LAYOUT_INFLATER_SERVICE.equals(name)) {
          if (mInflater == null) {
              mInflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
          }
          return mInflater;
      }
      return getBaseContext().getSystemService(name);
  }
}
```

而无论`activity.getLayoutInflater()`还是`LayoutInflater.from(activity)`，其内部最终都执行的是`ContextThemeWrapper#getSystemService`, 因此获取到的`LayoutInflater`自然是同一个对象了：

```java
public abstract class LayoutInflater {
  public static LayoutInflater from(Context context) {
      return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }
}
```

> activity.getLayoutInflater() 是调用的 PhoneWindow#getLayoutInflater()，返回的是 mLayoutInflater 成员变量，此变量在 PhoneWindow 构造函数中初始化，调用的是 LayoutInflater.from(context)；



### 四、布局填充流程

`Activity`启动的过程，这个过程中不可避免的要创建一个窗口，最终UI的布局都要展示在这个窗口上，`Android`中通过定义了`PhoneWindow`类对这个UI的窗口进行描述。

#### 1、PhoneWindow：setContentView()

`Activity`将布局填充相关的逻辑委托给了`PhoneWindow`，`Activity`的`setContentView()`函数，其本质是调用了`PhoneWindow`的`setContentView()`函数。

```java
public class PhoneWindow extends Window {

   public PhoneWindow(Context context) {
       super(context);
       mLayoutInflater = LayoutInflater.from(context);
   }

   // Activity.setContentView 实际上是调用了 PhoneWindow.setContentView()
   @Override
   public void setContentView(int layoutResID) {
       // ...
       mLayoutInflater.inflate(layoutResID, mContentParent);
   }
}
```

`activity.getLayoutInflater()`和`activity.setContentView()`等方法都使用到了`PhoneWindow`内部的`LayoutInflater`对象，而`PhoneWindow`内部对`LayoutInflater`的实例化，仍然是调用`context.getSystemService()`方法。

一个`Activity`对应一个`PhoneWindow`的UI窗口，因此当`Activity`被创建时，`PhoneWindow`就被需要被创建了，执行时机就在上文的`ActivityThread.performLaunchActivity()`中：

```java
public final class ActivityThread {

  // 每当`Activity`被创建
  private Activity performLaunchActivity() {
      // ....
      // 3.将 ContextImpl 也注入到 activity中
      activity.attach(appContext, ....);
      // ....
  }
}

public class Activity extends ContextThemeWrapper {

  final void attach(Context context, ...) {
    // ...
    // 初始化 PhoneWindow
    // window构造方法中又通过 Context 实例化了 LayoutInflater
    PhoneWindow mWindow = new PhoneWindow(this, ....);
  }
}
```

`LayoutInflater`的整体流程总结：

- 无论是哪种方式获取到的`LayoutInflater`,都是通过`ContextImpl.getSystemService()`获取的，并且在`Activity`等组件的生命周期内保持单例；
- 即使是`Activity.setContentView()`函数,本质上也还是通过`LayoutInflater.inflate()`函数对布局进行解析和创建。



#### 2、inflate() 流程的设计和实现

```java
public View inflate(@LayoutRes int resource, ViewGroup root, boolean attachToRoot) {
      // ...
}
```

对该函数的参数进行简单归纳如下：

- 第一个参数代表所要加载的布局；
- 第二个参数是`ViewGroup`，这个参数需要与第3个参数配合使用，`attachToRoot`如果为`true`就把布局添加到`ViewGroup`中；若为`false`则只采用`ViewGroup`的`LayoutParams`作为测量的依据却不直接添加到`ViewGroup`中。

##### 2.1 参数解析

- 第一个参数，资源 id resource

  UI 的创建必然依赖了布局文件资源的引用；

- 第二个参数，root

  此参数可为 null。

  对于`Activity`UI的创建而言，根结点最顶层的`ViewGroup`必然是没有父控件的，这时在布局的创建时，就必须通过将`null`作为第二个参数交给`LayoutInlater`的`inflate()`方法，当`View`被创建好后，将`View`的布局参数配置为对应屏幕的宽高：

  ```java
  // DecorView.onResourcesLoaded()函数
  void onResourcesLoaded(LayoutInflater inflater, int layoutResource) {
      // ...
      // 创建最顶层的布局时，需要指定父布局为null
      final View root = inflater.inflate(layoutResource, null);
      // 然后将宽高的布局参数都指定为 MATCH_PARENT（屏幕的宽高）
      mDecorCaptionView.addView(root, new ViewGroup.MarginLayoutParams(MATCH_PARENT, MATCH_PARENT));
  }
  ```

- 第三个参数，attachToRoot

  boolean 类型的值，用以决定是否将创建的 View 直接添加到指定的 ViewGroup 中。

  这个问题的本质其实是：是否每个`View`的创建都必须立即添加在`ViewGroup`中？答案当然是否定的，为了保证性能，设计者不可能让所有的`View`被创建后都能够立即被立即添加在`ViewGroup`中，这与目前`Android`中很多组件的设计都有冲突，比如`ViewStub`、`RecyclerView`的条目、`Fragment`等等。

  因此，通过一个`boolean`的开关将整个过程切分成2个小步骤，当`View`生成并根据`ViewGroup`的布局参数生成了对应的测量依据后，开发者可以根据需求手动灵活配置是否立即添加到`ViewGroup`中——这就是第三个参数的由来。

##### 2.2 xml 解析流程

`xml`解析过程的思路：

- 首先根据布局文件，生成对应布局的 XmlPullParser 解析器对象；
- 对于单个 View 的解析而言，一个 View 的实例化依赖 Context 上下文对象和 attr 的属性集，而设计者正是通过将上下文对象和属性集作为参数，通过 **反射** 注入到 View 的构造函数中对单个 View 进行创建；
- 对于整个 xml 文件的解析而言，整个流程依然是通过递归的思想，对 xml 文件中的布局进行遍历解析，自底至顶对 View 依次进行创建，最终完成了整个 View 树的创建；



单个`View`的实例化实现如下，这里采用伪代码的方式实现：

```java
// LayoutInflater类
public final View createView(String name, String prefix, AttributeSet attrs) {
    // ...
    // 1.根据View的全名称路径，获取View的Class对象
    Class<? extends View> clazz = mContext.getClassLoader().loadClass(name + prefix).asSubclass(View.class);
    // 2.获取对应View的构造器
    Constructor<? extends View> constructor = clazz.getConstructor(mConstructorSignature);
    // 3.根据构造器，通过反射生成对应 View
    args[0] = mContext;
    args[1] = attrs;
    final View view = constructor.newInstance(args);
    return view;
}
```

对于整体解析流程而言，伪代码实现如下：

```java
void rInflate(XmlPullParser parser, View parent, Context context, AttributeSet attrs) {
  // 1.解析当前控件
  while (parser.next()!= XmlPullParser.END_TAG) {
    final View view = createViewFromTag(parent, name, context, attrs);
    final ViewGroup viewGroup = (ViewGroup) parent;
    final ViewGroup.LayoutParams params = viewGroup.generateLayoutParams(attrs);
    // 2.解析子布局
    rInflateChildren(parser, view, attrs, true);
    // 所有子布局解析结束，将当前控件及布局参数添加到父布局中
    viewGroup.addView(view, params);
  }
}

final void rInflateChildren(XmlPullParser parser, View parent, AttributeSet attrs, boolean finishInflate){
  // 3.子布局作为根布局，通过递归的方式，层级向下一层层解析
  // 继续执行 1
  rInflate(parser, parent, parent.getContext(), attrs, finishInflate);
}
```

至此，一般情况下的布局填充流程到此结束，`inflate()`方法执行完毕，对应的布局文件解析结束，并根据参数配置决定是否直接添加在`ViewGroup`根布局中。



### 五、拦截机制和解耦策略

**问题：**

- 布局的加载流程中，每一个 View 的实例化都依赖了 Java 的反射机制，这意味着额外的性能损耗；
- 如果在 xml 布局中声明了一个 fragment 标签，会导致模块之间的极高耦合。

什么叫做 **fragment标签会导致模块之间极高的耦合** ？举例来说，开发者在`layout`文件中声明这样一个`Fragment`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <!-- 声明一个fragment -->
    <fragment
        android:id="@+id/fragment"
        android:name="com.github.qingmei2.myapplication.AFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

</android.support.constraint.ConstraintLayout>
```

如果这是一个v4包的`Fragment`，是否意味着`LayoutInflater`额外增加了对`Fragment`类的依赖，类似这样：

```java
// LayoutInflater类
void rInflate(XmlPullParser parser, View parent, Context context, AttributeSet attrs) {
  // 1.解析当前控件
  while (parser.next()!= XmlPullParser.END_TAG) {
    //【注意】2.如果标签是一个Fragment，反射生成Fragment并返回
    if (name == "fragment") {
      Fragment fragment = clazz.newInstance();
      // .....还会关联到SupportFragmentManager、FragmentTransaction的依赖！
      supportFragmentManager.beginTransaction().add(....).commit();
      return;
    }

    final View view = createViewFromTag(parent, name, context, attrs);
    final ViewGroup viewGroup = (ViewGroup) parent;
    final ViewGroup.LayoutParams params = viewGroup.generateLayoutParams(attrs);
    // 3.解析子布局
    rInflateChildren(parser, view, attrs, true);
    // 所有子布局解析结束，将当前控件及布局参数添加到父布局中
    viewGroup.addView(view, params);
  }
}
```

**解决思路**：

考虑到 **性能优化** 和 **可扩展性**，设计者为`LayoutInflater`设计了一个`LayoutInflater.Factory`接口：在`xml`解析过程中，开发者可以通过配置该接口对`View`的创建过程进行拦截：**通过new的方式创建控件以避免大量地使用反射**，亦或者 **额外配置特殊标签的解析逻辑以创建特殊组件** ：

```java
public abstract class LayoutInflater {
  private Factory mFactory;
  private Factory2 mFactory2;
  private Factory2 mPrivateFactory;

  public void setFactory(Factory factory) {
    //...
  }

  public void setFactory2(Factory2 factory) {
      // Factory 只能被set一次
      if (mFactorySet) {
          throw new IllegalStateException("A factory has already been set on this LayoutInflater");
      }
      mFactorySet = true;
      mFactory = mFactory2 = factory;
      // ...
  }

  public interface Factory {
    public View onCreateView(String name, Context context, AttributeSet attrs);
  }

  public interface Factory2 extends Factory {
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs);
  }
}
```

`Factory`接口的意义是在`xml`解析过程中，开发者可以通过配置该接口对`View`的创建过程进行拦截，对于`View`的实例化，最终实现的伪代码如下：

```java
View createViewFromTag() {
  View view;
  // 1. 如果mFactory2不为空, 用mFactory2 拦截创建 View
  if (mFactory2 != null) {
      view = mFactory2.onCreateView(parent, name, context, attrs);
  // 2. 如果mFactory不为空, 用mFactory 拦截创建 View
  } else if (mFactory != null) {
      view = mFactory.onCreateView(name, context, attrs);
  } else {
      view = null;
  }

  // 3. 如果经过拦截机制之后，view仍然是null，再通过系统反射的方式，对View进行实例化
  if (view == null) {
      view = createView(name, null, attrs);
  }
}
```



**减少反射次数**

`AppCompatActivity`的源码中隐晦地配置`LayoutInflater.Factory`减少了大量反射创建控件的情况——设计者的思路是，在`AppCompatActivity`的`onCreate()`方法中，为`LayoutInflater`对象调用了`setFactory2()`方法：

```java
// AppCompatActivity类
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    getDelegate().installViewFactory();
    //...
}

// AppCompatDelegateImpl类
@Override
public void installViewFactory() {
    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
    if (layoutInflater.getFactory() == null) {
      LayoutInflaterCompat.setFactory2(layoutInflater, this);
    }
}
```

配置之后，在`inflate()`过程中，系统的基础控件的实例化都通过代码拦截，并通过`new`的方式进行返回：

```java
switch (name) {
    case "TextView":
        view = new AppCompatTextView(context, attrs);
        break;
    case "ImageView":
        view = new AppCompatImageView(context, attrs);
        break;
    case "Button":
        view = new AppCompatButton(context, attrs);
        break;
    case "EditText":
        view = new AppCompatEditText(context, attrs);
        break;
    // ...
    // Android 基础组件都通过new方式进行创建
}
```

源码也说明了，即使开发者在`xml`文件中配置的是`Button`，`setContentView()`之后，生成的控件其实是`AppCompatButton`, `TextView`或者`ImageView`亦然，在避免额外的性能损失的同时，也保证了`Android`版本的向下兼容。



**特殊标签的解析策略**

为什么`Fragment`没有定义类似`void setContentView(R.layout.xxx)`的函数对布局进行填充，而是使用了`View onCreateView()`这样的函数，让开发者填充并返回一个对应的`View`呢？

原因就在于在布局填充的过程中，`Fragment`最终被视为一个子控件并添加到了`ViewGroup`中，设计者将`FragmentManagerImpl`作为`FragmentManager`的实现类，同时实现了`LayoutInflater.Factory2`接口。

而在布局文件中`fragment`标签解析的过程中，实际上是调用了`FragmentManagerImpl.onCreateView()`函数，生成了`Fragment`之后并将`View`返回，跳过了系统反射生成`View`相关的逻辑:

```java
# android.support.v4.app.FragmentManager$FragmentManagerImpl
@Override
public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
   if (!"fragment".equals(name)) {
       return null;
   }
   // 如果标签是`fragment`，生成Fragment，并返回Fragment的Root
   return fragment.mView;
}
```

通过定义`LayoutInflater.Factory`接口，设计者将`Fragment`的功能抽象为一个`View`（虽然`Fragment`并不是一个`View`），并交给`FragmentManagerImpl`进行处理，减少了模块之间的耦合，可以说是非常优秀的设计。



### 六、其它