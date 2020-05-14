package com.dingjianjun.basetech.algorithm.hash;

import org.springframework.util.CollectionUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author : Jianjun.Ding
 * @description: 一致性哈希路由实现
 * @date 2020/5/14
 */
public class ConsistentHashRouter<K,V,T extends Node<K,V>> {
    /**
     * 代表key映射的哈希环
     */
    private final SortedMap<Long, VirtualNode<K,V,T>> ring = new TreeMap<>();

    /**
     * hash函数接口
     */
    private final HashFunction hashFunction;

    /**
     * 构造一致性哈希路由，hash函数默认使用MD5算法
     *
     * @param pNodes     物理节点集合
     * @param vNodeCount 每个物理节点的副本数量
     */
    public ConsistentHashRouter(Collection<T> pNodes, int vNodeCount) {
        this(pNodes, vNodeCount, new MD5Hash());
    }

    /**
     * 构造一致性哈希路由
     *
     * @param pNodes       物理节点集合
     * @param vNodeCount   每个物理节点的副本数量
     * @param hashFunction hash函数
     */
    public ConsistentHashRouter(Collection<T> pNodes, int vNodeCount, HashFunction hashFunction) {
        if (CollectionUtils.isEmpty(pNodes)) {
            throw new IllegalArgumentException("[ConsistentHashRouter] pNodes must be non empty!");
        }

        this.hashFunction = hashFunction;
        // 遍历集合，为每个物理节点创建vNodeCount数量的虚拟节点加入哈希环上
        pNodes.forEach(pNode -> addNode(pNode, vNodeCount));
    }

    /**
     * 增加物理节点
     *
     * @param pNode      物理节点
     * @param vNodeCount 虚拟节点数量
     */
    public void addNode(T pNode, int vNodeCount) {
        if (null == pNode || vNodeCount <= 0) {
            throw new IllegalArgumentException("[addNode] pNode must be not null and vNodeCount is greater than 0");
        }

        // 该物理节点已存在的副本数量
        int replicasIndex = existVirtualNodesCount(pNode);
        for (int i = 0; i < vNodeCount; i++) {
            VirtualNode virtualNode = new VirtualNode(pNode, replicasIndex + i);
            // 创建的虚拟节点hash到环上的位置
            long key = hashFunction.hash(virtualNode.getKey());
            // 创建的虚拟节点加入到哈希环
            ring.put(key, virtualNode);
        }
    }

    /**
     * 删除指定的物理节点
     *
     * @param pNode
     */
    public void removeNode(T pNode) {
        if (null == pNode) {
            throw new IllegalArgumentException("[removeNode] pNode must be not null");
        }

        Iterator<Long> it = ring.keySet().iterator();
        while (it.hasNext()) {
            VirtualNode<K,V,T> vNode = ring.get(it.next());
            // 如果该虚拟节点是指定物理节点的副本，则从哈希环上移除
            if (vNode.isVirtualNodeOf(pNode)) {
                it.remove();
            }
        }
    }

    /**
     * 指定key路由到的物理节点
     *
     * @param key
     * @return 物理节点
     */
    public T route(String key) {
        if (ring.isEmpty()) {
            return null;
        }
        // 获取哈希环上位置大于等于指定位置的所有虚拟节点
        SortedMap<Long, VirtualNode<K,V,T>> tailMap = ring.tailMap(hashFunction.hash(key));
        // 获取哈希环上离指定位置最近的虚拟节点的位置
        Long ringKey = !tailMap.isEmpty() ? tailMap.firstKey() : ring.firstKey();
        return ring.get(ringKey).getPhysicalNode();
    }

    /**
     * 设置缓存key-value键值对
     * @param key
     * @param value
     * @return
     */
    public V set(K key, V value) {
        T route = route(String.valueOf(key));
        V oldVal = route.get(key);
        route.set((K)key, value);
        return oldVal;
    }

    /**
     * 根据key获取缓存value
     * @param key 缓存的key
     * @return
     */
    public V get(Object key) {
        T route = route(String.valueOf(key));
        return route.get(key);
    }

    /**
     * 某个物理节点已存在的虚拟节点数量
     *
     * @param pNode 物理节点
     * @return
     */
    private int existVirtualNodesCount(T pNode) {
        if (null == pNode) {
            return 0;
        }

        int replicasIndex = 0;
        for (VirtualNode vNode : ring.values()) {
            if (vNode.isVirtualNodeOf(pNode)) {
                replicasIndex++;
            }
        }

        return replicasIndex;
    }

    /**
     * 默认的hash 函数
     */
    private static class MD5Hash implements HashFunction {
        MessageDigest instance;

        public MD5Hash() {
            try {
                instance = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
            }
        }

        @Override
        public long hash(String key) {
            instance.reset();
            instance.update(key.getBytes());
            byte[] digest = instance.digest();

            long h = 0;
            for (int i = 0; i < 4; i++) {
                h <<= 8;
                h |= ((int) digest[i]) & 0xFF;
            }
            return h;
        }
    }


}
