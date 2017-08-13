package com.tjp.design.chain;

/**
 * Created by tujinpeng on 2017/5/21.
 */
public abstract class AbstractHandler implements Handler {
    protected AbstractHandler prev;
    protected AbstractHandler next;
}
