package com.xht.androidnote.module.generic;

/*
前面的T的声明，跟类后面的 <T> 没有关系。
方法前面的<T>是给这个方法级别指定泛型。

show_2 和 show_3 方法其实是完完全全等效的。意思就是说ClassName中一旦T被指定为Fruit后，那么 show_1 没有前缀<T>的话，该方法中只能是show_1 (Fruit对象)。

而你要是有前缀<T>或<E>的话，那么你就是告诉编译器对它说：这是我新指定的一个类型,跟ClassName<T>类对象中的T没有半毛钱的关系。也就是说这个show_3中的T和show_2中的E是一个效果，也就是你可以把show_3同等程度地理解为<E> void show_3(E e){~~~~~}。

这个方法返回值前也加个的话，这个T就代表该方法自己独有的某个类，而不去和类中限定的T产生冲突。
原文链接：https://blog.csdn.net/ywyngq/article/details/113517859
 */
public class ClassName<T> {

    void show1(T t) {
        System.out.println("show1" + t.toString());
    }

    <E> void show2(E e) {
        System.out.println("show2" + e.toString());
    }

    <T> void show3(T t) {
        System.out.println("show3" + t.toString());
    }

    <X> void show4(X x) {
        System.out.println("show4" + x.toString());
    }

    public static void main(String[] args) {
        ClassName<Fruit> o = new ClassName<>();
        Fruit fruit = new Fruit();
        Apple apple = new Apple();
        Dog dog = new Dog();

        System.out.println("---------------演示show1---------------");
        o.show1(fruit);
        o.show1(apple);
        //o.show1(dog); 编译失败，不是fruit类或它的的子类
        o.show4(dog);
        System.out.println("---------------演示show2---------------");
        o.show2(fruit);
        o.show2(apple);
        o.<Dog>show2(dog);
        o.show4(dog);
        System.out.println("---------------演示show3---------------");
        o.show3(fruit);
        o.show3(apple);
        o.show3(dog);
        o.show4(dog);
    }
}

class Dog {
    public String toString() {
        return "dog";
    }
}

class Apple extends Fruit {
    @Override
    public String toString() {
        return "apple";
    }
}

class Fruit {
    public String toString() {
        return "fruit";
    }
}