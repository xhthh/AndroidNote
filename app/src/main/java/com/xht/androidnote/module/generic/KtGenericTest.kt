package com.xht.androidnote.module.generic

class KtGenericTest {
    fun main() {
        val person1: Person = Student()
        val p1: SimpleData<Student> = SimpleData<Person>()
        val list2: ArrayList<in Person> = ArrayList<Any>()
    }
}

open class Person {}
class Student : Person()
class Teacher : Person()

//限定下边界，T及其父类
class SimpleData<in T> {}


