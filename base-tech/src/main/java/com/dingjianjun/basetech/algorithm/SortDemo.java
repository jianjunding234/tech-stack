package com.dingjianjun.basetech.algorithm;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author : Jianjun.Ding
 * @description: 排序算法
 * @date 2020/4/25
 */
public class SortDemo {

    /**
     * 数组进行排序，升序使用大根堆，逆序使用小根堆
     * 堆排序：时间复杂度O(N*logN)，额外空间复杂度O(1)，非稳定排序
     * @param arr
     */
    public static void heapSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }

        // 建初始大根堆
        // 从上往下依次加入节点到堆中，然后向上调整，时间复杂度O(N*logN)
//        for (int i = 0; i < arr.length; i++) {
//            heapInsert(arr, i);
//        }

        // 由于所有的节点已知，从下往上，从右到左，每个节点向下调整成大根堆
        for (int i = arr.length - 1; i >= 0; i--) {
            heapify(arr, i, arr.length);
        }

        int heapSize = arr.length;
        swap(arr, 0, arr.length - 1);
        --heapSize;
        // 堆规模逐步缩小
        while (heapSize > 0) {
            // 重新向下调整成大根堆
            heapify(arr,0, heapSize);
            // 堆顶与堆尾交换
            swap(arr, 0, --heapSize);
        }
    }

    /**
     * 重新向下调整成大根堆
     * @param arr
     * @param index 开始调整的位置
     * @param heapSize 堆中元素个数
     */
    private static void heapify(int[] arr, int index, int heapSize) {
        // 左孩子下标
        int left = 2 * index + 1;
        while (left < heapSize) {
            int largeIndex = (left + 1 < heapSize && arr[left + 1] > arr[left]) ? left + 1 : left;
            largeIndex = arr[largeIndex] < arr[index] ? index : largeIndex;
            // index位置元素值比它的孩子元素值大
            if (largeIndex == index) {
                break;
            }
            swap(arr, index, largeIndex);
            index = largeIndex;
            left = 2 * index + 1;
        }

    }

    /**
     * 指定位置的元素插入到堆中，然后进行向上调整
     * @param arr
     * @param index
     */
    private static void heapInsert(int[] arr, int index) {
        while (index >= 0) {
            int parent = (index - 1) >> 1;
            if (parent >= 0 && arr[index] > arr[parent]) {
                swap(arr, index, parent);
                index = parent;
            } else {
                break;
            }
        }

    }

    /**
     * 数组元素排序
     * 递归实现快速排序
     * @param arr
     */
    public static void quickSort1(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }

        quickProcess(arr, 0, arr.length - 1);
    }

    /**
     * 数组元素排序
     * 递归实现快速排序
     * @param arr
     */
    public static void quickSort2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }

        quickProcess2(arr, 0, arr.length - 1);
    }

    /**
     * 数组元素排序
     * 随机快排
     * @param arr
     */
    public static void quickSort3(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }

        quickProcess3(arr, 0, arr.length - 1);
    }

    /**
     * 递归实现快排：时间复杂度O(N*logN), 额外空间复杂度O(logN)，排序稳定性：不稳定
     *
     * @param arr
     * @param l
     * @param r
     */
    public static void quickProcess(int[] arr, int l, int r) {
        if (l >= r) {
            return;
        }

        // 进行一次划分，返回枢轴位置（枢轴左边的值都小于等于枢轴位置元素值，枢轴右边的值都大于枢轴位置元素值）
        int pivot = partition(arr, l, r);
        quickProcess(arr, l, pivot - 1);
        quickProcess(arr, pivot + 1, r);
    }

    /**
     * 对数组子序列arr[l...r]进行一次划分，返回枢轴位置，枢轴左边的值都小于等于枢轴位置元素值，枢轴右边的值都大于枢轴位置元素值）
     * @param arr
     * @param l
     * @param r
     * @return 返回枢轴位置
     */
    private static int partition(int[] arr, int l, int r) {
        if (l > r) {
            return -1;
        }

        if (l == r) {
            return l;
        }
        /**
         * 选择arr[r]作为枢轴位置的元素值
         */
        int value = arr[r];
        /**
         * leIndex表示小于等于arr[r]的序列的最后位置
         */
        int leIndex = l - 1;
        int index = l;
        while (index < r) {
            if (arr[index] <= value) {
                swap(arr, ++leIndex, index);
            }
            index++;
        }

        swap(arr, ++leIndex, r);
        return leIndex;
    }


    /**
     * 递归实现快排
     *
     * @param arr
     * @param l
     * @param r
     */
    public static void quickProcess2(int[] arr, int l, int r) {
        if (l >= r) {
            return;
        }

        /**
         * 使用arr[r]对数组子序列arr[l...r]进行一次划分，元素值小于arr[r]位于最左边，等于arr[r]位于中间，大于arr[r]位于最右边，
         * 确定一批数的位置
         */
        int[] equalArea = netherlandsFlag(arr, l, r);
        quickProcess2(arr, l, equalArea[0] - 1);
        quickProcess2(arr, equalArea[1] + 1, r);
    }

    /**
     * 随机快排：每次随机在序列arr[l...r]中找一个数进行一次划分
     *
     * @param arr
     * @param l
     * @param r
     */
    public static void quickProcess3(int[] arr, int l, int r) {
        if (l >= r) {
            return;
        }
        // 每次随机在序列arr[l...r]中找一个数进行一次划分
        swap(arr, (int)(l + (r - l + 1) * Math.random()), r);
        /**
         * 使用arr[r]对数组子序列arr[l...r]进行一次划分，元素值小于arr[r]位于最左边，等于arr[r]位于中间，大于arr[r]位于最右边，
         * 确定一批数的位置
         */
        int[] equalArea = netherlandsFlag(arr, l, r);
        quickProcess2(arr, l, equalArea[0] - 1);
        quickProcess2(arr, equalArea[1] + 1, r);
    }


    /**
     * 用元素arr[r]对数组子序列arr[l...r]进行一次划分，元素值小于arr[r]位于最左边，等于arr[r]位于中间，大于arr[r]位于最右边，
     * 返回等于arr[r]的位置边界
     * @param arr
     * @param l
     * @param r
     * @return 返回等于arr[r]的位置边界
     */
    private static int[] netherlandsFlag(int[] arr, int l, int r) {
        if (l > r) {
            return new int[] {-1, -1};
        }

        if (l == r) {
            return new int[] {l, l};
        }

        // 选择arr[r]作为参照值
        int value = arr[r];
        // leIndex表示小于arr[r]的序列的最后位置
        int leIndex = l - 1;
        // more表示等于arr[r]的右边界
        int more = r;
        int index = l;
        while (index < more) {
            if (arr[index] < value) {
                swap(arr, ++leIndex, index++);
            } else if (arr[index] > value) {
                swap(arr, index, --more);
            } else {
                index++;
            }
        }

        swap(arr, more, r);
        return new int[] {leIndex + 1, more};
    }

    /**
     * 数组元素排序
     * 递归实现归并排序
     * @param arr
     */
    public static void mergeSort1(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }

        process(arr, 0, arr.length - 1);
    }

    /**
     * 归并排序
     * 时间复杂度 O(N*logN), 额外空间复杂度O(N)， 稳定
     * @param arr
     * @param l 子序列起始位置
     * @param r 子序列截止位置
     */
    public static void process(int[] arr, int l, int r) {
        if (l == r) {
            return;
        }

        // 取中间位置
        int mid = l + ((r - l) >> 1);
        process(arr, l, mid);
        process(arr, mid + 1, r);
        // 两个有序子序列合并成一个有序子序列
        merge(arr, l, mid, r);
    }

    /**
     * 合并两个相邻有序子序列arr[l...mid]、arr[mid+1...r]，变成一个有序子序列
     * @param arr
     * @param l
     * @param mid
     * @param r
     */
    public static void merge(int[] arr, int l, int mid, int r) {
        int[] help = new int[r - l + 1];
        int i = l, j = mid + 1, k = 0;
        while (i <= mid && j <= r) {
            help[k++] = (arr[i] <= arr[j]) ? arr[i++] : arr[j++];
        }

        while (i <= mid) {
            help[k++] = arr[i++];
        }

        while (j <= r) {
            help[k++] = arr[j++];
        }

        // help数组元素拷贝到原数组的第l位置开始
        for (k = 0; k < help.length; k++) {
            arr[l + k] = help[k];
        }
    }

    /**
     * 非递归实现归并排序，时间复杂度O(N*logN)，额外空间复杂度O(N)
     * @param arr
     */
    public static void mergeSort2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }

        int n = arr.length;
        // 当前子序列有序，左组长度
        int mergeSize = 1;
        while (mergeSize < n) {
            int l = 0;
            while (l < n) {
                // l ... m 子序列有序
                int m = l + mergeSize - 1;
                if (m >= n) {
                    break;
                }
                // m+1...r 子序列有序
                int r = Math.min(m + mergeSize, n - 1);
                merge(arr, l, m, r);
                l = r + 1;
            }

            // 防止溢出
            if (mergeSize > n / 2) {
                break;
            }

            mergeSize <<= 1;
        }
    }


    /**
     * 选择排序
     * 时间复杂度 O(N^2), 不稳定
     * @param arr
     */
    public static void selectionSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }

        for (int i = 0; i < arr.length; i++) {
            // 默认最小值下标
            int min = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
            }

            // 如果当前位置元素不是最小
            if (min != i) {
                swap(arr, i, min);
            }
        }
    }

    /**
     * 冒泡排序
     * 时间复杂度 O(N^2), 稳定
     * @param arr
     */
    public static void bubbleSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }

        for (int i = arr.length - 1; i > 0; i--) {
            boolean hasSwap = false;
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j+1]) {
                    swap(arr, j, j + 1);
                    hasSwap = true;
                }
            }

            // 该轮排序没有发生交换，数组已经有序
            if (!hasSwap) {
                break;
            }
        }

    }

    /**
     * 插入排序
     * 时间复杂度 O(N^2), 稳定
     * @param arr
     */
    public static void insertionSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }

        for (int i = 1; i < arr.length; i++) {
            int data = arr[i];
            for (int j = i - 1; j >= 0 && arr[j] > arr[j + 1]; j--) {
                swap(arr, j, j + 1);
            }
        }
    }

    /**
     * 异或运算完成两个数交换（！！！前提条件 i != j）
     * @param arr
     * @param i
     * @param j
     */
    private static void swap(int[] arr, int i, int j) {
        if (i == j) {
            return;
        }

        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }

    /**
     * 随机生成数组（数组长度在给定范围随机，数组元素在给定范围随机）
     * @param maxSize
     * @param maxValue
     * @return
     */
    private static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int)((maxSize + 1) * Math.random())];
        IntStream.range(0, arr.length).forEach(i ->
                arr[i] = (int)((maxValue + 1) * Math.random()) - (int)((maxValue + 1) * Math.random())
        );
        return arr;
    }

    private static int[] copyArray(int[] source) {
        if (source == null) {
            return null;
        }

        int[] dest = new int[source.length];
        IntStream.range(0, source.length).forEach(i -> dest[i] = source[i]);
        return dest;
    }

    /**
     * test 使用JDK封装的数组排序接口
     * @param arr
     */
    private static void comparator(int[] arr) {
        Arrays.sort(arr);
    }

    private static void printArray(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }

        IntStream.range(0, arr.length).forEach(i -> System.out.print(arr[i] + " "));
        System.out.println();
    }

    /**
     * 比较两个数组是否相等
     * @param arr1
     * @param arr2
     * @return
     */
    private static boolean isEqual(int[] arr1, int[] arr2) {
        if (arr1 == arr2) {
            return true;
        }

        if (arr1 == null || arr2 == null || arr1.length != arr2.length) {
            return false;
        }

        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }



    public static void main(String[] args) {
        int maxTimes = 1000;
        int maxSize = 100;
        int maxValue = 100;
        boolean success = true;
        for (int i = 0; i < maxTimes; i++) {
            int[] arr1 = generateRandomArray(maxSize, maxValue);
            int[] arr2 = copyArray(arr1);
            heapSort(arr1);
            comparator(arr2);
            if (!isEqual(arr1, arr2)) {
                printArray(arr1);
                printArray(arr2);
                success = false;
                break;
            }
        }

        System.out.println(success ? "Nice!!!" : "Fuck~~~");

        int[] arr = generateRandomArray(maxSize, maxValue);
        printArray(arr);
        heapSort(arr);
        printArray(arr);

    }

}
