package com.dingjianjun.basetech.midware.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/8
 */
public class CustomPartitioner implements Partitioner {
    private ConcurrentMap<String, AtomicInteger> topicCounterMap = new ConcurrentHashMap<>();

    /**
     * 对于指定的消息计算分区
     * @param topic
     * @param key
     * @param keyBytes
     * @param value
     * @param valueBytes
     * @param cluster
     * @return
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        if (null == keyBytes) {
            // 使用轮询
            return Utils.toPositive(nextVal(topic)) % partitionInfos.size();

        } else {
            // 使用hash
            return Utils.toPositive(Utils.murmur2(keyBytes)) % partitionInfos.size();
        }

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {
        System.out.println("CustomePartitioner configure");
    }

    private int nextVal(String topic) {
        // 考虑并发
        AtomicInteger counter = topicCounterMap.get(topic);
        if (null == counter) {
            counter = new AtomicInteger(ThreadLocalRandom.current().nextInt());
            AtomicInteger oldCounter = topicCounterMap.putIfAbsent(topic, counter);
            if (null != oldCounter) {
                counter = oldCounter;
            }
        }

        return counter.getAndIncrement();
    }

}
