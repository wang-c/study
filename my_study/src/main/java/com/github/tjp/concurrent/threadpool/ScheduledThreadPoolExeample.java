package com.github.tjp.concurrent.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tujinpeng on 2017/3/31.
 */
public class ScheduledThreadPoolExeample {

    public static void main(String[] args) {
        //有5个核心线程的定时调度线程池
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

        /*
         * 一次性调度任务:延时3s执行任务
         */
//        scheduledExecutorService.schedule(new ScheduledTask(), 3, TimeUnit.SECONDS);

        /*
         * 按固定速率周期性调度任务:
         * 初始化延时0s执行任务,
         * 周期为3s:上一次任务执行开始时间到下一次任务开始执行间隔3s(下一个任务的执行要依赖上一个任务的结束)
         */
        scheduledExecutorService.scheduleAtFixedRate(new ScheduledTask(), 0, 3, TimeUnit.SECONDS);

        /*
         * 按固定的延时周期性调度任务:
         * 初始化延时0s执行任务,
         * 周期为3s:上一次任务执行结束时间到下一次任务开始执行间隔3s(下一个任务的执行也要依赖上一个任务的结束)
         */
//        scheduledExecutorService.scheduleWithFixedDelay(new ScheduledTask(), 0, 3, TimeUnit.SECONDS);
    }

    static class ScheduledTask implements Runnable {

        //原子的任务编号
        private static AtomicInteger number = new AtomicInteger(1);

        public void run() {
            int num = number.getAndIncrement();
            System.out.println("executed task " + num + ", start time : " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
            try {
                Thread.sleep(1000);//一个任务执行要3s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("executed task " + num + " , end time : " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
            System.out.println("-------------------------------------");

        }
    }
}
