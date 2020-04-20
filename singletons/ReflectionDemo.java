package com.test.multithread.singletons;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionDemo {

    public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Singleton1 singleton = Singleton1.INSTANCE;
        Constructor<? extends Singleton1> constructor = singleton.getClass().getDeclaredConstructor();
        constructor.setAccessible(true);
        
        Singleton1 singleton1 = (Singleton1) constructor.newInstance();
        if(singleton == singleton1) {
            System.out.println("Two objects are the same");
        } else {
            System.out.println("Two objects are not same");
        }
        
        singleton.setValue(1);
        singleton1.setValue(2);
        
        System.out.println(singleton.getValue());
        System.out.println(singleton1.getValue());
    }

}
