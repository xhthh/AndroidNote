### Kotlin 随笔

### 一、基础

#### 1、类与对象

构造函数中的变量声明加 val 和 var

主构造函数的参数加上 var 和 val 这只是声明属性以及从主构造函数初始化属性的一种简洁的语法，意思是将该变量作为类的成员变量来使用，是因为主构造函数是类头的一部分吧，在这里直接声明属性使得属性的声明变得很方便、简洁。

#### 2、属性与字段

#### 3、接口

#### 4、函数式接口

#### 5、可见性修饰符

#### 6、扩展

Kotlin 能够扩展一个类的新功能而无需继承该类或者使用像装饰者这样的设计模式。 这通过叫做 *扩展* 的特殊声明完成。 例如，你可以为一个你不能修改的、来自第三方库中的类编写一个新的函数。 这个新增的函数就像那个原始类本来就有的函数一样，可以用普通的方法调用。 这种机制称为 *扩展函数* 。此外，也有 *扩展属性* ， 允许你为一个已经存在的类添加新的属性。

> kotlin中的扩展函数，实际上就是通过给类添加public static final函数的做法来实现，这样做可以减少utils类的使用。

- **扩展是静态解析的**

  - 扩展函数是由函数调用所在的表达式的类型来决定的， 而不是由表达式运行时求值结果决定的。

    ```kotlin
    open class Shape
    
    class Rectangle: Shape()
    
    fun Shape.getName() = "Shape"
    
    fun Rectangle.getName() = "Rectangle"
    
    fun printClassName(s: Shape) {
        println(s.getName())
    }    
    
    printClassName(Rectangle())//输出 Shape，因为 s 在方法中声明的类型为 Shape
    ```

  - 如果一个类定义有一个成员函数与一个扩展函数，而这两个函数又有相同的接收者类型、 相同的名字，并且都适用给定的参数，这种情况**总是取成员函数**。

- **可空接收者**

  即可以对一个可能为空的对象进行扩展，在其方法内部判断是否为空；

  ```kotlin
  fun Any?.toString(): String {
      if(this == null) return "null"
      //空检测之后，“this”会自动转换为非空类型，所以下面的 toString() 解析为 Any 类的成员函数
      return toString()
  }
  ```

#### 7、数据类

#### 8、密封类

#### 9、泛型

#### 10、嵌套类

#### 11、枚举类

#### 12、对象

#### 13、类型别名

#### 14、内联类

#### 15、委托

委托模式在 Kotlin 中的实现，通过 `by` 关键字

```kotlin
interface Base {
    fun print()
}
class BaseImpl(val x: Int) : Base {
    override fun print() { print(x) }
}
class Derived(b: Base) : Base by b
fun main() {
    val b = BaseImpl(10)
    Derived(b).print()
}
```

`Derived` 的超类型列表中的 *by*-子句表示 `b` 将会在 `Derived` 中内部存储， 并且编译器将生成转发给 `b` 的所有 `Base` 的方法。

> 在被委托类中，可以覆盖由委托实现的接口成员，编译器会使用覆盖实现，而不是委托对象中的。但是，以这种方式重写的成员不会在委托对象的成员中调用，委托对象的成员只能访问其自身对接口成员实现。举个栗子：在被委托类中声明一个和委托对象中一样的成员变量，在调用委托对象的方法打印这个变量时，只会显示委托对象中的数据，因为委托对象访问不到覆盖的这个变量。

#### 16、委托属性

- 延迟属性 Lazy

  [`lazy()`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/lazy.html) 是接受一个 lambda 并返回一个 `Lazy <T>` 实例的函数，返回的实例可以作为实现延迟属性的委托： 第一次调用 `get()` 会执行已传递给 `lazy()` 的 lambda 表达式并记录结果， 后续调用 `get()` 只是返回记录的结果。

  ```kotlin
  val lazyValue: String by lazy {
      println("computed!")
      "Hello"
  }
  
  fun main() {
      println(lazyValue)
      println(lazyValue)
  }
  ```

- 可观察属性 Observable

  [`Delegates.observable()`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.properties/-delegates/observable.html) 接受两个参数：初始值与修改时处理程序（handler）。 每当我们给属性赋值时会调用该处理程序（在赋值*后*执行）。它有三个参数：被赋值的属性、旧值与新值：

  ```kotlin
  class User {
      var name: String by Delegates.observable("<no name>") {
          prop, old, new ->
          println("$old -> $new")
      }
  }
  
  fun main() {
      val user = User()
      user.name = "first"
      user.name = "second"
  }
  ```

- 委托给另一个属性

  为将一个属性委托给另一个属性，应在委托名称中使用恰当的 `::` 限定符，例如，`this::delegate` 或 `MyClass::delegate`。

  ```kotlin
  class MyClass {
     var newName: Int = 0
     @Deprecated("Use 'newName' instead", ReplaceWith("newName"))
     var oldName: Int by this::newName
  }
  
  fun main() {
     val myClass = MyClass()
     // 通知：'oldName: Int' is deprecated.
     // Use 'newName' instead
     myClass.oldName = 42
     println(myClass.newName) // 42
  }
  ```

  



### 二、函数与 Lambda 表达式

#### 1、Lambda 表达式语法

> val sum: (Int, Int) -> Int = { x: Int, y: Int -> x + y }



### 三、集合



### 四、协程

https://www.jianshu.com/p/76d2f47b900d

**协程**，也叫微线程，是一种多任务并发操作的手段；

- 特点：协程是运行在单线程中的并发程序；
- 优点：省去了传统 Thread 多线程并发机制中切换线程时带来的线程上下文切换、线程状态切换、Thread 初始化上的性能损耗，能大幅度提高并发新能；
- 简单理解：在单线程上由程序员自己调度运行的并行计算；

> 协程开发人员这样描述协程：协程就像非常轻量级的线程。线程是由系统调度的，线程的切换或线程阻塞的开销都比较大。而协程依赖于线程，但是协程挂起时不需要阻塞线程，几乎是无代价的，协程是由开发者控制的。所以协程也像用户态的线程，非常轻量级，一个线程中可以创建任意个协程。

> Coroutine，翻译成“协程”。
>
> - Coroutine 是编译器级的，Process 和 Thread 是操作系统级的。
>
> - Coroutine 的实现，通常是对某个语言做相应的提议，通过后成为编译器标准，由编译器厂商来实现该机制。
>
>   Process 和 Thread 看起来也在语言层次，但却是操作系统先有这个东西，然后通过一定的 API 暴露给用户使用。
>
> - Process 和 Thread 是 os 通过调度算法，保存当前的上下文，然后从上次暂停的地方再次开始计算，重新开始地方不可预期，每次 CPU 计算的指令数量和代码跑过的 CPU 时间是相关的，os 分配的 cpu 时间到达后寄回被 os 强制挂起。
>
>   Coroutine 是由编译器实现，通过插入相关的代码使得代码能够实现分段式的执行，重新开始的地方是 yield 关键字指定的，一次一定会跑到一个 yield 对应的地方；、



#### 1、创建协程

kotlin 中 GlobalScope 类提供了几个协程构造函数：

- launch - 创建协程
- async - 创建带返回值的协程，返回的是 Deferred 类
- withContext - 不创建新的协程，在指定协程上运行代码块
- runBlocking - 不是 GlobalScope 的 API，可以独立使用，区别是 runBlocking 里面的 delay 会阻塞线程，而 launch 创建的不会

kotlin 在 1.3 之后要求协程必须由 CoroutineScope 创建，CoroutineScope 不阻塞当前线程，在后台创建一个新协程，也可以指定协程调度器。比如 CoroutineScope.launch{} 可以看成 new Coroutine。

**GlobalScope.launch**

```kotlin
public fun CoroutineScope.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job

/*
launch 是扩展函数，前面2个是常规参数，最后一个是对象函数，使用闭包的写法，() 中写常规参数，{}中写函数式对象的实现。
*/
fun test() {
    //方法声明中，参数若有默认值，可省略
	GlobalScope.launch(Dispatchers.Unconfined) {
        //todo
    }    
}
```

- CoroutineContext，理解为协程的上下文，有 4 种线程模式：

  - Dispatchers.Default
  - Dispatchers.IO
  - Dispatchers.Main - 主线程
  - Dispatchers.Unconfined - 没指定，就是在当前线程

  不写的话就是 Dispatchers.Default 模式的，我们也可以自己创建协程上下文，也就是线程池：

  - newSingleThreadContext - 单线程
  - newFixedThreadPoolContext - 线程池

- CoroutineStart - 启动方式，默认是 DEFAULT，也就是创建就启动；还有一个是 LAZY，是等你需要它的时候再调用启动。

  - DEAFAULT - 模式模式，不写就是默认
  - ATOMIC -
  - UNDISPATCHED
  - LAZY - 懒加载模式，你需要它的时候，再调用启动

- block - 闭包方法体，定义协程内需要执行的操作

- Job - 协程构建函数的返回值，可以把 Job 看成协程对象本身，协程的操作方法都在 Job 身上：

  - job.start() - 启动协程，除了 lazy 模式，协程都不需要手动启动
  - job.join() - 等待协程执行完毕
  - job.cancel() - 取消一个协程
  - job.cancelAndJoin() - 等待协程执行完毕然后再取消

**GlobalScope.async**

async 与 launch 唯一的区别就是 async 是有返回值的，deferred

```kotlin
public fun <T> CoroutineScope.async(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> {}

//Deferred 继承自 Job 接口，Job 有的它都有，增加了一个 await 方法，这个方法接收的是 async 闭包中返回的值，async 的特点是不会阻塞当前线程，但会阻塞所在协程，也就是挂起。
```

**runBlocking**

runBlocking 和 launch 的区别是 runBlocking 的 delay 方法是可以阻塞当前线程的，和 Thread.sleep() 一样

#### 2、协程的挂起和恢复

- **协程执行时，协程和协程，协程和线程内代码是顺序运行的；**

  前面的协程挂起后，线程不会空闲，会继续运行下一个协程，而前面挂起的协程在挂起结束后不会马上运行，而是等待当前正在运行的协程运行完后再去执行。

- **协程挂起时，就不会执行了，而是等待挂起完成且线程空闲时才能继续执行；**

  suspend 表示挂起，用来修饰方法，一个协程内由多个 suspend 修饰的方法顺序书写时，代码也是顺序运行的，因为 suspend 函数会将整个协程挂起，而不仅仅是这个 suspend 函数。

  - **单个协程内多 suspend 函数运行**

    suspend 修饰的方法挂起的是 `协程` 本身，而非该方法

    ```kotlin
    suspend fun getToken(): String {
      delay(300)
      Log.d("AA", "getToken 开始执行，时间:  ${System.currentTimeMillis()}")
      return "ask"
    }
    suspend fun getResponse(token: String): String {
      delay(100)
      Log.d("AA", "getResponse 开始执行，时间:  ${System.currentTimeMillis()}")
      return "response"
    }
    fun setText(response: String) {
      Log.d("AA", "setText 执行，时间:  ${System.currentTimeMillis()}")
    }
    // 运行代码
    GlobalScope.launch(Dispatchers.Main) {
      Log.d("AA", "协程 开始执行，时间:  ${System.currentTimeMillis()}")
      val token = getToken()//在 getToken 方法将协程挂起时，getResponse 函数永远不会运行
      val response = getResponse(token)
      setText(response)
    }
    ```

  - **多协程间 suspend 函数运行**

    async 并发

    ```kotlin
    //外层必须包一层协程，因为 await() 只能在一个协程中或者另外一个 suspend 方法中运行
    GlobalScope.launch(Dispatchers.Unconfined){
      var token = GlobalScope.async(Dispatchers.Unconfined) {
        return@async getToken()
       }.await()
      var response = GlobalScope.async(Dispatchers.Unconfined) {
        return@async getResponse(token)
      }.await()
    
      setText(response)
    }
    ```

    await() 阻塞外部协程，所以代码还是顺序执行的，适用于多个同级 IO 操作的情况。

  - **协程挂起后何时恢复**

  - **协程挂起后再恢复时在哪个线程运行**

    哪个线程恢复的协程，协程就运行在哪个线程中。

    - Dispatchers.Main

      主线程中写在协程后面的代码先执行了。。。？？？

    - Dispatchers.Unconfined

    - Dispatchers.IO

    > 注意协程内部，若是在前面有代码切换了线程，后面的代码若是没有指定线程，那么就是运行在这个切换到的线程上的？？？（IO，貌似又切回了 IO 线程）
    >
    > 我们最好给异步任务在外面套一个协程，这样我们可以挂起整个异步任务，然后给每段代码指定运行线程调度器，这样省的因为协程内部挂起恢复变更线程而带来的问题
    >
    > 非 Dispatchers.Main 调度器的协程，会在协程挂起后把协程当做一个任务 DelayedResumeTask 放到默认线程池 DefaultExecutor 队列的最后，在延迟的时间到达才会执行恢复协程任务。虽然多个协程之间可能不是在同一个线程上运行的，但是协程内部的机制可以保证我们书写的协程是按照我们制定的顺序或逻辑执行的。

  - **协程的取消**

    创建协程后可以接收一个 Job 类型的返回值，job.cancel() 可以取消协程任务。

    因为协程内部可以再创建协程，这样的协程组织关系成为父协程-子协程：

    - 父协程手动调用 cancel() 或者异常结束，会立即取消它的所有子协程；
    - 父协程必须等待所有子协程完成（处于完成或者取消状态）才能完成；
    - 子协程抛出未捕获的异常时，默认情况下会取消其父协程；

    > 在协程中，cancel() 只是修改了协程的状态，在协程自身的方法比如 realy()、yield() 等中会判断协程的状态从而结束协程，但若是在协程中我们没有用这几个方法怎么办，比如都是逻辑代码，这时就要我们自己手动判断了，使用 job.isActive()，isActive() 是个标记，用来检查协程状态。

  - 









### 五、kotlin 标准库中函数的使用

#### 1、回调函数的Kotin的lambda的简化

在 kotlin 中对 Java 中的一些借口的回调做了优化，可以使用一个 lambda 函数来代替。可以简化一些不必要的嵌套回调方法。但是 **在 lambda 表达式中，只支持单抽象方法模型，即设计的接口中只有一个抽象方法，才符合 lambda 表达式的规则，多个回调方法不支持。**

- 用 Java 代码实现一个接口的回调

  ```java
   mView.setEventListener(new ExamPlanHomeEventListener(){
      public void onSuccess(Data data){
        //todo
      }
   });
  ```

- 在 kotlin 中实现一个接口的回调，

  ```kotlin
  //不使用 lambda 表达式（适用于接口中包含多个回调方法）
  mView.setEventListener(object: ExamPlanHomeEventListener{
      public void onSuccess(data: Data){
          //tood
      }
  })
  
  //接口只有一个回调方法，使用 lambda 函数
  mView.setEventListener({
      data: Data ->
      //todo
  })
  //或者可以直接省略 Data，借助 kotlin 的智能类型推导
  mView.setEventListener({
      data ->
      //todo
  })
  
  //如果以上代码 data 参数没有用到的话，可以直接把 data 去掉
  mView.setEventListener({
      //todo
  })
  
  //由于 setEventListener 函数最后一个参数是一个函数的话，可以直接把括号的实现提到圆括号外面
  mView.setEventListener(){
      //todo
  }
  
  //由于 setEventListener 这个函数只有一个参数，可以直接省略圆括号
  mView.setEventListener{
      //todo
  }
  ```

#### 2、内联扩展函数值 let

> let扩展函数的实际上是一个作用域函数，当你需要去定义一个变量在一个特定的作用域范围内，let函数的是一个不错的选择；let函数另一个作用就是可以避免写一些判断null的操作。 

**let 函数的使用**：

```kotlin
//1、let函数的使用的一般结构
object.let{
   it.todo()//在函数体内使用it替代object对象去访问其公有的属性和方法
   ...
}

//另一种用途 判断object为null的操作
object?.let{//表示object不为null的条件下，才会去执行let函数体
   it.todo()
}

//2、let函数底层的inline扩展函数+lambda结构
@kotlin.internal.InlineOnly
public inline fun <T, R> T.let(block: (T) -> R): R = block(this)

//3、let函数inline结构的分析
/*
从源码let函数的结构来看它是只有一个lambda函数块block作为参数的函数,调用T类型对象的let函数，则该对象为函数的参数。在函数块内可以通过 it 指代该对象。返回值为函数块的最后一行或指定return表达式。
*/
```

**let 函数使用场景**：

- 最常用的场景就是使用 let 函数处理需要针对一个可 null 的对象统一做判空处理；
- 需要去明确一个变量所处特定的作用于范围可以使用；

**let 函数使用前后对比**：

```kotlin
//使用前
mVideoPlayer?.setVideoView(activity.course_video_view)
mVideoPlayer?.setControllerView(activity.course_video_controller_view)
mVideoPlayer?.setCurtainView(activity.course_video_curtain_view)

//使用后
mVideoPlayer?.let {
    it.setVideoView(activity.course_video_view)
    it.setControllerView(activity.course_video_controller_view)
    it.setCurtainView(activity.course_video_curtain_view)
}
```



#### 3、内联函数 with

```kotlin
//1、with 函数使用的一般结构
with(object) {
    //todo
}

//2、with 函数底层的 inline 扩展函数 + lambda 结构
@kotlin.internal.InlineOnly
public inline fun <T, R> with(receiver: T, block: T.() -> R): R = receiver.block()

//with 函数 inline 结构的分析
/*
with 函数，不是以扩展的形式存在的。它是将某对象作为函数的参数，在函数块内可以通过 this 指代该对象。返回值为函数块的最后一行或指定 return 表达式。
*/
fun testWith() {
    val student = Student("嘻嘻")
    val result = with(student) {
        println("my name is $name")
        1000
    }
    println("result = $result")
    /*
    my name is 嘻嘻
	result = 1000
    */
}
```

**with 适用场景：**

- 适用于调用同一个类的多个方法时，可以省去类名重复，直接调用类的方法即可，经常用于 Android 中 RecyclerView 中 onBinderViewHolder 中，数据 model 的属性映射到 UI 上。



#### 4、内联扩展函数 run

```kotlin
//1、run函数使用的一般结构
object.run{
//todo
}

//2、run函数的inline+lambda结构
@kotlin.internal.InlineOnly
public inline fun <T, R> T.run(block: T.() -> R): R = block()

//3、run函数的inline结构分析
/*
run 函数实际上可以说是 let 和 with 两个函数的结合体，run 函数只接收一个 lambda 函数为参数，以闭包形式返回，返回值为最后一行的值或者指定的 return 的表达式。
*/
fun testRun() {
    val student = Student("嘻嘻")
    val result = student.run {
        println("my name is $name")
        1000
    }
    println("result = $result")
}
```

**run 函数适用场景：**

- 适用于 let、with 函数任何场景。因为 run 函数是 let、with 两个函数结合体，它弥补了 let 函数在函数体内必须使用 it 参数替代对象，在 run 函数中可以像 with 函数一样可以省略，直接访问实例中的公有属性和方法，另一方面它弥补了 with 函数传入对象判空问题，在 run 函数中可以像 let 函数一样做判空处理。



#### 5、内联扩展函数 apply

```kotlin
//1、apply函数使用的一般结构
object.apply{
//todo
}

//2、apply函数的inline+lambda结构
@kotlin.internal.InlineOnly
public inline fun <T> T.apply(block: T.() -> Unit): T { block(); return this }

//3、apply函数的inline结构分析
/*
apply 函数和 run 函数很像，唯一不同点就是它们各自返回的值不一样，run 函数是以闭包形式返回最后一行代码的值，而 apply 函数的返回值是传入对象的本身。
*/
fun testApply() {
    val student = Student("嘻嘻")
    val result = student.apply {
        println("my name is $name")
        1000
    }
    println("result = $result")
    /*
    my name is 嘻嘻
    result = Student(name=嘻嘻)
     */
}
```

**apply 函数的适用场景：**

- 整体作用功能和 run 函数很像，唯一不同点就是它返回的值是对象本身，而 run 函数是一个闭包形式返回，返回的是最后一行的值。
- apply 一般用于一个对象实例初始化的时候，需要对对象中的属性进行赋值。或者动态 inflate 出一个 xml 的 View 时需要给 View 绑定数据也会用到。特别是开发中会有一些数据 model 向 View model 转化实例化的过程中需要用到。
- 多级判空问题



#### 6、内联扩展函数 also

```kotlin
//1、also 函数使用的一般结构
object.also{
//todo
}

//2、also函数的inline+lambda结构
@kotlin.internal.InlineOnly
public inline fun <T> T.also(block: (T) -> Unit): T {
    block(this)
    return this
}

//3、also函数的inline结构分析
also 函数的结构和 let 很像，唯一的区别是返回值不一样，let 是以闭包的形式返回，返回函数体内最后一行的值，如果最后一行为空就返回一个 Unit 类型的默认值。而 also 函数返回的则是传入的对象的本身。
fun testAlso() {
    val student = Student("嘻嘻")
    val result = student.also {
        println("also--name = " + it.name)
        1000
    }
    println("result = $result")
    /*
    also--name = 嘻嘻
    result = Student(name=嘻嘻)
     */
}
```

**also 适用场景：**

- 适用于let函数的任何场景，also函数和let很像，只是唯一的不同点就是let函数最后的返回值是最后一行的返回值而also函数的返回值是返回当前的这个对象。
- 一般可用于多个扩展函数链式调用。



#### 7、let,with,run,apply,also函数区别

| 函数名 | 定义inline的结构                                             | 函数体内使用的对象       | 返回值       | 是否是扩展函数 | 适用的场景                                                   |
| ------ | ------------------------------------------------------------ | ------------------------ | ------------ | -------------- | ------------------------------------------------------------ |
| let    | fun <T, R> T.let(block: (T) -> R): R = block(this)           | it指代当前对象           | 闭包形式返回 | 是             | 适用于处理不为null的操作场景                                 |
| with   | fun <T, R> with(receiver: T, block: T.() -> R): R = receiver.block() | this指代当前对象或者省略 | 闭包形式返回 | 否             | 适用于调用同一个类的多个方法时，可以省去类名重复，直接调用类的方法即可，经常用于Android中RecyclerView中onBinderViewHolder中，数据model的属性映射到UI上 |
| run    | fun <T, R> T.run(block: T.() -> R): R = block()              | this指代当前对象或者省略 | 闭包形式返回 | 是             | 适用于let,with函数任何场景。                                 |
| apply  | fun T.apply(block: T.() -> Unit): T { block(); return this } | this指代当前对象或者省略 | 返回this     | 是             | 1、适用于run函数的任何场景，一般用于初始化一个对象实例的时候，操作对象属性，并最终返回这个对象。 2、动态inflate出一个XML的View的时候需要给View绑定数据也会用到. 3、一般可用于多个扩展函数链式调用 4、数据model多层级包裹判空处理的问题 |
| also   | fun T.also(block: (T) -> Unit): T { block(this); return this } | it指代当前对象           | 返回this     | 是             | 适用于let函数的任何场景，一般可用于多个扩展函数链式调用      |



#### 8、lateinit 和 by lazy

lateinit 和 lazy 是 Kotlin 中的两种不同的延迟初始化的实现。

**lateinit 只用于变量 var，而 lazy 只用于常量 val**

> **lazy 应用于单例模式（if-null-then-init-else-return）,而且当且仅当变量被第一次调用的时候，委托方法才会执行。**

`lazy()`是接受一个 lambda 并返回一个 `Lazy <T>` 实例的函数，返回的实例可以作为实现延迟属性的委托： 第一次调用 `get()` 会执行已传递给 `lazy()` 的 lambda 表达式并记录结果， 后续调用 `get()` 只是返回记录的结果。

使用场景：

- 比如只获取，不赋值，并且多次使用的对象；
- 比如 activity 中控件初始化的操作，一般传统的进入界面就初始化错有的控件，而使用懒加载，只有用到时才会对控件初始化。



> ##### lateinit 则用于只能生命周期流程中进行获取或者初始化的变量，比如 Android 的 onCreate()









=======================================================================================

#### kotlin 常见写法总结

1. **@Synchronized**

   ```java
   public synchronized int getMaxRequests() {
   	return maxRequests;
   }
   ```

   ```kotlin
   //①注解形式
   @Synchronized
   fun getMaxRequests(): Int {
   	return maxRequests
   }
   
   @get:Synchronized var maxRequests = 64
   set(maxRequests) {
       //②代码块形式
       synchronized(this){
           field = maxRequests
       }
   }
   ```

2. **@JvmName**

   注解说明：**更改方法名，更改get方法名，更改set方法名，更改文件名**

   使用说明：该注解可以用在方法前， 文件前（package声明前），get 和 set 方法前。需要传入替换的name。

   > @get:JvmName("XXX")  把get 方法名改为 XXX

3. **注解使用处目标**

4. **open 关键字**

   在Kotlin开发中类和方法默认不允许被继承和重写，等同于Java中用 final 修饰类和方法。
   如果在Kotlin 中类和方法想被继承和重写，需添加open 关键字修饰。

5. 

   

