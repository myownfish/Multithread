package com.test.multithread;

public class ProducerConsumer {

    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub

        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        
        Runnable produceTask = ()->{
            for(int i = 0; i < 500; i++) {
                producer.produce();
            }
            System.out.println("Done producing");
        };
        
        Runnable consumeTask = () -> {
            for (int i = 0; i < 500; i++) {
                consumer.consume();
            }
            System.out.println("Done consuming");
        };
        
        Thread consumerThread  = new Thread(consumeTask);
        Thread producerThread = new Thread(produceTask);
        consumerThread.start();
        producerThread.start();
        
        consumerThread.join();
        producerThread.join();
        System.out.println("Remaining task is " + count);
    }

    /*
     * There is race condition on count and buffer
     */
    static int count = 0;
    static int[] buffer = new int[10];
    
    static class Producer {
        public void produce() {
            while(isFull(buffer)) {}
            buffer[count++] = 1;
            
        }
    }
    
    static class Consumer {
        public void consume() {
            while(isEmpty(buffer)) {}
            buffer[--count] = 0;
        }
    }

    public static boolean isFull(int[] buffer2) {
        return count == buffer2.length;
    }

    public static boolean isEmpty(int[] buffer2) {
        return count == 0;
    }
}

