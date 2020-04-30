package com.test.multithread.executorservice.scheduledthreadpool;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

/*
 * To be able to test our DelayQueue we need to implement producer and consumer logic. 
 * The producer class takes the queue, the number of elements to produce, and the delay of 
 * each message in milliseconds as arguments.Then when the run() method is invoked, 
 * it puts elements into the queue, and sleeps for 500 milliseconds after each put.
 */

class DelayQueueProducer implements Runnable {
    
    private BlockingQueue<DelayObject> queue;
    private Integer numberOfElementsToProduce;
    private Integer delayOfEachProducedMessageMilliseconds;

    public DelayQueueProducer(BlockingQueue<DelayObject> queue, Integer numberOfElementsToProduce,
            Integer delayOfEachProducedMessageMilliseconds) {
        super();
        this.queue = queue;
        this.numberOfElementsToProduce = numberOfElementsToProduce;
        this.delayOfEachProducedMessageMilliseconds = delayOfEachProducedMessageMilliseconds;
    }

    @Override
    public void run() {

        for(int i = 0; i < numberOfElementsToProduce; i++) {
            DelayObject object = new DelayObject(UUID.randomUUID().toString(), delayOfEachProducedMessageMilliseconds);
            System.out.println("Put object" + object);
            try {
                queue.put(object);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
