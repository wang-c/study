package com.github.tjp.design.chain.arr;

import com.github.tjp.design.chain.dto.CreateOrderRequest;
import com.github.tjp.design.chain.dto.CreateOrderResponse;
import com.github.tjp.design.chain.dto.OrderContext;

/**
 * 下单服务的责任链产生:
 * (1)通过OrderProcessorChainFactory,为每次调用生成相应的处理链chain
 * (2)通过继承OrderProcessorContext的泛型,组装下单前的参数
 * (3)通过调用chain.doProcessor,实现下单流程的调用链扭转
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/9 上午12:46
 */
public class Case {

    //模拟订单服务的创建
    public static void main(String[] args) {
        //调用chain工厂 构建创建订单处理链
        OrderProcessorChainFactory chainFactory = new CreateOrderProcessorChainFactory();
        OrderProcessorChain createOrderChain = chainFactory.bulidOrderProcessorChain();

        //初始化订单处理的上下文
        OrderContext<CreateOrderRequest, CreateOrderResponse> context = new OrderContext<CreateOrderRequest, CreateOrderResponse>();
        context.setOrderRequest(new CreateOrderRequest());
        context.setOrderResponse(new CreateOrderResponse());

        //开始进行订单流程的处理调用
        createOrderChain.doProcessor(context);


    }
}
