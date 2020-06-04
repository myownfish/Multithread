package com.test.multithread.asynchronous;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UseCompletableFutureAsSimpleFuture {

    public static void main(String[] args) {
        UseCompletableFutureAsSimpleFuture test = new UseCompletableFutureAsSimpleFuture();
        try {
            Future<String> completableFuture = test.calculateAsync();
            String result = completableFuture.get();
            // this will block if the it is not completed yet.
            System.out.println(result);

            Future<String> nonBlockingFuture = CompletableFuture.completedFuture("Wolrd");
            String result1 = nonBlockingFuture.get();
            /*
             * If you already know the result of a computation, you can use the static
             * completedFuture method with an argument that represents a result of this
             * computation. Then the get method of the Future will never block, immediately
             * returning this result instead.
             */
            System.out.println(result1);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        /*
         * When we block on the result using the Future.get() method, 
         * it throws CancellationException if the future is canceled:
         */
        Future<String> future = test.calculateAsynchWithCancellation();
        try {
            future.get();//// CancellationException
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public Future<String> calculateAsync() throws InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(() -> {
            Thread.sleep(500);
            future.complete("Hello");
            return null;
        });
        service.shutdown();
        return future;
    }

    public Future<String> calculateAsynchWithCancellation() {
        CompletableFuture<String> completableFuture = new CompletableFuture<String>();
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(() -> {
            Thread.sleep(500);
            completableFuture.cancel(false);
            return null;
        });
        service.shutdown();
        return completableFuture;
    }

}
