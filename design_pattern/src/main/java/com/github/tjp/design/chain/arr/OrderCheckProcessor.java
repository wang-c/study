package com.github.tjp.design.chain.arr;

import com.github.tjp.design.chain.dto.CreateOrderInfo;
import com.github.tjp.design.chain.dto.CreateOrderRequest;
import com.github.tjp.design.chain.dto.CreateOrderResponse;
import com.github.tjp.design.chain.dto.OrderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 校验订单信息
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/8 下午11:56
 */
public class OrderCheckProcessor implements OrderProcessor<CreateOrderRequest, CreateOrderResponse> {

    private static final Logger logger = LoggerFactory.getLogger(OrderCheckProcessor.class);

    @Override
    public void doProcessor(OrderContext<CreateOrderRequest, CreateOrderResponse> orderContext, OrderProcessorChain chain) {
        logger.info("check order process ...");
        CreateOrderRequest orderRequest = orderContext.getOrderRequest();
        CreateOrderInfo.OrderDistVo booker = orderRequest.getBooker();
        //校验下单人信息
        if (booker == null) {
            throw new IllegalStateException("booker must be not null");
        }
        //订单信息校验通过,执行下一个处理器
        chain.doProcessor(orderContext);
    }
}
