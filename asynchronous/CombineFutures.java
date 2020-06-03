package com.test.multithread.asynchronous;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CombineFutures {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello")
                .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));

        System.out.println(completableFuture.get());

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello")
                .thenCombine(CompletableFuture.supplyAsync(() -> " Wolrd2"), (s1, s2) -> s1 + s2);

        System.out.println(future.get());

        CompletableFuture<Void> future1 = CompletableFuture.supplyAsync(() -> "Hello")
                .thenAcceptBoth(CompletableFuture.supplyAsync(() -> " World3"), (s1, s2) -> System.out.println(s1 + s2));

        System.out.println(future1.get());

    }

}
