package com.xht.androidnote.module.java.thread;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class ConcurrencyIssues2 {

    static List<Thread> threadList = new ArrayList<>();//存放线程的集合
    static int threadSize = 3;//总共多少个线程
    static int threadIndex = 0;//当前线程下标
    static int maxValue = 10;//需要输出的数的最大值
    static int curValue = 1;//数的当前值

    public static void main(String[] args) throws InterruptedException {
        //test2();
        test3();
    }

    private static void test3() {
        for (int i = 1; i <= threadSize; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        LockSupport.park();
                        if (curValue <= maxValue) {
                            System.out.println(Thread.currentThread().getName() + "：" + curValue++);
                        } else {
                            break;
                        }
                        LockSupport.unpark(threadList.get(++threadIndex % threadSize));
                    }

                    for (Thread thread : threadList) {
                        LockSupport.unpark(thread);
                    }
                }
            }, "Thread-" + i);
            threadList.add(thread);
        }

        for (int i = 0; i < threadList.size(); i++) {
            threadList.get(i).start();
        }

        LockSupport.unpark(threadList.get(0));
    }

    private static void test2() {
        //创建线程
        for (int i = 1; i <= threadSize; i++) {
            Thread thread = new Thread(() -> {
                System.out.println("当前线程---" + Thread.currentThread().getName());
                while (true) {
                    //阻塞当前线程
                    System.out.println("阻塞当前线程 " + Thread.currentThread().getName());
                    LockSupport.park();
                    //当前的值需要小于最大值
                    if (curValue <= maxValue) {
                        System.out.println(Thread.currentThread().getName() + "：" + curValue++);
                    } else {
                        break;
                    }
                    //唤起下一个线程
                    System.out.println("唤起下一个线程 " + threadList.get(++threadIndex % threadList.size()).getName());
                    LockSupport.unpark(threadList.get(threadIndex % threadList.size()));
                }
                System.out.println("唤起所有线程 " + Thread.currentThread().getName());
                //唤起所有线程
                for (Thread thread1 : threadList) {
                    LockSupport.unpark(thread1);
                }
            });
            thread.setName(String.format("Thread%d", i));
            threadList.add(thread);
        }

        //启动所有线程
        for (Thread thread : threadList) {
            System.out.println("启动所有线程---" + thread.getName());
            thread.start();
        }

        //唤起第一个线程
        LockSupport.unpark(threadList.get(0));
    }

    private static void test1() throws InterruptedException {
        ThreadJoinTest t1 = new ThreadJoinTest("今天");
        ThreadJoinTest t2 = new ThreadJoinTest("明天");
        ThreadJoinTest t3 = new ThreadJoinTest("后天");

        t1.start();
        t1.join();
        t2.start();
        t2.join();
        t3.start();
    }
}

class ThreadJoinTest extends Thread {
    public ThreadJoinTest(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(this.getName() + ":" + i);
        }
    }
}


