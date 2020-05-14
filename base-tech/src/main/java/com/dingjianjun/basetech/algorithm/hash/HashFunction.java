package com.dingjianjun.basetech.algorithm.hash;

/**
 * @author : Jianjun.Ding
 * @description: hash函数抽象
 * @date 2020/5/14
 */
public interface HashFunction {
    /***
     * hash函数
     * @param key
     * @return long 表示key映射到哈希环上的位置
     */
    long hash(String key);
}
