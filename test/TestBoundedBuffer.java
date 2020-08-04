package com.test.multithread.test;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TestBoundedBuffer {

    @Test
    public void testTakeBlocksWhenEmpty() {
        final BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        Thread taker = new Thread() {
            public void run() {
                try {
                    int unused = bb.take();
                    fail();
                } catch(InterruptedException success) {
                    System.out.println("Success");
                }
            }

            private void fail() {
            }
        };
        
        try {
            taker.start();
            Thread.sleep(1000);
            taker.interrupt();
            taker.join(1000);
            assertFalse(taker.isAlive());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    

}
