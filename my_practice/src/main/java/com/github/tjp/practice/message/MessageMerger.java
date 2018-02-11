package com.github.tjp.practice.message;

import com.github.tjp.common.CollectionUtils;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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

    /*
     * -多个线程会并发的初始化的商品变价set,所以要用ConcurrentHashMap
     * -AtomicReference 对象引用的原子更新
     * -LinkedHashSet 对消息的日期做去重,按插入顺序排序
     */
    private final ConcurrentHashMap<Long, AtomicReference<Set>> mergeMap;

    public MessageMerger() {
        mergeMap = new ConcurrentHashMap<>();
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
        AtomicReference<Set> reference = mergeMap.get(goodsId);
        if (reference == null) {
            reference = new AtomicReference<Set>();
            reference.set(new LinkedHashSet<>());
            /*
             * 这里存在多个线程put的情况,concurrentHashMap当put时,同一个key多个线程竞争时是要加【分段锁】的
             * 只有一个线程put成功,后续其他线程执行putIfAbsent,会看见第一个put的初始化值,直接返回,不做赋值操作
             * so这里一定要用putIfAbsent,不然会产生后续写线程将同一个key,value覆盖的问题
             */
            mergeMap.putIfAbsent(goodsId, reference);
        }

        //compareAndSet进行原子更新dateSet
        Set<String> current = null;
        //创建新的要更新的对象 某个商品的消息日期合并时间上是从【一个对象】转移成【另外一个对象】,通过引用的原子类AtomicReference,保证对象原子更新
        Set<String> update = new LinkedHashSet<>();
        do {
            current = reference.get();
            if (current == null) {
                update = new LinkedHashSet<>();
                update.add(date);
            } else {
                /*
                 * 这里对新对象update赋值的时候,要先清空:
                 * 防止并发要自旋的时候, 调用addAll添加了脏数据
                 */
                update.clear();
                update.add(date);
                update.addAll(current);//【update】=【current】+【当前变价日期date】
            }
        } while (!reference.compareAndSet(current, update));//旧对象---》新对象=旧对象+新的变价日期

        if (reference.get().contains(date)) {
            System.out.println("current : " + current + " , update : " + update);
        }

    }

    @Override
    public void send() {

        //定时发送合并消息,遍历map中所有entry
        for (ConcurrentHashMap.Entry<Long, AtomicReference<Set>> entry : mergeMap.entrySet()) {
            Long goodsId = entry.getKey();
            AtomicReference<Set> reference = entry.getValue();
            Set<String> oldDateSet = reference.get();
            if (CollectionUtils.isEmpty(oldDateSet)) {
                //无合并的消息列表,下一个
                continue;
            }

            //发送合并后的消息
            sendMergeMessage(goodsId, oldDateSet);

            //减掉发送过的日期集合
            Set<String> current = null;
            Set<String> update = new LinkedHashSet<>();
            do {
                current = reference.get();
                if (current == null) {
                    update = new LinkedHashSet<>();
                } else {
                    //移除已经发送完毕的日期set
                    update.clear();
                    update.addAll(current);
                    update.removeAll(oldDateSet);
                }

            } while (!reference.compareAndSet(current, update));//旧对象---》新对象=旧对象-已经发送的日期集合

        }
    }

    //发送消息合并的服务
    public void sendMergeMessage(Long goodsId, Set dateSet) {
        //send mergeMessage
    }

    public ConcurrentHashMap<Long, AtomicReference<Set>> getMergeMap() {
        return mergeMap;
    }
}
