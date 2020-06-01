package com.test.multithread.exchanger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Exchanger;

public class ExchangeBetweenMainAndThread {

    public static void main(String[] args) throws InterruptedException {

        Exchanger<String> exchanger = new Exchanger<>();

        Runnable runner = () -> {
            try {
                String message = exchanger.exchange("from runner");
                System.out.println("Message get on runner is " + message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        };
        
        CompletableFuture<Void> result = CompletableFuture.runAsync(runner);
        String msg = exchanger.exchange("to runner");
        System.out.println("Message get on main is " + msg);
        result.join();
    }

}
