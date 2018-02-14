package com.github.tjp.design.chain.arr;

import com.github.tjp.design.chain.dto.OrderContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 订单流程处理链实现:
 * <p/>
 * 基于数组的责任链,通过数组的方式组合一个个处理节点,用一个position方式标记执行到的位置,
 * 通过调用doProcessor执行下一个节点
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/8 下午7:49
 */
public class OrderProcessorChainImpl implements OrderProcessorChain {

    /**
     * 调用链上的处理节点集合
     */
    private List<OrderProcessor> processors = new ArrayList<>();

    /**
     * 标记处理链执行到的位置
     */
    private Iterator<OrderProcessor> iterator;

    @Override
    public void doProcessor(OrderContext orderContext) {
        if (iterator == null) {
            iterator = processors.iterator();
        }
        //调用订单处理链的下一个节点
        if (iterator.hasNext()) {
            OrderProcessor next = iterator.next();
            next.doProcessor(orderContext, this);
        }
    }

    @Override
    public void addProcessor(OrderProcessor orderProcessor) {
        processors.add(orderProcessor);
    }


}
