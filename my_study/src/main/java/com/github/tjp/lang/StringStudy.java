package com.github.tjp.lang;

/**
 * Created by TJP on 2017/10/21.
 */
public class StringStudy {

    private static String PERFIX = "PRODUCT_";

    private static int count = 0;


    public static void main(String[] args) {
        /*
         * 字符串赋值方式1：赋值的是静态字符串时(编译期可以确定的)，直接进入常量池；若常量池中存在此字符串，直接返回对应的引用
         */
        String str1 = "abc";
        String str2 = "a" + "b" + "c";
        String str3 = "a" + "bc";
        System.out.println(str1 == str2 && str1 == str3);//--true


        String str4 = "bc";
        /*
         *2.字符串赋值方式2：赋值时包含有变量的（编译器没法确定的），不会进入常量池，实例在java堆上
         */
        String str5 = "a" + str4;
        System.out.println(str1 == str5);//--false

        /*
         * 字符串赋值方式3：new String对象这种赋值，不会进入常量池 实例分配在java堆上
         */
        String newString = new String("abc");
        System.out.println(str1 == newString);//--false,常量池中引用str1和newString不是同一个内存地址


        /*
         * String.intern方法：
         *  到堆中字符串常量池中寻找等于该字符串对象，若有则返回常量池中引用，若没有将此字符串对象加入到常量池中
         */
        String newStr = new String("abc");
        System.out.println(newStr.intern() == str1);//--true

        /*
         * stringBulider.toString创建的实例在java堆上,不在字符串常量池中
         */
        String javaStr = new StringBuilder().append("ja").append("va").toString();
        System.out.println("stringBulider.toString == string.intern result : " + (javaStr.intern() == javaStr));

        /*
         *当用动态字符串当lock对象时,相同的字符串对象调用intern方法作为lock对象
         */
        Long productId = 1111l;
        //下面字符串赋值时带有变量productId，都不是从常量池中获取引用，所以两者的引用不相等
        String lock1 = PERFIX + productId;
        String lock2 = PERFIX + productId;
        System.out.println(lock1 == lock2);//--false
        //lock1和lock2内存地址不同，但是字符串相等，调用intern时，返回常量池中字符串的相同引用
        System.out.println(lock1.intern() == lock2.intern());//--true


        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    addUnSafe(1111L);
//                    addSafe(1111L);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "synchronized-string-thread" + i);
            t.start();
        }
    }

    private static void addUnSafe(Long productId) {
        //相同productId的string对象，由于包含有变量所以new lock的时候每次都创建对象new String(PERFIX + productId)
        //所以这里lock对象锁不住线程
        String lock = PERFIX + productId;
        synchronized (lock) {
            count++;
        }
        System.out.println(count);
    }

    private static void addSafe(Long productId) {
        String lock = PERFIX + productId;
        //包含有相同值的不同对象lock，调用intern方法，访问堆中线程共享的字符串常量池，若两对象字符串相等，在常量池中返回同一个对象引用
        synchronized (lock.intern()) {
            count++;
            System.out.println(count);
        }

    }
}
