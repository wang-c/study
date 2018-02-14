package com.github.tjp.design.chain.link;

import com.github.tjp.design.chain.dto.OrderContext;
import com.github.tjp.design.chain.dto.CreateOrderRequest;
import com.github.tjp.design.chain.dto.CreateOrderResponse;
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

    private Long createOrder(OrderContext orderRequest) {
        return 111l;
    }

    @Override
    public void doProcessor(OrderInvoker next, OrderContext<CreateOrderRequest, CreateOrderResponse> context) {
        logger.info("create order process ...");

        Long orderId = createOrder(context);
        if (orderId == null) {
            throw new IllegalStateException("创建订单失败");
        }
        context.getOrderResponse().setOrderId(orderId);

        //调用下一个节点
        next.invoke(context);
    }
}
