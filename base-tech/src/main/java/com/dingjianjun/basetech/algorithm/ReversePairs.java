package com.dingjianjun.basetech.algorithm;

/**
 * @author : Jianjun.Ding
 * @description: 数组的逆序对
 * @date 2020/7/4
 */
public class ReversePairs {
    /**
     * 在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。
     * 输入一个数组，求出这个数组中的逆序对的总数。
     * 解决思路：利用归并排序
     * @param nums
     * @return
     */
    public int reversePairs(int[] nums) {
        if (null == nums || nums.length < 2) {
            return 0;
        }

        return process(nums, 0, nums.length - 1);
    }

    private int process(int[] nums, int low, int high) {
        if (low >= high) {
            return 0;
        }

        int mid = low + ((high - low) >> 1);
        return process(nums, low, mid) +
                process(nums, mid + 1, high) +
                merge(nums, low, mid, high);
    }

    /**
     * 两个相邻有序子序列nums[low...mid]、nums[mid+1...high]，变成一个有序子序列，返回有序子序列的逆序对总数
     * @param nums
     * @param low
     * @param mid
     * @param high
     * @return
     */
    private int merge(int[] nums, int low, int mid, int high) {
        int[] help = new int[high - low + 1];
        int i = low, j = mid + 1;
        int k = 0;
        int count = 0; // 逆序对总数
        while (i <= mid && j <= high) {
            if (nums[i] <= nums[j]) {
                help[k++] = nums[i++];
            } else {
                help[k++] = nums[j++];
                // 出现逆序对
                count += mid - i + 1;
            }
        }

        while (i <= mid) {
            help[k++] = nums[i++];
        }

        while (j <= high) {
            help[k++] = nums[j++];
        }

        for (k = 0; k < help.length; k++) {
            nums[low + k] = help[k];
        }

        return count;
    }

    public static void main(String[] args) {
        int[] nums = {7, 5, 6, 4};
        int count = new ReversePairs().reversePairs(nums);
        System.out.println(count);



    }




}
