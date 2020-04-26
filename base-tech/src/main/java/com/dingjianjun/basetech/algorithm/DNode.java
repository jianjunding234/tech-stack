package com.dingjianjun.basetech.algorithm;

/**
 * @author : Jianjun.Ding
 * @description: 双向链表
 * @date 2020/4/25
 */
public class DNode<T> {
    int value;
    DNode prev;
    DNode next;

    public DNode(int value, DNode prev, DNode next) {
        this.value = value;
        this.prev = prev;
        this.next = next;
    }

    public DNode(int value) {
        this.value = value;
        this.prev = null;
        this.next = null;
    }
}
