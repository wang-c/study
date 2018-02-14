package com.github.tjp.design.chain.arr;

import com.github.tjp.design.chain.dto.OrderContext;

/**
 * 订单流程处理链
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/8 下午7:47
 */
public interface OrderProcessorChain {

    /**
     * 调用处理链的下一个
     *
     * @param orderContext
     */
    void doProcessor(OrderContext orderContext);

    /**
     * 向订单调用链里添加处理节点
     *
     * @param orderProcessor
     */
    void addProcessor(OrderProcessor orderProcessor);

}
