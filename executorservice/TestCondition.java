package com.test.multithread.executorservice;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestCondition {

    static Lock lock = new ReentrantLock();
    static Condition conditionMet = lock.newCondition();

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> method1());
        Thread t2 = new Thread(() -> method2());
        t1.start();
        t2.start();
    }

    public static void method1() {
        lock.lock();
        try {
            conditionMet.await();
            //This can only been called when current thread hold the lock.
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void method2() {
        lock.lock();
        try {
            conditionMet.signal();
        } finally {
            lock.unlock();
        }
    }

}
