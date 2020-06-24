package com.test.multithread.concurrentcontainer.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TestMapPerformance {

    public static void main(String[] args) throws InterruptedException {
        TestMapPerformance test = new TestMapPerformance();
        test.givenMaps_whenGetPut500KTimes_thenConcurrentMapFaster();
    }

    public void givenMaps_whenGetPut500KTimes_thenConcurrentMapFaster() throws InterruptedException {
        Map<String, Object> hashtable = new Hashtable<>();
        Map<String, Object> synchronizedHashMap = Collections.synchronizedMap(new HashMap<>());
        Map<String, Object> concurrentHashMap = new ConcurrentHashMap<String, Object>();

        System.out.println("Time for Hashtable:" + timeElapseForGetPut(hashtable));
        System.out.println("Time for SynchronizedHashMap:" + timeElapseForGetPut(synchronizedHashMap));
        System.out.println("Time for ConcurrentHashMap:" + timeElapseForGetPut(concurrentHashMap));
    }

    private long timeElapseForGetPut(Map<String, Object> map) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        long startTime = System.nanoTime();
        for (int i = 0; i < 4; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 500000; j++) {
                    int value = ThreadLocalRandom.current().nextInt(10000);
                    String key = String.valueOf(value);
                    map.put(key, value);
                    map.get(key);
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        return (System.nanoTime() - startTime) / 500000;
    }
}
