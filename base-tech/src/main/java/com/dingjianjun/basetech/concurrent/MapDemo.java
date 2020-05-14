package com.dingjianjun.basetech.concurrent;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author : Jianjun.Ding
 * @description: Map 实现类剖析
 * @date 2020/4/23
 */
public class MapDemo {
    public static void main(String[] args) {
        // HashMap hash表 + 单链表 + 红黑树
        HashMap<String, Long> map1 = new HashMap<>(16);
        map1.put("No002", 1L);
        map1.put("No004", 1L);
        map1.put("No003", 1L);
        for (Map.Entry<String, Long> e : map1.entrySet()) {
            System.out.println("k:" + e.getKey() + " v:" + e.getValue());
        }
        System.out.println("------------------------------------");
        // TreeMap 红黑树 按照key有序存储
        TreeMap<String, Long> map2 = new TreeMap();
        map2.put("No002", 1L);
        map2.put("No004", 1L);
        map2.put("No003", 1L);
        for (Map.Entry<String, Long> e : map2.entrySet()) {
            System.out.println("k:" + e.getKey() + " v:" + e.getValue());
        }
        System.out.println("------------------------------------");
        // LinkedHashMap hash表 + 双向链表 实现LRU Cache
        LinkedHashMap<String, Long> map3 = new LinkedHashMap<>(16, .75f, true);
        map3.put("No002", 1L);
        map3.put("No004", 1L);
        map3.put("No003", 1L);
        // 最近访问的k-v放在双向链表的尾节点
        map3.get("No004");
        for (Map.Entry<String, Long> e : map3.entrySet()) {
            System.out.println("k:" + e.getKey() + " v:" + e.getValue());
        }
        System.out.println("------------------------------------");
        // ConcurrentHashMap hash表 + 单链表 + 红黑树  CAS + Synchronized 实现线程安全
        ConcurrentHashMap<String, Long> map4 = new ConcurrentHashMap<>(16);
        map4.put("No002", 1L);
        map4.put("No004", 1L);
        map4.put("No003", 1L);
        map4.get("No004");
        for (Map.Entry<String, Long> e : map4.entrySet()) {
            System.out.println("k:" + e.getKey() + " v:" + e.getValue());
        }
        System.out.println("------------------------------------");
        // ConcurrentSkipListMap 跳表
        ConcurrentSkipListMap<String, Long> map5 = new ConcurrentSkipListMap();
        map5.put("No002", 1L);
        map5.put("No004", 1L);
        map5.put("No003", 1L);
        map5.get("No004");
        for (Map.Entry<String, Long> e : map5.entrySet()) {
            System.out.println("k:" + e.getKey() + " v:" + e.getValue());
        }

    }
}
