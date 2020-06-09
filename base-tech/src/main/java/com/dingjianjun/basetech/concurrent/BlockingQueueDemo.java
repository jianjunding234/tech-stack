package com.dingjianjun.basetech.concurrent;

import java.util.concurrent.*;

/**
 * @author : Jianjun.Ding
 * @description: 阻塞队列剖析
 * @date 2020/4/22
 */
public class BlockingQueueDemo {

    public static void main(String[] args) {
        ConcurrentLinkedQueue<String> concurrentQueue = new ConcurrentLinkedQueue<>();
        concurrentQueue.size();

        // ArrayBlockingQueue 底层基于数组

        // LinkedBlockingQueue 底层基于单链表 最大容量 Integer.MAX_VALUE


        // SynchronousQueue 容量为0

        // DelayQueue 底层基于PriorityQueue（小根堆算法实现排序）

        // LinkedTransferQueue 底层基于单链表 无界队列

        BlockingQueue<String> queue = new SynchronousQueue<>();
        boolean success = queue.offer("A");
        System.out.println(success);

    }
}
