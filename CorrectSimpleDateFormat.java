package com.test.multithread;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CorrectSimpleDateFormat {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ThreadLocal<DateFormat> format = new ThreadLocal<DateFormat> () {
            protected DateFormat initialValue() {
                return new SimpleDateFormat("yyyyMMdd");

            }
        };
        Callable<Date> task = new Callable<Date>() {

            @Override
            public Date call() throws Exception {
                return format.get().parse("20101022");
            }
            
        };
        
        ExecutorService exec = Executors.newFixedThreadPool(5);
        List<Future<Date>> result = new ArrayList<Future<Date>>();

        for(int i = 0; i < 10; i++) {
            result.add(exec.submit(task));
        }
        exec.shutdown();
        
        for(Future<Date> r : result) {
            System.out.println(r.get());
        }
    }

}
