package com.xht.androidnote.module.java;

import android.os.Build;

import java.util.Base64;

/**
 * Created by xht on 2018/5/25.
 */

public class JavaTest {

    public static void main(String[] args) {


        String number = "15037102559";
        byte[] bytes = number.getBytes();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            byte[] decode = Base64.getDecoder().decode(bytes);
            String str = new String(decode);
            System.out.println("str=" + str);
        }


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
