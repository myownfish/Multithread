package com.test.multithread.adderandaccumulator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAccumulator;

public class TestAccumulator {

    public static void main(String[] args) throws InterruptedException {
        LongAccumulator counter = new LongAccumulator((x, y) -> x + y, 0);
        ExecutorService service = Executors.newFixedThreadPool(16);
        for (int i = 0; i < 100; i++) {
            service.submit(new Task(counter));
        }
        service.shutdown();
        service.awaitTermination(5, TimeUnit.SECONDS);
        //All thread-local variable added
        System.out.println(counter.get());

    }

    private static class Task implements Runnable {
        private final LongAccumulator counter;

        public Task(LongAccumulator counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            //Thread-local variable incremented
            counter.accumulate(1);
            System.out.println(Thread.currentThread().getName() + ":" + this.counter.get());

        }
    }
}
