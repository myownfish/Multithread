package com.test.multithread.asynchronous;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CompareInvokeAllWithCompletionService {
    /*
     * One difference over invokeAll() is the order in which the Futures,
     * representing the executed tasks are returned. ExecutorCompletionService uses
     * a queue to store the results in the order they are finished, while
     * invokeAll() returns a list having the same sequential order as produced by
     * the iterator for the given task list. 
     * invokeAll: returns a list of Future
     * objects after all tasks finish or the timeout expires. 
     * CompletionService:
     * CompletionService.take() Retrieves and removes the Future representing the next completed task,
     * waiting if none are yet present.
     */
    public static void main(String[] args) {
        try {
            testCompletionService();
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        testInvokeAll();
    }

    public static void testCompletionService() throws InterruptedException, ExecutionException {
        ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(10);
        CompletionService<String> service = new ExecutorCompletionService<>(WORKER_THREAD_POOL);
        List<Callable<String>> callables = Arrays.asList(
                new DelayedCallable("slow thread", 3000),
                new DelayedCallable("fast thread", 100));
        for (Callable<String> callable : callables) {
            service.submit(callable);
        }

        long startProcessingTime = System.currentTimeMillis();
        Future<String> future = service.take();
        String firstThreadResponse = future.get();
        long totalProcessingTime = System.currentTimeMillis() - startProcessingTime;
        System.out.println(totalProcessingTime);
        System.out.println(firstThreadResponse);

        future = service.take();
        String secondThreadResponse = future.get();
        totalProcessingTime = System.currentTimeMillis() - startProcessingTime;
        System.out.println(totalProcessingTime);
        System.out.println(secondThreadResponse);
        awaitTerminationAfterShutdown(WORKER_THREAD_POOL);

    }

    public static void testInvokeAll() {

        ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(10);
        List<Callable<String>> callables = Arrays.asList(
                new DelayedCallable("slow thread", 3000),
                new DelayedCallable("fast thread", 100));

        long startProcessingTime = System.currentTimeMillis();
        try {
            List<Future<String>> futures = WORKER_THREAD_POOL.invokeAll(callables);
            awaitTerminationAfterShutdown(WORKER_THREAD_POOL);

            long totalProcessingTime = System.currentTimeMillis() - startProcessingTime;
            System.out.println(totalProcessingTime);
            String firstThreadResponse = futures.get(0).get();
            System.out.println(firstThreadResponse);

            String secondThreadResponse = futures.get(1).get();
            System.out.println(secondThreadResponse);
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
