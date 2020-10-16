package com.xht.androidnote.module.java.reflect;

import java.lang.reflect.Method;

public class ReflectTest {

    public static void main(String[] args) {
        try {
            Class clazz = Class.forName("com.xht.androidnote.module.java.reflect.ReflectTest");
            Object reflectTest = clazz.newInstance();
            Method method = clazz.getDeclaredMethod("refMethod");
            method.invoke(reflectTest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void refMethod() {
        System.out.println("hello reflect");
    }

}
