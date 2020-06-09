package com.dingjianjun.basetech.midware.kafka;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/8
 */
@Slf4j
public class CustomProducerInterceptor implements ProducerInterceptor {
    /**
     * 对生产者发送的消息（到达Kafka服务器之前）进行拦截
     * @param record
     * @return
     */
    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        log.info("CustomProducerInterceptor onSend>>>");
        record.headers().add("01", "xx".getBytes(Charsets.UTF_8));
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        log.info("CustomProducerInterceptor onAcknowledgement>>> metadata:{}, exception:{}", metadata, exception);

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {
        log.info("CustomProducerInterceptor configure");

    }
}
