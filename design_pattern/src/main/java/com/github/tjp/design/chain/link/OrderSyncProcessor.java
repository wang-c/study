package com.github.tjp.design.chain.link;

import com.github.tjp.design.chain.dto.OrderContext;
import com.github.tjp.design.chain.dto.CreateOrderRequest;
import com.github.tjp.design.chain.dto.CreateOrderResponse;
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
    public void doProcessor(OrderInvoker next, OrderContext<CreateOrderRequest, CreateOrderResponse> context) {
        logger.info("sync order process ...");
        sync(context);
        //调用下一个处理器
        next.invoke(context);
    }

    private void sync(OrderContext context) {
        //做一些订单后续的同步操作
    }
}
