package com.test.multithread.concurrentcontainer.map;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * results of aggregate status methods including size, isEmpty, 
 * and containsValue are typically useful only when a map is not 
 * undergoing concurrent updates in other threads.
 * Note that usage of size() of ConcurrentHashMap should be 
 * replaced by mappingCount(), for the latter method returns a long count,
 *  although deep down they are based on the same estimation.
 */
public class TestMapSize {

    public static void main(String[] args) throws InterruptedException {
        TestMapSize test = new TestMapSize();
        test.givenConcurrentMap_whenUpdatingAndGetSize_thenError();
    }

    public void givenConcurrentMap_whenUpdatingAndGetSize_thenError() throws InterruptedException {
        List<Integer> mapSized = new ArrayList<>();
        ConcurrentMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        Runnable collectMapSizes = () -> {
            for (int i = 0; i < 10000; i++) {
                mapSized.add(concurrentMap.size());
            }
        };

        Runnable updateMapData = () -> {
            for (int i = 0; i < 10000; i++) {
                concurrentMap.put(String.valueOf(i), i);
            }
        };

        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(updateMapData);
        service.execute(collectMapSizes);
        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println(mapSized.get(9999));
        System.out.println(concurrentMap.size());
    }

}
