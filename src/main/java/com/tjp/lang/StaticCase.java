package com.tjp.lang;

/**
 * 静态变脸和静态代码块的执行顺序:
 * <p/>
 * 根据编码的顺序,都是在类第一次被调用的时候初始化
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/8 上午9:43
 */
public class StaticCase {

    private static int num = 111;

    static {
        num = 222;
    }


    public static void main(String[] args) {
        System.out.println(StaticCase.num);//-222
    }
}
