package com.tjp.concurrent.threadpool;

import java.util.concurrent.*;

/**
 * java线程池核心类 ThreadPoolExecutor
 * Created by TJP on 2017/4/3.
 */
public class ThreadPoolExecutorTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.声明一个线程池
        ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 200, 100, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
        //2。提交任务
        pool.execute(new Runnable() {
            public void run() {
                System.out.println("poll execute");
            }
        });
        Future<?> future = pool.submit(new Runnable() {
            public void run() {
                System.out.println("poll submit");
            }
        });
        if(future.get()!=null){
            System.out.println("end");
        }

        //3。关闭线程池
        pool.shutdown();
        pool.shutdownNow();
        pool.isShutdown();
        pool.isTerminating();
        pool.isTerminated();


        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Executors.newSingleThreadExecutor();
        Executors.newCachedThreadPool();
    }

    private static boolean ranOrCancelled(int state) {
        return (state & (2 | 4)) != 0;
    }


}
