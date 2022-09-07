### Android基础

#### 一、Activity

##### 1、Activity 的生命周期相关？

###### 1.1 Activity 的生命周期？

![img](https://developer.android.com/guide/components/images/activity_lifecycle.png?hl=zh-cn)

###### 1.2 A Activity 打开 B Activity 生命周期？

<font color='red'>启动 Activity 的请求会由 Instrumentation 来处理，然后它通过 Binder 向 AMS 发请求，AMS 内部维护着一个 ActivityStack 并负责栈内的 Activity 的状态同步，AMS 通过 ActivityThread 去同步 Activity 的状态从而完成生命周期的调用。</font>在 ActivityStack 中的 resumeTopActivityInnerLocked() 中控制着，在新的 Activity 启动之前，栈顶的 Activity 需要先 onPause() 后，新的 Activity 才能启动。

由 ActivityStack 中的代码决定：

- A 启动 B

  A onPause()--->B onCreate()--->B onStart()--->B onResume()--->A onStop()

- B 按返回键

  B onPause()--->A onRestart()--->A onStart()--->A onResume()--->B onStop()--->B onDestroy()

如果 B 是透明主题的 Activity 就不会走 A 的 onStop()。

这样做的原因应该是为了，视觉效果体验上更好些，如果前一个 Activity 的 onStop() 先走 页面不可见，而后一个 Activity 的 onResume() 还没走 还没显示出来，可能会有一瞬间的白屏，体验不好。

###### 1.3 生命周期相关问题

- 下拉手机状态栏 Activity 会走生命周期吗？

  <font color='red'>不会</font>

- onStart() 和 onResume() 的区别？

  - onStart() 表示 Activity 正在启动，此时 Activity 已经出现，但还没有处于前台，无法与用户交互；
  - onResume() 表示 Activity 已经可见，并且出现在前台，可以与用户交互；

- onPause() 和 onStop() 的区别？

  - onPause() 表示 Activity 正在停止，失去焦点，仍可见；
  - onStop() 表示 Activity 不可见，处于后台；

  场景：

  - 锁屏的时候会依次调用 onPause() 和 onStop()；
  - Toast、Dialog、menu 三者都不会使 Activity 调用 onPause()；
  - 一个非全面屏的 Activity 在前面时，后面的 Activity 只调用 onPause()；

- onPause() 中为什么不能做耗时操作？

  因为 onPause() 执行完，新的 Activity 的 onResume() 才会执行，耗时操作影响新 Activity 的显示。

- Activity 上有 Dialog 的时候按 Home 键时的生命周期？

  弹出 Dialog 并不会使得 Activity 走任何生命周期。

  按下 Home 键后：

  onPause()--->onStop()

  我们弹出的AlertDialog对话框实际上是Activity的一个组件，我们对Activity并不是不可见而是被一个布满屏幕的组件覆盖掉了其他组件，所以我们无法对其他内容进行操作，也就是AlertDialog实际上是一个布满全屏的组件。

  <font color='red'>怎样使得，按 Home 键后，再次点击图标进入 App，还在之前的页面？</font>

  这个是因为之前以主页面为启动页面，且模式为 singleTask，所以每次按 Home 键再返回启动 App 后会销毁掉之前打开的页面。只要修改启动模式或者增加一个 SplashActivity 即可。



##### 2、Activity 的启动模式？

###### 2.1 四种启动模式

1. 标准模式（standard）

   每次启动一个 Activity 都会重新创建一个新的实例，不管这个实例是否已经存在。

2. 栈顶复用模式（singleTop）

   如果新 Activity 已经位于任务栈的栈顶，那么此 Activity 不会被重新创建，同时它的 onNewIntent() 方法会被回调，通过此方法的参数我们可以取出当前请求的信息。

3. 栈内复用模式（singleTask）

   只要 Activity 在一个栈中存在，那么多次启动此 Activity 都不会重新创建实例，回调 onNewIntent() ，同时 clearTop，将该 Activity 之上的 Activity 都弹出栈。

4. 单例模式（singleInstance）

   此模式的 Activity 只能单独位于一个任务栈中，同时栈内复用，后续的请求均不会创建新的 Activity 实例。

5. TaskAffinity

   这个参数标识了一个 Activity 所需要的任务栈的名字，默认情况下，所有 Activity 所需的任务栈的名字为应用的包名。

   可以在AndroidManifest.xml中设置 android:taskAffinity，用来指定Activity希望归属的栈。

   TaskAffinity 属性主要和 singleTask 启动模式或者 allowTaskReparenting 属性配对使用。（<font color='red'>单独使用 taskAffinity 属性不生效，两个启动模式均为 standard 的 Activity，指定不同的 taskAffinity 还是会创建在同一个任务栈中</font>）

   ①taskAffinity 与 FLAG_ACTIVITY_NEW_TASK 或者 singleTask 配合。如果新启动 Activity 的 taskAffinity 和栈的 taskAffinity 相同则加入到该栈中；如果不同，就会创建新栈。
   ②taskAffinity 与 allowTaskReparenting 配合。如果 allowTaskReparenting 为true，说明activity具有转移能力。一个在后台任务栈中的 Activity A，当有其他任务进入前台，并且 taskAffinity 与 A 相同，则会自动将 A 添加到当前启动的任务栈中。

###### 2.2 启动模式使用场景

1. singleTop 的使用场景

   - 消息推送

     通知栏弹出Notification，点击Notification跳转到指定Activity，但是如果我现在页面就停留在那个指定的Activity，会再次打开我当前的Activity，这样返回的时候回退的页面和当前页面一样，感官上就会很奇怪。

   - 登录

   - 耗时操作返回页面

     一种场景 从activity A启动了个service进行耗时操作，或者某种监听，这个时候你home键了，service收集到信息，要返回activityA了，就用singleTop启动，实际不会创建新的activityA，只是resume了。不过使用standard又会创造2个A的实例。

2. singleTask

   - 一般App主页面会采用 singleTask 的启动模式

     保证任务栈中只有一个 MainActivity，不管打开多少个页面，再次打开 MainActivity 时都会将其上方所有的 Activity 弹出栈，保证退出应用的时候，Activity 都能够销毁。

###### 2.3 onNewIntent()

singleTop、singleTask、singleInstance 模式下的 Activity，再次启动该 Activity 会走 onNewIntent()：

- 该页面处于可见状态，onPause()---onNewIntent()---onResume()
- 该页面处于不可见状态
  - singleTop 不会走 onNewIntent()
  - singleTask、singleInstance() 会走 B onPause()---A onRestart()---onStart()---onNewIntent()---onResume()

> 有时候，内存吃紧的情况下，系统可能会kill掉后台运行的Activity，如果不巧要启动的Activity实例被系统kill了，那么系统就会**调用**`onCreate()`方法，而**不是调用**`onNewIntent()`方法（即`onCreate()`和`onNewIntent()`**不会都调用**，**只会调用**其中**一个**方法）。

##### 3、Activity 状态的保存与恢复？

在 Activity 由于异常情况下终止时，系统会调用 onSaveInstanceState() 来保存当前 Activity 的状态。该方法只在Activity被异常终止的情况下调用。当异常终止的Activity被重建以后，系统会调用 onRestoreInstanceState() ，并且把 Activity 销毁时onSaveInstanceState方法所保存的Bundle对象参数同时传递给 onRestoreInstanceState() 和 onCreate() 方法。

###### 3.1 使用场景

系统配置发生改变时导致 Activity 被杀死并重新创建、资源内存不足导致低优先级的 Activity 被杀死
系统会用onSaveInstanceState()来保存Activity的状态，调用时机在onStop()之后（Android api为28时，onStop会在其之前，28以前是在其之后）。

场景：

- 横竖屏切换
- 按home键返回，不管是返回桌面还是切到其它程序，都会走
- 熄屏
- 启动一个新的Activity时

​    onSaveInstanceState方法和onRestoreInstanceState方法“不一定”是成对的被调用的，
​    onRestoreInstanceState被调用的前提是，activity A“确实”被系统销毁了，而如果仅仅是停留在有这种可能性的情况下，则该方法不会被调用

​    比如②③④都不会走onRestoreInstanceState()

###### 3.2 横竖屏切换

- 默认情况

  onPause()-> onStop()->onSaveInstanceState()->onDestroy()->onCreate()->onStart()->onRestoreInstanceState->onResume()

- 在 Mainfest 中配置 configChanges="orientation|screenSize"

  只会走 onConfigurationChanged()

###### 3.3 锁屏、解锁的生命周期

- 锁屏：onPause()--->onStop()---onSaveInstanceState()
- 解锁：onRestart()--->onStart()--->onResume()

###### 3.4 弹出 Dialog 对 Activity 生命周期的影响

生命周期回调都是 AMS 通过 Binder 通知应用进程调用的；而弹出 Dialog、Toast、PopupWindow 本质上都是直接通过 WindowManager.addView() 显示的（没有经过 AMS），所以不会对生命周期有任何影响。

如果是启动一个 Theme 为 Dialog 的 Activity，则生命周期为：

A onPause()--->B onCreate()--->B onStart()--->B onResume()

> 因为弹出 Dialog 主题的 Activity 时，前一个页面还是可见的，所以不会走 onStop()

##### 4、App 间的跳转

###### 4.1 调起其他 App 的方法

1. 通过包名拉起其他 App，进入之后打开的是启动页

   ```java
   Intent intent = getPackageManager().getLaunchIntentForPackage("com.xht.test");
   if(test != null) {
       intent.putExtra("name","xht");
       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       startActivity(intent);
   }
   ```

2. 通过 ComponentName 打开指定 App 的指定页面

   需要将要打开的页面在 AndroidManifest 中声明为 exported = true

   ```java
   Intent intent = new Intent(Intent.ACTION_MAIN);
   ComponentName componentName = new ComponentName("com.xht.testapp", "com.xht.testapp.LoginActivity");
   intent.setComponent(componentName);
   startActivity(intent);
   ```

3. 通过 url 打开其他 App

   需要在要打开的 Activity 添加 `<intent-filter>` 声明约定的 scheme、host、path 等

   ```xml
   <intent-filter>
   	<data
   		android:host="pull.xht.demo"
   		android:path="/login"
   		android:scheme="xht" />
   	<action android:name="android.intent.action.VIEW" />

   	<category android:name="android.intent.category.DEFAULT" />
   	<category android:name="android.intent.category.BROWSABLE" />
   </intent-filter>
   ```

   ```java
   Intent intent = new Intent();
   intent.setData(Uri.parse("xht://pull.xht.demo/login?type=110"));
   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   startActivity(intent);
   ```

###### 4.2 任务栈相关

1. App A 页面 A1 打开 App B 页面 B1，此时 B1 的任务栈 TaskRecord 和 A1 相同，按返回键 返回的是 A1 页面。

   ```java
   Running activities (most recent first):
         TaskRecord{1ad7c09 #13649 A=com.xht.androidnote U=0 StackId=3678 sz=3}
           Run #2: ActivityRecord{1d16ae9 u0 com.xht.testapp/.LoginActivity t13649}
           Run #1: ActivityRecord{1d38209 u0 com.xht.androidnote/.module.activity.ATestActivity t13649}
           Run #0: ActivityRecord{1d383c9 u0 com.xht.androidnote/.MainActivity t13649}

       mResumedActivity: ActivityRecord{1d16ae9 u0 com.xht.testapp/.LoginActivity t13649}
       mLastPausedActivity: ActivityRecord{1d38209 u0 com.xht.androidnote/.module.activity.ATestActivity t13649}
   ```



2. Application 的 context 启动 Activity 会怎样？

   使用 Application 和 Service 启动 launchMode 为 standard 的 Activity 会报错，因为它们没有任务栈，需要为待启动的 Activity 指定 FLAG_ACTIVITY_NEW_TASK，但此时 Activity 是以 singleTask 模式启动的。



#### 二、Fragment

##### 1、Fragment 生命周期

###### 1.1 Activity 与 Fragment 生命周期

```java
Activity---onCreate()
Fragment---onAttach()-onCreate()-onCreateView()-onViewCreated()-onActivityCreated()-onStart()
Activity---onStart()-onResume()
Fragment---onResume()

Fragment---onPause()
Activity---onPause()
Fragment---onStop()
Activity---onStop()
Fragment---onDestroyView()-onDestroy()-onDetach()
Activity---onDestroy()
```

###### 1.2 replace、add/hide Fragment 生命周期

- replace

  fragment 会销毁重新创建

  FragmentOne onAttach() ... onResume()

  替换为 FragmentTwo

  FragmentTwo onAttach()、onCreate()

  FragmentOne onPause()、onStop()、onDestroyView()、onDestroy()、onDetach()

  FragmentTwo onCreateView() ... onResume()

- add/hide

  fragment 不会销毁，走 onHiddenChanged() 方法

##### 3、FragmentPagerAdapter 和 FragmentStatePagerAdapter 的区别？

两者不同的地方在于 fragment 存储、恢复、销毁的方式不同

> 对滑动过去的页面是否销毁
>
> 例如：依次从左向右有 fragment1、fragment2、fragment3 三个页面
>
> - FragmentPagerAdapter
>
>   切换 Fragment 过程中不会销毁 Fragmet，只是会销毁 Fragment 中的 View。
>
>   比如 滑动到fragment3时，fragment1会依次调用onPause()、onStop()、onDestroyView()，再向左滑动到fragment2时，fragment1会调用onCreateView()、onActivityCreated()、onStart()、onResume()。
>
>   适合比较固定的少量 Fragment。
>
> - FragmentStatePagerAdapter
>
>   切换 Fragment 时会销毁 Fragment 实例，需要初始化的时候，会重新初始化页面。
>
>   比如在滑动到fragment3时，fragment1会依次调用onPause()、onStop()、onDestroyView()、onDestroy()、onDetach()方法，再向左滑动到fragment2时，fragment1会调用onAttach()、onCreate()、onCreateView()、onActivityCreated()、onStart()、onResume()。
>
>   适合 Fragment 数量较多的时候。



##### 4、Fragment 懒加载

以前处理 Fragment 的懒加载，我们通常会在 Fragment 中处理 `setUserVisibleHint + onHiddenChanged` 这两个函数，而在 Androidx 模式下，我们可以使用 `FragmentTransaction.setMaxLifecycle()` 的方式来处理 Fragment 的懒加载。



##### 5、Fragment 通信

1. Fragment 之间的通信方式

   - 通过宿主 Activity
   - getActivity() 根据 tag 找到对应的 Fragment，调用其方法
   - 接口回调
   - EventBus
   - 广播

2. Activity 与 Fragment 之间的通信方式

   - 创建 fragment 时传入数据，通过 bundle
   - 通过接口回调
   - 在 Activity 中定义方法，fragment 中 getActivity() 获取
   - 通过 EventBus 等框架
   - 广播

3. 系统为何会设计Fragment#setArgument方法

   因为设备横竖屏切换时，当前展示给用户的 Activity 默认情况下会重新创建并展示给用户，此时依附于 Activity 的 Fragment 也会重新创建。

   > 由于我们的 Fragment 是由 FragmentManager 来管理，所以可以跟进 FragmentManager.restoreAllState() 方法，最终会通过反射无参构造函数实例化一个新的 Fragment，并且给 mArguments 初始化为原先的值，而原来的 Fragment 实例的数据都丢失了，并重新进行了初始化。
   >
   > 通过上面的分析，可以知道 Activity 重新创建时，会重新构建它所管理的 Fragment，原先的 Fragment 的字段值将会全部丢失，但是通过 `Fragment.setArguments(Bundle bundle)`方法设置的 bundle 会保留下来。
   >
   > 所以尽量使用 Fragment.setArguments(Bundle bundle) 方式来传递参数。

##### 6、ViewPager+三个Fragment的生命周期？加载第一个的时候，第一第二的生命周期，第三个会不会走？怎样实现懒加载？

- 默认预加载1个，刚进入页面，前两个都会走到onResume()，第三个 Fragment 不会走生命周期，等点击第二个 tab 的时候，第三个才 onResume()，此时第一个 hiden()；

  切换到第三个时，第一个会onDestroyView()；

- 懒加载，自定义一个基类，通过setUserVisibleHint()来实现

##### 7、Fragment 的使用场景

- 适配手机平板。

- 一般主页面Activity+多个fragment，减少内存消耗，提升性能。

- 面对非常复杂的页面时，可以把它拆分成几个fragment，这样就分担了activity的压力。

  activity只需要负责管理fragment的生命周期和数据传递，具体的业务逻辑由对应的fragmnet处理。

- 需要对某个页面进行反复替换——动态添加碎片。

- 结合 ViewPager 作为导航栏，实现切换。



#### 三、Service

##### 1、Service 的两种启动方式及区别？

- startService()

  - startService() 会调用service的 onCreate()、onStartCommand()，调用 stopService() 后就执行 service 的 onDestroy()；
  - 第一次 startService 会触发 onCreate 和 onStartCommand，以后在服务运行过程中，每次 startService 都只会触发 onStartCommand；
  - 不论 startService 多少次，stopService 一次就会停止服务；
  - 和调用者生命周期无关，调用者结束之后 service 并不会销毁；

- bindService()

  - bindService() 则会调用 service 的 onCreate()、onBind()，调用 unBindService() 后就执行 service 的onDestroy()；
  - 当调用者生命结束了，系统会自动调用 unBindService()、onDestory()；
  - 第一次 bindService 会触发 onCreate 和 onBind，以后在服务运行过程中，每次 bindService 都不会触发任何回调；
  - 可以通过IBinder接口获取Service的实例，从而可以实现在client端直接调用Service中的方法以实现灵活的交互，并且可借助IBinder实现跨进程的client-server的交互；

  > 当同时执行了启动和绑定同一个service时，则只有当以上两个条件都满足时service才会结束掉。



##### 2、Activity  与 Service 之间的通信

- Binder

  - Service 中 自定义 MyBinder，提供一个获取 Service 的方法 getService()，在 Service 类中 重写 onBind() 方法，返回 MyBinder 对象。
  - Activity 中 在 ServiceConnection 的回调用中获取 Binder 并获取 Service 实例，就可调用 Service 中的方法。

- 广播

  - Service 中 sendBroadcast(intent)；
  - Activity 中注册广播接收器，接收 Service 发送的广播；

  > 适用于 Service 要向多个 Activity 发送同样的消息。

##### 3、一个 service 和 5个 activity 绑定，有几个 service 实例？4个 activity 退出，service 还在吗？

- 多个 activity 与 service 绑定只会在第一个 activity 绑定时走 onCreate() 和 onBind() 回调，service 实例在多个 activity 之间是共享的，后面的 activity 绑定 service 时，不会再走 onCreate() 和 onBind() 回调，但是会走 onServiceConnected() 回调；
- 当一个 activity 解除绑定或者退出销毁时，只要还有其他 activity 与 service 绑定，是不会走 onUnbind() 和 onDestroy() 方法的，只有最后一个 activity 解除绑定、退出销毁时，service 才会走 onUnbind() 和 onDestroy() 回调，销毁 service 实例。

##### 4、Service 启动过程

1. Service 的启动过程从 ContextWrapper#startService() 开始；

   mBase 的类型是 ContextImpl，Activity 被创建的时候会通过 attach() 将一个 ContextImpl 的对象传递赋值给 mBase。

2. ContextImpl#startService()--->startServiceCommon()--->AMS#startService()

3. AMS 通过 mServices 这个对象来完成 Service 后续的启动过程，mServices 对象类是是 ActivieServices，它是一个辅助 AMS 进行 Service 管理的类，包括 Service 的启动、绑定和停止等。

4. startServiceLocked()--->startServiceInnerLocked()--->bringUpServiceLocked()--->realStartServiceLocked()--->app.thread.scheduleCreateService(()

   此处的 app.thread 即 ActivityThread 中的内部类 ApplicationThread

5. scheduleCreateService() 中通过 Handler 发送消息 CREATE_SERVICE，通过 handleCreateService() 进行处理，通过反射创建 Service，调用 service.attahc()、onCreate()

6. 然后通过 ActivieServices#sendServiceArgsLocked() 调用其他方法，如 onStartCommond()，也是通过 app.thread.scheduleServiceArgs() 调用的。

> 总结：
>
> Service 的启动从 ContextWrapper#startService() 开始，一直到 AMS#startService()，然后通过 IPC 调用 ActivityThread#ApplicationThread 中的方法，通过 Handler 发送消息进行处理，创建 Service 实例，回调 onCreate() 等方法。



#### 四、BroadcastReceiver

