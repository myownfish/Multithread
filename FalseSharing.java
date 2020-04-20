package com.test.multithread;

import java.util.Comparator;

public class FalseSharing {
    public static int NUM_THREAD_MAX = 4;
    public final static long ITERATIONS = 50_000_0000L;

    private static VolatileLongPadded[] paddedLongs;
    private static VolatileLongUnPadded[] unPaddedLongs;

    public final static class VolatileLongPadded {
        public long q1, q2, q3, q4, q5, q6;
        public volatile long value = 0L;
        public long q11, q12, q13, q14, q15, q16;
    }

    public final static class VolatileLongUnPadded {
        public volatile long value = 0L;
    }

    static {
        paddedLongs = new VolatileLongPadded[NUM_THREAD_MAX];
        for (int i = 0; i < paddedLongs.length; i++) {
            paddedLongs[i] = new VolatileLongPadded();
        }

        unPaddedLongs = new VolatileLongUnPadded[NUM_THREAD_MAX];
        for (int i = 0; i < paddedLongs.length; i++) {
            unPaddedLongs[i] = new VolatileLongUnPadded();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub
        runBenchMark();
    }

    private static void runBenchMark() throws InterruptedException {
        long begin, end;
        for (int n = 1; n <= NUM_THREAD_MAX; n++) {
            Thread[] threads = new Thread[n];
            for (int j = 0; j < threads.length; j++) {
                threads[j] = new Thread(createPaddedRunnable(j));
            }
            begin = System.currentTimeMillis();
            for (Thread t : threads) {
                t.start();
            }
            for (Thread t : threads) {
                t.join();
            }
            end = System.currentTimeMillis();
            System.out.printf(" Padded # threads %d - T = %dms\n", n, end - begin);
            for(VolatileLongPadded padded : paddedLongs) {
                System.out.println(padded.value);
            }
            
            
            for (int j = 0; j < threads.length; j++) {
                threads[j] = new Thread(createUnPaddedRunnable(j));
            }
            begin = System.currentTimeMillis();
            for (Thread t : threads) {
                t.start();
            }
            for (Thread t : threads) {
                t.join();
            }
            end = System.currentTimeMillis();
            System.out.printf(" UnPadded # threads %d - T = %dms\n", n, end - begin);
            for(VolatileLongPadded padded : paddedLongs) {
                System.out.println(padded.value);
            }
        
        }

    }

    private static Runnable createUnPaddedRunnable(int j) {
        return () -> {
            long i = ITERATIONS + 1;
            while (0 != --i) {
                unPaddedLongs[j].value = i;
            }
        };
    }

    private static Runnable createPaddedRunnable(int j) {
        return () -> {
            long i = ITERATIONS + 1;
            while (0 != --i) {
                paddedLongs[j].value = i;
            }
        };
    }

}

/*
 * Padded # threads 1 - T = 3652ms
 UnPadded # threads 1 - T = 3686ms
 Padded # threads 2 - T = 4221ms
 UnPadded # threads 2 - T = 14164ms
 Padded # threads 3 - T = 5836ms
 UnPadded # threads 3 - T = 12517ms
 Padded # threads 4 - T = 7800ms
 UnPadded # threads 4 - T = 13013ms
 */
