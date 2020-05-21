package com.test.multithread.scattergather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ScatterGatherUseFutureCallable {

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        ScatterGatherUseFutureCallable test = new ScatterGatherUseFutureCallable();
        System.out.println(test.getPrices(1));
    }

    private Set<Integer> getPrices(int productId) {
        Set<Integer> prices = new HashSet<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        List<Callable<Integer>> task = new ArrayList<>();
        task.add(new Task("url1", 1));
        task.add(new Task("url2", 2));
        task.add(new Task("url3", 3));
        
        try {
            List<Future<Integer>> results = threadPool.invokeAll(task, 3, TimeUnit.SECONDS);
            for(Future<Integer> future: results) {
                if(!future.isCancelled()) {
                    try {
                        prices.add(future.get());
                    } catch (ExecutionException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 

        threadPool.shutdown();

        return prices;
    }

    private class Task implements Callable<Integer> {

        private String url;
        private int productId;

        public Task(String url, int productId) {
            super();
            this.url = url;
            this.productId = productId;
        }

        @Override
        public Integer call() throws Exception {
            if (this.productId == 1) {
                throw new NullPointerException();
            }
            return this.productId;
        }

    }

}
