package com.xht.androidnote.module.kotlin.practice


fun main() {

    testMapOperate()
}

fun testMapOperate() {
    val list = arrayListOf<User2>(User2(1), User2(2), User2(3))

    val newList = list.map {
        //类的构造函数中的参数有默认值，则创建对象时，无需传参
        //在 JVM 上，如果主构造函数的所有的参数都有默认值，编译器会生成 一个额外的无参构造函数，它将使用默认值。
        User3().apply {
            age = it.age
        }
        //lambda 表达式最后一个为返回结果
        User4().apply {
            age = it.age
        }
    }.toMutableList()

    /*
        data 数据类型打印的结果
        [User3(age=1), User3(age=2), User3(age=3)]
     */
    /*
        普通类打印的结果
        [com.xht.androidnote.module.kotlin.practice.User4@77459877, com.xht.androidnote.module.kotlin.practice.User4@5b2133b1, com.xht.androidnote.module.kotlin.practice.User4@72ea2f77]
     */
    println(newList)

    list.map {
        it.age += 1
        User3().apply {
            age = it.age
        }
    }
    println(list)

    //map 进行转换操作，如果不返回新的list，打印的还是之前的user2
    val map = list.map {
        it.age += 1
        User3().apply {
            age = it.age
        }
    }
    println(map)

    val toMutableList = list.map {
        it.age += 1
        User3().apply {
            age = it.age
        }
    }.toMutableList()
    println(toMutableList)
}


data class User2(var age: Int)

data class User3(var age: Int = 0)

class User4(var age: Int = 0){
//    override fun toString(): String {
//        return age.toString()
//    }
}