package com.xht.androidnote.module.pattern.singleton;

/**
 * Created by xht on 2018/8/18.
 *
 * 静态内部类单例模式
 *
 * 当第一次加载 Singleton类时并不会初始化 instance，只有在第一次调用 getInstance()时才会导致
 * instance被初始化。因此，第一次调用 getInstance() 会导致虚拟机加载 SingletonHolder类，
 * 这种方式不仅能够确保线程安全，也能够保证单例对象的唯一性，同时也延迟了单例的实例化。
 */

public class Singleton2 {

    private Singleton2() {
    }

    public static Singleton2 getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final Singleton2 instance = new Singleton2();
    }

}
