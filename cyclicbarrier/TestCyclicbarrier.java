package com.test.multithread.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestCyclicbarrier {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        CyclicBarrier barrier = new CyclicBarrier(3);
        executor.submit(new Task(barrier, "task1"));
        executor.submit(new Task(barrier, "task2"));
        executor.submit(new Task(barrier, "task3"));

        Thread.sleep(2000);
        System.out.println("Main thread is done");
    }

    public static class Task implements Runnable {
        private String name;
        private CyclicBarrier barrier;

        public Task(CyclicBarrier barrier, String name) {
            super();
            this.name = name;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("Task " + this.name + " is running");
            }
        }

    }

}
