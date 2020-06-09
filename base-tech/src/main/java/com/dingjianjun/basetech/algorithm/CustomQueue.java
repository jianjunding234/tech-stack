package com.dingjianjun.basetech.algorithm;

import org.springframework.util.Assert;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/5
 */
public class CustomQueue<T> {
    private DouNode<T> head;
    private DouNode<T> tail;
    private int size;

    /**
     * 进入队尾
     * @param e
     * @return
     */
    public boolean offer(T e) {
        Assert.notNull(e, "e is null");
        DouNode<T> cur = new DouNode<>(e);
        if (tail == null) {
            head = tail = cur;
        } else {
            tail.next = cur;
            cur.prev = tail;
            tail = cur;
        }
        size++;
        return true;
    }

    /**
     * 获取并移除队头元素
     * @return
     */
    public T poll() {
        if (nonEmpty()) {
            T value = head.value;
            if (null != head.next) {
                DouNode<T> next = head.next;
                head.next = null;
                next.prev = null;
                head = next;
            } else {
                head = tail = null;
            }
            size--;
            return value;
        }
        return null;
    }

    /**
     * 获取队头元素
     * @return
     */
    public T peek() {
        if (nonEmpty()) {
            return head.value;
        }
        return null;
    }

    public boolean nonEmpty() {
        return size != 0;
    }

    public static void main(String[] args) {
        
    }


}
