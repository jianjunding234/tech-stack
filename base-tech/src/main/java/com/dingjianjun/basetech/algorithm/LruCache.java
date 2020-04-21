package com.dingjianjun.basetech.algorithm;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author : Jianjun.Ding
 * @description: LRU 缓存 借助LinkedHashMap实现
 * @date 2020/4/15
 */
public class LruCache<K,V> extends LinkedHashMap<K,V> {
    /**
     * 缓存最大的元素个数（K-V键值对的个数）
     */
    private static final int CACHE_MAX_SIZE = 2;

    public LruCache() {
        super(16, .75f, true);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return size() > CACHE_MAX_SIZE;
    }

    public static void main(String[] args) {
        LruCache<String, Long> cache = new LruCache<>();
        cache.put("xx", 1L);
        cache.put("oo", 2L);
        cache.put("cc", 3L);
        Optional.ofNullable(cache.entrySet()).orElse(Collections.emptySet()).stream()
                .forEach(entry -> {
                    System.out.println("key:" + entry.getKey() + " value:" + entry.getValue());
                });
    }

}
