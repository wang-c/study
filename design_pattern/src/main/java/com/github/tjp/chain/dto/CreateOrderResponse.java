package com.github.tjp.chain.dto;

import com.github.tjp.chain.dto.OrderResponse;

/**
 * 创建订单响应对象
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/9 上午12:10
 */
public class CreateOrderResponse implements OrderResponse {

    /**
     * 创建订单成功生成的订单id
     */
    private Long orderId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
