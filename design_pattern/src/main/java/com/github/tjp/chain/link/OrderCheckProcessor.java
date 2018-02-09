package com.github.tjp.chain.link;

import com.github.tjp.chain.dto.OrderContext;
import com.github.tjp.chain.dto.CreateOrderInfo;
import com.github.tjp.chain.dto.CreateOrderRequest;
import com.github.tjp.chain.dto.CreateOrderResponse;
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
    public void doProcessor(OrderInvoker next, OrderContext<CreateOrderRequest, CreateOrderResponse> request) {
        logger.info("check order process ...");
        //校验下单人信息
        CreateOrderInfo.OrderDistVo booker = request.getOrderRequest().getBooker();
        if (booker == null) {
            throw new IllegalStateException("booker must be not null");
        }
        //调用下一个节点
        next.invoke(request);
    }
}
