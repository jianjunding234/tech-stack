package com.dingjianjun.basetech.midware.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/8
 */
@Slf4j
public class CustomDeserializer implements Deserializer<User> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        log.info("CustomeDeserializer configure");


    }

    @Override
    public User deserialize(String topic, byte[] data) {
        return SerializationUtils.deserialize(data);
    }

    @Override
    public void close() {

    }
}
