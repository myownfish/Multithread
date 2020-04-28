package com.test.multithread.executorservice.cachedthreadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class HandoffsUsingSynchronousQueue {

    public static void main(String[] args) throws InterruptedException {
        /*
         * Let's now implement the same functionality as in HandoffsUsingSharedVariable,
         * but with a SynchronousQueue. It has a double effect because we can use it for
         * exchanging state between threads and for coordinating that action so that we
         * don't need to use anything besides SynchronousQueue.
         * 
         * Firstly, we will define a queue:
         */
        ExecutorService executor = Executors.newFixedThreadPool(2);
        SynchronousQueue<Integer> queue = new SynchronousQueue<>();

        /*
         * The producer will call a put() method that will block until some other thread
         * takes an element from the queue:
         */
        Runnable producer = () -> {
            Integer producedElement = ThreadLocalRandom.current().nextInt();
            try {
                queue.put(producedElement);
                System.out.println("Produced Element is " + producedElement + " and queue size is " + queue.size());
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        };

        /*
         * The consumer will simply retrieve that element using the take() method:
         */
        Runnable consumer = () -> {
            try {
                Integer consumedElement = queue.take();
                System.out.println("Consumed Element is " + consumedElement + " and queue size is " + queue.size());
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        };

        executor.execute(producer);
        executor.execute(consumer);

        executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        executor.shutdown();
        System.out.println(queue.size());
        /*
         * We can see that a SynchronousQueue is used as an exchange point between the
         * threads, which is a lot better and more understandable than the previous
         * example which used the shared state together with a CountDownLatch.
         */
    }

}
