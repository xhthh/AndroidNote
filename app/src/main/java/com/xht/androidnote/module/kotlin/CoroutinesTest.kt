package com.xht.androidnote.module.kotlin

import kotlinx.coroutines.*

//fun main() {
////    thread { // 在后台启动一个新的协程并继续
////        Thread.sleep(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
////        println("World!") // 在延迟后打印输出
////    }
//
//    GlobalScope.launch { // 在后台启动一个新的协程并继续
//        delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
//        println("World!") // 在延迟后打印输出
//    }
//    println("Hello,") // 协程已在等待时主线程还在继续
//    runBlocking {
//        delay(2000L)
//    }
////    Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活
//}

//fun main() = runBlocking<Unit> {
//    GlobalScope.launch { // 在后台启动一个新的协程并继续
//        delay(1000L)
//        println("World!")
//    }
//    println("Hello,") // 主协程在这里会立即执行
//    delay(2000L)      // 延迟 2 秒来保证 JVM 存活
//}

/**
 * GlobalScope.launch 每次创建的都是一个顶层协程，当前应用程序结束协程跟着结束
 * 和当前函数和main都有关  delay时间内main函数走完 协程会结束，不会打印第二句
 * 当前函数阻塞足够时间 会打印
 */
private fun test1() {
    GlobalScope.launch {
        println("codes run is coroutine scope")
        delay(1500)
        println("codes run is coroutine scope finished")
    }
    Thread.sleep(1000)
}

/**
 * runBlocking函数也会创建一个协程的作用域，与GlobalScope.launch 不同的是，
 * 它可以保证在协程作用域内的所有代码和子协程没有执行完成之前一直阻塞当前线程。
 * 因为它可能会阻塞当前线程，而你又恰好在主线程中调用它，就有可能卡死界面，因此该函数只推荐在测试环境下使用，否则会影响性能。
 */
private fun test2() {
    runBlocking {
        println("codes run is coroutine scope")
        delay(1500)
        println("codes run is coroutine scope finished")
    }
    Thread.sleep(1000)
}

/**
 * launch函数只能在协程作用域中才能被调用，且它会在当前协程作用域下创建子协程。子协程会随外层作用域协程的结束而一同结束。
 * 子作用域可以并发执行，并且根据编程语言先后执行顺序
 */
private fun test3() {
    runBlocking {
        launch {
            //doWork()
            printStr("launch1")
        }
        launch {
//            println("launch2")
//            delay(1000L)
//            println("launch2 finished")
            printStr("launch2")
        }
    }
    Thread.sleep(1000)
}

/**
 * 如果在launch函数中调用一个其他函数会产生一个问题：launch函数中的代码是有协程作用域的，
 * 但是抽取到一个单独函数中的代码就没有协程作用域了，无法调用类似于delay()这种挂起函数。
 * 解决办法：加suspend关键字，它可以将任意函数声明成挂起函数，而挂起函数是可以相互调用的。
 */
private suspend fun doWork() {
    println("launch1")
    delay(1000L)
    println("launch1 finished")
}

/**
 * suspend关键字只能声明为挂起函数，无法提供作用域，因此1.4中的问题只解决了挂起函数相互调用的问题，
 * 被调用的函数中还是没有协程作用域，无法调用类似于launch()函数。此时就用到了coroutineScope 函数。
 *
 * coroutineScope 是挂起函数，它可以继承外部作用域创建子作用域。
 * coroutineScope 只会阻塞当前的协程，不会阻塞线程，runBlocking 会阻塞当前线程
 */
private suspend fun printStr(name: String) = coroutineScope {
    launch {
        println("$name")
        delay(1000L)
        println("$name finished")
    }
}

/**
 * 获取协程内容返回值
 * launch函数返回值永远是一个Job对象，利用async函数可以创建一个协程并获取它的执行结果。
 * async函数必须在协程作用域中才能调用，它会创建一个新子协程并返回一个Deferred对象，
 * 调用deferred.await()方法就可获得async函数代码块的结果。
 *
 * withContext()函数可以理解成async函数的简化版写法，但是它强制要求指定一个线程参数。线程参数主要有3种可选：
 * -Dispatchers.Default：默认的低并发线程策略，适用于计算密集型任务。
 * -Dispatchers.IO：较高并发线程策略，适用于主要在阻塞和等待中的代码。-
 * -Dispatchers.Main：只能在安卓项目中使用，在Android主线程中执行。
 */
private fun test4() {
    runBlocking {
//        val result = async {
//            5 + 5
//        }.await()//后面的代码会在有返回值后才执行，所以有多个async请在最后执行await，不然会串行执行，效率很低
        val result = withContext(Dispatchers.Default) {
            5 + 5
        }//后面的代码会在有返回值后才执行，所以有多个async请在最后执行await，不然会串行执行，效率很低
        println(result)
    }
}

private fun test5() {
    val job = GlobalScope.launch { }
    job.cancel()

    val job1 = Job()
    val coroutineScope = CoroutineScope(job)
    coroutineScope.launch {

    }
    job1.cancel()
}

private fun test6() {

}


//================================
fun main() {
//    test3()
//    test4()
    test6()
    println("main 函数结束")
}