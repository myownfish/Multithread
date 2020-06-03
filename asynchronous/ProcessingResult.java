package com.test.multithread.asynchronous;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ProcessingResult {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> future = completableFuture.thenApply(s -> s + " World");
        /*
         * If you don't need to return a value down the Future chain, you can use an
         * instance of the Consumer functional interface. Its single method takes a
         * parameter and returns void.
         */
        CompletableFuture<Void> voidFuture = future.thenAccept(s -> System.out.println(s));
        System.out.println(voidFuture.get());
    }

}
