package com.test.multithread.executorservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class TestFixedThreadPool {

    public static void main(String[] args) {

        int coreCount = Runtime.getRuntime().availableProcessors(); //return the count of cores your CPU has
        ExecutorService service = Executors.newFixedThreadPool(1);
//        IntStream.range(0, 10).forEach(i ->
//        service.execute(new CpuIntensiveTask())
//        );
        service.submit(new CpuIntensiveTask());

        service.submit(new CpuIntensiveTask());
        service.shutdown();
    }
    
    static class CpuIntensiveTask implements Runnable{

        @Override
        public void run() {
            try {
                Thread.currentThread().sleep(10000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " is running");
        }
        
    }

}
