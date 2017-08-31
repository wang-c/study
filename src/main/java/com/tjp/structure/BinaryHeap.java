package com.tjp.structure;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 二叉堆(应用优先级队列 see PriorityQueue)
 * Created by tujinpeng on 2017/8/30.
 */
public class BinaryHeap<E extends Comparable> implements Serializable {

    //存放节点数组
    private E[] value;

    //大小
    private int size = 0;

    public BinaryHeap(E[] value) {
        this.value = value;
        this.size = value.length;
        //创建堆
        createHeap(value);

    }

    /**
     * 创建堆
     * 从最后一个非叶子节点向前。向下堆化
     *
     * @param value
     */
    private void createHeap(E[] value) {
        if (size == 0) {
            return;
        }
        for (int i = lastNoLeaf(); i >= 0; i--) {
            siftDown(i, value[i]);
        }
    }

    public E[] getValue() {
        return value;
    }

    /**
     * 位置k破坏了最大(小)堆结构,需要从k位置向下递归堆化
     *
     * @param k 开始堆化的位置 不平衡点
     * @param x 当前值
     */
    private void siftDown(int k, E x) {
        //在非页节点之间循环loop
        while (k <= lastNoLeaf()) {
            //假定左孩子最小 找出child中最小的值
            int least = lchild(k);//child中最小下标
            E c = value[least];//child中最小值
            int right = least + 1;
            if (right < size && value[right].compareTo(c) < 0) {
                c = value[least = right];
            }
            if (x.compareTo(c) <= 0)//不平衡点的值最小 不需要和child节点交换 向下堆化结束
                break;
            //child中较小值与父级值交换
            value[k] = c;
            k = least;//child中最小下标位置不平衡(不平衡点k向下转移)
        }
        value[k] = x;//找到下沉的x正确位置 赋值
    }

    /**
     * 从k位置向上,递归堆化
     *
     * @param k 开始堆化的位置 不平衡点
     * @param x 当前值
     */
    private void siftUp(int k, E x) {
        while (k > 0) {
            //父级下标
            int parent = parent(k);
            E e = value[parent];
            //若父节点值较小 堆化结束
            if (e.compareTo(x) < 0)
                return;
            //父节点值较大，与x交换
            value[parent] = x;
            //不平衡点k向上转移
            k = parent;
        }
        value[k] = x;
    }

    /**
     * i下标的父节点
     *
     * @param i
     * @return
     */
    private int parent(int i) {
        return (i - 1) >>> 1;
    }

    /**
     * i下标的左孩子
     *
     * @param i
     * @return
     */
    private int lchild(int i) {
        return (i << 1) + 1;
    }

    /**
     * i下标的右孩子
     *
     * @param i
     * @return
     */
    private int rchild(int i) {
        return (i << 1) + 2;
    }

    /**
     * 最后一个非叶子节点
     *
     * @return
     */
    private int lastNoLeaf() {
        //注意加减优先级高于位运算（这里位运算要加括号）
        return (size >>> 1) - 1;
    }


    public static void main(String[] args) {
        Integer[] arr = new Integer[]{10, 8, 2, 11, 12, 7, 8};
        //构建二叉堆
        BinaryHeap<Integer> binaryHeap = new BinaryHeap(arr);

        for (Integer e : binaryHeap.getValue()) {
            System.out.println(e);
        }

        System.out.println((4 >>> 1) - 1);
        System.out.println(4 >>> 1 - 1);


    }


}
