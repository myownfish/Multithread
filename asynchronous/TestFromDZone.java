package com.test.multithread.asynchronous;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TestFromDZone {

    public static void main(String[] args) {
//        TestFromDZone.completedFutureExample();
//        TestFromDZone.runAsyncExample();
//        TestFromDZone.thenApplyExample();
//        TestFromDZone.thenApplyAsyncExample();
//        TestFromDZone.thenApplyAsyncWithExecutorExample();
//        TestFromDZone.thenAcceptExample();
//        TestFromDZone.thenAcceptAsyncExample();
//        TestFromDZone.completeExceptionallyExample();
//        TestFromDZone.cancelExample();
//        TestFromDZone.applyToEitherExample();
//        TestFromDZone.acceptEitherExample();
//        TestFromDZone.runAfterBothExample();
//        TestFromDZone.thenAcceptBothExample();
//        TestFromDZone.thenCombineExample();
//        TestFromDZone.thenCombineAsyncExample();
//        TestFromDZone.thenComposeExample();
//        TestFromDZone.anyofExample();
//        TestFromDZone.allOfExample();
        TestFromDZone.allOfAsyncExample();

    }

    /*
     * The simplest example creates an already completed CompletableFuture with a
     * predefined result. Usually, this may act as the starting stage in your
     * computation.
     */
    static void completedFutureExample() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message");
        System.out.println(cf.isDone());
        System.out.println(cf.getNow(null));
    }

    /*
     * The next example is how to create a stage that executes a Runnable
     * asynchronously:
     */
    static void runAsyncExample() {
        CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
            System.out.println("Is daemon thread? " + Thread.currentThread().isDaemon());
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        System.out.println("Is Done? " + cf.isDone());
        try {
            Thread.currentThread().sleep(7000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Is Done? " + cf.isDone());
    }

    /*
     * Apply a Function on the previous stage Note the behavioral keywords in
     * thenApply : then, which means that the action of this stage happens when the
     * current stage completes normally (without an exception). In this case, the
     * current stage is already completed with the value “message”. Apply, which
     * means the returned stage will apply a Function on the result of the previous
     * stage. The execution of the Function will be blocking, which means that
     * getNow() will only be reached when the uppercase operation is done.
     */
    static void thenApplyExample() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApply(s -> s.toUpperCase());
        System.out.println(cf.getNow(null));
    }

    static void thenApplyAsyncExample() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(s -> s.toUpperCase());
        System.out.println(cf.getNow(null));
        System.out.println(cf.join());
    }

    /*
     * A very useful feature of asynchronous methods is the ability to provide an
     * Executor to use it to execute the desired CompletableFuture. This example
     * shows how to use a fixed thread pool to apply the uppercase conversion
     * Function:
     */
    static ExecutorService executor = Executors.newFixedThreadPool(3, new ThreadFactory() {
        int count = 1;

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "custom-executor-" + count++);
        }
    });

    static void thenApplyAsyncWithExecutorExample() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(s -> {
            System.out.println(Thread.currentThread().getName().startsWith("custom-executor-"));
            System.out.println(Thread.currentThread().isDaemon());
            return s.toUpperCase();
        }, executor);
        executor.shutdown();
        System.out.println(cf.getNow(null));
        System.out.println(cf.join());
    }

    /*
     * If the next stage accepts the result of the current stage but does not need
     * to return a value in the computation (i.e. its return type is void), then
     * instead of applying a Function, it can accept a Consumer, hence the method
     * thenAccept: The Consumer will be executed synchronously, so we don’t need to
     * join on the returned CompletableFuture.
     */
    static void thenAcceptExample() {
        StringBuilder result = new StringBuilder();
        CompletableFuture<Void> cf = CompletableFuture.completedFuture("thenAccept message").thenAccept(s -> result.append(s));
//        System.out.println(cf.join());
        System.out.println(cf.isDone());
        System.out.println(cf.getNow(null));
        System.out.println(result.toString());
    }

    static void thenAcceptAsyncExample() {
        StringBuilder result = new StringBuilder();
        CompletableFuture<Void> cf = CompletableFuture.completedFuture("thenAcceptAsync message")
                .thenAcceptAsync(s -> result.append(s));
        System.out.println(cf.isDone());
        System.out.println(cf.join());
        System.out.println(result.toString());
    }

    // 8. Completing a Computation Exceptionally???
    static void completeExceptionallyExample() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message")
                .thenApplyAsync(s -> {
                    new RuntimeException("completed exceptionally");
                    return "hello";
                });
//        CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS);
        System.out.println(cf.completeExceptionally(new RuntimeException("completed exceptionally")));

        CompletableFuture<String> exceptionHandler = cf.handle((s, th) -> {
            return (th != null) ? "message upon cancel" : s;
        });
//        cf.completeExceptionally(new RuntimeException("completed exceptionally"));
        System.out.println(cf.isCompletedExceptionally());
        cf.join();
    }

    // ??
    static void cancelExample() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(String::toUpperCase);
        CompletableFuture<String> cf2 = cf.exceptionally(throwable -> "canceled message");
        System.out.println(cf.cancel(true));
        System.out.println(cf.isCompletedExceptionally());
        System.out.println(cf2.join());
    }

    /*
     * The below example creates a CompletableFuture that applies a Function to the
     * result of either of two previous stages (no guarantees on which one will be
     * passed to the Function). The two stages in question are: one that applies an
     * uppercase conversion to the original string and another that applies a
     * lowercase conversion:
     */
    static void applyToEitherExample() {
        String original = "Message";
        CompletableFuture<String> cf1 = CompletableFuture.completedFuture(original)
                .thenApplyAsync(s -> {
                    try {
                        Thread.currentThread().sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return s.toUpperCase();
                });
        CompletableFuture<String> cf2 = cf1.applyToEither(CompletableFuture.completedFuture(original)
                .thenApplyAsync(s -> {
                    try {
                        Thread.currentThread().sleep(4000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return s.toLowerCase();
                }), s -> s + " from applyToEither");
        System.out.println(cf2.join());
    }

    static void acceptEitherExample() {
        String original = "Message";
        // Notice the use of the thread-safe StringBuffer here instead of StringBuilder.
        StringBuffer result = new StringBuffer();
        CompletableFuture<String> cf = CompletableFuture.completedFuture(original)
                .thenApplyAsync(s -> {
                    try {
                        Thread.currentThread().sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return s.toUpperCase();
                });
        CompletableFuture<Void> cf2 = cf.acceptEither(CompletableFuture.completedFuture(original)
                .thenApplyAsync(s -> {
                    try {
                        Thread.currentThread().sleep(6000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return s.toLowerCase();
                }), s -> result.append(s).append(" acceptEither"));
        System.out.println(cf2.join());
        System.out.println(result.toString());
    }

    /*
     * This example shows how the dependent CompletableFuture that executes a
     * Runnable triggers upon completion of both of two stages. Note that all the
     * stages below run synchronously, where a stage first converts a message string
     * to uppercase, then a second converts the same message string to lowercase.
     */
    static void runAfterBothExample() {
        String original = "Message";
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture(original).thenApply(String::toUpperCase).runAfterBoth(
                CompletableFuture.completedFuture(original).thenApply(String::toLowerCase),
                () -> result.append("done"));
        System.out.println(result.toString());
    }

    static void thenAcceptBothExample() {
        String original = "Message";
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture(original).thenApply(String::toUpperCase).thenAcceptBoth(
                CompletableFuture.completedFuture(original).thenApply(String::toLowerCase),
                (s1, s2) -> result.append(s1 + ":" + s2));
        System.out.println(result.toString());
    }

    /*
     * If the dependent CompletableFuture is intended to combine the results of two
     * previous CompletableFutures by applying a function on them and returning a
     * result, we can use the method thenCombine(). The entire pipeline is
     * synchronous, so getNow() at the end would retrieve the final result, which is
     * the concatenation of the uppercase and the lowercase outcomes.
     */
    static void thenCombineExample() {
        String original = "Message";
        CompletableFuture<String> cf = CompletableFuture.completedFuture(original).thenApply(s -> {
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return s.toUpperCase();
        }).thenCombine(CompletableFuture.completedFuture(original)
                .thenApply(s -> {
                    try {
                        Thread.currentThread().sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return s.toLowerCase();
                }), (s1, s2) -> s1 + s2);
        System.out.println(cf.getNow(null));
    }

    /*
     * Similar to the previous example, but with a different behavior: since the two
     * stages upon which CompletableFuture depends both run asynchronously, the
     * thenCombine() method executes asynchronously, even though it lacks the Async
     * suffix. This is documented in the class Javadocs: “Actions supplied for
     * dependent completions of non-async methods may be performed by the thread
     * that completes the current CompletableFuture, or by any other caller of a
     * completion method.” Therefore, we need to join() on the combining
     * CompletableFuture to wait for the result.
     */
    static void thenCombineAsyncExample() {
        String original = "Message";
        CompletableFuture<String> cf = CompletableFuture.completedFuture(original).thenApplyAsync(s -> {
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return s.toUpperCase();
        }).thenCombine(CompletableFuture.completedFuture(original)
                .thenApplyAsync(s -> {
                    try {
                        Thread.currentThread().sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return s.toLowerCase();
                }), (s1, s2) -> s1 + s2);
        System.out.println(cf.getNow(null));
        System.out.println(cf.join());
    }

    static void thenComposeExample() {
        String original = "Message";
        CompletableFuture<String> cf = CompletableFuture.completedFuture(original).thenApplyAsync(s -> {
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return s.toUpperCase();
        }).thenCompose(
                upper -> CompletableFuture.completedFuture(original)
                        .thenApplyAsync(s -> {
                            try {
                                Thread.currentThread().sleep(5000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            return s.toLowerCase();
                        }).thenApply(s -> upper + s));
        System.out.println(cf.getNow(null));
        System.out.println(cf.join());
    }

    /*
     * The below example illustrates how to create a CompletableFuture that
     * completes when any of several CompletableFutures completes, with the same
     * result. Several stages are first created, each converting a string from a
     * list to uppercase. Because all of these CompletableFutures are executing
     * synchronously (using thenApply()), the CompletableFuture returned from
     * anyOf() would execute immediately, since by the time it is invoked, all
     * stages are completed. We then use the whenComplete(BiConsumer<? super Object,
     * ? super Throwable> action), which processes the result (asserting that the
     * result is uppercase).
     */
    static void anyofExample() {
        StringBuilder result = new StringBuilder();
        List<String> message = Arrays.asList("a", "b", "c");
        List<CompletableFuture<String>> futures = message.stream().map(
                msg -> CompletableFuture.completedFuture(msg).thenApply(s -> {
                    return s.toUpperCase();
                })).collect(Collectors.toList());
        CompletableFuture.anyOf(futures.toArray(new CompletableFuture[futures.size()])).whenComplete(
                (res, th) -> {
                    if (th == null) {
                        result.append(res);
                    }
                });
        System.out.println(result.toString());
    }

    /*
     * This example illustrate how to create a CompletableFuture that completes when
     * all of several CompletableFutures completes, in a synchronous fashion. The
     * scenario is the same as the previous example: a list of strings is provided
     * where each element is converted to uppercase.
     */
    static void allOfExample() {
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture<String>> futures = messages.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApply(s -> {
                    return s.toUpperCase();
                })).collect(Collectors.toList());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).whenComplete(
                (v, th) -> {
                    futures.forEach(cf -> {
                        result.append(cf.getNow(null));
                    });
                    result.append(",done");
                });
        System.out.println(result.toString());
    }

    /*
     * By switching to thenApplyAsync() in the individual CompletableFutures, the
     * stage returned by allOf() gets executed by one of the common pool threads
     * that completed the stages. So we need to call join() on it to wait for its
     * completion.
     */
    static void allOfAsyncExample() {
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture<String>> futures = messages.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApplyAsync(s -> {
                    return s.toUpperCase();
                })).collect(Collectors.toList());
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).whenComplete(
                (v, th) -> {
                    futures.forEach(cf -> {
                        result.append(cf.getNow(null));
                    });
                    result.append(",done");
                });
        System.out.println(result.toString());
        System.out.println(allOf.join());
        System.out.println(result.toString());
    }
}
