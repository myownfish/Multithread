package com.test.multithread.adderandaccumulator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class TestAtomicLong {

    public static void main(String[] args) throws InterruptedException {
        AtomicLong counter = new AtomicLong();
        ExecutorService service = Executors.newFixedThreadPool(16);
        for (int i = 0; i < 1000; i++) {
            service.submit(new Task(counter));
        }
        service.shutdown();
        service.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println(counter.get());

    }

    private static class Task implements Runnable {
        private final AtomicLong counter;

        public Task(AtomicLong counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
           System.out.println(Thread.currentThread().getName() + ":" + this.counter.incrementAndGet());
        }
    }
}
