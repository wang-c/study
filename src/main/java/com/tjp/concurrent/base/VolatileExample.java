package com.tjp.concurrent.base;

/**
 *
 * volatile轻量的 不加锁的方式实现线程间的可见性
 *
 * 内存语义:
 *  (1)直接让线程本地本地缓存中所有的共享变量刷新回主存
 *  (2)保证线程对它的写,后续所有线程对他的读立马可见
 *  (3)volatile不允许重排序
 * Created by tujinpeng on 2017/10/23.
 */
public class VolatileExample {

    //用volatile变量flag 不加锁的方式实现多个线程间对普通共享变量a的可见性
    private int a = 0;

    //代表a是否被修改过,true-修改过 false-未修改
    private volatile boolean flag = false;


    public void write() {
        //步骤1.
        a = 1;
        //步骤2.对volatile变量flag写,会将a,flag的本地缓存刷会主存
        flag = true;
    }

    public void read() {
        //步骤3.上面第2步的写,对这里后续的读立马可见
        if (flag) {
            //步骤4.由于第2步将共享内存a变更的值也刷新回主要存了,所以这里a可见
            int i = a * a;
            System.out.println("a is modify : " + i);
        }
    }


    /*
     *
     * 注意:正常情况下步骤1和步骤2没有依赖关系,编译器会重排序,但是flag是vilatile变量,静止这种重排序
     * 所以:
     *    步骤1 happens before 步骤2
     *    步骤3 happens before 步骤4
     *
     * 根据volatile写,后续对它的读立马可见,所以:
     *    步骤2 happens before 步骤3
     *
     * 最终根据happens before传递性:
     *    步骤1 happens before 步骤4
     *
     */

}
