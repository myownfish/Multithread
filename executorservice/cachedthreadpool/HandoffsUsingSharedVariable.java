package com.test.multithread.executorservice.cachedthreadpool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HandoffsUsingSharedVariable {
    static Integer sharedState = null;
    public static void main(String[] args) throws InterruptedException {
        /*
         * we have two threads – a producer and a consumer – and when the producer is
         * setting a value of a shared variable, we want to signal that fact to the
         * consumer thread. Next, the consumer thread will fetch a value from a shared
         * variable. We will use the CountDownLatch to coordinate those two threads, to
         * prevent a situation when the consumer accesses a value of a shared variable
         * that was not set yet. We will define a sharedState variable and a
         * CountDownLatch that will be used for coordinating processing:
         */
        ExecutorService executor = Executors.newFixedThreadPool(2);
//        AtomicInteger sharedState = new AtomicInteger();
        
        CountDownLatch countDownLatch = new CountDownLatch(1);
        
        /*
         * The producer will save a random integer to the sharedState variable, and
         * execute the countDown() method on the countDownLatch, signaling to the
         * consumer that it can fetch a value from the sharedState:
         */
        Runnable producer = () -> {
            Integer producedElement = ThreadLocalRandom.current().nextInt();
            System.out.println("Produced element is " + producedElement);
//            sharedState.set(producedElement);
            sharedState = producedElement;
            countDownLatch.countDown();
        };
        
        /*
         * The consumer will wait on the countDownLatch using the await() method. When
         * the producer signals that the variable was set, the consumer will fetch it
         * from the sharedState:
         */
        Runnable consumer = () -> {
            try {
                countDownLatch.await();
//                Integer consumedElement = sharedState.get();
                Integer consumedElement = sharedState;
                System.out.println("Consumed element is " + consumedElement);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        };

        executor.execute(producer);
        executor.execute(consumer);

        executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        executor.shutdown();
        System.out.println(countDownLatch.getCount());
    }

}
