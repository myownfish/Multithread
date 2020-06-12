package com.test.multithread.deadlockandlivelock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Both threads need two locks to complete their work. 
 * Each thread acquires its first lock but finds that the second lock is not available. 
 * So, in order to let the other thread complete first, 
 * each thread releases its first lock and tries to acquire both the locks again.
 */
public class LiveLock {

    private Lock lock1 = new ReentrantLock(true);
    private Lock lock2 = new ReentrantLock(true);

    public static void main(String[] args) {
        LiveLock livelock = new LiveLock();
        new Thread(livelock::operation1, "T1").start();
        new Thread(livelock::operation2, "T2").start();

    }

    public void operation1() {
        while (true) {
            tryLock(lock1, 50);
            print("lock1 acquired, trying to acquire lock2.");
            sleep(50);

            if (tryLock(lock2)) {
                print("lock2 acquired.");
            } else {
                print("cannot acquire lock2, releasing lock1.");
                lock1.unlock();
                continue;
            }

            print("executing first operation.");
            break;
        }
        lock2.unlock();
        lock1.unlock();
    }

    public void operation2() {
        while (true) {
            tryLock(lock2, 50);
            print("lock2 acquired, trying to acquire lock1.");
            sleep(50);

            if (tryLock(lock1)) {
                print("lock1 acquired.");
            } else {
                print("cannot acquire lock1, releasing lock2.");
                lock2.unlock();
                continue;
            }

            print("executing second operation.");
            break;
        }
        lock1.unlock();
        lock2.unlock();
    }

    public void print(String message) {
        System.out.println("Thread " + Thread.currentThread()
            .getName() + ": " + message);
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void tryLock(Lock lock, long millis) {
        try {
            lock.tryLock(10, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean tryLock(Lock lock) {
        return lock.tryLock();
    }

}
