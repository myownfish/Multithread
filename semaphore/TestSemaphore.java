package com.test.multithread.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class TestSemaphore {

    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub

        Semaphore semaphore = new Semaphore(1);
        
        ExecutorService service = Executors.newFixedThreadPool(5);
        //IntStream.of(10000).forEach(i -> service.execute(new Task(semaphore)));
        for(int i = 0; i < 10; i++) {
            service.execute(new Task(semaphore));
        }
        
        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);
    }
    
    static class Task implements Runnable{
        
        Semaphore semaphore;
        public Task(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            semaphore.tryAcquire();
            
            System.out.println(Thread.currentThread().getName() + " is running");
            
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + " Hello");
        }
        
    }

}
