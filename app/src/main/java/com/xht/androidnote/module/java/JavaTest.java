package com.xht.androidnote.module.java;

import android.os.Build;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Created by xht on 2018/5/25.
 */

public class JavaTest {

    public static void main(String[] args) {

        List<Integer> list = new ArrayList<>();
        list.add(00);
        list.add(11);
        list.add(22);
        int data = list.get(list.size() - 1);
        System.out.println("data=" + data);


        int test = 10_000;

        System.out.println("10_000 = " + test);


        Integer a = 1000, b = 1000;

        Integer c = 100, d = 100;

        System.out.println("a==b：" + (a == b));//false
        System.out.println("c==d：" + (c == d));//true
        /**
         * Integer.Java 类，有一个内部私有类，IntegerCache.java，它缓存了从-128到127之间的所有的整数对象。
         */
    }

}
