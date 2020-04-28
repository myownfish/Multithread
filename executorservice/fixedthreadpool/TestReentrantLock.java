package com.test.multithread.executorservice.fixedthreadpool;

import java.util.concurrent.locks.ReentrantLock;

public class TestReentrantLock {

    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> accessResource());

        Thread t2 = new Thread(() -> accessResource1());
        t2.start();
        t1.start();
    }

    private static Object accessResource1() {
        lock.lock();
        lock.lock();
        System.out.println(lock.getHoldCount());
        lock.unlock();
        lock.unlock();
        /*
         * must unlock twice here, otherwise, lock is not released and other thread will
         * not able to get the lock
         */
        return null;
    }

    private static Object accessResource() {
        lock.lock();
        System.out.println(lock.getHoldCount());
        lock.unlock();
        return null;
    }

}
