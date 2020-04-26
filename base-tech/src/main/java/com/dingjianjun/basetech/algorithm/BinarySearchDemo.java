package com.dingjianjun.basetech.algorithm;

import java.util.Arrays;

/**
 * @author : Jianjun.Ding
 * @description: 二分查找算法
 * @date 2020/4/25
 */
public class BinarySearchDemo {
    /**
     * 有序数组中进行二分查找
     * @param arr 有序数组
     * @param num 待查找元素
     * @return
     */
    public static boolean binarySearch(int[] arr, int num) {
        if (arr == null || arr.length == 0) {
            return false;
        }

        int low = 0, high = arr.length - 1;
        int mid;
        while (low < high) {
            mid = low + ((high - low) >> 1);
            if (arr[mid] == num) {
                return true;
            } else if (arr[mid] < num) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return arr[low] == num;
    }

    /**
     * 有序数组中查找 >=num 的最左侧元素位置
     * @param arr 有序数组
     * @param num 待查找元素
     * @return
     */
    public static int nearestIndex(int[] arr, int num) {
        if (arr == null || arr.length == 0) {
            return -1;
        }

        int low = 0, high = arr.length - 1;
        int index = -1;
        int mid;
        while (low <= high) {
            mid = low + ((high - low) >> 1);
            if (arr[mid] >= num) {
                high = mid - 1;
                index = mid;
            } else {
                low = mid + 1;
            }
        }

        return index;
    }

    /**
     * 数组（不一定有序）的局部最小值下标
     * @param arr
     * @return
     */
    public static int lessIndex(int[] arr) {
        if (arr == null || arr.length == 0) {
            return -1;
        }

        if (arr.length == 1 || arr[0] < arr[1]) {
            return 0;
        }

        if (arr[arr.length - 2] > arr[arr.length - 1]) {
            return arr.length - 1;
        }

        int low = 1, high = arr.length - 2;
        int mid;
        while (low < high) {
            mid = low + ((high - low) >> 1);
            if (arr[mid] > arr[mid - 1]) {
                high = mid - 1;
            } else if (arr[mid] > arr[mid + 1]) {
                low = mid + 1;
            } else {
                return mid;
            }
        }

        return low;
    }

    private static int test(int[] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] >= value) {
                return i;
            }
        }

        return -1;
    }

    // for test
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
        }
        return arr;
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
        int testTime = 500000;
        int maxSize = 10;
        int maxValue = 100;
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr = generateRandomArray(maxSize, maxValue);
            Arrays.sort(arr);
            int value = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
            if (test(arr, value) != nearestIndex(arr, value)) {
                printArray(arr);
                System.out.println(value);
                System.out.println(test(arr, value));
                System.out.println(nearestIndex(arr, value));
                succeed = false;
                break;
            }
        }
        System.out.println(succeed ? "Nice!" : "Fucking fucked!");
    }
}
