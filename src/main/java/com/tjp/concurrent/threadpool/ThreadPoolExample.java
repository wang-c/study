package com.tjp.concurrent.threadpool;

import org.junit.Test;

import java.lang.ref.WeakReference;
import java.util.concurrent.*;

/**
 * java线程池核心类 ThreadPoolExecutor
 * Created by TJP on 2017/4/3.
 */
public class ThreadPoolExample {

    /**
     * 定制的线程池：
     * -核心线程数：2
     * -最大线程数：2
     * -线程空闲时间(最大线程)：0
     * -任务队列：LinkedBlockingQueue(1)
     * -ThreadFactory：  NamedThreadFactory
     * -丢弃策略：AbortPolicyWithReport （dump堆栈）
     *
     * @throws InterruptedException
     */
    @Test
    public void customizedPool() throws InterruptedException {
        //自定义的线程池
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(1), new NamedThreadFactory("tjp-pool", false), new AbortPolicyWithReport());

        //线程池并发处理能力为3
        pool.execute(new Task("task-1"));
        pool.execute(new Task("task-2"));
        pool.execute(new Task("task-3"));
        //模拟线程池耗尽 执行丢弃策略的场景
//        pool.execute(new Task("task-4"));

        //让主线程不要结束
        Thread.sleep(20000);

    }

    /**
     * 向线程池提交任务的方式：
     * 1.execute
     * 2.submit
     * 3。batch submit
     * @throws InterruptedException
     */
    @Test
    public void submitTaskWay() throws InterruptedException {
        //自定义的线程池
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(1), new NamedThreadFactory("tjp-pool", false), new AbortPolicyWithReport());

        //线程池并发处理能力为3
        pool.execute(new Task("task-1"));
        Future<?> future = pool.submit(new CallableTask("task-1"));//提交一个有返回值的任务
        pool.execute(new Task("task-2"));

        //让主线程不要结束
        Thread.sleep(20000);

    }

    /**
     * 线程池的生命周期：
     * -状态
     * -终结
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public  void lifeCyclePool() throws ExecutionException, InterruptedException {
        //定制一个核心线程和最大线程为2 任务队列大小为1的线程池(同一时间最多消费3个任务【两个核心线程 队列1个】)
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(1), new NamedThreadFactory("tjp-pool", false), new AbortPolicyWithReport());
        //两种方式提交任务
        Future<?> future = pool.submit(new CallableTask("task-1"));//提交一个有返回值的任务
        pool.execute(new Task("task-2"));//提交一个没有返回值的任务
        pool.execute(new Task("task-3"));


        //1.这时候在提交一个任务,此时线程池满了,则执行丢弃策略
//        pool.execute(new Task("task-4"));

        //2.future方式获取异步任务执行结束的结果
        if (future.get() != null) {
            System.out.println(future.get());
        }

        /*
         * 3.线程池何时终结满足的两大要素:
         * (1)线程池外部强引用为null
         * (2)内部工作者线程全部退出
         */
        //弱引用对象 跟踪引用对象是否被gc回收
        WeakReference<ThreadPoolExecutor> refernece = new WeakReference<ThreadPoolExecutor>(pool);
        //只设置外部引用为null,你以为线程池对象就会被回收了,太天真了?
        pool = null;
        System.gc();
        System.out.println("memory pool obj : " + refernece.get());//线程池只外部的强引用为null了，但是内部线程还持有线程池引用,线程池对象不会被gc回收

        //终结线程池,让工作者线程完全退出
        pool.shutdown();
        try {
            //等待工作者线程池完全终结,全部工作者线程退出
            if (pool.awaitTermination(10, TimeUnit.SECONDS)) {
                //外部强引用设为null
                pool = null;
                System.gc();
                System.out.println("memory pool obj : " + refernece.get());//线程池外部强引用为null了，内部子线程也退出了，此时会被gc回收
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    static class Task implements Runnable {
        private String name;

        public Task(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                //do somenthing
                Thread.sleep(5000);//模拟大量很慢的任务
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " executed " + name);
        }
    }

    static class CallableTask implements Callable {

        private String name;

        public CallableTask(String name) {
            this.name = name;
        }

        @Override
        public Object call() throws Exception {
            try {
                //do somenthing
                Thread.sleep(5000);//模拟大量很慢的任务
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " executed " + name);
            return name + " executed result";
        }

    }

}
