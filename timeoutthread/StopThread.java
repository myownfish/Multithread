package com.test.multithread.timeout;

import java.util.Date;

public class StopThread {

    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub
        Thread t1 = new Thread(() -> {
            System.out.println("Start at " + new Date());
            //Must check whether current thread is interrupted.
            //if not check, interrupt method will not have any effect
            while(!Thread.currentThread().isInterrupted()) {
                
            }
            System.out.println("Stop at " + new Date());

        });
        
        t1.start();
        //Timeout for 1 minutes
        Thread.sleep(60 * 1000);
        //Try to stop it
        t1.interrupt();

    }

}
