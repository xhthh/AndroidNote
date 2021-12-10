package com.xht.androidnote.module.kotlin.practice

fun main() {
    printClassName(Rectangle())

    //Host("kotl.in").printConnectionString(443)  // 错误，该扩展函数在 Connection 外不可用

    Connection(Host("kotl.in"), 443).connect()

    // 创建一个类的实例
    val isEven = object : IntPredicate {
        override fun accept(i: Int): Boolean {
            return i % 2 == 0
        }
    }
}

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

