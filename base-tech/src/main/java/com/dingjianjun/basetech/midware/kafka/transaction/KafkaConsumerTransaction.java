package com.dingjianjun.basetech.midware.kafka.transaction;

import com.dingjianjun.basetech.midware.kafka.CustomDeserializer;
import com.dingjianjun.basetech.midware.kafka.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/7
 */
@Slf4j
public class KafkaConsumerTransaction {
    private static final Pattern TOPIC_REX = Pattern.compile("^topic.*");
    public static void subscribe() {
        Properties prop = new Properties();
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "node1:9092,node2:9092,node3:9092");
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomDeserializer.class.getName());
        prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        // 是否自动提交offset，如果false，需要用户自己提交
        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        // 每隔多长时间自动提交offset
        prop.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 10000);
        // 消费者消费消息的事务隔离级别
        prop.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

        // 指定consumer 所属的consumer group
        prop.put(ConsumerConfig.GROUP_ID_CONFIG, "g2");
        KafkaConsumer<String,User> consumer = new KafkaConsumer<>(prop);
        // 订阅所有符合模式的topic，具有消费者分组管理特性
        consumer.subscribe(Arrays.asList("topic02"));


        while (true) {
            ConsumerRecords<String,User> consumerRecords = consumer.poll(Duration.ofSeconds(5L));
            if (!consumerRecords.isEmpty()) {
                Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>(16);
                Iterator<ConsumerRecord<String, User>> it = consumerRecords.iterator();
                while (it.hasNext()) {
                    ConsumerRecord<String, User> record = it.next();
                    log.info("key:{}, value:{}, timestamp:{}, topic:{}, partition:{}, offset:{}", record.key(), record.value(),
                            record.timestamp(), record.topic(),  record.partition(), record.offset());


                    TopicPartition tp = new TopicPartition(record.topic(), record.partition());
                    OffsetAndMetadata metadata = new OffsetAndMetadata(record.offset() + 1);
                    offsets.put(tp, metadata);
                }

                // 异步提交offset
                consumer.commitAsync(offsets, (offsetMap, exception) -> {
                    log.info("{}, {}", offsetMap.entrySet(), exception);

                });
            }
        }
    }

    public static void assign() {
        Properties prop = new Properties();
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "node1:9092,node2:9092,node3:9092");
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 指定consumer 所属的consumer group
//        prop.put(ConsumerConfig.GROUP_ID_CONFIG, "g2");
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(prop);

        List<TopicPartition> partitions = Arrays.asList(new TopicPartition("topic01", 0));
        // 给消费者手动分配分区，失去组的特性
        consumer.assign(partitions);
        // 从指定分区的头开始
       // consumer.seekToBeginning(partitions);
        consumer.seek(new TopicPartition("topic01", 0), 5);
        while (true) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(5L));
            if (!consumerRecords.isEmpty()) {
                Iterator<ConsumerRecord<String, String>> it = consumerRecords.iterator();
                while (it.hasNext()) {
                    ConsumerRecord<String, String> record = it.next();
                    log.info("key:{}, value:{}, timestamp:{}, topic:{}, partition:{}, offset:{}", record.key(), record.value(),
                            record.timestamp(), record.topic(),  record.partition(), record.offset());

                }
            }
        }



    }

}
