package com.xht.androidnote.module.java;

import android.os.Build;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xht on 2018/5/25.
 */

public class JavaTest {

    private final Object tail;

    public JavaTest() {
        tail = null;
    }

    public static void main(String[] args) {

        //extracted();
        testJoin();
    }

    private static void testJoin() {
        final Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("t1");
            }
        });
        final Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    //引用t1线程，等待t1线程执行完
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t2");
            }
        });
        Thread t3 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    //引用t2线程，等待t2线程执行完
                    t2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t3");
            }
        });
        t3.start();
        t2.start();
        t1.start();

    }

    private static void extracted() {
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

        System.out.println(1<<30);

        HashMap<Integer, Integer> map = new HashMap<>();
        map.size();
    }

}
