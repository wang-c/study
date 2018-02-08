package com.tjp.concurrent.base;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <pre>
 * Lock:
 *  (1)加锁让线程互斥地进入同步块
 *  (2)对一个锁的解锁,happens before于随后对这个锁的加锁
 *  (3)线程释放锁时,会将所有的共享变量的本地缓存写会到主内存中
 *  (4)线程获取锁时,会将所有的共享变量的本地缓存设置成无效,强制让其读主内存
 *  (5)重量级的锁,对volatile变量的增强,保证共享变量的可见性 原子性(包括volatile未保证的复合操作的原子性)
 *
 * Created by tujinpeng on 2017/10/23.
 * <pre/>
 */
public class LockExample {

    //加锁的方式实现多个线程间对普通共享变量a的可见性
    private int a = 0;

    private ReentrantLock lock = new ReentrantLock();


    //写线程
    public void write() {
        //步骤1 竞争获取锁
        lock.lock();
        try {
            //步骤2 执行共享变量的复合操作
            a++;
        } finally {
            //步骤3 释放锁,同时将所有共享变量刷新回主存中
            lock.unlock();
        }
    }

    //读线程
    public void read() {
        //步骤4 获取锁 jmm会将线程中共享变量的本地缓存设置成无效
        lock.lock();
        try {
            //步骤5 执行复合操作 a的本地缓存失效了 要从主内存中获取最新的a值,因此能获取到上面写线程的修改
            int i = a * a;
            System.out.println("a is modify : " + i);
        } finally {
            //步骤6 释放锁
            lock.unlock();
        }
    }

    /*
     * 分析------
     *    步骤2 happens before 步骤3
     *    步骤4 happens before 步骤5
     *
     * 根据锁的happens before原则 对一个锁的解锁,happens before于随后对这个锁的加锁
     *    步骤3 happens before 步骤4
     *
     * 最终根据happens before传递性:
     *    步骤2 happens before 步骤5
     *
     */

}
