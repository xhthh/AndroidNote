package com.xht.androidnote.module.kotlin.practice

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

/**
 * 过渡流操作符
 * map 操作符
 */
fun test1() = runBlocking {
    (1..3).asFlow()
        .map { request -> performRequest(request) }
        .collect { response -> println(response) }
}

suspend fun performRequest(request: Int): String {
    delay(1000)
    return "response $request"
}

/**
 * 转换操作符
 * 在流转换操作符中，最通用的一种称为 transform。它可以用来模仿简单的转换，例如 map 与 filter，以及实施更复杂的转换。
 * 使用 transform 操作符，我们可以 发射 任意值任意次。
 * 比如说，使用 transform 我们可以在执行长时间运行的异步请求之前发射一个字符串并跟踪这个响应
 */
fun test2() = runBlocking {
    (1..3).asFlow()
        .transform { request ->
            emit("Making request $request")
            emit(performRequest(request))
        }
        .collect { response -> println(response) }
}

/**
 * 限长操作符
 * 限长过渡操作符（例如 take）在流触及相应限制的时候会将它的执行取消。
 * 协程中的取消操作总是通过抛出异常来执行，这样所有的资源管理函数（如 try {...} finally {...} 块）会在取消的情况下正常运行：
 */
fun test3() = runBlocking {
    numbers()
        .take(2)//只获取前2个
        .collect { value -> println(value) }
}

fun numbers(): Flow<Int> = flow {
    try {
        emit(1)
        emit(2)
        println("This line will not execute")
        emit(3)
    } finally {
        println("Finally in numbers")
    }
}

fun test4() = runBlocking {
    val sum = (1..5).asFlow()
        .map { it * it }
        .reduce { a, b -> a + b }
    println(sum)
}

/**
 * 流是连续的
 * 流的每次单独收集都是按顺序执行的，除非进行特殊操作的操作符使用多个流。该收集过程直接在协程中运行，该协程调用末端操作符。
 * 默认情况下不启动新协程。 从上游到下游每个过渡操作符都会处理每个发射出的值然后再交给末端操作符。
 */
fun test5() = runBlocking {
    (1..5).asFlow()
        .filter {
            println("Filter $it")
            it % 2 == 0
        }
        .map {
            println("Map $it")
            "string $it"
        }
        .collect { println("Collect $it") }
}

/**
 * 流上下文
 * 流的收集总是在调用协程的上下文中发生。例如，如果有一个流 simple，然后以下代码在它的编写者指定的上下文中运行，
 * 而无论流 simple 的实现细节如何：
 */
fun test6() = runBlocking {
    simple2().collect { value -> println("Collected $value") }
}

fun simple2(): Flow<Int> = flow {
    println("Started simple flow")
    for (i in 1..3) {
        emit(i)
    }
}

/**
 * flowOn 更改流发射的上下文
 */
fun simple3(): Flow<Int> = flow {
    for (i in 1..3) {
        Thread.sleep(100) // pretend we are computing it in CPU-consuming way
        log("Emitting $i")
        emit(i) // emit next value
    }
}.flowOn(Dispatchers.Default)// 在流构建器中改变消耗 CPU 代码上下文的正确方式

fun test7() = runBlocking {
    simple3().collect { value -> log("Collected $value") }
}

fun simple4(): Flow<Int> = flow {
    for (i in 1..5) {
        delay(100) // 假装我们异步等待了 100 毫秒
        emit(i) // 发射下一个值
    }
}

/**
 * 在流上使用 buffer 操作符来并发运行这个 simple 流中发射元素的代码以及收集的代码
 * 仅仅需要等待第一个数字产生的 100 毫秒以及处理每个数字各需花费的 300 毫秒
 */
fun test8() = runBlocking {
    val time = measureTimeMillis {
        simple4()
            .buffer()// 缓冲发射项，无需等待
            .collect { value ->
                delay(300)
                println(value)
            }
    }
    println("Collected in $time ms")
}

/**
 * 当流代表部分操作结果或操作状态更新时，可能没有必要处理每个值，而是只处理最新的那个。
 * 在本示例中，当收集器处理它们太慢的时候， conflate 操作符可以用于跳过中间值。
 * 虽然第一个数字仍在处理中，但第二个和第三个数字已经产生，因此第二个是 conflated ，只有最新的（第三个）被交付给收集器
 */
fun test9() = runBlocking {
    val time = measureTimeMillis {
        simple4()
            .conflate()
            .collect { value ->
                delay(300)
                println(value)
            }
    }
    println("Collected in $time ms")
}

/**
 * 当发射器和收集器都很慢的时候，合并是加快处理速度的一种方式。它通过删除发射值来实现。
 * 另一种方式是取消缓慢的收集器，并在每次发射新值的时候重新启动它。有一组与 xxx 操作符执行相同基本逻辑的 xxxLatest 操作符，
 * 但是在新值产生的时候取消执行其块中的代码。
 */
fun test10() = runBlocking {
    val time = measureTimeMillis {
        simple4()
            .collectLatest { value -> // 取消并重新发射最后一个值
                println("Collecting $value")
                delay(300) // 假装我们花费 300 毫秒来处理它
                println("Done $value")
            }
    }
    println("Collected in $time ms")
}

fun test11() = runBlocking {
    val nums = (1..3).asFlow()
    val strs = flowOf("one", "two", "three")
    nums.zip(strs) { a, b ->
        "$a -> $b"
    }.onCompletion {
        println("---test11()---结束---")
    }.collect { println(it) }
}

fun test12() = runBlocking {
    val f = flow {
        for (i in 1..3) {
            delay(500)
            println("emit $i")
            emit(i)
        }
    }
    withTimeoutOrNull(1600) {
        f.collect {
            delay(500)
            println("consume $it")
        }
    }
    println("cancel")
}

fun test13() = runBlocking {
// 1. 生成一个 Channel
    val channel = Channel<Int>()

    // 2. Channel 发送数据
    launch {
        for (i in 1..5) {
            delay(200)
            channel.send(i * i)
        }
        channel.close()
    }

    // 3. Channel 接收数据
    launch {
        for (y in channel)
            println("get $y")
    }

}

fun main() {
    test13()
//    test12()
//    test11()
//    test10()
//    test9()
//    test8()
//    test7()
//    test6()
//    test5()
//    test4()
//    test3()
//    test2()
//    test1()

    val launch = GlobalScope.launch { }
    launch.cancel()
}

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")
