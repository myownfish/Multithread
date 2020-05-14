package com.test.multithread.asynchronous;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TestJavaNio {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        System.out.println(System.getProperty("user.dir"));
        Path path = Paths.get("./src/com/test/multithread/asynchronous/test.txt");
        try {
            AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
            fileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {

                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    // TODO Auto-generated method stub
                    System.out.println(result);
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    // TODO Auto-generated method stub
                    
                }
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
