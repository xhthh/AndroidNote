package com.xht.androidnote.module.java;

import java.util.HashMap;

/**
 * Created by xht on 2020/8/17
 */
public class OuterClass {

    private static final OuterClass outerClass = new OuterClass();
    private static InnerClass2 innerClass2 = outerClass.new InnerClass2();
    private static final int age = 0;


    private HashMap<String, Integer> map = new HashMap<>();

    public static void main(String[] args) {



    }

    static class InnerClass {
        static Student student;
    }


    class InnerClass2{

    }
}
