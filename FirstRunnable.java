package com.test.multithread;

public class FirstRunnable {

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        Runnable runnable = () -> {
            synchronized (lock) {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println("I am running in " + Thread.currentThread().getName());
                }
            }
        };

        Thread t = new Thread(runnable);
        t.setName("My thread");
        t.start();

//        t.run();//I am running in main

        t.interrupt();
    }

}
