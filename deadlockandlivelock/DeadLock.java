package com.test.multithread.deadlockandlivelock;

public class DeadLock {
    
    public static void main(String[] args) throws InterruptedException {
        DeadLock a = new DeadLock();
        
        Runnable r1 = () -> a.a();
        Runnable r2 = () -> a.b();
        
        Thread t1 = new Thread(r1);
        t1.start();
        
        Thread t2 = new Thread(r2);
        t2.start();
        
        t1.join();
        t2.join();
    }
    
    
    private Object key1 = new Object();
    private Object key2 = new Object();
    
    public void a() {
        synchronized(key1) {
            System.out.println("[" + Thread.currentThread().getName() + "] I am in a()");
            b();
        }
    }

    private void b() {
        synchronized(key2) {
            System.out.println("[" + Thread.currentThread().getName() + "] I am in b()");
            c();
        }
    }

    private void c() {
        synchronized(key1) {
            System.out.println("[" + Thread.currentThread().getName() + "] I am in c()");
        }
    }

}
