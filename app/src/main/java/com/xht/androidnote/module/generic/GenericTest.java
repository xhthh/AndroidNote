package com.xht.androidnote.module.generic;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

public class GenericTest {
    public static void main(String[] args) {
        //        test1();
        test2();
    }

    private static void test2() {
        Pair<Integer, String> p1 = new Pair<>(1, "apple");
        Pair<String, String> p2 = new Pair<>("1", "pear");
        Pair<String, String> p3 = new Pair<>("2", "pear");
        //报错，显示声明了参数的泛型类型，p1 和 p2 参数泛型应该一致
        //boolean compare = GenericUtil.<Integer, String>compare(p1, p2);
        //GenericUtil.compare(p1,p2);
        boolean compare = GenericUtil.compare(p2, p3);
    }

    private static void test1() {
        Child child = new Child();
        ParameterizedType parameterizedType = (ParameterizedType) child.getClass().getGenericSuperclass();
        System.out.println(Arrays.toString(parameterizedType.getActualTypeArguments()));
    }
}

class Parent<T> {
}

class Child extends Parent<String> {
}
