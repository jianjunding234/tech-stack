package com.dingjianjun.basetech.util;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/5/29
 */
public class NumberUtils {

    /**
     * 一个数是否是2的n次幂
     * @param val
     * @return
     */
    public static boolean isPowerOfTwo(int val) {
        return (val & -val) == val;
    }
}
