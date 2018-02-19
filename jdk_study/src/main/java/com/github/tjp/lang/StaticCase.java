package com.github.tjp.lang;

/**
 * 静态变量和静态代码块:
 * <p/>
 * (1)都是在类第一次被调用的时候初始化
 * (2)执行的先后顺序:根据编码的顺序
 * (3)静态代码块初始化过程,保证线程安全并且保证只会被调用一次
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/8 上午9:43
 */
public class StaticCase {

    private static int num = 111;

    static {
        num = 222;

        //静态代码块初始化过程,由jvm保证线程安全,并且只会初始化一次,所以这里日志只会被打印一次
        System.out.println("static code area init start");
        try {
            //让静态代码块初始化时间长点
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        System.out.println(StaticCase.num);//-222

        Thread[] arr = new Thread[10];
        for (int i = 0; i < 10; i++) {
            arr[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    //多线程调用类
                    System.out.println(StaticCase.num);
                }
            });
        }

        //模拟并发
        for (int i = 0; i < 10; i++) {
            arr[i].start();

        }
    }
}
