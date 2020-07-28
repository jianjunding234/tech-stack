package com.dingjianjun.basetech.concurrent;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/17
 */
public class ConcurrentHashMapBug {
    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>(16);
        System.out.println("computeIfAbsent 执行before...");
        Integer retVal = map.computeIfAbsent("AaAa", key ->  map.computeIfAbsent("BBBB", key2 -> 42));
//        Integer retVal = map.computeIfAbsent("AAA", key -> 1000);
//        System.out.println("retVal:" + retVal);
//        retVal = map.computeIfAbsent("AAA", key -> -1);
//        System.out.println("retVal:" + retVal);
     }
}
