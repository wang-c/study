package com.tjp.structure;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 二叉堆(应用优先级队列 see PriorityQueue)
 * Created by tujinpeng on 2017/8/30.
 */
public class BinaryHeap<E extends Comparator> implements Serializable {

    //存放节点数组
    private Object[] value;

    //大小
    private int size = 0;

    /**
     * 位置k破坏了最大(小)堆结构,需要从k位置向下递归堆化
     *
     * @param k 开始堆化的位置
     * @param x 当前值
     */
    private void siftDown(int k, E x) {

    }

    /**
     * 从k位置向上,递归堆化
     *
     * @param k 开始堆化的位置
     * @param x 当前值
     */
    private void siftUp(int k, E x) {

    }

    /**
     * i下标的父节点
     *
     * @param i
     * @return
     */
    private int parent(int i) {
        return (i - 1) >>>1;
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
     * @param i
     * @return
     */
    private int lastNoLeaf(int i) {
        return size >>> 1;
    }


}
