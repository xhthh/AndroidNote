package com.xht.androidnote.summarize.test;


public class ThreadTest {

    private static Thread t1;
    private static Thread t2;

    public static void main(String[] args) {
//        test1();
        test2();
    }

    private static void test2() {
        Thread t1 = new Thread(new Work(null));
        Thread t2 = new Thread(new Work(t1));
        Thread t3 = new Thread(new Work(t2));
        t1.start();
        t2.start();
        t3.start();
    }

    /**
     * 2个线程交替打印
     * synchronized
     * LockSupport
     * BlockingQueue
     * SynchronizeQueue
     */
    private static void test1() {
        Object lock = new Object();
        char[] aI = "1234567".toCharArray();
        char[] aC = "ABCDEFG".toCharArray();

        t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < aI.length; i++) {
                    synchronized (lock) {
                        System.out.print(aI[i] + "    ");
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

        t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < aC.length; i++) {
                    synchronized (lock) {
                        System.out.print(aC[i]);
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
        t1.start();
        t2.start();
    }

    static class Work implements Runnable {
        private Thread beforeThread;

        public Work(Thread beforeThread) {
            this.beforeThread = beforeThread;
        }

        @Override
        public void run() {
            if (beforeThread != null) {
                try {
                    beforeThread.join();
                    System.out.println("thread start:" + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("thread start:" + Thread.currentThread().getName());
            }
        }
    }

}
