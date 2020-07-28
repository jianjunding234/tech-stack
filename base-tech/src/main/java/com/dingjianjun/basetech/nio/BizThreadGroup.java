package com.dingjianjun.basetech.nio;

import com.dingjianjun.basetech.concurrent.MyThreadFactory;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/28
 */
public class BizThreadGroup {
    private final ExecutorService[] executors;
    private final AtomicInteger counter = new AtomicInteger(0);
    // channelId 作为key
    private final Map<String, ExecutorService> map = new ConcurrentHashMap<>(256);

    public BizThreadGroup(int nThread) {
        executors = new ExecutorService[nThread];
        for (int i = 0; i < nThread; i++) {
            executors[i] =  new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(1000),
                    new MyThreadFactory("biz"),
                    new ThreadPoolExecutor.AbortPolicy());
        }
    }


    public void register(String channelId) {
        ExecutorService executor = next();
        map.putIfAbsent(channelId, executor);
    }

    public void unregister(String channelId) {
        map.remove(channelId);
    }

    public void execute(String channelId, Runnable task) {
        ExecutorService executor = map.get(channelId);
        executor.execute(task);
    }

    public int curConnNum () {
        return map.size();
    }

    private ExecutorService next() {
        int index = counter.getAndIncrement() % executors.length;
        return executors[index];
    }

}
