package com.test.multithread.phaser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class UsePhaserAsCountdownLatch {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        Phaser phaser = new Phaser(3);//The phase after the initialization is equal to zero.
        executor.submit(new DependentService(phaser));
        executor.submit(new DependentService(phaser));
        executor.submit(new DependentService(phaser));
        System.out.println("Value is " + phaser.awaitAdvance(0));// return immediately when phaser is not 0 anymore.
        System.out.println("All dependant services initialized");
        executor.shutdown();
    }
    
    public static class DependentService implements Runnable {

        private Phaser phaser;
        public DependentService(Phaser phaser) {
            this.phaser = phaser;
        }
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is arrived.");
            System.out.println(phaser.arrive());
        }
        
    }

}
