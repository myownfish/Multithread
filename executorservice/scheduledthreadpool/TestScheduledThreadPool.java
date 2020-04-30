package com.test.multithread.executorservice.scheduledthreadpool;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestScheduledThreadPool {

    public static void main(String[] args) {
        //for scheduling of tasks
        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        
        // task to run after 10 seconds delay, this task will only run once
        service.schedule(new Task("Task1"), 5, TimeUnit.SECONDS);
        
        //task to run repeatedly every 10 seconds
        service.scheduleAtFixedRate(new Task("Task2"), 15, 10, TimeUnit.SECONDS);
        
        //task to run repeatedly 10 seconds after previous task completes
        service.scheduleWithFixedDelay(new Task("Task3"), 15, 10, TimeUnit.SECONDS);
    }

    static class Task implements Runnable{
        private String taskName;
        
        Task(String name) {
            this.taskName = name;
        }
        
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " runs " + this.taskName + " at " + LocalDateTime.now());
        }
        
    }
    
}
