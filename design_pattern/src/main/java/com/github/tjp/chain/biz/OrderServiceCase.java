package com.github.tjp.chain.biz;

import com.github.tjp.chain.OrderProcessorChain;
import com.github.tjp.chain.OrderProcessorChainFactory;
import com.github.tjp.chain.OrderProcessorContext;

/**
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/9 上午12:46
 */
public class OrderServiceCase {

    //模拟订单服务的创建
    public static void main(String[] args) {
        //调用chain工厂 构建创建订单处理链
        OrderProcessorChainFactory chainFactory = new CreateOrderProcessorChainFactory();
        OrderProcessorChain createOrderChain = chainFactory.bulidOrderProcessorChain();

        //初始化订单处理的上下文
        OrderProcessorContext<CreateOrderRequest, CreateOrderResponse> context = new OrderProcessorContext<CreateOrderRequest, CreateOrderResponse>();
        context.setOrderRequest(new CreateOrderRequest());
        context.setOrderResponse(new CreateOrderResponse());

        //开始进行订单流程的处理调用
        createOrderChain.doProcess(context);


    }
}
