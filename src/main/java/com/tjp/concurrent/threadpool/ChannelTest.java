package com.tjp.concurrent.threadpool;

import java.util.concurrent.*;

/**
 * 模拟线路频道同时加载150产品信息
 * Created by tujinpeng on 2017/11/30.
 */
public class ChannelTest {

    private static final int THREAD_NUM = 60;


    public static void main(String[] args) throws InterruptedException {

        ThreadPoolExecutor pool1 = new ThreadPoolExecutor(THREAD_NUM, THREAD_NUM, 0, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(1000), new NamedThreadFactory("tjp-pool", false), new AbortPolicyWithReport());
        CompletionService completionService1 = new ExecutorCompletionService(pool1);

//        ThreadPoolExecutor pool2 = new ThreadPoolExecutor(THREAD_NUM, THREAD_NUM, 0, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(1000), new NamedThreadFactory("tjp-pool", false), new AbortPolicyWithReport());
//        CompletionService completionService2 = new ExecutorCompletionService(pool2);
//
//        ThreadPoolExecutor pool3 = new ThreadPoolExecutor(THREAD_NUM, THREAD_NUM, 0, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(1000), new NamedThreadFactory("tjp-pool", false), new AbortPolicyWithReport());
//        CompletionService completionService3 = new ExecutorCompletionService(pool3);

        for (int i = 0; i < 150; i++) {
            completionService1.submit(new ChannelTask());
        }
//        for (int i = 0; i < 50; i++) {
//            completionService2.submit(new ChannelTask());
//        }
//
//        for (int i = 0; i < 50; i++) {
//            completionService3.submit(new ChannelTask());
//        }

        long start = System.currentTimeMillis();
        //等待所有任务执行完毕
        for (int j = 0; j < 150; j++) {
            completionService1.take();
        }
//        for (int j = 0; j < 50; j++) {
//            completionService2.take();
//        }
//        for (int j = 0; j < 50; j++) {
//            completionService3.take();
//        }


        System.out.println("time : " + (System.currentTimeMillis() - start));

    }

    static class ChannelTask implements Callable {

        @Override
        public Object call() throws Exception {
            //模拟任务完成需要1s
            Thread.sleep(1000);
            System.out.println("task finish");
            return true;
        }
    }
}
