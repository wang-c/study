package com.tjp.structure.heap;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 二叉堆(应用优先级队列 see PriorityQueue):
 * (1)性质:
 * 近似二叉树的结构,父节点大于或者小于左右孩子,由数组存取从左到右一次填充
 * (2)形式:
 * 最大堆:所有非叶子节点大于左右孩子
 * 最小堆:所有非叶子节点大于左右孩子
 * (3)基本操作:
 * 建堆
 * 插入
 * 删除
 * 堆排序
 * Created by tujinpeng on 2017/8/30.
 */
public class BinaryHeap<E extends Comparable> implements Serializable {

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    //存放节点数组
    private E[] value;

    //元素大小
    private int size = 0;

    public BinaryHeap(E[] value) {
        //copy 数组
        this.value = Arrays.copyOf(value, value.length);
        this.size = value.length;
        //创建堆
        createHeap();

    }

    /**
     * 堆插入:直接插入到堆末尾 然后从末尾向上堆化
     *
     * @param e
     * @return
     */
    public boolean add(E e) {
        if (e == null)
            throw new NullPointerException();
        int oldSize = size;
        if (oldSize >= value.length) {
            //扩容 最小扩容+1
            grow(oldSize + 1);
        }
        size += 1;
        if (oldSize == 0) {
            //数组为空,直接插入头部
            value[0] = e;
        } else {
            //数组不为空,新增元素插入尾部,向上递归(迭代)堆化
            siftUp(size - 1, e);
        }
        return true;
    }

    /**
     * 删除节点e:
     *
     * @param e
     * @return
     */
    private boolean remove(Object e) {
        //找到删除元素的位置
        int i = indexOf(e);
        if (i == -1) {
            return false;
        } else {
            //删除i位置的节点
            removeAt(i);
            return true;
        }
    }

    /**
     * 删除i位置节点:
     * (1)用尾节点来填充删除节点
     * (2)从删除节点位置向下递归堆化
     * (3)若删除节点下标的值==尾节点值,还要递归向上堆化
     *
     * @param
     * @return
     */
    private void removeAt(int i) {
        int tail = --size;
        if (i == tail) {//若删除的是尾节点 直接删除尾部
            value[tail] = null;
        } else {
            E moved = value[tail];
            value[tail] = null;
            //从删除下表i,向下堆化
            siftDown(i, moved);
            if (value[i] == moved) {//若向下堆化后,删除的下标==moved(moved最小,可能会破坏向上的堆结构),则要向上堆化
                siftUp(i, moved);
            }
        }
    }

    private int indexOf(Object e) {
        if (e != null) {
            for (int i = 0; i < size; i++) {
                if (e.equals(value[i])) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * 扩容
     *
     * @param minCapacity
     */
    private void grow(int minCapacity) {
        int oldCapacity = value.length;
        int newCapacity = oldCapacity < 64 ? (oldCapacity + 2) : (oldCapacity << 1);
        //overflow
        if (newCapacity > MAX_ARRAY_SIZE) {
            if (minCapacity < 0) {
                throw new OutOfMemoryError();
            }
            newCapacity = minCapacity > MAX_ARRAY_SIZE ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
        }
        //copy to new array
        value = Arrays.copyOf(value, newCapacity);
    }


    /**
     * 创建堆
     * 从最后一个非叶子节点向前。向下堆化
     * <p/>
     * 时间复杂度:
     * O(n*logn)
     */
    private void createHeap() {
        if (size == 0) {
            return;
        }
        for (int i = lastNoLeaf(); i >= 0; i--) {
            //不断向下堆化
            siftDown(i, value[i]);
        }
    }

    /**
     * 堆排序:
     * 先建堆-》堆排
     * 时间复杂度:O(n*logn)
     */
    public Object[] heapSort(Object[] sort) {
        int i, j;
        for (i = size - 1, j = 0; i > 0; i--, j++) {
            //将头节点用尾节点替换
            E moved = value[i];
            //删除尾部,值记录到新数组中
            removeAt(i);
            sort[j] = value[0];//尾部值是原先头的值 最小的值
            //从头节点向下堆化
            siftDown(0, moved);
        }
        //堆中还剩最后一个元素,直接赋值
        sort[j] = value[0];
        return sort;
    }


    public E[] getValue() {
        return value;
    }

    public int getSize() {
        return size;
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
            E leastV = value[least];//child中最小值
            int right = least + 1;
            if (right < size && value[right].compareTo(leastV) < 0) {
                leastV = value[least = right];
            }
            if (x.compareTo(leastV) <= 0)//不平衡点的值最小 不需要和child节点交换 向下堆化结束
                break;
            //child中较小值与父级值交换
            value[k] = leastV;
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
            E parentV = value[parent];
            //若父节点值较小 堆化结束
            if (parentV.compareTo(x) < 0)
                return;
            //父节点值较大，父节点向下移动到k位置
            value[k] = parentV;
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
        Integer[] arr = new Integer[]{10, 8, 2, 11, 12, 7, 6};
        //构建二叉堆
        BinaryHeap<Integer> binaryHeap = new BinaryHeap(arr);
        //添加节点1
        binaryHeap.add(1);
        //删除元素
        binaryHeap.remove(6);

        binaryHeap.heapSort(arr);
        for (Object e : arr) {
            System.out.println(e);
        }

    }


}
