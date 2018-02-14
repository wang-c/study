package com.github.tjp.practice.message;

import com.github.tjp.common.threadPool.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <pre>
 * 合并消息组件(Singleton, ThreadSafe):商品变价消息首先经过此组件,进行消息合并,后续定时发送每个商品合并后的消息
 *
 * -背景:
 *    分销这边有个酒店时间价格同步,接受供应商时间价格消息,存储到mysql的一张trigger表,用一个job定时扫描这张表,
 * 执行时间价格同步过程.
 *
 * -问题:
 *    因为供应商系统发送消息不合理,改价按天发送消息,导致我们trigger表数据量太大,我们消费能力(开多个job)上不去,
 * 造成价格同步不及时.
 *
 * -解决:
 *    (1)一开始解决方案,提高消费方这边的能力,多开几个job,多线程消费-------》但是还是时间价格同步还是有延时,效果不明显
 *    (2)接受消息,立马存trigger表,但是job去扫描的时候,按商品id,做消息的合并,job批量计算价格同步入库----》有一定效果,但是当消息高峰来临时,还是有延时
 *    (3)接受到供应商按天发送的消息,在【本地内存】做消息合并,然后定时的将合并的消息存到trigger表?
 *   因此才有了一下本地合并消息组件(解决这种供应商按天发送消息的不合理导致trigger表数据量大,导致消费跟不上的根源)
 *
 *   毕竟本地内存是非常快的操作?
 *
 * </pre>
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/9 上午11:14
 */
public class MessageMerger implements Merger {

    private static final Logger logger = LoggerFactory.getLogger(MessageMerger.class);

    /*
     * -多个线程会并发的初始化的商品变价set,所以要用ConcurrentHashMap
     * -AtomicReference 对象引用的原子更新
     * -LinkedHashSet 对消息的日期做去重,按插入顺序排序
     */
    private final ConcurrentHashMap<Long, AtomicReference<MergeMessage>> mergeMap;

    private static final int SEND_INTERVAL = 5000;//ms

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1, new NamedThreadFactory("message-sender", true));


    public MessageMerger() {
        mergeMap = new ConcurrentHashMap<>();

        //启动定时任务,定时发送合并的消息
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                send();
            }
        }, SEND_INTERVAL, SEND_INTERVAL, TimeUnit.MILLISECONDS);
    }

    @Override
    public void collect(Message message) {
        if (message == null || message.getGoodsId() == null) {
            return;
        }
        Long goodsId = message.getGoodsId();
        //变价的日期
        String date = message.getDate();

        //初始化引用原子变量
        AtomicReference<MergeMessage> reference = mergeMap.get(goodsId);
        if (reference == null) {
            /*
             * 注意这里初始化reference不能这样写,reference相当于一个共享变量,这里存在对共享变量修改,会有线程不安全的问题
             * 要保证只有对共享变量的访问,没有修改?
             */
            //reference = new AtomicReference<Set>();
            /*
             * 这里存在多个线程put的情况,concurrentHashMap当put时,同一个key多个线程竞争时是要加【分段锁】的
             * 只有一个线程put成功,后续其他线程执行putIfAbsent,会看见第一个put的初始化值,直接返回,不做赋值操作
             * so这里一定要用putIfAbsent,不然会产生后续写线程将同一个key,value覆盖的问题
             */
            //putIfAbsent分段锁保证只有一个线程初始化reference成功
            mergeMap.putIfAbsent(goodsId, new AtomicReference<MergeMessage>());
            //立马get,通过volatile保证,其他线程立马可见reference
            reference = mergeMap.get(goodsId);
        }

        //compareAndSet进行原子更新dateSet
        MergeMessage current = null;
        //创建新的要更新的对象 某个商品的消息日期合并时间上是从【一个对象】转移成【另外一个对象】,通过引用的原子类AtomicReference,保证对象原子更新
        MergeMessage update = null;
        do {
            current = reference.get();
            if (current == null) {
                update = new MergeMessage(goodsId, date, date);
            } else {
                //暴力算法取最小最大日期
                String min = current.getStartTime();
                String max = current.getEndTime();
                if (date.compareTo(min) < 0) {
                    min = date;
                }
                if (date.compareTo(max) > 0) {
                    max = date;
                }
                update = new MergeMessage(goodsId, min, max);
            }
        } while (!reference.compareAndSet(current, update));//旧对象---》新对象
    }

    @Override
    public void send() {
        logger.info("==> execute interval send ");

        //定时发送合并消息,遍历map中所有entry
        for (ConcurrentHashMap.Entry<Long, AtomicReference<MergeMessage>> entry : mergeMap.entrySet()) {
            Long goodsId = entry.getKey();
            AtomicReference<MergeMessage> reference = entry.getValue();
            MergeMessage mergeMessage = reference.get();

            if (mergeMessage == null) {//没有变价信息
                continue;
            }

            //CAS找出可以发送合并消息的message
            MergeMessage current = null;
            MergeMessage update = null;
            do {
                //要发送的商品合并消息
                current = reference.get();
                //从null开始,重新开始合并消息
                update = null;
            } while (!reference.compareAndSet(current, update));//旧对象---》新对象=null

            //发送合并后的消息
            sendMergeMessage(current);

            //调试用
//            logger.info("==> current : " + current + " , update : " + update);
        }
    }

    //发送消息合并的服务
    public void sendMergeMessage(MergeMessage mergeMessage) {
        //send mergeMessage
        logger.info("==> send merge message  : " + mergeMessage);

    }

    /**
     * 当商品按天发消息合并时,要合并成一个时间段日期对象(对象尽可能小):
     * <p/>
     * 暴力算法:
     * 保留最小,最大日期
     */
    protected static class MergeMessage {
        private Long goodsId;

        private String startTime;

        private String endTime;

        public MergeMessage(Long goodsId, String startTime, String endTime) {
            this.goodsId = goodsId;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public Long getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(Long goodsId) {
            this.goodsId = goodsId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        @Override
        public String toString() {
            return "MergeMessage" + super.hashCode() + "{" +
                    "goodsId=" + goodsId +
                    ", startTime='" + startTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    '}';
        }
    }

    public ConcurrentHashMap<Long, AtomicReference<MergeMessage>> getMergeMap() {
        return mergeMap;
    }
}
