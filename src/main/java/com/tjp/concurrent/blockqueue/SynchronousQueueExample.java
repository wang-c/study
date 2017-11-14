package com.tjp.concurrent.blockqueue;

import java.util.concurrent.SynchronousQueue;

/**
 * Created by tujinpeng on 2017/11/13.
 */
public class SynchronousQueueExample {

    private static boolean fair =false;
    private static SynchronousQueue queue = new SynchronousQueue(fair);//公平-队列 非公平-栈

    public static void main(String[] args) {
        Thread producter = new Thread(new Producter(), "SynchronousQueue-product-thread-01");
        producter.start();

        Thread consumer1 = new Thread(new Consumer(), "SynchronousQueue-consumer-thread-01");
        Thread consumer2 = new Thread(new Consumer(), "SynchronousQueue-consumer-thread-02");
        consumer1.start();
        consumer2.start();


    }

    static class Producter implements Runnable {
        @Override
        public void run() {
            boolean offer = queue.offer(1);
            System.out.println(Thread.currentThread().getName() + "offer result :" + offer);
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            try {
                Object result = queue.take();
                System.out.println(Thread.currentThread().getName() + " take result :" + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
