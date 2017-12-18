package com.tjp.concurrent.blockqueue.delayqueue;

import java.util.concurrent.*;

/**
 * Created by tujinpeng on 2017/12/15.
 */
public class BoundedBlockingQueue {

    private static final int THREAD_NUM = 50;

    private static final int THREAD_TASK = 2000000;

    private static final BlockingQueue queue = new ArrayBlockingQueue(5000000);
//    private static final BlockingQueue queue = new LinkedBlockingQueue(5000000);

    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(2 * THREAD_NUM);

    private static CountDownLatch countDownLatch = new CountDownLatch(2 * THREAD_NUM);

//    private static final AtomicInteger count = new AtomicInteger(1);

    public static void main(String[] args) throws InterruptedException {
        //先让队列中有任务
        for (int i = 0; i <= THREAD_TASK; i++) {
            queue.offer("task");
        }

        //开启THREAD_SIZE大小的生产者消费者线程
        long start = System.currentTimeMillis();
        for (int i = 0; i < 2 * THREAD_NUM; i++) {
            Thread thread = null;
            if (i % 2 == 0) {
                thread = new Thread(new Producter(), "producter-" + i);
            } else {
                thread = new Thread(new Consumer(), "consumer-" + i);
            }
            thread.start();
        }
        //当消费者消费完size大小的任务后,这里才返回
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("finish tasks time : " + (end - start) + ", queue size : " + queue.size());


    }

    private static class Producter implements Runnable {

        @Override
        public void run() {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            for (int i = 0; i <= THREAD_TASK; i++) {
                queue.offer("task");
            }
            countDownLatch.countDown();
        }
    }

    private static class Consumer implements Runnable {

        @Override
        public void run() {
            try {
                cyclicBarrier.await();
                while (true) {
                    Object task = queue.poll();
                    if (task == null) {
                        countDownLatch.countDown();
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

}
