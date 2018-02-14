package com.github.tjp.framework.spring.aop;

/**
 * Created by IntelliJ IDEA.
 * User: tujinpeng
 * Date: 2017/3/15
 * Time: 15:22
 * Email:tujinpeng@lvmama.com
 */
public class LogAspect {
    public void before() {
        System.out.println("------------start--------");
    }

    public void after() {
        System.out.println("------------after--------");
    }

}
