package com.test.multithread.scattergather;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ScatterGatherUseCountDownLatch {

    public static void main(String[] args) throws InterruptedException {
        ScatterGatherUseCountDownLatch test = new ScatterGatherUseCountDownLatch();
        System.out.println(test.getPrices(1));

    }

    private Set<Integer> getPrices(int productId) throws InterruptedException {
        Set<Integer> prices = Collections.synchronizedSet(new HashSet<>());
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(3);
        threadPool.submit(new Task("url1", 1, prices, latch));
        threadPool.submit(new Task("url2", 2, prices, latch));
        threadPool.submit(new Task("url3", 3, prices, latch));
        latch.await(3, TimeUnit.SECONDS);
        // Thread.sleep(3);
         threadPool.shutdown();
        return prices;
    }

    private class Task implements Runnable {

        private String url;
        private int productId;
        private Set<Integer> prices;
        private CountDownLatch latch;

        public Task(String url, int productId, Set<Integer> prices, CountDownLatch latch) {
            super();
            this.url = url;
            this.productId = productId;
            this.prices = prices;
            this.latch = latch;
        }

        @Override
        public void run() {
            // make http call to get price
            prices.add(this.productId);
            this.latch.countDown();
        }

    }

}
