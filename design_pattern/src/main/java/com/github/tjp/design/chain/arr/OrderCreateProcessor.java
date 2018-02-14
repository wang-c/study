package com.github.tjp.design.chain.arr;

import com.github.tjp.design.chain.dto.CreateOrderRequest;
import com.github.tjp.design.chain.dto.CreateOrderResponse;
import com.github.tjp.design.chain.dto.OrderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 创建订单信息处理器
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/8 下午11:56
 */
public class OrderCreateProcessor implements OrderProcessor<CreateOrderRequest, CreateOrderResponse> {

    private static final Logger logger = LoggerFactory.getLogger(OrderCreateProcessor.class);

    @Override
    public void doProcessor(OrderContext<CreateOrderRequest, CreateOrderResponse> orderContext, OrderProcessorChain chain) {
        logger.info("create order process ...");

        Long orderId = createOrder(orderContext.getOrderRequest());
        if (orderId == null) {
            throw new IllegalStateException("创建订单失败");
        }
        //创建订单成功,设置响应对象
        orderContext.getOrderResponse().setOrderId(orderId);
        //调用下一个处理器
        chain.doProcessor(orderContext);
    }

    private Long createOrder(CreateOrderRequest orderRequest) {
        return 111l;
    }
}
