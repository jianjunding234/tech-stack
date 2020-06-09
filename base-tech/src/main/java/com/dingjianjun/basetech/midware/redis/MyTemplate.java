package com.dingjianjun.basetech.midware.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * @author : Jianjun.Ding
 * @description: 自定义RedisTemplate
 * @date 2020/5/15
 */
@Configuration
public class MyTemplate {
    @Bean
    public StringRedisTemplate myRedisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate(connectionFactory);
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        return template;
    }
}
