package com.github.tjp.design.singleton;

/**
 * (推荐)最安全的单例方式:
 * <p/>
 * 内部静态枚举实现的【懒加载】【线程安全】的安全单例模式
 * 枚举保证:
 * (1)不能被反射
 * (2)私有的构造函数,只会被初始化一次
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/19 下午10:25
 */
public class EnumSingleton {

    //构造函数私有化,不放外界实例化
    private EnumSingleton() {

    }

    public static Singleton getInstance() {
        //第一次调用枚举类,会初始化所有枚举实例,线程安全,实现懒加载
        return SingletonEnum.INSTANCE.getSingleton();
    }

    //枚举
    private static enum SingletonEnum {
        INSTANCE;

        private Singleton singleton;

        //所有的枚举实例都是靠static静态代码块初始化,所以这里实例的构造函数,只会初始化一次,并且线程安全
        private SingletonEnum() {
            singleton = new Singleton();
        }

        public Singleton getSingleton() {
            return singleton;
        }

        public void setSingleton(Singleton singleton) {
            this.singleton = singleton;
        }
    }

    public static void main(String[] args) {
        Singleton obj1 = EnumSingleton.getInstance();
        Singleton obj2 = EnumSingleton.getInstance();
        //输出结果：obj1==obj2?true
        System.out.println("obj1==obj2?" + (obj1 == obj2));
    }
}
