package com.xht.dsa;

/**
 * Created by xht on 2019/4/15.
 */

public class Test {

    static {
        System.out.println("这是静态代码块");
    }

    {
        System.out.println("这是普通代码块");
    }

    public Test() {
        System.out.println("这是构造方法");
    }

    public static void show() {
        System.out.println("这是静态方法");
    }

    public void fun() {
        System.out.println("这是普通方法");
    }

    public static void main(String[] args) {
        Test.show();
        Test test = new Test();
        test.fun();
    }

}
