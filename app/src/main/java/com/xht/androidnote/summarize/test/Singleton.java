package com.xht.androidnote.summarize.test;

public class Singleton {
    private Singleton() {
    }

    private static volatile Singleton instance;

    public Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}

class Singleton2 {
    private Singleton2() {
    }

    public static Singleton2 getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final Singleton2 instance = new Singleton2();
    }
}
