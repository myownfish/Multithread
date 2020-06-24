package com.test.multithread.concurrentcontainer.map;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CompareTreeMapWithSkipListMap {

    public static void main(String[] args) {
        CompareTreeMapWithSkipListMap test = new CompareTreeMapWithSkipListMap();
        test.givenSkipListMap_whenNavConcurrently_thenCountCorrect();
        test.givenTreeMap_whenNavConcurrently_thenCountError();
    }

    public void givenSkipListMap_whenNavConcurrently_thenCountCorrect() {
        NavigableMap<Integer, Integer> skipListMap = new ConcurrentSkipListMap<>();
        int count = countMapElementByPollingFirstEntry(skipListMap, 10000, 4);
        System.out.println("Count for ConcurrentSkipListMap is: " + count);
    }

    public void givenTreeMap_whenNavConcurrently_thenCountError() {
        NavigableMap<Integer, Integer> treeMap = new TreeMap<>();
        int count = countMapElementByPollingFirstEntry(treeMap, 10000, 4);
        System.out.println("Count for TreeMap is: " + count);
    }

    private int countMapElementByPollingFirstEntry(NavigableMap<Integer, Integer> navigableMap,
            int elementCount, int concurrencyLevel) {

        for (int i = 0; i < elementCount * concurrencyLevel; i++) {
            navigableMap.put(i, i);
        }

        AtomicInteger counter = new AtomicInteger(0);
        ExecutorService service = Executors.newFixedThreadPool(concurrencyLevel);
        for (int j = 0; j < concurrencyLevel; j++) {
            service.execute(() -> {
                for (int i = 0; i < elementCount; i++) {
                    if (navigableMap.pollFirstEntry() != null) {
                        counter.incrementAndGet();
                    }
                }
            });
        }
        service.shutdown();
        try {
            service.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return counter.get();
    }

}
