package com.github.tjp.algorithm.sort;

/**
 * 快速排序
 *
 * -时间复杂度
 * 最好:n*logn
 * 最坏:n2
 * 平均:n*logn
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/25 下午3:30
 */
public class QuickSort {

    /**
     * 分割
     * 以第一个元素为基准pivot,将数组分为小于pivot,pivot,大于pivot区域
     *
     * @param arr
     * @param start
     * @param end
     * @return
     */
    private static int getPartition(int arr[], int start, int end) {
        int pivot = arr[start];//选取第一个元素为基准
        int i = start;//<基准区域的尾元素
        int j = start + 1;//遍历索引,从基准后面一个元素遍历
        //1.遍历
        for (; j <= end; j++) {
            if (arr[j] < pivot) {
                //遍历到小于基准的元素
                //立即和【左面<pivot区域的尾部元素的下一个】作交换,加入到<pivot区
                exchange(arr, ++i, j);
            }
        }
        //2.交换基准和<pivot区的尾元素位置,最终形成小于pivot,pivot,大于pivot区域
        exchange(arr, start, i);
        //3.返回实际基准的位置,用于分割子序列
        return i;
    }

    private static void exchange(int[] arr, int position1, int position2) {
        //若要交换的位置是同一个,不需要交换
        if (position1 == position2) {
            return;
        }
        int tmp = arr[position1];
        arr[position1] = arr[position2];
        arr[position2] = tmp;
    }

    public static void quickSort(int[] arr, int start, int end) {
        if (start < end) {
            int partition = getPartition(arr, start, end);
            quickSort(arr, start, partition - 1);
            quickSort(arr, partition + 1, end);
        }
    }

    public static void main(String[] args) {
        int arr[] = {49, 38, 65, 97, 76, 13, 27, 49, 78, 34, 12, 64, 5, 4, 62, 99, 98, 54, 56, 17, 18, 23, 34, 15, 35, 25, 53, 51};
        quickSort(arr, 0, arr.length - 1);
        for (Integer e : arr) {
            System.out.println(e);
        }

    }


}
