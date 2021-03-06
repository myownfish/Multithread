package com.test.multithread;

public class ProducerConsumer_synchronized_wrong {

    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub

        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        Runnable produceTask = () -> {
            for (int i = 0; i < 50; i++) {
                producer.produce();
            }
            System.out.println("Done producing");
        };

        Runnable consumeTask = () -> {
            for (int i = 0; i < 50; i++) {
                consumer.consume();
            }
            System.out.println("Done consuming");
        };

        Thread consumerThread = new Thread(consumeTask);
        Thread producerThread = new Thread(produceTask);
        consumerThread.start();
        producerThread.start();

        consumerThread.join();
        producerThread.join();
        System.out.println("Remaining task is " + count);
    }

    static int count = 0;
    static int[] buffer = new int[10];
    static Object lock = new Object();

    static class Producer {
        public void produce() {
            synchronized (lock) {
                while (isFull(buffer)) {
                    System.out.println("Buffer is full");
                }
                buffer[count++] = 1;
            }

        }
    }
    /*
     * as we have while(isEmpty(buffer)) here,
     * the key of the lock will not be release.
     * And Producer is not able to produce.
     */
    static class Consumer {
        public void consume() {
            synchronized (lock) {
                while (isEmpty(buffer)) {
                    System.out.println("Buffer is empty");
                }
                buffer[--count] = 0;
            }
        }
    }

    public static boolean isFull(int[] buffer2) {
        return count == buffer2.length;
    }

    public static boolean isEmpty(int[] buffer2) {
        return count == 0;
    }
}
