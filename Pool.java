package com.test.multithread.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/*
 * Before obtaining an item each thread must acquire a permit from
 * the semaphore, guaranteeing that an item is available for use. When
 * the thread has finished with the item it is returned back to the
 * pool and a permit is returned to the semaphore, allowing another
 * thread to acquire that item.  Note that no synchronization lock is
 * held when {@link #acquire} is called as that would prevent an item
 * from being returned to the pool.  The semaphore encapsulates the
 * synchronization needed to restrict access to the pool, separately
 * from any synchronization needed to maintain the consistency of the
 * pool itself
 */
class Pool {
    private static final int MAX_AVAILABLE = 100;
    private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

    public static void main(String args[]) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(5);
        Pool pool = new Pool();

        for(int i = 0; i < 5; i++) {
            service.submit(() -> {
                int get = 0;
                try {
                    get = pool.getItem();
                    pool.putItem(get);
                    pool.putItem(2);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            });
        }
        
    }
    
    public int getItem() throws InterruptedException {
        available.acquire();
        return getNextAvailableItem();
    }

    public void putItem(int x) {
        if (markAsUnused(x))
            available.release();
    }

    // Not a particularly efficient data structure; just for demo

    protected int[] items = new int[MAX_AVAILABLE];
    {
        for (int i = 0; i < MAX_AVAILABLE; i ++) {
            items[i] = i + 1;
        }
    }
    protected boolean[] used = new boolean[MAX_AVAILABLE];

    protected synchronized int getNextAvailableItem() {
        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if (!used[i]) {
                used[i] = true;
                return items[i];
            }
        }
        return 0; // not reached
    }

    protected synchronized boolean markAsUnused(int item) {
        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if (item == items[i]) {
                if (used[i]) {
                    used[i] = false;
                    return true;
                } else
                    return false;
            }
        }
        return false;
    }
}
