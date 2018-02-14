package com.github.tjp.concurrent.utils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Semaphore 信号量: 控制并发访问的个数
 * <p/>
 * Created by tujinpeng on 2017/11/20.
 */
public class SemaphoreExample {

    private static final int LIMIT = 5;//并发控制个数为5个

    public static void main(String[] args) {

        //控制并发访问的个数10
        Semaphore semaphore = new Semaphore(LIMIT);

        //保证所有线程准备就绪的阻塞屏障(10个线程到达)
        CyclicBarrier barrier = new CyclicBarrier(10, new Runnable() {
            @Override
            public void run() {
                System.out.println("所有线程准备完毕!!!");
            }
        });

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new SemaphoreTask("task-" + i, barrier, semaphore));
            t.start();
        }


    }

    static class SemaphoreTask implements Runnable {

        private String name;

        private CyclicBarrier barrier;

        private Semaphore semaphore;

        public SemaphoreTask(String name, CyclicBarrier barrier, Semaphore semaphore) {
            this.name = name;
            this.barrier = barrier;
            this.semaphore = semaphore;
        }


        @Override
        public void run() {
            try {
                //等待所有线程到达屏障(所有线程准备就绪),然后同时运行
                barrier.await();

                //获取许可
                semaphore.acquire();
                System.out.println("Accessing: " + name);
                Thread.sleep(1000);//模拟处理业务

                //释放许可
                semaphore.release();
                System.out.println("----------" + semaphore.availablePermits());//获取当前信号量中可用的许可

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }


        }
    }
}

