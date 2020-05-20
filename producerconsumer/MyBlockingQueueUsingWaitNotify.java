package com.test.multithread.producerconsumer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueueUsingWaitNotify<E> {

    private Queue<E> queue;
    private int max = 16;

    public MyBlockingQueueUsingWaitNotify(int size) {
        queue = new LinkedList<>();
        this.max = size;
    }

    public void put(E e) throws InterruptedException {
        synchronized (queue) {
            /*
             * must use while loop instead of if. Using if may cause queue exceed max amount
             * Assume queue is full and two producers are all waiting for empty space,
             * one consumer consume one item, then invoke notifyAll, then both producers are
             * awake and waiting for acquire the lock. Both producers when they acquire the
             * lock, will go to queue.add(e) without checking the size again. 
             * Using signal instead of signalAll looks like will not have this kind of issue.
             */
//            if (queue.size() == max) {
            while (queue.size() == max) {
                // block until queue has at least 1 empty slot to add item.
                // This will release the lock
                // if not add this, as queue is a linkedlist, it will continueously grow
                queue.wait();
            }
            queue.add(e);
            queue.notifyAll();

        }
    }

    public E take() throws InterruptedException {
        synchronized (queue) {
            /*
             * must use while loop instead of if. Using if may cause when queue is already
             * empty, but still try to remove from it which will throw
             * java.util.NoSuchElementException. Assume queue is empty and two consumers are
             * all waiting for item, one producer produce one item, then
             * invoke notifyAll, then both consumers are awake and waiting for acquire the
             * lock. Both consumers when they acquire the lock, will go to queue.remove()
             * without checking the size again. Using notify instead of notifyAll looks like
             * will not have this kind of issue.
             */
//             if (queue.size() == 0) {
            while (queue.size() == 0) {
                // block until queue has at least 1 item to take
                // This will release the lock
                queue.wait();
            }
            E item = queue.remove();
            queue.notifyAll();

            return item;
        }

    }

    public static void main(String[] args) {

        MyBlockingQueueUsingWaitNotify<Integer> queue = new MyBlockingQueueUsingWaitNotify<>(2);

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
