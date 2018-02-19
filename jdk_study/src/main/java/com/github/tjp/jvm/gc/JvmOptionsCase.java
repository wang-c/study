package com.github.tjp.jvm.gc;

/**
 * jvm参数学习
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/18 上午11:14
 */
public class JvmOptionsCase {

    /**
     * -Xms20M                    初始堆大小
     * -Xmx20M                    堆最大值
     * -XX:PermSize=50m           初始非堆大小(永久代、方法区大小)
     * -XX:MaxPermSize=50m        非堆最大值
     * -XX:NewRatio=8             堆中新生代:老年代 1:8
     * -XX:SurvivorRatio=8        堆中新生代中eden和survivor区大小默认8:1:1
     * -XX:+PrintGCDetails        打印gc日志
     *
     * @param args
     */
    public static void main(String[] args) {
        System.gc();
    }
}
