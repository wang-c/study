package com.github.tjp.practice.message;

import junit.framework.Assert;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 消息合并组件测试
 * <p/>
 * 并发场景测试要点:
 * (1)多线程调用collect()和send()方法时,消息有没有丢失,看两者的生产消费数量是否一致
 * (2)多线程调用collect()和send()方法时,看生产消费的数据是否一致
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/11 下午3:31
 */
public class MessageMergerCase {

    private static MockMessageMerger MERGER = new MockMessageMerger();

    private static final Long GOODS_ID = 1111l;

    private static final int MESSAGE_NUM = 4;

    //并发度
    private static final int THREAD_NUM = 4;

    private static CountDownLatch countDownLatch = new CountDownLatch(MESSAGE_NUM);

    //并发屏障,让所有线程准备就绪后,统一放行,模拟多线程并发场景
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(THREAD_NUM);

    private static final Map<String, Object> dateMap = new ConcurrentHashMap<>();


    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        for (int index = 0; index < THREAD_NUM; index++) {
            Thread collector = new Thread(new Collector(index, MESSAGE_NUM / THREAD_NUM));
            collector.start();
        }

        //主线程等待所有消息调用collect完毕
        countDownLatch.await();

        //断言生产的消息数量==消息合并器中的日期集合数量
        Set dateSet = MERGER.getMergeMap().get(GOODS_ID).get();
//        Assert.assertEquals(MESSAGE_NUM, dateSet.size());


        //MERGER组件并发会丢失消息,打印下那些日期丢失了
        for (String key : dateMap.keySet()) {
            if (!dateSet.contains(key)) {
                System.out.println("lost date : " + key);
            }
        }
        System.out.println("time : " + (System.currentTimeMillis() - startTime));
    }

    //Collector线程 模拟多线程并发访问 MessageMerger.collect()
    private static class Collector implements Runnable {

        private int index;

        private int messageNum;

        public Collector(int index, int messageNum) {
            this.index = index;
            this.messageNum = messageNum;
        }

        @Override
        public void run() {
            //模拟并发
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            //每个搜集线程,专门用来创建消息,发送给Merger
            for (int i = 0; i < messageNum; i++) {
                Message message = new Message();
                message.setGoodsId(GOODS_ID);
                message.setDate("2018-" + index + "-" + i);
                //消息合并器,并发搜集消息
                MERGER.collect(message);

                dateMap.put("2018-" + index + "-" + i, new Object());
                //代表生产完一个消息
                countDownLatch.countDown();
            }

        }
    }

    //测试用的mock
    private static class MockMessageMerger extends MessageMerger {

        //统计搜集的消息数量
        private final AtomicLong addCount = new AtomicLong(0);

        //统计发送的消息数量
        private final AtomicLong sendCount = new AtomicLong(0);

        public void collect(Message message) {
            addCount.incrementAndGet();
            super.collect(message);
        }

        public void sendMergeMessage(Long goodsId, Set dateSet) {
            sendCount.addAndGet(dateSet.size());
            super.sendMergeMessage(goodsId, dateSet);
        }

    }
}
