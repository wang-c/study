package com.tjp.design.chain;

/**
 * handler责任链:
 * 维护一个handler双向链表
 * Created by tujinpeng on 2017/5/21.
 */
public class DefaultHandlerChain implements HandlerChain {
    private AbstractHandler head;
    private AbstractHandler tail;

    public DefaultHandlerChain() {
        this.head = new HeadHandler();
        this.tail = new TailHandler();
        head.next = tail;
        tail.prev = head;
    }


    public void handleRequest() {
        head.handleRequest();
    }

    public void addFirst(AbstractHandler newHandler) {
        AbstractHandler next = head.next;
        newHandler.prev = head;
        newHandler.next = head.next;
        head.next = newHandler;
        next.prev = newHandler;

    }

    public void addLast(AbstractHandler newHandler) {
        AbstractHandler prev = tail.prev;
        newHandler.prev = this.tail.prev;
        newHandler.next = tail;
        prev.next = newHandler;
        tail.prev = newHandler;
    }

    static final class HeadHandler extends AbstractHandler {
        public void handleRequest() {
            System.out.println("head handle");
            super.next.handleRequest();
        }
    }

    static final class TailHandler extends AbstractHandler {
        public void handleRequest() {
            System.out.println("tail handle");
        }
    }


}
