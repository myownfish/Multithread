package com.test.multithread.asynchronous;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class TestCompletableFuture {

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < 10; i ++) {
            final int id = i;
            CompletableFuture.supplyAsync(() -> fetchEmployee(id)) //This will starts a DaemonThread (ForkJoinPool)
            .thenApplyAsync(employee -> fetchTaxRate(employee)) // This will starts another DaemonThread (ForkJoinPool)
            .thenAcceptAsync(value -> sendEmail(value)); // This will starts another DaemonThread (ForkJoinPool)
        }
        Thread.sleep(200);
        System.out.println("Last Line of Main");
    }

    private static void sendEmail(Integer value) {
        System.out.println(value);
    }

    private static Integer fetchTaxRate(Integer employee) {
       
        return employee.intValue() + 1;
    }

    private static Integer fetchEmployee(int i) {
       try {
        Thread.sleep(10);
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
       return new Random().nextInt();
    }

}
