package com.github.tjp.chain;

/**
 * 订单处理链上传递的上下文
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/8 下午7:54
 */
public class OrderProcessorContext<REQ extends OrderRequest, RESP extends OrderResponse> {

    /**
     * 下单请求对象
     */
    private REQ orderRequest;

    /**
     * 下单响应对象
     */
    private RESP orderResponse;

    public REQ getOrderRequest() {
        return orderRequest;
    }

    public void setOrderRequest(REQ orderRequest) {
        this.orderRequest = orderRequest;
    }

    public RESP getOrderResponse() {
        return orderResponse;
    }

    public void setOrderResponse(RESP orderResponse) {
        this.orderResponse = orderResponse;
    }
}
