package com.github.tjp.chain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 订单流程处理链实现
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

    private Iterator<OrderProcessor> iterator;

    @Override
    public void doProcess(OrderProcessorContext orderProcessorContext) {
        if (iterator == null) {
            iterator = processors.iterator();
        }
        //调用订单处理链的下一个节点
        if (iterator.hasNext()) {
            OrderProcessor next = iterator.next();
            next.doProcess(orderProcessorContext, this);
        }
    }

    @Override
    public void addProcessor(OrderProcessor orderProcessor) {
        processors.add(orderProcessor);
    }


}
