package com.test.multithread.interrupt;

public class TestInterrupt2 {
/*
 * 下面代码的本意是当前线程被中断之后，退出while(true)，你觉得这段代码是否正确呢？
 */
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            Thread th = Thread.currentThread();
            while (true) {
                if (th.isInterrupted()) {
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // InterruptedException - if any thread has interrupted the current thread. The
                    // interrupted status of the current thread is cleared when this exception is
                    // thrown.
                    System.out.println(th.isInterrupted());
                    e.printStackTrace();
                    /*
                     * 这段代码有可能会无限循环，因为如果线程是在sleep期间被打断，会抛出InterruptedException异常，
                     * 抛出异常后，中断标识会自动清除掉，所以th.isInterrupted()会返回false。
                     * 可以在抛出异常后，重置一下中断标识 th.interrupt()
                     */
                }
            }
        });
        t1.start();
        t1.interrupt();

    }
}
