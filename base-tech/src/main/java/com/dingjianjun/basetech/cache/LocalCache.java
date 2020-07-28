package com.dingjianjun.basetech.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/11
 */
public class LocalCache {
    public static void main(String[] args) throws ExecutionException {
        Cache<String, String> cache = getCacheInstance();
        cache.put("k1", "Guava");
        String result = cache.get("k2", () -> "HELLO");
        System.out.println(result);


    }


    public static Cache<String, String> getCacheInstance() {
        return CacheBuilder.newBuilder().concurrencyLevel(4)
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .maximumSize(10000)
                .build();
    }
}
