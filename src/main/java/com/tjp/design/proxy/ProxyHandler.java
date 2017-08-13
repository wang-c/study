package com.tjp.design.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理对象关联的调用处理器
 * Created by tujinpeng on 2017/3/14.
 */
public class ProxyHandler implements InvocationHandler {
    private Object target;

    public ProxyHandler(Object target) {
        super();
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("---------------before-------------");
        Object obj = method.invoke(target, args);
        System.out.println("---------------end----------------");
        return obj;
    }
}
