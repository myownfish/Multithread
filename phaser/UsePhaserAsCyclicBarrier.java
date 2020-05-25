package com.test.multithread.phaser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class UsePhaserAsCyclicBarrier {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        Phaser phaser = new Phaser(3);
        System.out.println(phaser.getPhase());
        executor.submit(new DependentService(phaser));
        executor.submit(new DependentService(phaser));
        executor.submit(new DependentService(phaser));
        
//        phaser.awaitAdvance(0);
//        System.out.println("All dependant services initialized");
        executor.shutdown();
    }
    
    public static class DependentService implements Runnable {

        private Phaser phaser;
        public DependentService(Phaser phaser) {
            this.phaser = phaser;
        }
        @Override
        public void run() {
            while(true) {
            System.out.println(phaser.arriveAndAwaitAdvance());
            System.out.println(Thread.currentThread().getName() + " is arrived.");
            }
        }
        
    }

}
