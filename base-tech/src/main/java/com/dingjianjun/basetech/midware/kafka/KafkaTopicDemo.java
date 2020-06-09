package com.dingjianjun.basetech.midware.kafka;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/7
 */
@Slf4j
public class KafkaTopicDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Properties prop = new Properties();
        prop.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "node1:9092,node2:9092,node3:9092");
        KafkaAdminClient adminClient = (KafkaAdminClient)KafkaAdminClient.create(prop);
        // 创建topic
//        CreateTopicsResult createResult = adminClient.createTopics(Arrays.asList(new NewTopic("topic02", 3, (short) 3)));
//        createResult.all().get();



        // 查看topic列表
        ListTopicsResult result = adminClient.listTopics();
        KafkaFuture<Set<String>> names = result.names();
        try {
            Set<String> strings = names.get();
            log.info("topic：{}", strings);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // 删除topic
//        adminClient.deleteTopics(Arrays.asList("topic01"));
//        adminClient.deleteTopics(Arrays.asList("topic02"));

        // 查看topic详细信息
//        DescribeTopicsResult topicsResult = adminClient.describeTopics(Arrays.asList("topic01"));
//        Map<String, TopicDescription> resultMap = topicsResult.all().get();
//        for (Map.Entry<String, TopicDescription> entry : resultMap.entrySet()) {
//            log.info("{}", entry);
//
//
//        }


        // 关闭连接
        adminClient.close();
    }
}
