package com.test.multithread.executorservice;

public class TestWaitNotify {

    Object key = new Object();

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        TestWaitNotify test = new TestWaitNotify();
        Thread t1 = new Thread(() -> test.execute1());
        Thread t2 = new Thread(() -> test.execute2());
        t1.start();
        t2.start();
    }

    public synchronized void execute1() {
        try {
            key.wait(); 
            /*
             * This will throw exception: java.lang.IllegalMonitorStateException
             * The reason is, this method hold the key of test object.
             * But its wait for another object key.
             * It must be the test object: test.wait()/this.wait()
             */
            System.out.println("haha");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public synchronized void execute2() {
        key.notify();
        /*
         * This will throw exception: java.lang.IllegalMonitorStateException
         * The reason is, this method hold the key of test object.
         * But its wait for another object key.
         * It must be the test object: test.notify()/this.wait()
         */
    }
}
