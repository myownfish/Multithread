package com.test.multithread.singletons;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeDemo {

    public static void main(String[] args) {
        Singleton1 singleton = Singleton1.INSTANCE;
        singleton.setValue(1);

        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream("out.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(singleton);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        singleton.setValue(2);

        Singleton1 singleton2 = null;
        try {
            FileInputStream fileIn = new FileInputStream("out.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            singleton2 = (Singleton1) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if(singleton == singleton2) {
            System.out.println("Two objects are the same");
        } else {
            System.out.println("Two objects are not same");
        }
        
        System.out.println(singleton.getValue());
        System.out.println(singleton2.getValue());
    }

}
