package com.test.multithread.scattergather;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ScatterGatherUseCompletableFuture {

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        ScatterGatherUseCompletableFuture test = new ScatterGatherUseCompletableFuture();
        System.out.println(test.getPrices(1));

    }

    private Set<Integer> getPrices(int productId) throws InterruptedException, ExecutionException, TimeoutException {
        Set<Integer> prices = Collections.synchronizedSet(new HashSet<>());
        
        CompletableFuture<Void> task1 = CompletableFuture.runAsync(new Task("url1", 1, prices));
        CompletableFuture<Void> task2 = CompletableFuture.runAsync(new Task("url2", 2, prices));
        CompletableFuture<Void> task3 = CompletableFuture.runAsync(new Task("url3", 3, prices));
        
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(task1,task2, task3);
        allTasks.get(3, TimeUnit.SECONDS);
        return prices;
    }

    private class Task implements Runnable {

        private String url;
        private int productId;
        private Set<Integer> prices;

        public Task(String url, int productId, Set<Integer> prices) {
            super();
            this.url = url;
            this.productId = productId;
            this.prices = prices;
        }

        @Override
        public void run() {
            // make http call to get price
            prices.add(this.productId);
        }

    }

}
