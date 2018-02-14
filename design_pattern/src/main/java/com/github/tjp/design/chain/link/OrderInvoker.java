package com.github.tjp.design.chain.link;

import com.github.tjp.design.chain.dto.OrderContext;

public interface OrderInvoker {

    void invoke(OrderContext orderContext);

}
