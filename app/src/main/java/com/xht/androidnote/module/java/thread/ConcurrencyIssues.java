package com.xht.androidnote.module.java.thread;

import com.xht.androidnote.module.dsa.manhuasuanfa.LruCache;

import java.util.concurrent.locks.LockSupport;

public class ConcurrencyIssues {
    static Thread t1 = null, t2 = null, t3 = null, t4 = null;
    public static void main(String[] args) {
        char[] aI = "1234567".toCharArray();
        char[] aC = "ABCDEFG".toCharArray();

        t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < aC.length; i++) {
                    // 起始先打印一个字母
                    System.out.println(aC[i]);
                    // 打印完唤醒t2打印数字
                    LockSupport.unpark(t2);
                    // 自己阻塞，等待唤醒
                    LockSupport.park();
                }
            }
        });
        t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < aI.length; i++) {
                    // 起始先阻塞等待
                    LockSupport.park();
                    // 被唤醒后打印数字
                    System.out.println(aI[i]);
                    // 唤醒t1
                    LockSupport.unpark(t1);
                }
            }
        });
        //t1.start();
        //t2.start();

        Object lock = new Object();
        t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < aC.length; i++) {
                    synchronized (lock) {
                        System.out.println(aC[i]);
                        lock.notify();
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < aI.length; i++) {
                    synchronized (lock) {
                        System.out.println(aI[i]);
                        lock.notify();
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        t3.start();
        t4.start();
    }
}
