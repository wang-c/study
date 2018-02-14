package com.github.tjp.concurrent.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/12 上午10:31
 */
public class AtomicIntegerCase {

    private static AtomicInteger count = new AtomicInteger(0);

    public static void write() {
        int current;
        int update;

        do {
            current = count.get();
            update = current + 10;
        } while (!count.compareAndSet(current, update));

        System.out.println(Thread.currentThread() + "==> current : " + current + " , update : " + update);
    }

    public static void main(String[] args) {
        int threadNum = 10;
        Thread[] threads = new Thread[threadNum];

        for (int i = 0; i < threadNum; i++) {
            threads[i] = new Thread(new Producter());
        }
        for (int i = 0; i < threadNum; i++) {
            threads[i].start();
        }

    }

    private static class Producter implements Runnable {
        @Override
        public void run() {
            AtomicIntegerCase.write();
        }
    }


}
