package com.github.tjp.chain.biz;

import com.github.tjp.chain.OrderProcessorChain;
import com.github.tjp.chain.OrderProcessorChainFactory;
import com.github.tjp.chain.OrderProcessorChainImpl;

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
