package com.xht.androidnote.module.kotlin.practice

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlin.system.measureTimeMillis


fun testLaunch() {
    println("协程初始化开始，时间: " + System.currentTimeMillis())

    GlobalScope.launch(Dispatchers.Unconfined) {
        println("协程初始化完成，时间: " + System.currentTimeMillis())
        for (i in 1..3) {
            println("协程任务1打印第$i 次，时间: " + System.currentTimeMillis())
        }
        delay(500)
        for (i in 1..3) {
            println("协程任务2打印第$i 次，时间: " + System.currentTimeMillis())
        }
    }

    println("主线程 sleep ，时间: " + System.currentTimeMillis())
    Thread.sleep(1000)
    println("主线程运行，时间: " + System.currentTimeMillis())

    for (i in 1..3) {
        println("主线程打印第$i 次，时间: " + System.currentTimeMillis())
    }
}

fun testLaunchStart() {
//    val newSingleThreadContext = newSingleThreadContext("aa")
//
//    GlobalScope.launch(newSingleThreadContext) { }

    var job: Job = GlobalScope.launch(start = CoroutineStart.LAZY) {
        println("协程开始运行，时间: " + System.currentTimeMillis())
    }

    Thread.sleep(1000L)
// 手动启动协程
    job.start()
}

fun testAsync() {
    GlobalScope.launch(Dispatchers.Unconfined) {
        val deferred = GlobalScope.async {
            delay(1000L)
            println("This is async ")
            return@async "嘻嘻"
        }

        println("协程 other start")
        val result = deferred.await()
        println("async result is $result")
        println("协程 other end ")
    }

    println("主线程位于协程之后的代码执行，时间:  ${System.currentTimeMillis()}")
}

fun testRunBlocking() {
//    runBlocking {
//        // 阻塞1s
//        delay(1000L)
//        println("This is a coroutines ${System.currentTimeMillis()}")
//    }
//
//    // 阻塞2s
//    Thread.sleep(3000L)
//    println("main end ${System.currentTimeMillis()}")
    GlobalScope.launch { // 在后台启动一个新的协程并继续
        delay(1000L)
        println("World!")
    }
    println("Hello,") // 主线程中的代码会立即执行
    runBlocking {     // 但是这个表达式阻塞了主线程
        delay(5000L)  // ……我们延迟 2 秒来保证 JVM 的存活
    }
}

fun testRunBlocking2() = runBlocking<Unit> {
    GlobalScope.launch { // 在后台启动一个新的协程并继续
        delay(1000L)
        println("World!")
    }
    println("Hello,") // 主协程在这里会立即执行
    delay(2000L)      // 延迟 2 秒来保证 JVM 存活
}

suspend fun testt() {
    val job = GlobalScope.launch { // 启动一个新协程并保持对这个作业的引用
        delay(1000L)
        println("World!")
    }
    println("Hello,")
    job.join() // 等待直到子协程执行结束
}

fun testSuspend() {
    suspend fun getToken(): String {
        delay(300)
        println("getToken 开始执行，时间:  ${System.currentTimeMillis()}")
        return "ask"
    }

    suspend fun getResponse(token: String): String {
        delay(100)
        println("getResponse 开始执行，时间:  ${System.currentTimeMillis()}")
        return "response"
    }

    fun setText(response: String) {
        println("setText 执行，时间:  ${System.currentTimeMillis()}")
    }

// 运行代码
    GlobalScope.launch(Dispatchers.Main) {
        println("协程 开始执行，时间:  ${System.currentTimeMillis()}")
        val token = getToken()
        val response = getResponse(token)
        setText(response)
    }
}

suspend fun getToken(): String {
    delay(300)
    println("getToken 开始执行，时间:  ${System.currentTimeMillis()}")
    return "ask"
}

suspend fun getResponse(token: String): String {
    delay(100)
    println("getResponse 开始执行，时间:  ${System.currentTimeMillis()}")
    return "response"
}

fun setText(response: String) {
    println("setText 执行，时间:  ${System.currentTimeMillis()}")
}

fun testSus() {
    // 运行代码
    GlobalScope.launch {
        println("协程 开始执行，时间:  ${System.currentTimeMillis()}")
        val token = getToken()
        val response = getResponse(token)
        setText(response)
    }
}

fun testRunBlockingAndLaunch() {
    runBlocking {
        launch {
            doWork()
        }

        coroutineScope { // 创建一个协程作用域
            launch {
                delay(500L)
                println("Task from nested launch")
            }

            delay(100L)
            println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
        }

        println("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
    }
}

private suspend fun doWork() {
    delay(200L)
    println("Task from runBlocking")
}

/**
 * 协程任务可以取消：
 * 1、定期调用挂起函数来检查状态，如 yield()
 * 2、显示的检查取消状态
 */
fun testCancel() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (isActive) { // 可以被取消的计算循环
            // 每秒打印消息两次
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // 等待一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消该作业并等待它结束
    println("main: Now I can quit.")
}

fun testFinallyCatch() = runBlocking {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            println("job: I'm running finally")
        }
    }
    delay(1300L) // 延迟一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消该作业并且等待它结束
    println("main: Now I can quit.")
}

suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // pretend we are doing something useful here
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // pretend we are doing something useful here, too
    return 29
}

fun testSusp() = runBlocking {
//    val time = measureTimeMillis {
//        val one = doSomethingUsefulOne()
//        val two = doSomethingUsefulTwo()
//        println("The answer is ${one + two}")
//    }
//    println("Completed in $time ms")

    //async 并发操作，await() 获取结果
//    val time = measureTimeMillis {
//        val one = async { doSomethingUsefulOne() }
//        val two = async { doSomethingUsefulTwo() }
//        println("The answer is ${one.await() + two.await()}")
//    }
//    println("Completed in $time ms")


    /**
     * 惰性启动的 async
     * 只有结果通过 await 获取的时候协程才会启动，或者在 Job 的 start 函数调用的时候。
     * 如果没有调用 start，只调用了 await()，将会导致代码顺序执行
     */
    val time = measureTimeMillis {
        val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
        val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
        // 执行一些计算
//        one.start() // 启动第一个
//        two.start() // 启动第二个
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}

/**
 * 测试协程调度器，运行在哪个线程上
 * 协程调度器可以将协程限制在一个特定的线程执行，或将它分派到一个线程池，亦或是让它不受限地运行。
 */
fun testDispatchersThread() = runBlocking {
    launch { // 运行在父协程的上下文中，即 runBlocking 主协程
        println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Unconfined) { // 不受限的——将工作在主线程中
        println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Default) { // 将会获取默认调度器
        println("Default               : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(newSingleThreadContext("MyOwnThread")) { // 将使它获得一个新的线程
        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
    }
}


/**
 * 测试协程上下文中的job
 * 当一个协程被其它协程在 CoroutineScope 中启动的时候， 它将通过 CoroutineScope.coroutineContext 来承袭上下文，
 * 并且这个新协程的 Job 将会成为父协程作业的 子 作业。当一个父协程被取消的时候，所有它的子协程也会被递归的取消。
 * 然而，当使用 GlobalScope 来启动一个协程时，则新协程的作业没有父作业。 因此它与这个启动的作用域无关且独立运作。
 */
fun testContextJob() = runBlocking {
// 启动一个协程来处理某种传入请求（request）
    val request = launch {
        // 孵化了两个子作业, 其中一个通过 GlobalScope 启动
        GlobalScope.launch {
            println("job1: I run in GlobalScope and execute independently!")
            delay(1000)
            println("job1: I am not affected by cancellation of the request")
        }
        // 另一个则承袭了父协程的上下文
        launch {
            delay(100)
            println("job2: I am a child of the request coroutine")
            delay(1000)
            println("job2: I will not execute this line if my parent request is cancelled")
        }
    }
    delay(500)
    request.cancel() // 取消请求（request）的执行
    delay(1000) // 延迟一秒钟来看看发生了什么
    println("main: Who has survived request cancellation?")
    /*
        job1: I run in GlobalScope and execute independently!
        job2: I am a child of the request coroutine
        job1: I am not affected by cancellation of the request
        main: Who has survived request cancellation?
     */
}

/**
 * 一个父协程总是等待所有的子协程执行结束。父协程并不显式的跟踪所有子协程的启动，并且不必使用 Job.join 在最后的时候等待它们：
 */
fun testContextJob2() = runBlocking {
// 启动一个协程来处理某种传入请求（request）
    val request = launch {
        repeat(3) { i -> // 启动少量的子作业
            launch {
                delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒的时间
                println("Coroutine $i is done")
            }
        }
        println("request: I'm done and I don't explicitly join my children that are still active")
    }
    request.join() // 等待请求的完成，包括其所有子协程
    println("Now processing of the request is complete")
}


//================================================
class Activity {
    private val mainScope = CoroutineScope(Dispatchers.Default) // use Default for test purposes

    fun destroy() {
        mainScope.cancel()
    }

    fun doSomething() {
        // launch ten coroutines for a demo, each working for a different time
        repeat(10) { i ->
            mainScope.launch {
                delay((i + 1) * 200L) // variable delay 200ms, 400ms, ... etc
                println("Coroutine $i is done")
            }
        }
    }
}

fun testCoroutineScope() = runBlocking {
    val activity = Activity()
    activity.doSomething() // run test function
    println("Launched coroutines")
    delay(500L) // delay for half a second
    println("Destroying activity!")
    activity.destroy() // cancels all coroutines
    delay(1000) // visually confirm that they don't work
}
//================================================


//==============================flow====================================
/**
 * 如果使用一些消耗 CPU 资源的阻塞代码计算数字（每次计算需要 100 毫秒）那么我们可以使用 Sequence 来表示数字
 */
fun testFlowSequence(): Sequence<Int> = sequence {//序列构建器
    for (i in 1..3) {
        Thread.sleep(100) // 假装我们正在计算
        yield(i) // 产生下一个值
    }
}

fun simple(): Flow<Int> = flow { // 流构建器
    for (i in 1..3) {
        delay(100) // 假装我们在这里做了一些有用的事情
        emit(i) // 发送下一个值
    }
}

fun testFlow() = runBlocking {
    // 启动并发的协程以验证主线程并未阻塞
    launch {
        for (k in 1..3) {
            println("I'm not blocked $k")
            delay(100)
        }
    }
    // 收集这个流
    simple().collect { value -> println(value) }
}

fun testAsFlow() = runBlocking {
    (1..3).asFlow().collect { value -> println(value) }
}
//==============================flow====================================

fun testDeferred() = runBlocking {
    val deferred = CompletableDeferred<Int>()

    launch {
        delay(1000)
        deferred.complete(23)
    }

    System.err.println("(Demo.kt:72)    结果 = ${deferred.await()}")
}

fun testImmediate() = runBlocking {
    launch(Job() + Dispatchers.Main.immediate) {
        println("------launch------")
    }
    println("------testImmediate------")
}

fun main() {
    testImmediate()
//    testDeferred()
//    testAsFlow()
//    testFlow()
//    testFlowSequence().forEach { value -> println(value) }
    //testLaunchStart()
//    testAsync()
//    testRunBlocking()
//    testRunBlocking2()
//    testSus()
//    testRunBlockingAndLaunch()
//    testCancel()
//    testFinallyCatch()
//    testSusp()
//    testDispatchersThread()
//    testContextJob()
//    testContextJob2()
//    testCoroutineScope()
}