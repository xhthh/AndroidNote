package com.xht.androidnote.module.dsa.test;

/**
 * Created by xht on 2020/7/30
 *
 * 静态内部类方式
 * 保证对象唯一性，线程安全，延迟初始化
 *
 *
 * 双重锁问题，因为 instance = new Singleton(); 不是原子操作，这句代码会做三件事：
 * ①给Singleton 分配内存
 * ②调用 Singleton 构造函数，初始化成员变量
 * ③将 instance 对象指向分配的内存空间（此时 instance就不是null了）
 * 但是Java编译器允许编译器乱序处理，所以第三步可能先于第二步执行，如果此时切换线程，此时 instance已经不为空，就会出错
 *
 * 将 instance 用 volatile 修饰
 * 保证执行顺序
 */
public class Singleton {

    private Singleton() {
    }


    public static Singleton getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final Singleton instance = new Singleton();
    }
}
