package com.tjp.design.chain;

/**
 * Created by tujinpeng on 2017/5/21.
 */
public class HandlerA extends AbstractHandler {

    public void handleRequest() {
        System.out.println("handle -----------begin");

        System.out.println("handle A");
        super.next.handleRequest();

        System.out.println("handle -----------end");

    }
}
