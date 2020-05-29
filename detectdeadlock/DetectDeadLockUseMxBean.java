package com.test.multithread.detectdeadlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DetectDeadLockUseMxBean {
    Lock lockA = new ReentrantLock();
    Lock lockB = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        
        DetectDeadLockUseMxBean test = new DetectDeadLockUseMxBean();
        Thread threadA = new Thread(test::processThis); 
        threadA.start();
        Thread threadB = new Thread(test::processThat);
        threadB.start();
        
        Thread.sleep(1000);
        threadB.interrupt();
        //make threadB release lockB. Then threadA can acquire the lock and resume.
        
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        long[] threadIds = threadBean.findDeadlockedThreads();
        System.out.println(threadIds);
    }
    
    public void processThis()  {
        try {
            lockA.lock();
            Thread.sleep(100);
            lockB.lock();
            System.out.println("processThis");
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            lockA.unlock();
            lockB.unlock();
        }
    }

    public void processThat()  {
        try {
            lockB.lock();
            Thread.sleep(100000000);
            lockA.lock();
            System.out.println("processThat");
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
//            lockA.unlock();
            lockB.unlock();
        }
    }
}
