package com.github.tjp.jvm.gc;

/**
 * Created by TJP on 2017/10/22.
 */
public class ReferenceCountGC {

    private Object instance = null;

    private static final int _1MB = 1024 * 1024;

    //占点内存 方便GC日志中看清楚是否被回收过
    private byte[] bigSize = new byte[2 * _1MB];

    public static void main(String[] args) {
        ReferenceCountGC objA = new ReferenceCountGC();
        ReferenceCountGC objB = new ReferenceCountGC();

        objA.instance = objB;
        objB.instance = objA;

        //销毁objA和objB引用
        objA = null;
        objB = null;

        /**
         * 此时objA和objB被相互引用着，还是会被gc回收 说明jvm不是靠引用计数法来判断对象是否存活
         */
        System.gc();
    }
}
