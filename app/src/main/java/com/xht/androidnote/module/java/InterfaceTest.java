package com.xht.androidnote.module.java;

public interface InterfaceTest {

    void work();

    default void test1() {
        System.out.println("------test------");
    }

    static void test2() {

    }
}
