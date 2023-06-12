package com.xht.androidnote.module.generic.kotlin

data class Plate<T>(var item: T)

//只需要返回值，不需要传入参数
interface Source<T> {
    fun getSource(): Source<T>
}

class Test {
    fun <T : Fruit> setPlate(plate: Plate<T>) {}
}

//out即java中的<? extends T>
//意为仅可作为返回值，返回值类型是T或T的子类
data class Basket<out T>(val item: T)

//in即java中的<? super T>
//意为仅可作为参数传入，传入的参数类型是T或T的子类
class Bowl<in T> {
    fun setItem(item: T) {}
}

open class Food
open class Fruit : Food()
class Apple : Fruit()
class Banana : Fruit()

fun main() {
    //test1()
    test2()
}

fun test2() {
    //泛型实化 这种情况在 Java 是会被类型擦除的
    val genericType1 = getGenericType<String>()
    val genericType2 = getGenericType<Number>()
    println(genericType1)
    println(genericType2)
}

private fun test1() {
    val plate1 = Plate(Food())
    plate1.item = Fruit()
    val test = Test()
    //无法通过 因为Food不是Fruit或其子类
    //test.setPlate(plate1)
    val plate3 = Plate(Apple())
    test.setPlate(plate3)
    val fruit: Fruit = plate1.item as Fruit
    val basket: Basket<Fruit> = Basket<Apple>(Apple())
    //抛出异常
    //Exception in thread "main" java.lang.ClassCastException: Apple cannot be cast to Banana
    //val banana:Banana = basket.item as Banana
    val food: Food = basket.item
    val bowl: Bowl<Fruit> = Bowl()
    //抛出异常
    //Exception in thread "main" java.lang.ClassCastException: Food cannot be cast to Fruit
    //bowl.setItem(Food() as Fruit)
    bowl.setItem(Apple())
}

fun demo(fruit: Source<Fruit>) {
    //不能通过，
    //val food:Source<Food> = fruit.getSource()
}

inline fun <reified T> getGenericType() = T::class.java