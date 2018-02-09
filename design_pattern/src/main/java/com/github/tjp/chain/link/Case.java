package com.github.tjp.chain.link;

import com.github.tjp.chain.dto.OrderContext;
import com.github.tjp.chain.dto.CreateOrderRequest;
import com.github.tjp.chain.dto.CreateOrderResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/9 下午2:42
 */
public class Case {

    public static void main(String[] args) {
        //初始化订单处理的上下文
        OrderContext<CreateOrderRequest, CreateOrderResponse> context = new OrderContext<CreateOrderRequest, CreateOrderResponse>();
        context.setOrderRequest(new CreateOrderRequest());
        context.setOrderResponse(new CreateOrderResponse());

        //构建链表形式的下单处理链
        OrderInvoker orderChain = bulidOrderProcessorChain();

        //发起下单流程
        orderChain.invoke(context);
    }

    private static OrderInvoker bulidOrderProcessorChain() {
        /*
         * 获取处理器集合:
         * (1)后续这里可以仿造dubbo的ExtentionLoader,自动发现这些processors
         * (2)或者依赖spring工厂加载出OrderProcessor类型的所有实现
         */
        List<OrderProcessor> orderProcessors = new ArrayList<>();
        orderProcessors.add(new OrderCheckProcessor());
        orderProcessors.add(new OrderCreateProcessor());
        orderProcessors.add(new OrderSyncProcessor());

        //采用匿名类构建下单调用链表
        OrderInvoker last = new OrderInvoker() {
            @Override
            public void invoke(OrderContext orderContext) {
            }
        };
        if (orderProcessors.size() > 0) {
            for (int i = orderProcessors.size() - 1; i >= 0; i--) {
                final OrderProcessor processor = orderProcessors.get(i);
                final OrderInvoker next = last;
                //匿名类
                last = new OrderInvoker() {
                    @Override
                    public void invoke(OrderContext orderContext) {
                        processor.doProcessor(next, orderContext);
                    }
                };

            }
        }
        return last;
    }
}
