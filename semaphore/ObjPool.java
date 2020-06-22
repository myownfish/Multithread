package com.test.multithread.semaphore;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

public class ObjPool<T, R> {
    final List<T> pool;
    final Semaphore sem;

    ObjPool(int size, T t) {
        pool = new Vector<T>();
        for (int i = 0; i < size; i++) {
            pool.add(t);
        }
        sem = new Semaphore(size);
    }

    R exec(Function<T, R> func) throws InterruptedException {
        T t = null;
        sem.acquire();
        try {
            t = pool.remove(0);
            return func.apply(t);
        } finally {
            pool.add(t);
            sem.release();
        }
    }

    public static void main(String[] args) {

        ObjPool<Long, String> pool = new ObjPool<Long, String>(2, Long.valueOf(2));
        ExecutorService service = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 5; i++) {
            service.submit(() -> {
                try {
                    pool.exec(t -> {
                        System.out.println(t);
                        return t.toString();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        service.shutdown();
    }

}
