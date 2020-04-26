package com.dingjianjun.basetech.algorithm;

import java.util.stream.IntStream;

/**
 * @author : Jianjun.Ding
 * @description: 在一个数组中，一个数左边比它小的数的总和，叫数的小和，所有数的小和累加起来，叫数组小和。
 * @date 2020/4/26
 */
public class SmallSum {
    /**
     * 在一个数组中，一个数左边比它小的数的总和，叫数的小和，所有数的小和累加起来，叫数组小和
     * @param arr
     * @return 数组的小和
     */
    public static int smallSum(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }

        return process(arr, 0, arr.length - 1);
    }

    /**
     * 使用归并排序求数组的小和
     * 时间复杂度 O(N*logN), 额外空间复杂度O(N)， 稳定
     * @param arr
     * @param l 子序列起始位置
     * @param r 子序列截止位置
     * @return 数组的小和
     */
    public static int process(int[] arr, int l, int r) {
        if (l == r) {
            return 0;
        }

        // 取中间位置
        int mid = l + ((r - l) >> 1);
        return process(arr, l, mid) +
                process(arr, mid + 1, r) +
                merge(arr, l, mid, r);
    }

    /**
     * 合并两个相邻有序子序列arr[l...mid]、arr[mid+1...r]，变成一个有序子序列，返回有序子序列的小和
     * @param arr
     * @param l
     * @param mid
     * @param r
     * @return 返回有序子序列的小和
     */
    public static int merge(int[] arr, int l, int mid, int r) {
        int[] help = new int[r - l + 1];
        int p1 = l, p2 = mid + 1, k = 0;
        int res = 0;
        while (p1 <= mid && p2 <= r) {
            res += (arr[p1] < arr[p2]) ? (r - p2 + 1) * arr[p1] : 0;
            help[k++] = (arr[p1] < arr[p2]) ? arr[p1++] : arr[p2++];
        }

        while (p1 <= mid) {
            help[k++] = arr[p1++];
        }

        while (p2 <= r) {
            help[k++] = arr[p2++];
        }

        // help数组元素拷贝到原数组的第l位置开始
        for (k = 0; k < help.length; k++) {
            arr[l + k] = help[k];
        }

        return res;
    }

    private static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int)((maxSize + 1) * Math.random())];
        IntStream.range(0, arr.length).forEach(i ->
                arr[i] = (int)((maxValue + 1) * Math.random()) - (int)((maxValue + 1) * Math.random())
        );
        return arr;
    }

    // for test
    public static int comparator(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int res = 0;
        for (int i = 1; i < arr.length; i++) {
            for (int j = 0; j < i; j++) {
                res += arr[j] < arr[i] ? arr[j] : 0;
            }
        }
        return res;
    }

    private static int[] copyArray(int[] source) {
        if (source == null) {
            return null;
        }

        int[] dest = new int[source.length];
        IntStream.range(0, source.length).forEach(i -> dest[i] = source[i]);
        return dest;
    }
    // for test
    public static void printArray(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }



    public static void main(String[] args) {
        int maxTimes = 1000;
        int maxSize = 100;
        int maxValue = 100;
        boolean success = true;
        for (int i = 0; i < maxTimes; i++) {
            int[] arr1 = generateRandomArray(maxSize, maxValue);
            int[] arr2 = copyArray(arr1);
            if (smallSum(arr1) != comparator(arr2)) {
                printArray(arr1);
                printArray(arr2);
                success = false;
                break;
            }
        }

        System.out.println(success ? "Nice!!!" : "Fuck~~~");
    }
}
