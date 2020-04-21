package com.dingjianjun.basetech.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @author : Jianjun.Ding
 * @description: AQS剖析（volatile int state + 自旋锁实现的FIFO线程等待队列）
 * @date 2020/4/9
 */
public class AqsDemo {
    public static void main(String[] args) {
        testLock();




//        int nThreads = 3;
//        testCountDownLatch(nThreads, () -> {
//            System.out.println("threadId " + Thread.currentThread().getId() + " executing task");
//        });

//        testFutureTask();
       // testSemaphore();
//        testCyclicBarrier();
    }

    public static void testCyclicBarrier() {
        final int parties = 3;
        CyclicBarrier barrier = new CyclicBarrier(parties, () -> {
            // pass barrier action
            System.out.println("thread name ->" + Thread.currentThread().getName() + " execute barrier action");
        });

        IntStream.range(0 ,parties).forEach(i -> {
            new Thread(() -> {
                System.out.println("thread name -> " + Thread.currentThread().getName() + " arrive barrier");
                try {
                    // 到达栅栏时await
                    barrier.await();
                    System.out.println("thread name -> " + Thread.currentThread().getName() + " pass barrier");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

            }, "t"+ i).start();
        });

    }


    public static void testSemaphore() {
        final Semaphore semaphore = new Semaphore(3);
        IntStream.range(0, 5).forEach(i -> {
            new SecurityCheckThread(i + 1, semaphore).start();
        });


//        final int poolSize = 10;
//        // 底层由AQS实现，同步时等待的线程进入队列，对资源的争抢方式有公平（先到先得）、非公平（竞争）
//        Semaphore semaphore = new Semaphore(poolSize);
//        new Thread(() -> {
//            try {
//                semaphore.acquire(poolSize / 2);
//                semaphore.release(poolSize / 2);
//                System.out.println("thread name:" + Thread.currentThread().getName());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, "t1").start();
//
//        new Thread(() -> {
//            try {
//                semaphore.acquire(poolSize / 2);
//                semaphore.release(poolSize / 2);
//                System.out.println("thread name:" + Thread.currentThread().getName());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, "t2").start();




    }

    public static void testFutureTask() {
        // 自旋5s
        final long spinTimeoutThreshold = 5L;
        final FutureTask<String> futureTask = new FutureTask<>(() -> {
            long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(spinTimeoutThreshold);
            while (true) {
                if (deadline <= System.nanoTime()) {
                    break;
                }
            }
            return "ok";
        });

        // 异步执行有结果的任务
        new Thread(() -> {
            System.out.println("thread name:" + Thread.currentThread().getName() + " run futureTask before");
            futureTask.run();
            System.out.println("thread name:" + Thread.currentThread().getName() + " run futureTask complete");
        }, "t1").start();

        System.out.println("thread name:" + Thread.currentThread().getName() + " get result before");
        try {
            futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("thread name:" + Thread.currentThread().getName() + " get result done");
    }

    public static void testCountDownLatch(int nThreads, Runnable task) {
        CountDownLatch latch = new CountDownLatch(3);
        TranslateThread thread1 = new TranslateThread("我是打印机...", latch);
        TranslateThread thread2 = new TranslateThread("我是复读机...", latch);
        TranslateThread thread3 = new TranslateThread("我是学习机...", latch);
        thread1.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        thread2.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        thread3.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());

        thread1.start();
        thread2.start();
        thread3.start();
        // 最长等待10s
        try {
            latch.await(10L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("翻译任务完成...");
//        final CountDownLatch startLatch = new CountDownLatch(1);
//        final CountDownLatch endLatch = new CountDownLatch(nThreads);
//
//        IntStream.range(0, nThreads).forEach(i -> {
//            new Thread(() -> {
//                try {
//                    startLatch.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                task.run();
//                endLatch.countDown();
//            }, "t" + (i + 1)).start();
//        });
//
//        long sTime = System.currentTimeMillis();
//        startLatch.countDown();
//        try {
//            startLatch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("spend time " + (System.currentTimeMillis() - sTime) + " ms");
    }

    public static void testLockSupport() {
        Thread currentThread = Thread.currentThread();
        System.out.println("begin...tid:" + currentThread.getId());
        new Thread(() -> {
            System.out.println("t1 unpark before...tid:" + Thread.currentThread().getId());
            // 如果指定线程没有许可证，给它发放许可证
            LockSupport.unpark(currentThread);
            System.out.println("t1 unpark after...tid:" + Thread.currentThread().getId());
        }, "t1").start();

        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("main park before...tid:" + Thread.currentThread().getId());
        // 如果当前线程有可用的许可证，消费这个许可证，调用立即返回
        LockSupport.park();
        System.out.println("main park after...tid:" + Thread.currentThread().getId());
    }

     private static class TranslateThread extends Thread {
        private String content;
        private CountDownLatch countDownLatch;

        public TranslateThread(String content, CountDownLatch countDownLatch) {
            super();
            this.content = content;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            if (Math.random() > 0.5) {
                throw new RuntimeException("原文件存在非法字符！");
            }

            System.out.println("tid->" + Thread.currentThread().getId() + "complete translating content-> " + content);
            countDownLatch.countDown();
        }
    }

    private static class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("tid->" + t.getId() + " exception msg:" + e.getMessage());
        }
    }

    private static class SecurityCheckThread extends Thread {
        private int seq;
        private Semaphore semaphore;

        public SecurityCheckThread(int seq, Semaphore semaphore) {
            this.seq = seq;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                // 获取信号量
                semaphore.acquire();
                System.out.println("第" + seq + "号乘客正在安检中");
                if (seq % 2 == 0) {
                    TimeUnit.SECONDS.sleep(5L);
                    System.out.println("第" + seq + "号乘客，身份可疑");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 释放信号量
                semaphore.release();
                System.out.println("第" + seq + "号乘客安检通过");
            }
        }
    }


    public static void testLock() {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        lock.lock();
        try {
            new Thread(() -> {
                //线程获取锁资源，获取失败进入同步队列等待
                lock.lock();
                try {
                    System.out.println("threadName:" + Thread.currentThread().getName() + " signal before");
                    condition.signal();
                    System.out.println("threadName:" + Thread.currentThread().getName() + " signal after");
                } finally {
                    lock.unlock();
                }
            }, "t1").start();

            // sleep后线程状态WAITING，线程不释放锁资源
            TimeUnit.SECONDS.sleep(1L);
            System.out.println("threadName:" + Thread.currentThread().getName() + " await");
            //线程释放锁资源，进入条件队列等待
            condition.await();
            System.out.println("threadName:" + Thread.currentThread().getName() + " signalled");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }




}
