package com.xht.androidnote.module.kotlin.practice

import kotlinx.coroutines.*


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
    runBlocking {
        // 阻塞1s
        delay(1000L)
        println("This is a coroutines ${System.currentTimeMillis()}")
    }

    // 阻塞2s
    Thread.sleep(2000L)
    println("main end ${System.currentTimeMillis()}")
}


fun main() {
    //testLaunchStart()
//    testAsync()
    testRunBlocking()
}