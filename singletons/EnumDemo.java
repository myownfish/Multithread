package com.test.multithread.singletons;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class EnumDemo {

    public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // TODO Auto-generated method stub
        Singleton sin = Singleton.INSTANCE;
        System.out.println(sin.getValue());
        sin.setValue(2);
        System.out.println(sin.getValue());
        

        Singleton singletonSerizlize = Singleton.INSTANCE;
        singletonSerizlize.setValue(1);

        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream("out.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(singletonSerizlize);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        singletonSerizlize.setValue(2);

        Singleton singleton2 = null;
        try {
            FileInputStream fileIn = new FileInputStream("out.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            singleton2 = (Singleton) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        if (singletonSerizlize == singleton2) {
            System.out.println("Two objects are the same");
        } else {
            System.out.println("Two objects are not same");
        }
        
        System.out.println(singletonSerizlize.getValue());
        System.out.println(singleton2.getValue());
    
        //reflection

        Singleton singleton = Singleton.INSTANCE;
        Constructor<? extends Singleton>[] constructors = (Constructor<? extends Singleton>[]) singleton.getClass().getConstructors();
        Constructor<? extends Singleton> constructor = singleton.getClass().getDeclaredConstructor();
        constructor.setAccessible(true);
        
        Singleton singleton1 = (Singleton) constructor.newInstance();
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
