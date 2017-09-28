package com.tjp.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * 当一个对象只有弱引用,没有其他任何的强引用后,gc发现后会立即回收,同时会通知到引用队列
 * Created by tujinpeng on 2017/9/28.
 */
public class WeakReferenceTest {

    public static void main(String[] args) throws InterruptedException {
        //创建引用队列(实际上是个栈???) 用来接收只有弱引用的对象被gc回收的通知
        ReferenceQueue<String> referenceQueue = new ReferenceQueue<String>();
        //创建一个string对象的强引用
        String str = new String("hello");
        //创建一个string对象的弱引用
        WeakReference weakReference = new WeakReference(str, referenceQueue);


        //此时string对象拥有一个强引用和一个弱引用 gc不会回收string对象
        System.gc();
        System.out.println(weakReference.get());
        System.out.println(referenceQueue.poll());

        //设置string对象强引用为空,此时对象只存在弱引用,gc会回收对象并且将弱引用加入到referenceQueue队列中
        str = null;
        System.gc();
        System.out.println(weakReference.get());
        System.out.println(referenceQueue.poll());

        Thread.sleep(10000);
    }

}
