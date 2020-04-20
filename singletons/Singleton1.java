package com.test.multithread.singletons;

import java.io.Serializable;

public class Singleton1 implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 6021137844590647468L;
    public static final Singleton1 INSTANCE = new Singleton1();
    private Singleton1( ) {}
    
    private int value;
    public void setValue(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

//    protected Object readResolve() {
//        return INSTANCE;
//    }
}
