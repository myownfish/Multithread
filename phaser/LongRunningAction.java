package com.test.multithread.phaser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class LongRunningAction implements Runnable {
    private String threadName;
    private Phaser ph;
    
    public LongRunningAction(String threadName, Phaser ph) {
        super();
        this.threadName = threadName;
        this.ph = ph;
        ph.register();// parties add 1
    }

    @Override
    public void run() {
        ph.arriveAndAwaitAdvance();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ph.arriveAndDeregister();// parties minus 1

    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ExecutorService executorService = Executors.newCachedThreadPool();
        Phaser ph = new Phaser(1);// this is equivalent to calling the register() method on this main thread.
        System.out.println(ph.getPhase());

        executorService.submit(new LongRunningAction("thread-1", ph));
        executorService.submit(new LongRunningAction("thread-2", ph));
        executorService.submit(new LongRunningAction("thread-3", ph));
        
        ph.arriveAndAwaitAdvance();
        System.out.println(ph.getPhase());
        
        //the next phase of processing
        executorService.submit(new LongRunningAction("thread-4", ph));
        executorService.submit(new LongRunningAction("thread-5", ph));
        ph.arriveAndAwaitAdvance();//arrived plus 1
        System.out.println(ph.getPhase());

        ph.arriveAndDeregister();
        executorService.shutdown();
    }

}
