package com.tjp.concurrent.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by tujinpeng on 2017/3/31.
 */
public class ScheduledThreadPoolTest {

    public static void main(String[] args) {
        //有10个核心线程的定时调度线程池
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

        /**
         * 一次性调度任务:延时1s执行任务
         */
        scheduledExecutorService.schedule(new ScheduledTask(), 1, TimeUnit.SECONDS);
        /**
         * 周期性调度任务:
         * 初始化延时1s执行任务,
         * 周期为1s:上一次任务执行开始时间到下一次任务开始执行间隔1s
         */
        scheduledExecutorService.scheduleAtFixedRate(new ScheduledTask(), 1, 1, TimeUnit.SECONDS);

        /**
         * 周期性调度任务:
         * 初始化延时1s执行任务,
         * 周期为1s:上一次任务执行结束时间到下一次任务开始执行间隔1s
         */
        scheduledExecutorService.scheduleWithFixedDelay(new ScheduledTask(), 1, 1, TimeUnit.SECONDS);
    }

    static class ScheduledTask implements Runnable {
        public void run() {
            System.out.println("scheduled task");
        }
    }
}
