package com.test.multithread.interrupt;

public class TestInterruptedException {
    static Object key = new Object();

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Thread taskThread = new Thread(new Task());
        taskThread.start();
        
        /*
         * this interrupt will make taskThread stop from waitting.
         * Then it will go the line 31 to handle the exception.
         * Then continue on the next loop, then wait again.
         * taskThread will not stop as we didn't make is stop when InterruptedException happens.
         */
        taskThread.interrupt();

    }

    static class Task implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                System.out.println("I am running, i = " + i);
                synchronized (key) {
                    try {
                        key.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //return;// adding this return here will make taskThread to stop.
                    }
                }
            }
        }

    }

}
