package com.dingjianjun.basetech.jvm;

/**
 * @author : Jianjun.Ding
 * @description:  JDK1.8 常量和静态变量存放在堆内存
 * @date 2020/7/11
 */
public class TestHeap {
    private final long[] a1 = new long[999999999];

    public static void main(String[] args) {
        new TestHeap();
    }
}
