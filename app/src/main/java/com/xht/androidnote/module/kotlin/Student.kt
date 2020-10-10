package com.xht.androidnote.module.kotlin

class Student : Person1 {

    constructor(name: String, age: Int, no: String, score: Int) : super(name, age)

}

open class Person1(name:String){
    /**次级构造函数**/
    constructor(name:String,age:Int):this(name){
        //初始化
        println("-------基类次级构造函数---------")
    }
}