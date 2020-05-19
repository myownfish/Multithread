package com.test.multithread.timeout;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class StopThreadUsingCallable {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        Future<Integer> future = threadPool.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {

            }
            System.out.println("Stop at " + new Date());
            return 1;
        });
        // wait for 10 minutes to get response
        try {
            future.get(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
         // Calls thread.interrupt for thread running the task.
            future.cancel(true);
        }
        

    }

}
