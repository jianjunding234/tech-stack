package com.dingjianjun.basetech.midware.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/8
 */
@Slf4j
public class CustomSerializer implements Serializer<User> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        log.info("CustomeSerializer configure");


    }

    @Override
    public byte[] serialize(String topic, User data) {
        return SerializationUtils.serialize(data);
    }

    @Override
    public void close() {

    }
}
