/**
 * 测试 kotlin 标准库中的函数 let、apply、run、with。。。
 */
fun main() {
    //testLet()
    //testWith()
    //testRun()
    //testApply()
    testAlso()
}

fun testLet() {
    val result = "testLet".let {
        println(it.length)
        1000
    }
    println(result)
}

fun testWith() {
    val student = Student("嘻嘻")
    val result = with(student) {
        println("my name is $name")
        1000
    }
    println("result = $result")
}

fun testRun() {
    val student = Student("嘻嘻")
    val result = student.run {
        println("my name is $name")
        1000
    }
    println("result = $result")
}

fun testApply() {
    val student = Student("嘻嘻")
    val result = student.apply {
        println("my name is $name")
        1000
    }
    println("result = $result")
    /*
    my name is 嘻嘻
    result = Student(name=嘻嘻)
     */
}

fun testAlso() {
    val student = Student("嘻嘻")
    val result = student.also {
        println("also--name = " + it.name)
        1000
    }
    println("result = $result")
    /*
    also--name = 嘻嘻
    result = Student(name=嘻嘻)
     */

    val person = Person("啦啦啦", 4)
    person.flying()   //运行不报错

}

data class Person(val name: String, val age: Int) {
    fun flying() {
        lateinit var sky: Person
        println("你给我飞呀")


        fun test() {
            sky = Person("嘻嘻", 18)
            println(sky.age)
        }
    }
}

data class Student(val name: String)