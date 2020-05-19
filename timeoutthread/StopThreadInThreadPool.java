package com.test.multithread.timeout;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StopThreadInThreadPool {

    public static void main(String[] args) {

        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        threadPool.submit(() -> {
            System.out.println("Start at " + new Date());
            // Must check whether current thread is interrupted.
            // if not check, interrupt method will not have any effect
            while (!Thread.currentThread().isInterrupted()) {

            }
            System.out.println("Stop at " + new Date());

        });

        /*
         * this shutdown will not able to stop the thread in threadpool. Because
         * shutdown method will only try to interrupt idleworker.
         * if (!t.isInterrupted() && w.tryLock()) {
         *          try {
         *              t.interrupt();
         *          } catch (SecurityException ignore) {
         *          } finally {
         *              w.unlock();
         *          }
         *      }
         *  
         *  This tryLock method will return false, as current state of worker is 1.
         *  So it can't lock it. So shutdown is not able to call interrupt method to stop the thread.
         *  
         *  public boolean tryLock()  { return tryAcquire(1); }
         *  protected boolean tryAcquire(int unused) {
         *     if (compareAndSetState(0, 1)) {
         *         setExclusiveOwnerThread(Thread.currentThread());
         *         return true;
         *      }
         *      return false;
         *  }
         */
        threadPool.shutdown();
        
        /*
         * shutdown now method can stop the thread in threadpool.
         * Because shutdownNow method will try to interrupt  all threads, even if active.
         * for (Worker w : workers)
         *      w.interruptIfStarted();
         * 
         * In interruptIfStarted method, as long as state >=0, and thread is not interrupted, 
         * we will call interrupt method.
         * 
         * void interruptIfStarted() {
         *  Thread t;
         *  if (getState() >= 0 && (t = thread) != null && !t.isInterrupted()) {
         *      try {
         *          t.interrupt();
         *      } catch (SecurityException ignore) {
         *      }
         *  }
         * }
         *      
         */
        threadPool.shutdownNow();

    }

}
