package com.github.tjp.chain.link;

import com.github.tjp.chain.dto.OrderContext;

public interface OrderInvoker {

    void invoke(OrderContext orderContext);

}
