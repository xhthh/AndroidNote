package com.xht.androidnote.module.pattern.singleton;

import com.xht.androidnote.utils.L;

/**
 * Created by xht on 2018/8/17.
 *
 * 双重检查锁定
 *
 * 第一层判断主要是避免不必要的同步（相比懒汉式，即使sInstance 已经被初始化，但是每次调用 getInstance方法时，仍会进行同步）
 * 第二层判断是为了在null的情况下创建实例
 *
 * 线程A执行到 sInstance = new Singleton()时，这并不是一个原子操作，
 * 这句代码最终会被编译成多条汇编指令，它大致做了3件事情：
 *      1）给 Singleton 的实例分配内存
 *      2）调用 Singleton() 的构造函数，初始化成员字段
 *      3）将 sInstance 对象指向分配的内存空间（此时 sInstance 就不是 null了）
 *
 * 但是由于Java编译器允许处理器乱序执行，在 JDK 1.5之前，上面的第二和第三的顺序是无法保证的。
 * 如果是 1-3-2，在 3 执行完、2 未执行之前，被切换到 B 线程，这时 sInstance 因为已经在 A 线程内
 * 执行过了第三点，sInstance 已经是非空了，所以，线程B 直接取走 sInstance，再使用时就会出错。
 *
 *
 * 在 JDK 1.5之后，只需要将 sInstance 的定义改成 private volatile static Singleton sInstance = null
 * 就可以使用了，但是会影响性能
 */

public class Singleton {

    //private static Singleton sInstance = null;//JDK1.5之前
    private volatile static Singleton sInstance = null;//JDK1.5之后

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (sInstance == null) {
            synchronized (Singleton.class) {
                if (sInstance == null) {
                    sInstance = new Singleton();
                }
            }
        }
        return sInstance;
    }

    public void doSomething() {
        L.i("Singleton---doSomething()");
    }

}
