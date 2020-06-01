package com.test.multithread.exchanger;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Exchanger;

/*
 * Here, we have three threads: reader, processor, and writer. Together, they work as a single pipeline exchanging data between them.
 * The readerExchanger is shared between the reader and the processor thread, 
 * while the writerExchanger is shared between the processor and the writer thread.
 * Note that the example here is only for demonstration. We must be careful while creating infinite loops with while(true). 
 * Also to keep the code readable, we've omitted some exceptions handling.
 * This pattern of exchanging data while reusing the buffer allows having less garbage collection. 
 * The exchange method returns the same queue instances and thus there would be no GC for these objects. 
 * Unlike any blocking queue, the exchanger does not create any nodes or objects to hold and share data
 */
public class PassThrough {

    public static void main(String[] args) {
        Exchanger<Queue<String>> readerExchanger = new Exchanger<>();
        Exchanger<Queue<String>> writerExchanger = new Exchanger<>();

        Runnable reader = () -> {
            Queue<String> readerBuffer = new ConcurrentLinkedQueue<>();
            while (true) {
                readerBuffer.add(UUID.randomUUID().toString());
                if (readerBuffer.size() >= 5) {
                    try {
                        readerBuffer = readerExchanger.exchange(readerBuffer);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };

        Runnable processor = () -> {
            Queue<String> processorBuffer = new ConcurrentLinkedQueue<>();
            Queue<String> outBuffer = new ConcurrentLinkedQueue<>();
            try {
                processorBuffer = readerExchanger.exchange(processorBuffer);
                while (true) {
                    outBuffer.add(processorBuffer.poll());
                    if (processorBuffer.isEmpty()) {
                        processorBuffer = readerExchanger.exchange(processorBuffer);
                        outBuffer = writerExchanger.exchange(outBuffer);
                    }
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        };

        Runnable writer = () -> {
            Queue<String> writerBuffer = new ConcurrentLinkedQueue<>();
            try {
                writerBuffer = writerExchanger.exchange(writerBuffer);
                while (true) {
                    System.out.println(writerBuffer.poll());
                    if (writerBuffer.isEmpty()) {
                        writerBuffer = writerExchanger.exchange(writerBuffer);
                    }
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        };

        CompletableFuture.allOf(CompletableFuture.runAsync(reader),
                CompletableFuture.runAsync(processor),
                CompletableFuture.runAsync(writer)).join();
    }

}
