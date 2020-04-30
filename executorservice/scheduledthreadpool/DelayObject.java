package com.test.multithread.executorservice.scheduledthreadpool;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/*
 * Each element we want to put into the DelayQueue needs to implement the Delayed interface.
 */
public class DelayObject implements Delayed {
    private String data;
    private long startTime;

    public DelayObject(String data, long delayInMilliseconds) {
        super();
        this.data = data;
        this.startTime = System.currentTimeMillis() + delayInMilliseconds;
    }

    /*
     * We also need to implement the compareTo() method, because the elements in the
     * DelayQueue will be sorted according to the expiration time. The item that
     * will expire first is kept at the head of the queue and the element with the
     * highest expiration time is kept at the tail of the queue:
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Delayed o) {
        if(this.startTime - ((DelayObject) o).startTime > 0) {
            return 1;
        } else if(this.startTime - ((DelayObject) o).startTime < 0) {
            return -1;
        }
        return 0;
    }

    /*
     * When the consumer tries to take an element from the queue, the DelayQueue
     * will execute getDelay() to find out if that element is allowed to be returned
     * from the queue. If the getDelay() method will return zero or a negative
     * number, it means that it could be retrieved from the queue.(non-Javadoc)
     * 
     * @see java.util.concurrent.Delayed#getDelay(java.util.concurrent.TimeUnit)
     */
    @Override
    public long getDelay(TimeUnit unit) {
        long diff = startTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

}
