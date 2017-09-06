package com.tjp.algorithms.sort;

/**
 * 基于交换的排序算法{
 * 冒泡排序
 * 快速排序
 * }
 * <p/>
 * Created by tujinpeng on 2017/9/6.
 */
public class SwapSort {

    /**
     * 分区:
     * 将第一个元素选为pivot基准点
     * 将区域内的元素,分成<pivot,pivot,>pivot的3部分
     *
     * @param arr
     * @param start
     * @param end
     */
    private static int partition(int arr[], int start, int end) {
        //1.选取基准为第一个元素
        int pivot = arr[start];
        int i = start;//<key区域尾端下标
        //2.将数组分为<key,>key两部分
        for (int j = start + 1; j <= end; j++) {
            if (arr[j] < pivot) {
                //<key区域尾端添加元素,尾端下标向右移动一位
                i++;
                //将新元素放到<key区域尾端
                exchange(arr, i, j);
            }
        }
        //3.将key主元位置start和<key区域尾部元素位置交换，最终形成<key,key,>key的3区完成分割
        exchange(arr, start, i);

        return i;//返回分割点下标
    }

    //交换数组元素
    private static void exchange(int arr[], int position1, int position2) {
        if (position1 != position2) {
            int tmp = arr[position2];
            arr[position2] = arr[position1];
            arr[position1] = tmp;
        }
    }

    /**
     * 快速排序
     * <p/>
     * 排序思想:
     * (1)选择基准：在待排序列中，按照某种方式挑出一个元素，作为 “基准”（pivot）
     * (2)分割操作：以该基准在序列中的实际位置，把序列分成两个子序列。此时，将区域内的元素,分成<pivot,pivot,>pivot的3部分
     * (3)分治思想: 递归地对分割的两个子序列进行快速排序，直到序列为空或者只有一个元素。
     * <p/>
     * 时间复杂度:
     * 最好:O(n*logn)
     * 最坏:O(n2) 当数组是排序好的 需要进行n次分割
     * 平均:O(n*logn)
     * <p/>
     * 空间复杂度:O(1)
     * <p/>
     * 稳定性:不稳定
     *
     * @param arr
     * @param start
     * @param end
     */
    public static void quickSort(int arr[], int start, int end) {
        if (start < end) {
            int part = partition(arr, start, end);
            //递归快排基准点两边的子序列
            quickSort(arr, start, part - 1);
            quickSort(arr, part + 1, end);
        }
    }

    /**
     * 冒泡排序:
     * <p/>
     * 思想:
     * 每次从第一个元素开始,比较当前元素和后面一个元素大小,若当前元素大则交换,。。。。以此类推,每一趟冒泡排序,会把最大的元素交换到数组最后一个去。
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
    public static void bubbleSort(int[] arr) {
        if (arr.length == 0) {
            return;
        }
        //冒泡需要(length - 1)趟
        for (int i = 1; i <= arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i; j++) {
                //每一趟冒泡排序 把大的数往后移动
                if (arr[j] > arr[j + 1])
                    exchange(arr, j, j + 1);
            }
        }
    }

    public static void main(String[] args) {
        int arr[] = {49, 38, 65, 97, 76, 13, 27, 49, 78, 34, 12, 64, 5, 4, 62, 99, 98, 54, 56, 17, 18, 23, 34, 15, 35, 25, 53, 51};
        long start = System.currentTimeMillis();
        quickSort(arr, 0, arr.length - 1);
        System.out.println("qsort time : " + (System.currentTimeMillis() - start));
//        bubbleSort(arr);
//        for (Integer e : arr) {
//            System.out.println(e);
//        }

    }


}
