package com.github.tjp.chain;

/**
 * 订单处理链上的节点定义
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/8 下午7:50
 */
public interface OrderProcessor<REQ extends OrderRequest, RESP extends OrderResponse> {

    /**
     * 订单节点处理流程
     *
     * @param orderContext 订单节点处理的上下文
     * @param chain        调用链(传入节点,看是否流程需要向后进行)
     */
    void doProcess(OrderProcessorContext<REQ, RESP> orderContext, OrderProcessorChain chain);
}
