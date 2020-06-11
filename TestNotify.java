package com.test.multithread.waitnotify;

/*
 * 假设我们有资源 A、B、C、D，线程 1 申请到了 AB，线程 2 申请到了 CD，此时线程 3 申请 AB，
 * 会进入等待队列（AB 分配给线程 1，线程 3 要求的条件不满足），线程 4 申请 CD 也会进入等待队列。
 * 我们再假设之后线程 1 归还了资源 AB，如果使用 notify() 来通知等待队列中的线程，有可能被通知的是线程 4，
 * 但线程 4 申请的是 CD，所以此时线程 4 还是会继续等待，而真正该唤醒的线程 3 就再也没有机会被唤醒了。
 */
public class TestNotify {
    private String a = "a";
    private String b = "b";
    private String c = "c";
    private String d = "d";

    public static void main(String args[]) {
        TestNotify test = new TestNotify();

        Thread t1 = new Thread(() -> {
            while (true) {
                synchronized (test) {
                    while (test.getA().equals("A")) {
                        try {
                            test.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    test.setA("A");
                    test.notify();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            while (true) {
                synchronized (test) {
                    while (test.getB().equals("B")) {
                        try {
                            test.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    test.setB("B");
                    test.notify();
                }
            }
        });

        Thread t3 = new Thread(() -> {
            while (true) {
                synchronized (test) {
                    while (test.getA().equals("a")) {
                        try {
                            test.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    test.setA("a");
                    test.notify();
                }
            }
        });
        Thread t4 = new Thread(() -> {
            while (true) {
                synchronized (test) {
                    while (test.getB().equals("b")) {
                        try {
                            test.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    test.setB("b");
                    test.notify();
                }
            }
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

    synchronized void setA(String a) {
        this.a = a;
    }

    synchronized String getA() {
        return this.a;
    }

    synchronized void setB(String b) {
        this.b = b;
    }

    synchronized String getB() {
        return this.b;
    }

    synchronized void setC(String c) {
        this.c = c;
    }

    synchronized String getC() {
        return this.c;
    }

    synchronized void setD(String d) {
        this.d = d;
    }

    synchronized String getD() {
        return this.d;
    }

}
