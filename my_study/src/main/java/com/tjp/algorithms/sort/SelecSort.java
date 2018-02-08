package com.tjp.algorithms.sort;

import com.tjp.structure.heap.BinaryHeap;

/**
 * 基于选择的排序算法{
 *      简单选择排序,
 *      堆排序。
 * }
 * <p/>
 * Created by tujinpeng on 2017/8/29.
 */
public class SelecSort {

    /**
     * 简单插入排序
     * <p/>
     * 思想:
     * 给定待排序序列A[ 1......n ] ，选择出第i小元素，并和A[i]交换，这就是一趟简单选择排序。
     * <p/>
     * 时间复杂度:
     * 最好:O(n2)
     * 最坏:O(n2)
     * 平均:O(n2)
     * <p/>
     * 空间复杂度:O(1)
     * <p/>
     * 稳定性:稳定
     *
     * @param arr
     */
    public static void simpleSelectSort(int arr[]) {
        if (arr.length == 0) {
            return;
        }
        int i, j, tmp, key;
        //进行arr.length-1趟选择，每次选出第i小记录
        for (i = 0; i < arr.length - 1; i++) {
            //i往后值最小下标 初始值为下标i
            key = i;
            for (j = i + 1; j < arr.length; j++) {
                //找寻i往后最小值下标
                if (arr[key] > arr[j]) {
                    key = j;
                }
            }

            //若最小值下标key不是i,则i,key相互交换
            if (key != i) {
                tmp = arr[i];
                arr[i] = arr[key];
                arr[key] = tmp;
            }
        }
    }

    /**
     * 简单选择排序递归实现
     * 由来:
     * 第一趟排序后：{ 1,5,8,9,3,2 } ，此时A[ 1 ]已经有序，第1个以后选出最小的
     * 第二趟排序后：{ 1,2,8,9,3,5 }，此时A[ 1...2 ]已经有序，第2个以后选出最小的
     * 第三趟排序后：{ 1,2,3,9,8,5 }，此时A[ 1...3 ]已经有序，第3个以后选出最小的
     * 第四趟排序后：{ 1,2,3,5,8,9 }，此时A[ 1...4 ]已经有序，第4个以后选出最小的
     * 第五趟排序后：{ 1,2,3,5,8,9 }，此时A[ 1...5 ]已经有序，第5个以后选出最小的
     * <p/>
     * 简单选择排序重复做一件事:找寻第个以后最小的,然后和头进行交换
     *
     * @param arr
     * @param n
     */
    public static void simpleSelectSort(int arr[], int n) {
        //到数组尾部结束递归
        if (arr.length - 1 == n) {
            return;
        }
        int i, tmp, key;
        key = n;
        //找寻数组下表n以后值最小的
        for (i = n + 1; i < arr.length; i++) {
            if (arr[key] > arr[i]) {
                key = i;
            }
        }
        //和头部进行交换
        if (key != n) {
            tmp = arr[n];
            arr[n] = arr[key];
            arr[key] = tmp;
        }
        simpleSelectSort(arr, n + 1);
    }

    /**
     * 堆排:
     * (1)先创建堆(堆根节点最小)
     * (2)不停的构建堆 选择最小的值出来(第1小,第2小,第3小。。。。)
     * <p/>
     *
     * 时间复杂度:
     * 最好:O(n*logn)
     * 最坏:O(n*logn)
     * 平均:O(n*logn)
     * <p/>
     * 空间复杂度:O(1)
     * <p/>
     * 稳定性:不稳定
     *
     * @param arr
     */
    public static void heapSelectSort(Integer arr[]) {
        BinaryHeap<Integer> heap = new BinaryHeap(arr);
        heap.heapSort(arr);
    }


    public static void main(String[] args) {
        Integer arr[] = {49, 38, 65, 97, 76, 13, 27, 49, 78, 34, 12, 64, 5, 4, 62, 99, 98, 54, 56, 17, 18, 23, 34, 15, 35, 25, 53, 51};
//        simpleSelectSort(arr);
//        simpleSelectSort(arr, 0);
        heapSelectSort(arr);
        for (Integer e : arr) {
            System.out.println(e);
        }

    }
}
