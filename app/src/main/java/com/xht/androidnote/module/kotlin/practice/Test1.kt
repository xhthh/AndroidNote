package com.xht.androidnote.module.kotlin.practice

import kotlin.properties.Delegates
import kotlin.reflect.KProperty

fun main() {
//    printClassName(Rectangle())
//
//    //Host("kotl.in").printConnectionString(443)  // 错误，该扩展函数在 Connection 外不可用
//
//    Connection(Host("kotl.in"), 443).connect()
//
//    // 创建一个类的实例
//    val isEven = object : IntPredicate {
//        override fun accept(i: Int): Boolean {
//            return i % 2 == 0
//        }
//    }


//    testDelegateProp()
    testLambda()
}

//==================================高阶函数和Lambda===========================
fun testLambda() {
    val items = listOf(1, 2, 3, 4, 5)

    // Lambdas 表达式是花括号括起来的代码块。
    // 如果一个 lambda 表达式有参数，前面是参数，后跟“->”
    items.fold(0, { acc: Int, nextElement: Int ->
        print("acc = $acc, i = $nextElement, ")
        val result = acc + nextElement
        println("result = $result")
        // lambda 表达式中的最后一个表达式是返回值
        result
    })

    // lambda 表达式的参数类型是可选的，如果能够推断出来的话：
//    val joinedToString = items.fold("Elements:", { acc, i -> acc + " " + i })

    // 函数引用也可以用于高阶函数调用：
//    val product = items.fold(1, Int::times)
}

fun <T, R> Collection<T>.fold(
    initial: R,
    combine: (acc: R, nextElement: T) -> R
): R {
    var accumulator: R = initial
    for (element: T in this) {
        accumulator = combine(accumulator, element)
    }
    return accumulator
}

//==================================高阶函数和Lambda===========================


//============================委托属性==============================
/**
 * 延迟属性 Lazy
 * lazy() 是接受一个 lambda 并返回一个 Lazy <T> 实例的函数，返回的实例可以作为实现延迟属性的委托：
 * 第一次调用 get() 会执行已传递给 lazy() 的 lambda 表达式并记录结果， 后续调用 get() 只是返回记录的结果。
 */
//val lazyValue: String by lazy { lazyTest() }
val lazyValue: String by lazy(fun(): String {
    println("computedddddd!")
    return "Hello"
})

fun lazyTest(): String {
    println("computedddddd!")
    return "Hello"
}

class Example {
    /**
     * 语法是： val/var <属性名>: <类型> by <表达式>。在 by 后面的表达式是该 委托，
     * 因为属性对应的 get()（与 set()）会被委托给它的 getValue() 与 setValue() 方法。
     * 属性的委托不必实现任何的接口，但是需要提供一个 getValue() 函数（与 setValue()——对于 var 属性）。
     */
    var p: String by Delegate()
}

class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name}' in $thisRef.")
    }
}

fun testDelegateProp() {
//    val example = Example()
//    println(example.p)
//    example.p = "NEW"

//    println(lazyValue)
//    println(lazyValue)

    val user = User()
    user.name = "First"
    user.name = "Second"
}

class User {
    var name: String by Delegates.observable(
        "<no name>",
//        fun(prop: KProperty<*>, old: String, new: String) {
//            println("$old -> $new")
//        }
        ::testObservable
    )

    fun testObservable(prop: KProperty<*>, old: String, new: String) {
        println("$old -> $new")
    }
}

class User1(val map: Map<String, Any?>) {
    val name: String by map
    val age: Int by map
}

fun test() {
    val user = User1(
        mapOf(
            "name" to "John Doe",
            "age" to 25
        )
    )
}
//============================委托属性==============================

//============================对象表达式=======================
fun testObjExp() {
    //如果超类型有一个构造函数，则必须传递适当的构造函数参数给它。 多个超类型可以由跟在冒号后面的逗号分隔的列表指定
    val ab = object : A(2), B {
        override val y: Int
            get() = 233
        override val z: Int
            get() = 666
    }

    println("ab.y=" + ab.y + "  ab.z=" + ab.z)

    //任何时候，如果我们只需要“一个对象而已”，并不需要特殊超类型，那么我们可以简单地写：
    val adHoc = object {
        var x: Int = 0
        var y: Int = 0
    }
    print(adHoc.x + adHoc.y)
}

open class A(x: Int) {
    public open val y: Int = x
}

interface B {
    public open val z: Int
        get() = 66
}

class C {
    // 私有函数，所以其返回类型是匿名对象类型
    private fun foo() = object {
        val x: String = "x"
    }

    // 公有函数，所以其返回类型是 Any
    fun publicFoo() = object {
        val x: String = "x"
    }

    fun bar() {
        val x1 = foo().x        // 没问题
        //val x2 = publicFoo().x  // 错误：未能解析的引用“x”
    }
}

//============================对象表达式=======================


interface IntPredicate {
    fun accept(i: Int): Boolean
}


class Host(val hostname: String) {
    fun printHostname() {
        print(hostname)
    }
}

class Connection(val host: Host, val port: Int) {
    fun printPort() {
        print(port)
    }

    fun Host.printConnectionString() {
        printHostname()   // 调用 Host.printHostname()
        print(":")
        printPort()   // 调用 Connection.printPort()
    }

    fun connect() {
        /*……*/
        host.printConnectionString()   // 调用扩展函数
    }
}


open class Shape

class Rectangle : Shape()

fun Shape.getName() = "Shape"

fun Rectangle.getName() = "Rectangle"

fun printClassName(s: Rectangle) {
    println(s.getName())
}

