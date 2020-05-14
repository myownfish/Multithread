package com.test.multithread.condition;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionConsumerProducer {

    private static Lock lock = new ReentrantLock();
    private static Condition added = lock.newCondition();
    private static Condition removed = lock.newCondition();
    private volatile static int count = 0;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(() -> {
            try {
                produce();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        service.submit(() -> {
            try {
                consume();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

    }

    public static void produce() throws InterruptedException {

        while (true) {
            lock.lock();
            try {
                while (count == 2) {
                    removed.await();
                }
                addData();
                added.signal();
            } finally {
                lock.unlock();
            }
        }

    }

    private static void addData() {
        lock.lock();
        count = count + 1;
        System.out.println("Add data:" + count);
        lock.unlock();
    }

    public static void consume() throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                while (count == 0) {
                    added.await();
                }
                getData();
                removed.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    private static void getData() {
        lock.lock();
        try {
            System.out.println("Get data:" + count);
            count = count - 1;
        } finally {
            lock.unlock();
        }
    }
}
