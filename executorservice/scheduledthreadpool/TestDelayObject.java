package com.test.multithread.executorservice.scheduledthreadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestDelayObject {

    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub
        ExecutorService executor = Executors.newFixedThreadPool(2);
        BlockingQueue<DelayObject> queue =  new DelayQueue<>();
        int numberOfElementsToProduce = 4;
        int delayOfEachProducedMessageMilliseconds = 500;
        DelayQueueConsumer consumer = new DelayQueueConsumer(queue, numberOfElementsToProduce);
        DelayQueueProducer producer = new DelayQueueProducer(queue, numberOfElementsToProduce, delayOfEachProducedMessageMilliseconds);

        executor.submit(producer);
        executor.submit(consumer);
        
        executor.awaitTermination(5, TimeUnit.SECONDS);
        executor.shutdown();
        
        System.out.println("Consumed: " + consumer.numberOfConsumedElements.get());
    }

}
