package com.tjp.design.decorator;

/**
 * 咖啡类 被装饰者
 * Created by tujinpeng on 2017/5/14.
 */
public class DarkRoast implements Beverage {
    public double cost() {
        return 5;
    }

    public String getDesc() {
        return "darkRoast";
    }
}
