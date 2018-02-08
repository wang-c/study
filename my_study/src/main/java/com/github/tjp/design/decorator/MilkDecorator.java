package com.github.tjp.design.decorator;

/**
 * 牛奶调料装饰器 扩展被装饰者的功能
 * Created by tujinpeng on 2017/5/14.
 */
public class MilkDecorator extends CondimentDecorator {

    public MilkDecorator(Beverage beverage) {
        super(beverage);
    }

    public double cost() {
        //在被装饰者的价格基础上加价0.99
        return 0.99 + this.beverage.cost();
    }

    public String getDesc() {
        return this.beverage.getDesc() + "+milk";
    }
}
