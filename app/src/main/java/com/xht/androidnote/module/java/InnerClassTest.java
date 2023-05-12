package com.xht.androidnote.module.java;

public class InnerClassTest {
    int a = 1;
    static int b = 2;
    private int c = 3;
    private static int d = 4;
    //非静态内部类
    class Inner1{
        void test(){
            System.out.println(a);   //外部类属性
            System.out.println(b);   //外部类静态属性
            System.out.println(c);   //外部私有类属性
            System.out.println(d);   //外部静态私有类属性
        }
    }

    //静态内部类
    static class Inner2{
        void test(){
//            System.out.println(a);  //外部类属性  有错误
            System.out.println(b);  //外部类静态属性
//            System.out.println(c);  //外部私有类属性  有错误
            System.out.println(d);  //外部静态私有类属性
        }
    }


}
