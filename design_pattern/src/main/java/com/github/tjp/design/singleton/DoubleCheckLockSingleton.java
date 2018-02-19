package com.github.tjp.design.singleton;

/**
 * 双重检查锁 线程安全 懒加载的单例模式
 *
 * 缺点:
 *   jdk1.5之后这种方式才能保证线程安全(volatile禁止重排序是1.5后的新增功能)
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/19 下午11:22
 */
public class DoubleCheckLockSingleton {

    /**
     * 这里申明单例为volatile:
     * 防止重排序,导致高并发下singleton引用不为空时,读到未初始化完毕的对象引用
     */
    private static volatile Singleton singleton = null;

    //私有构造方法 禁止外界访问 破坏单例模式
    private DoubleCheckLockSingleton() {

    }

    public static Singleton getInstance() {
        if (singleton == null) {//加快并发效率
            //控制并发访问 只初始化一次单例
            synchronized (DoubleCheckLockSingleton.class) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }

    public static void main(String[] args) {
        Singleton obj1 = DoubleCheckLockSingleton.getInstance();
        Singleton obj2 = DoubleCheckLockSingleton.getInstance();
        //输出结果：obj1==obj2?true
        System.out.println("obj1==obj2?" + (obj1 == obj2));
    }
}
