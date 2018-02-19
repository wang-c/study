package com.github.tjp.design.singleton;

/**
 * <p/>
 * (推荐)静态内部类实现的【懒加载】【线程安全】的安全单例模式
 * <p/>
 * 静态内部类:
 *   内部类第一次调用的时候,单例初始化 懒加载
 *
 * 缺点:
 *   反射下不安全
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/19 下午10:25
 */
public class StaticInnerClassSingleton {

    //构造函数私有化,不放外界实例化
    private StaticInnerClassSingleton() {

    }

    public static Singleton getInstance() {
        //第一次调用内部类,会初始化单例对象singleton
        return InnerClass.singleton;
    }

    //枚举
    private static class InnerClass {
        private static Singleton singleton = new Singleton();

    }

    public static void main(String[] args) {
        Singleton obj1 = StaticInnerClassSingleton.getInstance();
        Singleton obj2 = StaticInnerClassSingleton.getInstance();
        //输出结果：obj1==obj2?true
        System.out.println("obj1==obj2?" + (obj1 == obj2));
    }
}
