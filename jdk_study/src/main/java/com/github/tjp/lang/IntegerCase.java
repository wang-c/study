package com.github.tjp.lang;

/**
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/17 下午7:31
 */
public class IntegerCase {

    public static void main(String[] args) {
        //Integer最大值为(2的31次方-1)
        System.out.println("Integer max value : " + Integer.MAX_VALUE);
        //integer最大值+1,符号位变为1,变负数
        System.out.println("Integer.MAX_VALUE + 1 = " + (Integer.MAX_VALUE + 1));
        System.out.println("Integer.MAX_VALUE + 2 = " + (Integer.MAX_VALUE + 2));

        //基本数据类型赋值的,==比较的是两者的内容
        int a = 10, b = 10;
        System.out.println(a == b);

        //基本数据类型和包装对象的==,jdk会让包装对象自动拆箱,所以比较的还是两者内容
        System.out.println(a == new Integer(10));

        //包装对象和包装对象的==,比较的是对象的引用
        System.out.println(new Integer(10) == new Integer(10));
        /*
         * 包装对象和包装对象的equals,比较的是两者的内容值:
         *
         *  Integer类重写了object的hashcode()和equals()方法,equals方法只是比较两个对象的内容value
         */
        System.out.println(new Integer(10).equals(new Integer(10)));
    }
}
