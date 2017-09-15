package com.tjp.algorithms.sort;

/**
 * 基于分治思想的排序算法 归并排序
 * Created by tujinpeng on 2017/9/15.
 */
public class MergeSort {

    /**
     * 将{begin...mid},{mid+1....end}两个有序的数组合并成一个有序数组
     * 时间复杂度:o(n)
     * 空间复杂度:o()
     * 举例:{1,3,8,9},{2,4,6,7,11,17}
     *
     * @param a
     * @param begin
     * @param mid
     * @param end
     * @param tmp
     */
    private static void merge(int a[], int begin, int mid, int end, int[] tmp) {
        int i = begin, j = mid + 1;
        int k = 0;//合并后的新数组下标

        //对比合并操作
        while (i <= mid && j <= end) {//有一边数组遍历结束,循环就结束
            if (a[i] < a[j]) {
                tmp[k++] = a[i++];
            } else {
                tmp[k++] = a[j++];
            }
        }
        while (i <= mid) {//左边的{begin...mid}有剩余
            tmp[k++] = a[i++];
        }
        while (j <= end) {//右边的{mid+1....end}有剩余
            tmp[k++] = a[j++];
        }

        //将合并排序号的tmp数组copy会原数组a
        for (int q = 0; q < k; q++) {
            a[begin + q] = tmp[q];
        }

    }

    /**
     * <p/>
     * 归并排序思想,基于分治思想:
     * (1)递归划分子问题：从中间递归划分出两个子序列,直到划分出的子序列只有一个元素(有序)
     * (2)解决每一给子问题
     * (3)合并子问题:将连个有序的子序列合并
     * <p/>
     * 时间复杂度:
     * 最好:O(n*logn)
     * 最坏:O(n*logn)
     * 平均:O(n*logn)
     * <p/>
     * 空间复杂度:O(n)
     * <p/>
     * 稳定性:稳定
     *
     * @param a
     * @param begin
     * @param end
     * @param tmp
     */
    public static void mergeSort(int a[], int begin, int end, int tmp[]) {
        if (begin < end) {
            int mid = (begin + end) / 2;
            mergeSort(a, begin, mid, tmp);
            mergeSort(a, mid + 1, end, tmp);
            merge(a, begin, mid, end, tmp);
        }
    }

    public static void main(String[] args) {
        int arr[] = {49, 38, 65, 97, 76, 13, 27, 49, 78, 34, 12, 64, 5, 4, 62, 99, 98, 54, 56, 17, 18, 23, 34, 15, 35, 25, 53, 51};
        int tmp[] = new int[arr.length];
        long start = System.currentTimeMillis();
        mergeSort(arr, 0, arr.length - 1, tmp);
        System.out.println("qsort time : " + (System.currentTimeMillis() - start));
//        bubbleSort(arr);
        for (Integer e : arr) {
            System.out.println(e);
        }

    }


}
