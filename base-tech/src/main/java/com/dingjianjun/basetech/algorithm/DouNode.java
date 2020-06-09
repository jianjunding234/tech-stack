package com.dingjianjun.basetech.algorithm;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/5
 */
public class DouNode<T> {
    T value;
    DouNode<T> prev;
    DouNode<T> next;

    public DouNode(T value, DouNode prev, DouNode next) {
        this.value = value;
        this.prev = prev;
        this.next = next;
    }

    public DouNode(T value) {
        this.value = value;
        this.prev = null;
        this.next = null;
    }
}
