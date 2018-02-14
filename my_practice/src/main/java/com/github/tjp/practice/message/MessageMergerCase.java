package com.github.tjp.practice.message;

import com.github.tjp.common.DateUtils;
import junit.framework.Assert;

import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 消息合并组件测试用例
 * <p/>
 * 并发场景测试要点:
 * (1)多线程调用collect()和send()方法时,消息有没有丢失,看两者的生产消费数量是否一致
 * (2)多线程调用collect()和send()方法时,看生产消费的数据是否一致
 * <p/>
 * 线程安全
 * 执行时间 效率
 * cpu
 * 内存
 * 线程资源
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/11 下午3:31
 */
public class MessageMergerCase {

    private static MockMessageMerger MERGER = new MockMessageMerger();

    private static final Long GOODS_ID = 1111l;

    private static final String START_TIME = "2018-01-01";
    private static final String END_TIME = "2018-12-31";

    //并发搜集生产的日期范围
    private static final ConcurrentSkipListMap<String, Object> dateMap = new ConcurrentSkipListMap<>();

    private static final int MESSAGE_NUM = 1000;

    //并发度
    private static final int THREAD_NUM = 10;

    private static CountDownLatch countDownLatch = new CountDownLatch(MESSAGE_NUM);

    //并发屏障,让所有线程准备就绪后,统一放行,模拟多线程并发场景
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(THREAD_NUM);

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        for (int index = 0; index < THREAD_NUM; index++) {
            Thread collector = new Thread(new Collector(MESSAGE_NUM / THREAD_NUM), "collector-thread-" + index);
            collector.start();
        }

        //主线程等待所有消息调用collect完毕
        countDownLatch.await();

        //测试点一、并发搜集消息,断言最后生成的合并消息体,开始时间,结束时间的正确性
        MessageMerger.MergeMessage mergeMessage = MERGER.getMergeMap().get(GOODS_ID).get();

        Assert.assertEquals(mergeMessage.getStartTime(), dateMap.firstKey());
        Assert.assertEquals(mergeMessage.getEndTime(), dateMap.lastKey());

        //测试点二、统计时间
        System.out.println("time : " + (System.currentTimeMillis() - startTime));


        //让主线程不关闭
        Thread.sleep(60000);
    }

    //Collector线程 模拟多线程并发访问 MessageMerger.collect()
    private static class Collector implements Runnable {

        private int messageNum;

        public Collector(int messageNum) {
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
                String randomDate = DateUtils.randomDate(START_TIME, END_TIME);
                message.setDate(randomDate);//随机时间
                //搜集产生的日期范围
                dateMap.put(randomDate, Boolean.TRUE);

                //消息合并器,并发搜集消息
                MERGER.collect(message);

                //代表生产完一个消息
                countDownLatch.countDown();
            }

        }
    }

    //Sender线程 模拟多线程并发 发送合并器的合并的消息
    private static class Sender implements Runnable {

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

            //发送所有商品合并完的消息
            MERGER.send();
            countDownLatch.countDown();

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
        }

    }
}
