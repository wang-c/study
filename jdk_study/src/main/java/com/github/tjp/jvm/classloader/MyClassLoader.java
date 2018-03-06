package com.github.tjp.jvm.classloader;

/**
 * <pre>
 * 类加载机制:
 * 加载-》校验-》准备-》解析-》初始化
 *
 * 类加载机制双亲委托机制:
 * 一个用户自定义的类加载器去加载一个class,若父级的类加载器不为空,则委托交给父级去加载这个class,
 * 若父级的加载器加载失败,则还是交由子级的加载器去加载这个class
 *
 *
 * 类加载器体系:
 *    Bootstrap CLassloder【启动类加载器】       Java_Home/lib
 *           |
 *    Extention ClassLoader【扩展的类加载器】    Java_Home /lib/ext
 *           |
 *    AppClassLoader【应用的类加载器】           classpath
 *           |
 *       /               \
 *用户自定义的classloader 用户自定义的classloader 用户自定义目录
 *
 *
 * </pre>
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/3/6 下午3:06
 */
public class MyClassLoader {

    public static void main(String[] args) {

        /*
         * 1.int,String交由【Bootstrap类加载器】加载,扫描【Java_Home/lib目录】
         */
        ClassLoader intClassLoader = int.class.getClassLoader();
        System.out.println(intClassLoader);
        ClassLoader stringClassLoader = String.class.getClassLoader();
        System.out.println(stringClassLoader);

        /*
         * 2.【ExtClassLoader扩展类加载器】扫描【Java_Home /lib/ext】目录下class
         */

        /*
         * 3.用户自定义的类交由【AppClassLoader应用程序类加载器】加载,【扫描classpath目录下class】,保证用户类在jvm中的唯一性
         */
        ClassLoader classLoader = MyClassLoader.class.getClassLoader();
        System.out.println(classLoader);
        ClassLoader classLoader1 = InnerClass.class.getClassLoader();
        System.out.println(classLoader1);


    }

    private static class InnerClass {

    }
}
