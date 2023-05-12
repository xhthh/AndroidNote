package com.xht.androidnote.module.java;

import java.util.HashMap;

/**
 * Created by xht on 2020/8/17
 */
public class OuterClass {

    private static final OuterClass outerClass = new OuterClass();
    private static InnerClass2 innerClass2 = outerClass.new InnerClass2();
    private static final int age = 18;
    private String name = "嘻嘻";


    private HashMap<String, Integer> map = new HashMap<>();

    public static void main(String[] args) {

        int age = new InnerClass().test();
        System.out.println("---age=" + age);
    }

    private void test() {
        InnerClass2 innerClass2 = new InnerClass2();
        String name1 = innerClass2.name;
    }

    private int getAge() {
        return age;
    }

    static class InnerClass {
        static Student student;
        private int test() {
            return outerClass.getAge();
        }
        private static void test2() {
            System.out.println("-----静态内部类---静态方法-----" + age);
        }
    }


    class InnerClass2{
        private String name = "xxx";
        private int test() {
            return OuterClass.this.getAge();
        }
    }
}
