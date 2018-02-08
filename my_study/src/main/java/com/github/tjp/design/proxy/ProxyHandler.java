package com.github.tjp.design.proxy;

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
        //hander增强的只是接口方法,不适用于object对象的增强
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }
        System.out.println("---------------before-------------");
        Object obj = method.invoke(target, args);
        System.out.println("---------------end----------------");
        return obj;
    }
}
