package com.test.multithread.interrupt;

public class TestInterrupt {

    public static void main(String[] args) {
        Thread taskThread = new Thread(new Task());
        taskThread.start();
        System.out.println("Long running task started");
        taskThread.interrupt(); //set the interrupt status to true for taskThread
    }

    static class Task implements Runnable {

        @Override
        public void run() {
            // some long running operation
            // or some blocking operation
            while(true) {
                System.out.println("I am still running!");
                /**
                 * if I didn't add this isInterrupted check here, 
                 * line9 taskThread.interrupt() will not have any effects on taskThread.
                 * taskThread will keep running.
                **/
                if(Thread.currentThread().isInterrupted()) { 
                    /*
                     * Thread.currentThread().isInterrupted() this just check the interrupt flag and will not change it.
                     * There is another method Thread.interrupted() check and return the value of interrupt flag and then reset its value to false
                     */
                    System.out.println(Thread.interrupted());
                    System.out.println("Stopping the task");
                    System.out.println(Thread.interrupted());
                    return ;
                }
            }

        }

    }
}
