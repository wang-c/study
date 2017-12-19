package com.tjp.concurrent.base;

import sun.misc.Contended;

/**
 * <pre>
 * 伪共享【无声的性能杀手】:
 * 问题:
 *     当两个共享变量位于【同一缓存行】(一般64字节)时,彼此间进行操作时会相互影响性能,尤其是在高并发的场景下
 * 解决:
 *     (1)追加字节,让一个共享变量独占一个缓存行
 *     (2)sun.misc.Contended jdk1.8才有
 * </pre>
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2017/12/19 上午11:09
 */
public class FalseSharing {

    public static int NUM_THREADS = 4; // 线程数
    public final static long ITERATIONS = 500L * 1000L * 1000L;

    public static void main(final String[] args) throws Exception {
        /**
         * 展示伪共享的糟糕性能
         */
        performsFalseSharing();
        /**
         * 通过追加一个共享变量字节的方式,让它填充满一个缓存行
         */
        resolveFalseSharingByPadding();
        /**
         * sun.misc.Contended(jdk1.8) 同样会让一个共享变量填充满一个缓存行
         * 注意:要添加jvm参数支持【-XX:-RestrictContended】
         */
        resolveFalseSharingByContended();
    }

    private static void performsFalseSharing() throws InterruptedException {
        VolatileLong[] volatileLongArr = new VolatileLong[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            volatileLongArr[i] = new VolatileLong();
        }
        doFalseSharingTest(volatileLongArr);
    }

    private static void resolveFalseSharingByPadding() throws InterruptedException {
        VolatileLongPadding[] volatileLongPaddingArr = new VolatileLongPadding[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            volatileLongPaddingArr[i] = new VolatileLongPadding();
        }
        doFalseSharingTest(volatileLongPaddingArr);
    }

    private static void resolveFalseSharingByContended() throws InterruptedException {
        VolatileLongContended[] volatileLongContendedArr = new VolatileLongContended[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            volatileLongContendedArr[i] = new VolatileLongContended();
        }
        doFalseSharingTest(volatileLongContendedArr);
    }

    private static void doFalseSharingTest(VolatileLong[] arr) throws InterruptedException {
        final long start = System.nanoTime();
        Thread[] threads = new Thread[NUM_THREADS];
        /**
         * 启动线程,对数组中每个volatile变量不停写
         * 如果数组中有两个volatile变量位于同一缓存行,则会造成伪共享,尤其是高并发场景下,效率很低
         *
         */
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new VolatileTask(arr, i));
        }
        for (Thread t : threads) {
            t.start();
        }
        //等待所有线程写完 统计消耗的时间
        for (Thread t : threads) {
            t.join();
        }
        System.out.println("elapsed time " + (System.nanoTime() - start) / 1000000000 + "s");
    }

    public static class VolatileTask implements Runnable {

        private VolatileLong[] volatileArr;
        private int index;

        public VolatileTask(VolatileLong[] volatileArr, int index) {
            this.volatileArr = volatileArr;
            this.index = index;
        }

        @Override
        public void run() {
            long i = ITERATIONS + 1;
            //不停地向volatile的数组中元素写
            while (0 != --i) {
                volatileArr[index].value = i;
            }
        }
    }

    //存在伪共享的共享变量
    public static class VolatileLong {
        public volatile long value = 0L;
    }

    //追加字节的方式,填充缓存行,避免伪共享
    public static class VolatileLongPadding extends VolatileLongPadding0 {
        public volatile long value = 0L;

    }

    public static class VolatileLongPadding0 extends VolatileLong {
        public volatile long p1, p2, p3, p4, p5, p6; // 追加6的long型(6*8字节)+java对象头(8字节)
    }

    //jdk1.8原生的注解支持缓存行填充
    @Contended
    static class VolatileLongContended extends VolatileLong {
    }
}
