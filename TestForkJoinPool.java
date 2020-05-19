package com.test.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class TestForkJoinPool {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		ForkJoinPool pool = ForkJoinPool.commonPool();
		System.out.println("Pool init:" + pool);

		ForkJoinTask<Integer> task = pool.submit(new CountTask(1, 100));
		System.out.println("total:" + task.get());

		try {
			pool.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
		pool.shutdown();
	}

	static class CountTask extends RecursiveTask<Integer> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int start;
		private int end;

		public CountTask(int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		protected Integer compute() {
			int sum = 0;
			if (end - start <= 5) {
				for (int i = start; i <= end; i++) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sum += i;
				}
				System.out
						.println(Thread.currentThread().getName() + " - sum from " + start + " to " + end + " with result " + sum);
			} else {
				int mid = (start + end) / 2;
				CountTask leftTask = new CountTask(start, mid - 1);
				CountTask rightTask = new CountTask(mid, end);
				// 切分大任务
				leftTask.fork();
				rightTask.fork();
				// 合并小任务结果
				sum += leftTask.join();
				sum += rightTask.join();
			}
			return sum;
		}
	}
}
