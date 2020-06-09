package com.dingjianjun.basetech.midware.kafka.transaction;

import com.dingjianjun.basetech.midware.kafka.CustomPartitioner;
import com.dingjianjun.basetech.midware.kafka.CustomProducerInterceptor;
import com.dingjianjun.basetech.midware.kafka.CustomSerializer;
import com.dingjianjun.basetech.midware.kafka.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 * @author : Jianjun.Ding
 * @description: 生产者事务only
 * @date 2020/6/7
 */
@Slf4j
public class KafkaProducerTransaction {
    public static void main(String[] args) {
        KafkaProducer<String,User> producer = buildKafkaProducer();
        // 初始化事务
        producer.initTransactions();
        try {
            // 开启事务
            producer.beginTransaction();
            for (int i = 0; i < 10; i++) {
//                if (i == 7) {
//                    int j = i / 0;
//                }
                User u = new User((i + 1), (i + 1) + "", new Date());
                ProducerRecord<String,User> record = new ProducerRecord<>("topic01", "idempotence-" + i, u);
                producer.send(record);
                producer.flush();
            }

            // 提交事务
            producer.commitTransaction();
        } catch (Exception ex) {
            log.error("出错了", ex.getMessage());
            // 回滚事务
            producer.abortTransaction();
        } finally {
            // 关闭连接
            producer.close();
        }

    }

    private static KafkaProducer<String,User> buildKafkaProducer() {
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
}
