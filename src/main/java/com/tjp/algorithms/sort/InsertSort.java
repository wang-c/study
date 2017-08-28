package com.tjp.algorithms.sort;

/**
 * 插入排序{
 *      直接插入排序
 *      希尔排序(最小增量排序)
 * }
 * Created by tujinpeng on 2017/8/28.
 */
public class InsertSort {
    /**
     * 直接插入排序思想:
     * 从第二个元素开始往后，依次选择哨兵元素和前面的元素比较，如果前一个元素大于该哨兵元素（从小到大排序），则把前面那个元素移动到后一个位置；继续往前比较，直到找某个元素不大于该哨兵元素，则把哨兵元素插入到位置上；
     * <p/>
     * 插入排序的步骤：
     * 1、第二个元素开始外后选择一个哨兵元素；
     * 2、让哨兵元素和前面的元素进行比较，找到合适的位置插入；
     * 3、循环上面两步，直到选择完所有元素；
     * <p/>
     * 时间复杂度:
     * 最好:O(n)  排好序的
     * 最坏:O(n2)
     * 平均:O(n2)
     * <p/>
     * 空间复杂度:O(1)
     * <p/>
     * 稳定性:稳定
     *
     * @param arr
     */
    public static void insertSort(int arr[]) {
        if (arr.length == 0) {
            return;
        }
        for (int i = 1; i < arr.length; i++) {// 从第二个元素往后得到哨兵元素
            int tmp = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > tmp) {
                arr[j + 1] = arr[j];//将大于tmp的值整体向后移动一位
                j--;
            }
            arr[j + 1] = tmp;//找到合适的位置插入
        }
    }

    /**
     * shell排序思想:
     * shell排序是相当于把一个数组中的所有元素分成几部分来排序；
     * 先把几个小部分的元素排序好，让元素大概有个顺序，最后再全面使用插入排序。
     * 一般最后一次排序都是和上面的插入排序一样的；
     * <p/>
     * shell排序的步骤：
     * 比如：
     * 数组有10个元素，增量 d = 10/2=5；则比较元素为：array[0]    array[0+d]    array[0+2d]  array[0+3d]；（当然 d 会改变的，d = d/2  或者 d = d -2）
     * 第一次  d = 5  比较的元素为：array[0]  , array[5]
     * 第二次  d = d/2 = 2 比较元素为：array[0]  , array[2]  , array[4] , array[6] , array[8]
     * 第三次  d = d/2 = 1 比较元素为：从array[0] 到  array[9]
     * <p/>
     * 其实上面的插入排序可以看作是 增量 d = 1的shell排序；当然同理，shell排序 最后增量 d = 1时， 就是插入排序了；
     * <p/>
     * 时间复杂度:
     * 最好:O(n)
     * 最坏:O(n2)
     * 平均:O(n1.3)
     * <p/>
     * 空间复杂度:O(1)
     * <p/>
     * 稳定性:不稳定
     *
     * @param arr
     */
    public static void shellSort(int arr[]) {
        if (arr.length == 0) {
            return;
        }
        int d = arr.length;//初始化增量
        int i, j, tmp;
        while (d >= 1) { // 判断增量
            for (i = d; i < arr.length; i = i + d) { // 从第一个增量值往后  得到哨兵元素
                tmp = arr[i];
                for (j = i - d; j >= 0; j = j - d) { // 和前面有序的元素进行调整
                    if (arr[j] > tmp) arr[j + d] = arr[j];
                    else break;
                }
                arr[j + d] = tmp;
            }
            d = d / 2; // 缩小增量的值
        }
    }


    public static void main(String[] args) {
        int arr[] = {49, 38, 65, 97, 76, 13, 27, 49, 78, 34, 12, 64, 5, 4, 62, 99, 98, 54, 56, 17, 18, 23, 34, 15, 35, 25, 53, 51};
//        insertSort(arr);
        shellSort(arr);
        for (int e : arr) {
            System.out.println(e);
        }

    }


}
