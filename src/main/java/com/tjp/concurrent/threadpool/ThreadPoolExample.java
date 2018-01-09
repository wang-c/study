package com.tjp.concurrent.threadpool;

import org.junit.Test;

import java.lang.ref.WeakReference;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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
     * 3.group submit
     *
     * @throws InterruptedException
     */
    @Test
    public void submitTaskWay() throws InterruptedException, ExecutionException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(20, 20, 0, TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(1),
                new NamedThreadFactory("tjp-pool", false),
                new AbortPolicyWithReport());
        //1.execute
        pool.execute(new Task("task-1"));
         /*
         * 2.submit future模式:
         * 返回future 代表未来结果的占位符
         *
         * future模式(futureTask实现):
         *     (1)等待/通知机制实际上是实现了【实现生产者消费者线程之间的通信】,通信共享的是【任务完成的结果】
         *     (2)等待:调用future.get()方法,若提交的任务未完成则生产者线程进入等待状态(在future的栈中等待)
         *     (3)通知:由线程池中工作者线程完成任务后通知唤醒生产者线程,返回结果
         *
         *-传统的等待通知机制【锁+条件队列】:
         *-等待线程:
         *lock.lock();
         * try{
         *    while(result==null){//共享变量条件不满足时,生产者线程进入等待
         *      condition.await();
         *    }
         *    //条件满足时,返回结果
         *    result result;
         * }finally{
         *    lock.unLock();
         * }
         *-通知线程:
         * lock.lock()
         * try{
         *    result=run()//执行完任务
         *    condition.signalAll();//通知所有等待着线程,返回结果
         * }finally{
         *    lock.unLock();
         * }
         *
         *
         *-futureTask实现的等待通知机制【(volatile)state+简单栈】:
         *
         *-等待线程:
         * while(state<=COMPLETING){//当任务未执行完时,线程进入stack等待
         *      awaitStack();
         * }
         * if(state==normal)//当条件满足时,返回任务执行结果
         *    return result;
         *
         *-通知线程:
         * result=callable.call();
         * UNSAFE.putOrderedInt(this, stateOffset, NORMAL);//cas设置state为执行完成
         * finishCompletion();//通知栈上等待的所有线程
         *
         *future模式的通知等待机制和传统方式的区别?:
         *(1)future模式相对于传统方式,更加的轻量级
         *(2)future模式通过对【volatile变量写后读】的原则,保证线程间共享变量【任务执行的结果result】的可见性,
         *   而传统的模式通过加锁的方式,保证线程间共享变量的可见性
         *(3)这两者间的区别有点类似乐观锁和悲观锁
         *
         */
        Future<?> future = pool.submit(new CallableTask("task-2"));//提交一个有返回值的任务
        if (future.get() != null) {//等待task-2任务完成
            System.out.println("task-2 finish , return : " + future.get());
        }

        //3.group submit
        CompletionService completionService = new ExecutorCompletionService(pool);
        //向线程池提交一组任务
        for (int i = 0; i < 5; i++) {
            completionService.submit(new CallableTask("batch-task-" + i));
        }
        //让主线程等待所有的任务执行完毕
        for (int i = 0; i < 5; i++) {
            completionService.take();//当有一个任务执行完毕后,会加入到completionService阻塞队列中
        }
        System.out.println("all batch tasks finish");

        //让主线程不要结束
        Thread.sleep(10000);

    }

    /**
     * 一个默认扩展的一个线程池
     */
    static class DefaultThreadPoolExecutor extends ThreadPoolExecutor {

        public DefaultThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        protected void terminated() {
            System.out.println("---------------------------one thread pool is terminated----------------------------------");
        }

    }

    @Test
    public void lifeCyclePool() throws ExecutionException, InterruptedException {
        //定制一个核心线程和最大线程为2 任务队列大小为1的线程池(同一时间最多消费3个任务【两个核心线程 队列1个】)
        ThreadPoolExecutor pool = new DefaultThreadPoolExecutor(1, 1, 0, TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(10),
                new NamedThreadFactory("tjp-pool", false),
                new AbortPolicyWithReport());
        System.out.println(String.format(" pool start , status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)", pool.isShutdown(), pool.isTerminated(), pool.isTerminating()));
        //提交任务
        pool.execute(new Task("task-1"));
        pool.execute(new Task("task-2"));
        pool.execute(new Task("task-3"));
        /*
         * 终结线程池的两个方法：
         *   (1)shutdown    :  不在接受新任务，会处理正在运行的任务和任务队列中堆积的任务【平滑】
         *   (2)shutdownNow :  不在接受新任务，直接中断正在运行任务的线程，同时不在处理任务队列中的任务【暴力】
         */
        pool.shutdown();
//        pool.shutdownNow();
        System.out.println(String.format(" invoke shutdown , status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)", pool.isShutdown(), pool.isTerminated(), pool.isTerminating()));


//        pool.execute(new Task("task-4"));//模拟当线程池调用终结方法时，不在接受一个新的任务，新提交的任务直接执行丢弃策略

        /*
         *当所有工作者线程全部退出&调用terminated（）方法后，才意味着线程池最终的结束
         */
        if (pool.awaitTermination(100, TimeUnit.SECONDS)) {
            System.out.println(String.format(" pool is terminated , status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)", pool.isShutdown(), pool.isTerminated(), pool.isTerminating()));
        }
    }

    /**
     * 线程池的生命周期：
     * -状态
     * -终结
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void gcThreadPool() throws ExecutionException, InterruptedException {
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
                Thread.sleep(1000);//模拟大量很慢的任务
                System.out.println(Thread.currentThread().getName() + " executed " + name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class CallableTask implements Callable<String> {

        private String name;

        public CallableTask(String name) {
            this.name = name;
        }

        @Override
        public String call() throws Exception {
            try {
                //do somenthing
                Thread.sleep(1000);//模拟大量很慢的任务
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " executed " + name);
            return name + "-result";
        }

    }

}
