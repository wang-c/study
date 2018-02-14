package com.github.tjp.concurrent.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 线路自由行自动打包 异步编程模型(线程池+future)
 * Created by tujinpeng on 2017/11/30.
 */
public class RouteAyncPackage {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Long productId = 6700L;
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(1), new NamedThreadFactory("tjp-pool", false), new AbortPolicyWithReport());

        //1.异步并行处理线路,酒店,门票的一次打包逻辑(每个品类分组之间进行组合)
        Future hotelFuture = pool.submit(new HotelPakgTask(productId));
        Future ticketFuture = pool.submit(new TicketPakgTask(productId));
        Future routeFuture = pool.submit(new RoutePakgTask(productId));

        //2.等待所有的一次打包全部结束,执行线路二次打包逻辑(不同品类的组合进行二次组合)
        List<Object> hotelFirstPages = (List<Object>) hotelFuture.get();
        List<Object> ticketFirstPages = (List<Object>) ticketFuture.get();
        List<Object> routeFirstPages = (List<Object>) routeFuture.get();
        doSecondPackage(hotelFirstPages, ticketFirstPages, routeFirstPages);

    }

    private static void doSecondPackage(Object o, Object o1, Object o2) {
        System.out.println("---------------");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("线路二次打包结束!!!");
    }

    static class HotelPakgTask implements Callable {

        private Long productId;

        public HotelPakgTask(Long productId) {
            this.productId = productId;
        }


        @Override
        public Object call() {
            //do hotel first package
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("酒店一次打包结束");

            return new ArrayList<Object>();
        }
    }

    static class TicketPakgTask implements Callable {

        private Long productId;

        public TicketPakgTask(Long productId) {
            this.productId = productId;
        }


        @Override
        public Object call() {
            //do ticket first package
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("门票一次打包结束");
            return new ArrayList<Object>();
        }
    }

    static class RoutePakgTask implements Callable {

        private Long productId;

        public RoutePakgTask(Long productId) {
            this.productId = productId;
        }


        @Override
        public Object call() {
            //do route first package
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线路一次打包结束");
            return new ArrayList<Object>();

        }
    }

}
