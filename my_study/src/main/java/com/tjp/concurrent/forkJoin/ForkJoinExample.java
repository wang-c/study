package com.tjp.concurrent.forkJoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * Created by tujinpeng on 2017/11/20.
 */
public class ForkJoinExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();
        MyForkJoinTask task = new MyForkJoinTask(1, 4);
        Future<Integer> result = pool.submit(task);
        System.out.println("result is : " + result.get());
    }

    static class MyForkJoinTask extends RecursiveTask<Integer> {
        private final int SPLIT_SIZE = 2;
        private int start, end;

        public MyForkJoinTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        protected Integer compute() {
            int sum = 0;
            if ((end - start) <= SPLIT_SIZE) {
                for (int i = start; i <= end; i++) {
                    sum += i;
                }
            } else {
                int middle = (start + end) / 2;
                MyForkJoinTask firstTask = new MyForkJoinTask(start, middle);
                MyForkJoinTask secondTask = new MyForkJoinTask(middle + 1, end);
                firstTask.fork();   //提交任务
                secondTask.fork();  //
                int firstResult = firstTask.join();   //阻塞线程等待任务结果
                int secondResult = secondTask.join();
                sum = firstResult + secondResult;
            }
            return sum;
        }

    }

}


