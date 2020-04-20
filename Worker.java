package com.test.multithread;

import java.util.Timer;
import java.util.TimerTask;

public class Worker extends Thread{
    
    private volatile boolean quittingTime = false;
    
    public void run() {
        while(!quittingTime) {
            working();
            System.out.println(Thread.currentThread().getName() + " Still working...");
        }
        System.out.println(Thread.currentThread().getName() + "Coffe is good !");
    }

    private void working() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    synchronized void quit() throws InterruptedException {
        quittingTime = true;
        System.out.println(Thread.currentThread().getName() + " Calling join");
        join();
        System.out.println(Thread.currentThread().getName() + " Back from join");

    }

    synchronized void keepWorking() {
        quittingTime = false;
        System.out.println(Thread.currentThread().getName() + " Keep working");
    }
    
    public static void main(String[] args) throws InterruptedException {
        final Worker worker = new Worker();
        worker.start();
        
        Timer t = new Timer(true);
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                worker.keepWorking();
            }
        }, 500);
        
        Thread.sleep(400);
        worker.quit();
    }

}
