package com.dingjianjun.basetech;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/7/4
 */
public class TwoSum {

    /**
     * 输入一个递增排序的数组和一个数字s，在数组中查找两个数，使得它们的和正好是s。
     * 如果有多对数字的和等于s，则输出任意一对即可。
     * @param nums
     * @param target
     * @return
     */
    public int[] twoSum(int[] nums, int target) {
        if (nums == null || nums.length < 2) {
            return new int[0];
        }

        int low = 0, high = nums.length - 1;
        while (low < high) {
            int sum = nums[low] + nums[high];
            if (sum == target) {
                return new int[] {nums[low], nums[high]};
            } else if (sum < target) {
                low++;
            } else {
                high--;
            }
        }

        return new int[0];
    }

    public static void main(String[] args) {
        int[] arr = {2,7,11,15};
        int target = 20;
        int[] res = new TwoSum().twoSum(arr, target);
        System.out.println(res);

    }
}
