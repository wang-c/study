package com.tjp.concurrent.threadpool;

import java.lang.ref.WeakReference;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by tujinpeng on 2017/11/29.
 */
public class ThreadPoolFinalExample {

    public static void main(String[] args) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS, new SynchronousQueue());
        pool.execute(new Task());

        WeakReference<ThreadPoolExecutor> refernece = new WeakReference<ThreadPoolExecutor>(pool);

        /**
         * 线程池终结满足的两大要素:
         * (1)线程池外部强引用为null
         * (2)内部工作者线程全部退出
         */
        //外部引用设为null
//        pool = null;
        System.gc();
        //此时线程池内部的工作者线程还持有pool reference
        System.out.println("memory pool reference : " + refernece.get());

        //终结线程池,让工作者线程完全退出
        pool.shutdown();
        try {
            //等待工作者线程池完全终结,工作者线程全部退出
            if (pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool = null;
                System.gc();
                System.out.println("memory pool reference : " + refernece.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    static class Task implements Runnable {
        @Override
        public void run() {
            try {
                //do somenthing
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("executed one task ");
        }
    }

}
