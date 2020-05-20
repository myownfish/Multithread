package com.test.multithread.producerconsumer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueueUsingLockAndCondition<E> {

    private Queue<E> queue;
    private int max = 16;
    private ReentrantLock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    public MyBlockingQueueUsingLockAndCondition(int size) {
        queue = new LinkedList<>();
        this.max = size;
    }

    public void put(E e) throws InterruptedException {
        lock.lock();
        try {
            /*
             * must use while loop instead of if. Using if may cause queue exceed max amount
             * Assume queue is full and two producers are all waiting for notFull to signal,
             * one consumer consume one item, then invoke signalAll, then both producers are
             * awake and waiting for acquire the lock. Both producers when they acquire the
             * lock, will go to queue.add(e) without checking the size again. 
             * Using signal instead of signalAll looks like will not have this kind of issue.
             */
            //if (queue.size() == max) {
            while (queue.size() == max) {
                // block until queue has at least 1 empty slot to add item.
                // This will release the lock
                // if not add this, as queue is a linkedlist, it will continueously grow
                notFull.await();
            }
            queue.add(e);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        lock.lock();
        try {
            
            /*
             * must use while loop instead of if. Using if may cause when queue is already empty, but
             * still try to remove from it which will throw java.util.NoSuchElementException.
             * Assume queue is empty and two consumers are all waiting for notEmpty to signal,
             * one producer produce one item, then invoke signalAll, then both consumers are
             * awake and waiting for acquire the lock. Both consumers when they acquire the
             * lock, will go to queue.remove() without checking the size again. 
             * Using signal instead of signalAll looks like will not have this kind of issue.
             */
            //if (queue.size() == 0) {
            while (queue.size() == 0) {
                // block until queue has at least 1 item to take
                // This will release the lock
                notEmpty.await();
            }
            E item = queue.remove();
            notFull.signalAll();
//            notFull.notifyAll();
            // 报错，notifyAll需要当前线程hold notFull的monitor。但是当前线程没有
            // 当前线程只有lock的monitor
            // 而且调用这个方法也是错误的，跟await对应的方法是singal
            /*
             * notFull.notifyAll(); This will throws IllegalMonitorStateException void
             * java.lang.Object.notifyAll()
             * 
             * Wakes up all threads that are waiting on this object's monitor. A thread
             * waits on an object's monitor by calling one of the wait methods.
             * 
             * The awakened threads will not be able to proceed until the current thread
             * relinquishes the lock on this object. The awakened threads will compete in
             * the usual manner with any other threads that might be actively competing to
             * synchronize on this object; for example, the awakened threads enjoy no
             * reliable privilege or disadvantage in being the next thread to lock this
             * object.
             * 
             * This method should only be called by a thread that is the owner of this
             * object's monitor. See the notify method for a description of the ways in
             * which a thread can become the owner of a monitor.
             * 
             * Throws: IllegalMonitorStateException - if the current thread is not the owner
             * of this object's monitor. See Also: java.lang.Object.notify()
             * java.lang.Object.wait()
             */
            return item;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {

        MyBlockingQueueUsingLockAndCondition<Integer> queue = new MyBlockingQueueUsingLockAndCondition<>(2);

        Runnable producerRunnable = new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        queue.put(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread producer1 = new Thread(producerRunnable);
        Thread producer2 = new Thread(producerRunnable);

        producer1.start();
        producer2.start();

        Runnable consumerRunnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        queue.take();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread consumer2 = new Thread(consumerRunnable);
        Thread consumer1 = new Thread(consumerRunnable);

        consumer2.start();
        consumer1.start();

    }

}
