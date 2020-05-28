package com.test.multithread.adderandaccumulator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class TestAdder {

    public static void main(String[] args) throws InterruptedException {
        LongAdder counter = new LongAdder();
        ExecutorService service = Executors.newFixedThreadPool(16);
        for (int i = 0; i < 1000; i++) {
            service.submit(new Task(counter));
        }
        service.shutdown();
        service.awaitTermination(5, TimeUnit.SECONDS);
        //All thread-local variable added
        System.out.println(counter.sum());

    }

    private static class Task implements Runnable {
        private final LongAdder counter;

        public Task(LongAdder counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            //Thread-local variable incremented
            counter.increment();
            System.out.println(Thread.currentThread().getName() + ":" + this.counter.intValue());

        }
    }
}
