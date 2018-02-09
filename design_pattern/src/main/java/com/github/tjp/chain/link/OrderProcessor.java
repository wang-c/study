package com.github.tjp.chain.link;

import com.github.tjp.chain.dto.OrderContext;
import com.github.tjp.chain.dto.OrderRequest;
import com.github.tjp.chain.dto.OrderResponse;

/**
 * 订单处理链上的每一个节点抽象
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/9 下午2:00
 */
public interface OrderProcessor<REQ extends OrderRequest, RESP extends OrderResponse> {
    /**
     * 每个节点持有下一个节点的引用,以及处理量共享的请求对象
     *
     * @param next
     * @param orderContext
     * @return
     */
    void doProcessor(OrderInvoker next, OrderContext<REQ, RESP> orderContext);
}
