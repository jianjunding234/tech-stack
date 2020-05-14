package com.dingjianjun.basetech.jvm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : Jianjun.Ding
 * @description: Full GC 问题定位
 * 从数据库中读取信用数据，套用模型，并把结果进行记录和传输
 * @date 2020/5/9
 */
public class FullGcDemo {
    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(50,
            new ThreadPoolExecutor.DiscardOldestPolicy());

    private static ThreadPoolExecutor executor2 = new ThreadPoolExecutor(20, 50,
            0L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(500),
            new MyThreadFactory("FGCDemo"),
            new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) throws Exception {
        executor.setMaximumPoolSize(50);

        for (;;){
            modelFit2();
            Thread.sleep(100);
        }
    }

    private static void modelFit(){
        List<CardInfo> taskList = getAllCardInfo();
        taskList.forEach(info -> {
            executor.scheduleWithFixedDelay(() -> {
                info.m();

            }, 2, 3, TimeUnit.SECONDS);
        });
    }

    private static void modelFit2() {
        List<CardInfo> taskList = getAllCardInfo();
        taskList.forEach(info -> executor2.submit(() -> {
            info.m();

        }));
        taskList.clear();
    }

    private static List<CardInfo> getAllCardInfo(){
        List<CardInfo> taskList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            CardInfo ci = new CardInfo();
            taskList.add(ci);
        }

        return taskList;
    }

    private static class CardInfo {
        BigDecimal price = new BigDecimal(0.0);
        String name = "张三";
        int age = 5;
        Date birthdate = new Date();

        public void m() {}
    }

    private static class MyThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        /**
         * 定义线程组名称，使用jstack排查线程问题
         * @param whatFeaturesOfGroup
         */
        public MyThreadFactory(String whatFeaturesOfGroup) {
            namePrefix = "UserThreadFactory's " + whatFeaturesOfGroup + "-worker-";
        }

        @Override
        public Thread newThread(Runnable r) {
            String name = namePrefix + threadNumber.getAndIncrement();
            Thread t = new Thread(null, r, name,0);
            return t;
        }
    }
}
