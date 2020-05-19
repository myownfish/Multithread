package com.test.multithread.timeout;

import java.util.Date;

public class StopThreadUsingVolatile {

    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub

        MyTask task = new MyTask();
        Thread t1 = new Thread(task);
        t1.start();
        Thread.currentThread().sleep(500);
        //ask task to stop using volatile
        task.keepRunning = false;
    }

    static class MyTask implements Runnable{

        public volatile boolean keepRunning = true;
        @Override
        public void run() {
            System.out.println("Start at " + new Date());
            while(keepRunning) {
                
            }
            System.out.println("End at " + new Date());
        }
        
    }
}


