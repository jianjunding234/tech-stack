package com.dingjianjun.basetech.algorithm;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : Jianjun.Ding
 * @description: 算法实现两个字符串交替输出字符  思路：两个线程协同（一个线程等待，另一个线程唤醒）
 * 实现方法：1.LockSupport 2.ReentrantLock Condition 3.wait和notify 4.transferQueue
 * @date 2020/4/8
 */
public class AlternateOutput {
    /**
     * charArr1 先输出一个字符，接着charArr2 输出一个字符，然后交替输出
     */
    private static final char[] charArr1 = "123456789".toCharArray();
    private static final char[] charArr2 = "ABCDEFGHI".toCharArray();

    public static void main(String[] args) {
//        lockSupport();
//        lockCondition();
//        waitNotify();
        transferQueue();
    }

    public static void lockSupport() {
        final Thread current = Thread.currentThread();
        final Thread t1 = new Thread(() -> {
            for (char c : charArr1) {
                System.out.println(c);
                LockSupport.unpark(current);
                LockSupport.park();
            }
            LockSupport.unpark(current);
        }, "t1");

        t1.start();
        LockSupport.park();
        for (char c : charArr2) {
            System.out.println(c);
            LockSupport.unpark(t1);
            LockSupport.park();
        }
        LockSupport.unpark(t1);
    }

    public static void lockCondition() {
        ReentrantLock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();

        Thread t1 = new Thread(() -> {
            try {
                lock.lock();
                for (char c : charArr1) {
                    System.out.println(c);
                    condition2.signal();
                    condition1.await();
                }
                condition2.signal();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            try {
                lock.lock();
                condition2.await();
                for (char c : charArr2) {
                    System.out.println(c);
                    condition1.signal();
                    condition2.await();
                }
                condition1.signal();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t2");

        t2.start();
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.start();
    }

    public static void waitNotify() {
        Object o = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (o) {
                for (char c : charArr1) {
                    System.out.println(c);
                    o.notify();
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                o.notify();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (o) {
                try {
                    o.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (char c : charArr2) {
                    System.out.println(c);
                    o.notify();
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                o.notify();
            }
        }, "t2");

        t2.start();
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.start();
    }

    public static void transferQueue() {
        // 无界的阻塞队列
        TransferQueue<Character> queue = new LinkedTransferQueue<>();

        Thread t1 = new Thread(() -> {
            for (char c : charArr1) {
                try {
                    // transfer 阻塞方法，等待消费者取走元素
                    queue.transfer(c);
                    // take 阻塞方法
                    System.out.println(queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (char c : charArr2) {
                try {
                    System.out.println(queue.take());
                    queue.transfer(c);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2");

        t1.start();
        t2.start();
    }

}
