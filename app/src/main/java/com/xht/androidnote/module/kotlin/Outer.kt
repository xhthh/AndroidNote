package com.xht.androidnote.module.kotlin

class Outer {

    private val bar: Int = 1

    var v = "成员变量"

    inner class Inner {
        fun foo() = bar
        fun innerTest() {
            var o = this@Outer
            println("内部类可以引用外部类的成员：例如：" + o.v)
        }
    }
}

fun main() {
    val demo = Outer().Inner().foo()
    println(demo)

    val demo2 = Outer().Inner().innerTest()
    println(demo2)
}