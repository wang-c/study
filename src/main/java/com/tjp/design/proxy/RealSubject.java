package com.tjp.design.proxy;

/**
 * 委托类，实际执行业务的对象
 */
public class RealSubject  implements Subject{
    public void dosomething() {
        System.out.println("do something");
    }
}
