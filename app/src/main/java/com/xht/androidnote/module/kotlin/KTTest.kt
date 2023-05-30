package com.xht.androidnote.module.kotlin

import android.content.Context
import android.content.Intent
import kotlin.properties.Delegates
import kotlin.random.Random
import kotlin.reflect.KProperty


class B {
    //委托属性
    var a: String by Text()
}

//被委托类（真实类）
class Text {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "属性拥有者 = $thisRef, 属性的名字 = '${property.name}' 属性的值 "
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("属性的值 = $value 属性的名字 =  '${property.name}' 属性拥有者 =  $thisRef")
    }
}

val lazyValue: String by lazy {
    println("computed!")
    "hello"
}

class User {
    var name: String by Delegates.observable("init") { property, oldValue, newValue ->
        println("$oldValue -> $newValue")
    }

    fun call() {
        println("打电话")
    }
}

open class Person11(val name: String, val age: Int) {
}

//该类没有主构造函数，所以继承的父类不需要写()
class Student12 : Person11 {
    constructor(name: String, age: Int) : super(name, age) {
    }
}

//==================作用域函数===========================
private fun doStudy(user: User?) {
    //let 作用域函数，只有一个参数，即调用者本身
    user?.let { u ->
        u.call()
    }
}


/**
 * with 函数接收两个参数，第一个参数可以是一个任意类型的对象，第二个参数是Lambda表达式。
 * with 函数会在Lambda 表达式中提供第一个参数对象的上下文，并使用Lambda 表达式的最后一行代码作为返回值返回。
 */
private fun printFruits() {
    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
    val result = with(StringBuilder()) {
        append("Start eating fruits. \n")
        for (fruit in list) {
            append(fruit).append("\n")
        }
        append("Ate all fruits.")
        toString()
    }

    println(result)
}

/**
 * un函数和with函数几乎一模一样，with 函数是内置函数形式调用with(obj){} ，run 函数是 obj.run{} ，
 * 一个是通过传入对象，一个是通过对象调用，作用相同，也是Lambda表达式内包含上下文环境，
 * 最后一句代码为返回值，run 函数只有一个参数就是Lambda 表达式。
 */
private fun printFruits2() {
    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
    val result = StringBuilder().run {
        append("Start eating fruits. \n")
        for (fruit in list) {
            append(fruit).append("\n")
        }
        append("Ate all fruits.")
        toString()
    }

    println(result)
}

/**
 * apply 函数和 run 函数基本相同，不同的地方在于，apply 会返回对象本身，
 * Lambda 表达式内不存在返回值，也是在Lambda 表达式中提供对象的上下文，结构为 obj.apply{}。
 */
private fun printFruits3() {
    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
    val result = StringBuilder().apply {
        append("Start eating fruits. \n")
        for (fruit in list) {
            append(fruit).append("\n")
        }
        append("Ate all fruits.")
    }

    println(result.toString())
}

/**
 * let 及 also 将上下文对象作为 lambda 表达式参数。如果没有指定参数名，对象可以用隐式默认名称 it 访问。
 */
fun getRandomInt(): Int {
    return Random.nextInt(100).also {
        println("getRandomInt() generated value $it")
    }
}
//==============================================

//===================高阶函数===========================
fun plus(num1: Int, num2: Int): Int {
    return num1 + num2
}

fun num1AndNum2(num1: Int, num2: Int, operation: (Int, Int) -> Int): Int {
    return operation(num1, num2)
}

val a = num1AndNum2(2, 3, ::plus)

val b = num1AndNum2(10, 8) { n1, n2 ->
    n1 + n2
}

val c = num1AndNum2(10, 8, fun(num1: Int, num2: Int) = num1 + num2)


//==============================================

//====================泛型==========================

/**
 * 泛型类
 */
class MyClass1<T> {
    fun method(param: T): T {
        return param
    }
}

val myClass1 = MyClass1<Int>()
val result1 = myClass1.method(233)

/**
 * 泛型方法
 */
class MyClass2 {
    fun <T> method(param: T): T {
        return param
    }
}

val myClass2 = MyClass2()
val result2 = myClass2.method(233)

fun <T> T.build(block: T.() -> Unit): T {
    block
    return this
}

//==============================================
inline fun <reified T> startActivity(context: Context, block: Intent.() -> Unit) {
    val intent = Intent(context, T::class.java)
    intent.block()
    context.startActivity(intent)
}

//==============================Lambda表达式=========================================
/**
 * {参数名1: 参数类型, 参数名2: 参数类型 -> 函数体}
 *
 */
fun getMaxLengthOfFruit() {
    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape", "Watermelon")
//    val maxLengthFruit = list.maxBy { it: String -> it.length }
//    println("max length fruit is $maxLengthFruit")


    val length1 = list.maxBy({ fruit: String -> fruit.length })

    //当Lambda参数是函数的最后一个参数时，可以将Lambda表达式移到函数括号的外面，如下所示：
    val length2 = list.maxBy() { fruit: String -> fruit.length }

    //如果Lambda参数是函数的唯一一个参数的话，还可以将函数的括号省略
    val length3 = list.maxBy { fruit: String -> fruit.length }

    //Lambda表达式中的参数列表其实在大多数情况下不必声明参数类型
    val length4 = list.maxBy { fruit -> fruit.length }

    //当Lambda表达式的参数列表中只有一个参数时，也不必声明参数名，而是可以使用it关键字来代替
    val length5 = list.maxBy { it.length }
}

fun filterTest() {
    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape", "Watermelon")

    val newList = list.filter { it.length <= 5 }
        .map { it.toUpperCase() }
    for (fruit in newList) {
        println(fruit)
    }
}

fun anyAndAllTest() {
    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape", "Watermelon")
    //any函数用于判断集合中是否至少存在一个元素满足指定条件
    val anyResult = list.any { it.length <= 5 }
    //all函数用于判断集合中是否所有元素都满足指定条件
    val allResult = list.all { it.length <= 5 }
    println("anyResult is $anyResult, allResult is $allResult")
}

//=======================================================================

fun hsTest() {
    Thread { println("Thread is running") }.start()
}

//=======================================================================
fun main() {
//    anyAndAllTest()

//    var b = B()
//    println(b.a)
//    b.a = "ahaha"

//    println(lazyValue)
//    println(lazyValue)

//    val user = User()
//    user.name = "first"
//    user.name = "second"

//    printFruits()
//    getRandomInt()

//    println(a)
//    filterTest()

    people("Android") { say("World") }

    //Kotlin 中 双冒号操作符 表示把一个方法当做一个参数，传递到另一个方法中进行使用，通俗的来讲就是引用一个方法。
    println(lock("param1", "param2", ::getResult))
}

fun say(msg: String) {
    println("Hello $msg")
}

/**
 * 当调用的函数有形参时，
 * 需要在调用的函数声明，并使用声明的形参；
 * 函数参数中的形参无法使用
 */
fun people(arg0: String, hello: (arg1: String) -> Unit) {
    hello(arg0)
    // hello(arg1) 这样调用将报错
}


/**
 * @param str1 参数1
 * @param str2 参数2
 */
fun getResult(str1: String, str2: String): String = "result is {$str1 , $str2}"

/**
 * @param p1 参数1
 * @param p2 参数2
 * @param method 方法名称
 */
fun lock(p1: String, p2: String, method: (str1: String, str2: String) -> String): String {
    return method(p1, p2)
}

