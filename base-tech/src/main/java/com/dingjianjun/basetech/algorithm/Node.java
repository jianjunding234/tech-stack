package com.dingjianjun.basetech.algorithm;

/**
 * @author : Jianjun.Ding
 * @description: 单链表
 * @date 2020/4/25
 */
public class Node {
    int value;
    Node next;

    public Node(int value, Node next) {
        this.value = value;
        this.next = next;
    }

    public Node(int value) {
        this.value = value;
        this.next = null;
    }
}
