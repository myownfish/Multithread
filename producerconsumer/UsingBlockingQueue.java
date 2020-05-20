package com.test.multithread.producerconsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class UsingBlockingQueue {

    public static void main(String[] args) {

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(16);

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
        
        consumer1.interrupt();
        consumer2.interrupt();

    }

}
