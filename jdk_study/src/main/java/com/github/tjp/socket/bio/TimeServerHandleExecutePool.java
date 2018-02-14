package com.github.tjp.socket.bio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by tujinpeng on 2017/4/23.
 */
public class TimeServerHandleExecutePool {

    private ExecutorService executor;

    public TimeServerHandleExecutePool(int maxPoolSize, int blockingQueueSize) {
        //开启的都是i/o线程,要控制任务队列大小,防止内存溢出
        executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxPoolSize, 120l, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(blockingQueueSize));
    }

    public void execute(Runnable task) {
        executor.execute(task);
    }

}
