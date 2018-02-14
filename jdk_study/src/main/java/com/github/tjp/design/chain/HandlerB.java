package com.github.tjp.design.chain;

/**
 * Created by tujinpeng on 2017/5/21.
 */
public class HandlerB extends AbstractHandler {

    public void handleRequest() {
        System.out.println("handle B");
        super.next.handleRequest();
    }
}
