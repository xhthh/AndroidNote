package com.xht.androidnote.module.java.thread;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class MyLock {

    private Sync sync = new Sync();

    /**
     * 加锁
     */
    public void lock() {
        sync.acquire(1);
    }

    /**
     * 释放锁
     */
    public void unlock() {
        sync.release(1);
    }


    static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            return compareAndSetState(0, 1);
        }

        @Override
        protected boolean tryRelease(int arg) {
            setState(0);
            return true;
        }
    }

}
