package com.github.tjp.algorithm.sort;

/**
 * 归并排序
 * <p/>
 * -时间复杂度
 * 最好:n*logn
 * 最坏:n*logn
 * 平均:n*logn
 * <p/>
 * 空间复杂度:
 * <p/>
 * 2n
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/25 下午5:40
 */
public class MergeSort {

    //要将两个有序数组合并(a[0.....mid]和a[mid+1.......end])
    private static void merge(int[] arr, int start, int mid, int end, int tmp[]) {
        int i = start;//左边数组起始索引
        int j = mid + 1;//右边数组起始索引
        int k = 0;//合并后数组索引
        //遍历要合并的两个有序数组
        while (i <= mid && j <= end) {
            if (arr[i] < arr[j]) {
                tmp[k++] = arr[i++];
            } else {
                tmp[k++] = arr[j++];
            }
        }

        //a[0.....mid]数组有剩余,全部加入到新数组tmp中
        while (i <= mid) {
            tmp[k++] = arr[i++];
        }
        //a[mid+1.......end]有剩余,全部加入到新数组tmp中
        while (j <= end) {
            tmp[k++] = arr[j++];
        }

        //此时合并后的tmp数组是有序的了,将数据重新copy到arr中
        for (int q = 0; q < k; q++) {
            arr[start + q] = tmp[q];
        }
    }

    public static void mergeSort(int[] arr, int start, int end, int[] tmp) {
        if (start == end) {//递归结束的条件,划分的子序列只有一个元素
            return;
        }
        //从中间不断划分子序列{start.....mid,mid+1....end}
        int mid = (start + end) / 2;
        mergeSort(arr, start, mid, tmp);
        mergeSort(arr, mid + 1, end, tmp);
        //合并划分的两个子序列
        merge(arr, start, mid, end, tmp);
    }

    public static void main(String[] args) {
        int arr[] = {49, 38, 65, 97, 76, 13, 27, 49, 78, 34, 12, 64, 5, 4, 62, 99, 98, 54, 56, 17, 18, 23, 34, 15, 35, 25, 53, 51};
        int tmp[] = new int[arr.length];
        mergeSort(arr, 0, arr.length - 1, tmp);
        for (Integer e : arr) {
            System.out.println(e);
        }

    }

}
