package com.test.multithread.stampedlock;

import java.util.concurrent.locks.StampedLock;

public class TestStampedLock {

    private int x, y;
    final StampedLock s1 = new StampedLock();

    public void setX(int x) {
        long stamp = s1.tryWriteLock();
        this.x = x;
        s1.unlockWrite(stamp);
    }

    public void setY(int y) {
        long stamp = s1.tryWriteLock();
        this.y = y;
        s1.unlockWrite(stamp);
    }

    double distanceFromOrigin() {
        long stamp = s1.tryOptimisticRead();
        int curX = x, curY = y;
        if (!s1.validate(stamp)) {
            stamp = s1.readLock();
            try {
                curX = x;
                curY = y;
                return Math.sqrt(curX * curX + curY * curY);
            } finally {
                s1.unlockRead(stamp);
            }
        }
        return Math.sqrt(curX * curX + curY * curY);
    }

    public static void main(String[] args) {
        TestStampedLock lock = new TestStampedLock();
        lock.setX(10);
        lock.setY(10);
        Thread t1 = new Thread(() -> {
            lock.setX(10);
            lock.setY(10);
            System.out.println(lock.distanceFromOrigin());
        });
        Thread t2 = new Thread(() -> {
            lock.setX(20);
            lock.setY(20);
            System.out.println(lock.distanceFromOrigin());
        });
        t1.start();
        t2.start();
    }

}
