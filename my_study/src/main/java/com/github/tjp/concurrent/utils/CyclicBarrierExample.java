package com.github.tjp.concurrent.utils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier 屏障:
 * 让一组线程到达一个屏障(或者叫同步点)时被阻塞,知道最后一个线程到达屏障(全部线程到达屏障),屏障才会打开,所有被屏障拦截的线程才会干活
 * Created by tujinpeng on 2017/11/20.
 */
public class CyclicBarrierExample {

    public static void main(String[] args) {

        /**
         * 实现等待所有的选手都准备好了,选手在开始跑步
         */

        //等待所有的选手都准备好的屏障
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        Thread sportA = new Thread(new Sporter("选手A", cyclicBarrier));
        Thread sportB = new Thread(new Sporter("选手B", cyclicBarrier));
        Thread sportC = new Thread(new Sporter("选手C", cyclicBarrier));

        sportA.start();
        sportB.start();
        sportC.start();


    }

    static class Sporter implements Runnable {

        private String name;

        private CyclicBarrier cyclicBarrier;

        public Sporter(String name, CyclicBarrier cyclicBarrier) {
            this.name = name;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println(name + "-准备好了");
                //等待所有的选手都准备好了,在开始跑步
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {

            }
            System.out.println(name + "-开始跑步");
        }
    }

}
