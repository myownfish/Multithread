package com.test.multithread;

public class LongWrapper {
    private long l;

    public synchronized long getL() {
        return l;
    }

//    public void setL(long l) {
//        this.l = l;
//    }

    public LongWrapper(long l) {
        super();
        this.l = l;
    }
    
    public  synchronized void incrementValue() {
        l = l + 1;
    }

}
