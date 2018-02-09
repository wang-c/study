package com.github.tjp.chain.arr;

import com.github.tjp.chain.dto.CreateOrderRequest;
import com.github.tjp.chain.dto.CreateOrderResponse;
import com.github.tjp.chain.dto.OrderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 订单创建完成后的数据同步处理器
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/8 下午11:56
 */
public class OrderSyncProcessor implements OrderProcessor<CreateOrderRequest, CreateOrderResponse> {

    private static final Logger logger = LoggerFactory.getLogger(OrderSyncProcessor.class);

    @Override
    public void doProcessor(OrderContext<CreateOrderRequest, CreateOrderResponse> orderContext, OrderProcessorChain chain) {
        logger.info("sync order process ...");

        sync(orderContext);
        //调用下一个处理器
        chain.doProcessor(orderContext);
    }

    private void sync(OrderContext<CreateOrderRequest, CreateOrderResponse> orderContext) {
        //这里同步订单创建的信息F
    }

}
