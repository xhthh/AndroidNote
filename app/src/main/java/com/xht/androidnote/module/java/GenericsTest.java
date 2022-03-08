package com.xht.androidnote.module.java;

import com.xht.androidnote.summarize.test.Container;

import java.util.ArrayList;

public class GenericsTest {

    public static void main(String[] args) {

        //泛型类
        Container<String, String> stringContainer = new Container<>("name", "xht");
        Container<String, Integer> integerContainer = new Container<>("age", 18);

        //泛型方法
        out(stringContainer.getValue());
        out(integerContainer.getValue());
        System.out.println(stringContainer.getKey() + " ：" + stringContainer.getValue());
        System.out.println(integerContainer.getKey() + " ：" + integerContainer.getValue());

        B stature = new B("stature", 172);

        new Person<String>(233);


        ArrayList<String> list1 = new ArrayList();
        list1.add("1"); //编译通过
//        list1.add(1); //编译错误
        String str1 = list1.get(0); //返回类型就是String

        ArrayList list2 = new ArrayList<String>();
        list2.add("1"); //编译通过
        list2.add(1); //编译通过
        Object object = list2.get(0); //返回类型就是Object

        new ArrayList<String>().add("11"); //编译通过
//        new ArrayList<String>().add(22); //编译错误

        String str2 = new ArrayList<String>().get(0); //返回类型就是String
    }

    /**
     * 泛型方法
     *
     * @param t
     * @param <T>
     */
    public static <T> void out(T t) {
        System.out.println(t);
    }


    static class A extends Container {
        public A(Object key, Object value) {
            super(key, value);
        }
    }

    static class B extends Container<String, Integer> {
        public B(String key, Integer value) {
            super(key, value);
        }
    }


    public static class Person<E> {
        public <T> Person(T t) {
            System.out.println(t);
        }

    }

}
