package com.test.multithread.executorservice.lifecycle;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestThreadPoolLifeCycle {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ExecutorService service = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 10; i++) {
            service.execute(new Task("Task" + i));
        }
        // initiate shutdown
        /*
         * Initiates an orderly shutdown in which previously submitted tasks are
         * executed, but no new tasks will be accepted. Invocation has no additional
         * effect if already shut down.
         */
        service.shutdown(); //what will this method do?

        // will throw RejectedExecutionException
        //service.execute(new Task("LastTask"));
        
        // will return true, since shutdown has begun
        service.isShutdown();
        
        // will return true if all tasks are completed
        // including queued ones
        service.isTerminated();
        
        // block until all tasks are completed or if timeout occurs
        try {
            service.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // will initiate shutdown and return all quequed tasks
        List<Runnable> runnables = service.shutdownNow();
    }

    static class Task implements Runnable {
        private String name;

        public Task(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " is running " + this.name);
        }

    }

}
