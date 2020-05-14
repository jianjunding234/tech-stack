package com.dingjianjun.basetech.algorithm.hash;

/**
 * @author : Jianjun.Ding
 * @description: 节点抽象
 * @date 2020/5/14
 */
public interface Node<K,V> {
    /**
     * key用作hash mapping
     * @return
     */
    String getKey();

    /**
     * 缓存键值对
     * @param key
     * @param value
     * @return 如果key存在，返回key映射的旧value
     */
    V set(K key, V value);

    /**
     * 获取缓存key映射的value
     * @param key
     * @return
     */
    V get(Object key);
}
