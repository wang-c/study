package com.tjp.concurrent.threadpool;

import java.util.concurrent.*;

/**
 * java线程池核心类 ThreadPoolExecutor
 * Created by TJP on 2017/4/3.
 */
public class ThreadPoolExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
    }

    private static boolean ranOrCancelled(int state) {
        return (state & (2 | 4)) != 0;
    }


}
