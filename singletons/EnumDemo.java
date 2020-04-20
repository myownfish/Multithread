package com.test.multithread.singletons;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EnumDemo {

    public static void main(String[] args) {
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
            // TODO Auto-generated catch block
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if(singletonSerizlize == singleton2) {
            System.out.println("Two objects are the same");
        } else {
            System.out.println("Two objects are not same");
        }
        
        System.out.println(singletonSerizlize.getValue());
        System.out.println(singleton2.getValue());
    
    }

}
