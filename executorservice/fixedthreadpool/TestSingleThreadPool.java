package com.test.multithread.executorservice.fixedthreadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestSingleThreadPool {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Task("Task1"));
        service.submit(new Task("Task2"));
        service.submit(new Task("Task3"));
        service.submit(new Task("Task4"));

    }
    
    static class Task implements Runnable{
        private String name;
        
        public Task(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            System.out.println("Thread " + Thread.currentThread().getName() + " is running taks " + this.name);
        }
        
    }

}
