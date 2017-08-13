package com.tjp.concurrent.blockqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tujinpeng on 2017/3/26.
 */
public class BlockingQueueTest {

    public static void main(String[] args) {
        //申明一个容量为10的阻塞队列
        BlockingQueue<String> queue=new LinkedBlockingQueue<String>(10);

        //启动3个生产者 1个消费者 操作queue
        Thread product1=new Thread(new Producter(queue),"product-thread-01");
        Thread product2=new Thread(new Producter(queue),"product-thread-02");
        Thread product3=new Thread(new Producter(queue),"product-thread-03");
        Thread consumer1=new Thread(new Consumer(queue),"consumer-thread-01");
        product1.start();
        product2.start();
        product3.start();
        consumer1.start();


    }

    static class Producter implements Runnable {
        /**
         * 线程共享的统计生产元素的数量
         */
        private static AtomicInteger count = new AtomicInteger();//默认从0开始

        /**
         * 外部传进来的缓冲阻塞队列
         */
        private BlockingQueue<String> queue;

        public Producter(BlockingQueue queue) {
            super();
            this.queue = queue;

        }

        public void run() {
            String data = "";
            System.out.println("启动生产者线程" + Thread.currentThread().getName());
            try {
                while (true) {
                    data = "data-" + count.incrementAndGet();
                    System.out.println("入队元素" + data);
                    if(!queue.offer(data, 2, TimeUnit.SECONDS)){
                        System.out.println("入队失败,元素" + data );
                    }

                    //没生产一个就睡眠1s
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                System.out.println("生产者线程" + Thread.currentThread().getName()+"退出");
            }

        }
    }

    static class Consumer implements Runnable {

        /**
         * 外部传进来的缓冲阻塞队列
         */
        private BlockingQueue<String> queue;

        public Consumer(BlockingQueue<String> queue) {
            super();
            this.queue = queue;

        }

        public void run() {
            System.out.println("启动消费者线程" + Thread.currentThread().getName());
            try {
                while (true) {
                    String data = (String) queue.poll(2,TimeUnit.SECONDS);
                    if(data!=null){
                        System.out.println("出队元素" + data);
                    }else{
                        System.out.println("出队元素" + data);
                    }

                    //没消费一个就睡眠1s
                    Thread.sleep(1000);

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                System.out.println("消费者线程" + Thread.currentThread().getName()+"退出");

            }

        }
    }

}
