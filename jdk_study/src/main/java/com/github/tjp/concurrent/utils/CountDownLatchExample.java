package com.github.tjp.concurrent.utils;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch(闭锁):
 * <pre/>
 * 允许一个或者一组线程等待,直到其他线程执行完毕后再执行
 * <pre/>
 * Created by tujinpeng on 2017/11/20.
 */
public class CountDownLatchExample {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        Thread hotelThread = new Thread(new HotelPakgWork(latch));
        Thread ticketThread = new Thread(new TicketPakgWork(latch));
        Thread routeThread = new Thread(new RoutePakgWork(latch));
        hotelThread.start();
        ticketThread.start();
        routeThread.start();

        /**
         *二次打包等待所有一次打包任务执行完之后,才执行
         */
        latch.await();
        //do second packge
        System.out.println("自由行二次打包开始");
    }

    static class HotelPakgWork implements Runnable {

        private CountDownLatch latch;

        public HotelPakgWork(CountDownLatch latch) {
            this.latch = latch;
        }


        @Override
        public void run() {
            //do hotel first package
            System.out.println("酒店一次打包结束");
            latch.countDown();
        }
    }

    static class TicketPakgWork implements Runnable {

        private CountDownLatch latch;

        public TicketPakgWork(CountDownLatch latch) {
            this.latch = latch;
        }


        @Override
        public void run() {
            //do ticket first package
            System.out.println("门票一次打包结束");
            latch.countDown();
        }
    }

    static class RoutePakgWork implements Runnable {

        private CountDownLatch latch;

        public RoutePakgWork(CountDownLatch latch) {
            this.latch = latch;
        }


        @Override
        public void run() {
            //do route first package
            System.out.println("线路一次打包结束");
            latch.countDown();
        }
    }

}
