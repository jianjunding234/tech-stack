package com.dingjianjun.basetech.algorithm;

/**
 * @author : Jianjun.Ding
 * @description: 数组奇数次
 * @date 2020/4/25
 */
public class OddTimesEvenTimes {
    /**
     * 数组中一个数出现奇数次，其余数出现偶数次
     * @param arr
     */
    public static void printSingleOddTimes(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }

        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        System.out.println(eor);
    }

    /**
     * 数组中两个数出现奇数次，其余数出现偶数次，找出出现奇数次的两个数
     * @param arr
     */
    public static void printDoubleOddTimes(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }

        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            /**
             * 数组中两个数出现奇数次，其余数出现偶数次，得出 eor != 0
             */
            eor ^= arr[i];
        }

        // 由于eor != 0, 找出eor对应二进制数最右边一个1且其余位都是0的数
        int rightOne = eor & ((~eor) + 1);
        int onlyOne = 0;
        for (int i = 0; i < arr.length; i++) {
            if ((arr[i] & rightOne) != 0) {
                onlyOne ^= arr[i];
            }
        }

        System.out.println(onlyOne + " " + (eor ^ onlyOne));
    }

    /**
     * 一个整数对应二进制数中1出现的次数
     * @param n
     * @return
     */
    public static int bit1Counts(int n) {
        int count = 0;
        while (n != 0) {
            int rightOne = n & ((~n) + 1);
            count++;
            n ^= rightOne;
        }

        return count;
    }


    public static void main(String[] args) {
        int a = 5, b = 7;
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        System.out.println("a=" + a + " b="+ b);

        int[] arr1 = { 3, 3, 2, 3, 1, 1, 1, 3, 1, 1, 1 };
        printSingleOddTimes(arr1);

        int count = bit1Counts(14);
        System.out.println(count);

        int[] arr2 = { 4, 3, 4, 2, 2, 2, 4, 1, 1, 1, 3, 3, 1, 1, 1, 4, 2, 2 };
        printDoubleOddTimes(arr2);
    }
}
