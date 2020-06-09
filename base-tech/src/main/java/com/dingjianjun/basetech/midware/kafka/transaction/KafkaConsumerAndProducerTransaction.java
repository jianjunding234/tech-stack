package com.dingjianjun.basetech.midware.kafka.transaction;

import com.dingjianjun.basetech.midware.kafka.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.*;

/**
 * @author : Jianjun.Ding
 * @description: 消费者&生产者事务
 * @date 2020/6/9
 */
@Slf4j
public class KafkaConsumerAndProducerTransaction {
    public static void consumeAndProduce() {
        String consumerGroupId = "g1";
        String cTopic = "topic01";
        String pTopic = "topic02";
        KafkaProducer<String, User> producer = buildKafkaProducer();
        KafkaConsumer<String, User> consumer = buildConsumer(consumerGroupId);
        // 初始化事务
        producer.initTransactions();
        // 消费者订阅topic
        consumer.subscribe(Arrays.asList(cTopic));
        while (true) {
            // 轮询拉取
            ConsumerRecords<String, User> consumerRecords = consumer.poll(Duration.ofSeconds(1L));
            if (!consumerRecords.isEmpty()) {
                Iterator<ConsumerRecord<String, User>> it = consumerRecords.iterator();
                Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>(16);
                // 开启事务
                producer.beginTransaction();
                try {
                    // 处理业务逻辑，数据加工，发送消息到Kafka Server
                    while (it.hasNext()) {
                        ConsumerRecord<String, User> cRecord = it.next();
                        log.info("consumerRecord>>> {}", cRecord);
                        User u = cRecord.value();
                        u.setName(u.getName() + "-hi");
                        ProducerRecord<String, User> pRecord = new ProducerRecord<String, User>(pTopic, cRecord.key(), u);
                        producer.send(pRecord);

                        TopicPartition tp = new TopicPartition(cRecord.topic(), cRecord.partition());
                        // 消费消息的offset和元数据
                        OffsetAndMetadata metadata = new OffsetAndMetadata(cRecord.offset() + 1);
                        offsets.put(tp, metadata);
                    }
                    // 提交消费者的分区消息offset
                    producer.sendOffsetsToTransaction(offsets, consumerGroupId);
                    // 提交事务
                    producer.commitTransaction();
                } catch (Exception ex) {
                    log.error("出错了~", ex.getMessage());
                    // 回滚事务
                    producer.abortTransaction();
                }

            }
        }

    }


    private static KafkaProducer<String, User> buildKafkaProducer() {
        Properties prop = new Properties();
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "node1:9092,node2:9092,node3:9092");
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomSerializer.class.getName());
        prop.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());
        prop.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, CustomProducerInterceptor.class.getName());
        // acks
        prop.put(ProducerConfig.ACKS_CONFIG, "all");
        // 重试机制次数
        prop.put(ProducerConfig.RETRIES_CONFIG, 3);
        // 等待请求响应的最大时间，如果超过这个时间没有收到响应，则开启重试机制
        prop.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 20000);
        // 开启幂等：保证数据不丢失、不重复
        prop.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        prop.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        // 设置事务标识，唯一性
        prop.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "TransactionalId" + UUID.randomUUID().toString().replace("-",""));
        // 缓冲同一个分区的消息大小（bytes），消息先存放在socket buffer中，等待满足条件批量发送，提高生产者吞吐量
        prop.put(ProducerConfig.BATCH_SIZE_CONFIG, 1024);
        // 如果缓冲同一个分区的消息大小不足设置的batch size，等待的时间到达设置的逗留时间，就会立即批量发送
        prop.put(ProducerConfig.LINGER_MS_CONFIG, 5);

        return  new KafkaProducer<>(prop);
    }

    private static KafkaConsumer<String, User> buildConsumer(String groupId) {
        Properties prop = new Properties();
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "node1:9092,node2:9092,node3:9092");
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomDeserializer.class.getName());
        prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        // 消费者&生产者事务，需要关闭消费者offset自动提交功能，手动提交
        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        // 每隔多长时间自动提交offset
//        prop.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 10000);
        // 消费者消费消息的事务隔离级别
        prop.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

        // 指定consumer 所属的consumer group
        prop.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return new KafkaConsumer<>(prop);
    }
}
