package com.github.tjp.design.decorator;

/**
 * 调料抽象的装饰器
 * Created by tujinpeng on 2017/5/14.
 */
public abstract class CondimentDecorator implements Beverage {

    /**
     * 被装饰者的引用
     */
    public Beverage beverage;

    public CondimentDecorator(Beverage beverage) {
        this.beverage = beverage;
    }

}
