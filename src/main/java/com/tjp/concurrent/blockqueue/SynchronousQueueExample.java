package com.tjp.concurrent.blockqueue;

import java.util.concurrent.SynchronousQueue;

/**
 * Created by tujinpeng on 2017/11/13.
 */
public class SynchronousQueueExample {

    private static boolean fair = false;
    private static SynchronousQueue queue = new SynchronousQueue(fair);//公平-队列 非公平-栈

    /*
     * SynchronousQueue一个生产者线程必须等待另一个消费者线程 两者保持同步(生产消费能力对等)
     * -特别的操作:
     *  offer操作:若此时queue中没有可以匹配的的出队线程在,则不等待返回false
     *  poll操作:若此时queue中没有可匹配的入队线程在,则不等待直接返回false
     *
     *  put操作:若此时queue中没有可匹配的take出队线程在,则当前线程自旋一段时间后进入等待,直到后续其他take出队线程唤醒
     *  take操作:若此时queue中没有等待的put入队线程,则当前线程自旋一段时间后进入等待,直到后续其他put入队线程唤醒
     *
     * -公平和非公平:
     *  栈实现(LIFO):新的请求线程优先和栈顶元素(最后进入queue的线程节点)匹配
     *  队列实现(FIFO):新的请求优先和队列头部元素(最早进入队列的线程节点)匹配
     * @param args
     */
    public static void main(String[] args) {
        Thread producter = new Thread(new Producter(), "SynchronousQueue-product-thread-01");
        producter.start();

        Thread consumer1 = new Thread(new Consumer(), "SynchronousQueue-consumer-thread-01");
        Thread consumer2 = new Thread(new Consumer(), "SynchronousQueue-consumer-thread-02");
        consumer1.start();
        consumer2.start();


    }

    //生产者线程
    static class Producter implements Runnable {
        @Override
        public void run() {
            //offer操作
            boolean offer = queue.offer(1);
//            try {
//                //put操作
//                queue.put(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            System.out.println(Thread.currentThread().getName() + "offer result :" + offer);
        }
    }

    //消费者线程
    static class Consumer implements Runnable {
        @Override
        public void run() {
            Object result = null;
            try {
                //poll操作
//                result = queue.poll();
                //take 操作
                result = queue.take();
                System.out.println(Thread.currentThread().getName() + " take result :" + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
