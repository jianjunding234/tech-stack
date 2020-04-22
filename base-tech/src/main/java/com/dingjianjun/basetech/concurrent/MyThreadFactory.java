package com.dingjianjun.basetech.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : Jianjun.Ding
 * @description: 自定义线程工厂
 * @date 2020/4/21
 */
@Slf4j
public class MyThreadFactory implements ThreadFactory {
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
        log.info(t.getName());
        return t;
    }
}
