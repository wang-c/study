package com.github.tjp.design.chain.arr;

/**
 * 创建订单的chain工厂
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/9 上午12:37
 */
public class CreateOrderProcessorChainFactory implements OrderProcessorChainFactory {

    @Override
    public OrderProcessorChain bulidOrderProcessorChain() {
        OrderProcessorChain chain = new OrderProcessorChainImpl();
        chain.addProcessor(new OrderCheckProcessor());
        chain.addProcessor(new OrderCreateProcessor());
        chain.addProcessor(new OrderSyncProcessor());
        return chain;
    }
}
