package com.dingjianjun.basetech.algorithm;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/10
 */
public class LimitFlow {
    public static void main(String[] args) {
        try {
            counterLimit();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    public static void rateLimiter() {
        // 每秒发放的令牌数
        RateLimiter limiter = RateLimiter.create(2.0);
        // acquire 阻塞
        System.out.println(limiter.acquire(1));
        System.out.println("拿到令牌后具体服务处理请求");
        System.out.println(limiter.acquire(1));
        // tryAcquire 非阻塞
        System.out.println(limiter.tryAcquire(2));
        System.out.println(limiter.tryAcquire(Duration.ofSeconds(1L)));

        RateLimiter limiter2 = RateLimiter.create(10, 2, TimeUnit.SECONDS);
        // acquire 阻塞
        System.out.println(limiter2.acquire(1));
        System.out.println("拿到令牌后具体服务处理请求");
        System.out.println(limiter2.acquire(1));
        // tryAcquire 非阻塞
        System.out.println(limiter2.tryAcquire(2));
        System.out.println(limiter2.tryAcquire(Duration.ofSeconds(1L)));

    }


    // 限制接口时间窗请求数
    public static void counterLimit() throws ExecutionException {
        LoadingCache<Long, AtomicLong> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .build(new CacheLoader<Long, AtomicLong>() {
                    @Override
                    public AtomicLong load(Long key) throws Exception {
                        return new AtomicLong(0);
                    }
                });

        // 每秒请求数上限
        long threshold = 100;
        long unit = 1000;
        while (true) {
            long secondsTimestamp = System.currentTimeMillis() / unit;
            long count = cache.get(secondsTimestamp).incrementAndGet();
            if (count > threshold) {
                // 拒绝服务
                System.out.println("当前请求数已达上限~");
                return;
            }
            System.out.println("请求等待处理");
        }

    }
}
