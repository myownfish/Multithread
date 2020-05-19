package com.test.multithread.timeout;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class StopThreadUsingAutomicBoolean {

    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub

        MyTask task = new MyTask();
        Thread t1 = new Thread(task);
        t1.start();
        //schedule stop after 10 minutes
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() ->{
            //ask task to stop using volatile
            task.stop();
        }, 10, TimeUnit.MINUTES);
       
    }
    
    static class MyTask implements Runnable{

        private AtomicBoolean keepRunning = new AtomicBoolean(true);
        @Override
        public void run() {
            System.out.println("Start at " + new Date());
            while(keepRunning.get()) {
                
            }
            System.out.println("End at " + new Date());
        }
        
        public void stop() {
            keepRunning.set(false);
        }
        
    }

}


