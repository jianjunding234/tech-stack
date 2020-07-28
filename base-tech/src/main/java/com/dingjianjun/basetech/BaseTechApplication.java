package com.dingjianjun.basetech;

import com.dingjianjun.basetech.midware.kafka.KafkaConsumerDemo;
import com.dingjianjun.basetech.midware.kafka.transaction.KafkaConsumerAndProducerTransaction;
import com.dingjianjun.basetech.midware.kafka.transaction.KafkaConsumerTransaction;
import com.dingjianjun.basetech.midware.redis.RedisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Field;

@SpringBootApplication
public class BaseTechApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BaseTechApplication.class, args);
//        KafkaConsumerTransaction.subscribe();
//        KafkaConsumerAndProducerTransaction.consumeAndProduce();
//        RedisTest redisTest = context.getBean(RedisTest.class);
//        redisTest.testBase();
    }

}
