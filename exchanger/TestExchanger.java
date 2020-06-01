package com.test.multithread.exchanger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Exchanger;

public class TestExchanger {

    public static void main(String[] args) {

        Exchanger<String> exchanger = new Exchanger<>();

        Runnable taskA = () -> {
            String message;
            try {
                message = exchanger.exchange("from A");
                System.out.println("Message get on taskA is " + message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        };
        
        Runnable taskB = () -> {
            String message;
            try {
                message = exchanger.exchange("from B");
                System.out.println("Message get on taskB is " + message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        };
        
        CompletableFuture.allOf(
                CompletableFuture.runAsync(taskA), 
                CompletableFuture.runAsync(taskB)).join();
    }

}
