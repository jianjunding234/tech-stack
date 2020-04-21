package com.dingjianjun.basetech.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author : Jianjun.Ding
 * @description: ThreadLocal线程本地变量
 * @date 2020/4/8
 */
public class ThreadLocalDemo {
    //private static final ThreadLocal<String> domain = ThreadLocal.withInitial(() -> "vip.com");
    private static final ThreadLocal<String> domain = new InheritableThreadLocal<>();

    public static void test() {
        domain.set("vip2.com");
        new Thread(() -> {
            int count = 0;
            do {
                System.out.println("a " + domain.get());
                domain.set("vip1.com");
                count++;

            } while (count < 20);
        }).start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main " + domain.get());
        // 用完及时调用remove()，防止内存泄漏
        domain.remove();
    }


    public static void test2() throws InterruptedException {
        domain.set("线程1...");
        // 线程池复用已创建的线程
        ExecutorService service = Executors.newFixedThreadPool(1);
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);
        service.submit(() -> {
            System.out.println(String.format("1Thread-%s", domain.get()));
            latch.countDown();
        });

        latch.await();

        domain.set("线程2...");
        service.submit(() -> {
            System.out.println(String.format("1Thread-%s", domain.get()));
            latch2.countDown();
        });
        latch2.await();

        service.shutdown();
    }


}
