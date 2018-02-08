package com.github.tjp.design.chain;

/**
 * Created by tujinpeng on 2017/5/21.
 */
public interface HandlerChain extends Handler {

    void addFirst(AbstractHandler newHandler);

    void addLast(AbstractHandler newHandler);
}
