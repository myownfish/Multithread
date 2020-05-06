package com.test.multithread.executorservice.customthreadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestCustomeThreadPool {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
//        ExecutorService service = new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1),
//                new ThreadPoolExecutor.CallerRunsPolicy());

//        ExecutorService service = new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1),
//                new ThreadPoolExecutor.AbortPolicy());
        
//        ExecutorService service = new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1),
//                new ThreadPoolExecutor.DiscardOldestPolicy());
        
        ExecutorService service = new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1),
                new ThreadPoolExecutor.DiscardPolicy());
        service.execute(new Task("Task 1"));
        service.execute(new Task("Task 2"));
        service.execute(new Task("Task 3"));
        
        
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
