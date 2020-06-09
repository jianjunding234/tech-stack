package com.dingjianjun.basetech.midware.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Date;
import java.util.Properties;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/7
 */
public class KafkaProducerDemo {
    public static void main(String[] args) {
        KafkaProducer<String,User> kafkaProducer = buildKafkaProducer();
        for (int i = 0; i < 1; i++) {
            User u = new User((i + 1), (i + 1) + "", new Date());
            ProducerRecord<String,User> record = new ProducerRecord<>("topic01", "idempotence-" + i, u);
            kafkaProducer.send(record);
        }

        // 关闭连接
        kafkaProducer.close();
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
        prop.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 1);
        // 开启幂等：保证数据不丢失、不重复
        prop.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        prop.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);

        return  new KafkaProducer<>(prop);
    }

}
