package com.dingjianjun.basetech.midware.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @author : Jianjun.Ding
 * @description: test
 * @date 2020/5/15
 */
@Component
@Slf4j
public class RedisTest implements InitializingBean {
    @Resource
    @Qualifier("myRedisTemplate")
    private StringRedisTemplate template;
    @Resource
    private ObjectMapper objectMapper;

    public void testBase() {
//        RedisConnection connection = template.getConnectionFactory().getConnection();
//        connection.set("k1".getBytes(Charsets.UTF_8), "aaa".getBytes(Charsets.UTF_8));

        // String类型
//        template.opsForValue().set("ooxx", "sx");
//        log.info("key:ooxx,value:{}", template.opsForValue().get("ooxx"));
//        template.opsForValue().setBit("xx", 1, true);
//        log.info("key:xx, value:{}", template.opsForValue().getBit("xx", 1));

        // List类型
//        template.opsForList().rightPushAll("listxx", "234", "345");
//        log.info("key:listxx, value:{}", template.opsForList().leftPop("listxx"));
//        template.opsForList().set("listxx", 0, "aa");

        // hash类型
//        People p = new People("sx", 12);
//        Map<String,Object> objMap = new Jackson2HashMapper(false).toHash(p);
//        template.opsForHash().putAll("hooxx", objMap);
//        Map<Object, Object> resMap = template.opsForHash().entries("hooxx");
//        People retObj = objectMapper.convertValue(resMap, People.class);
//        log.info("{}", retObj);

        // set 类型
//        template.opsForSet().add("sxx", "aa", "bb", "cc", "dd", "ee");
////        // 取出一个不带重复元素的结果集
////        Set<String> randSet = template.opsForSet().distinctRandomMembers("sxx", 4);
////        log.info("{}", randSet);
////        // 取出一个带重复元素的结果集
////        List<String> dupList2 = template.opsForSet().randomMembers("sxx", 4);
////        log.info("{}", dupList2);
////
////        // 移除并返回 count 个元素 （不超过当前元素个数）
////        List<String> popList = template.opsForSet().pop("sxx", 3);
////        log.info("{}", popList);

//        // zset 类型
//        template.opsForZSet().add("zsxx", "apple", 50.0);
//        template.opsForZSet().add("zsxx", "banana", 30.0);
//        template.opsForZSet().add("zsxx", "orange", 80.0);
//        template.opsForZSet().add("zsxx", "grape", 70.0);
//        Set<String> set = template.opsForZSet().range("zsxx", 0, 2);
//        log.info("{}", set);
//        Set<String> set2 = template.opsForZSet().rangeByScore("zsxx", 50.0, 80.0);
//        log.info("{}", set2);

        // 发布
//        RedisConnection connection = template.getConnectionFactory().getConnection();
//        //订阅
//        connection.subscribe(new MessageListener() {
//            @Override
//            public void onMessage(Message message, byte[] pattern) {
//                log.info("{}", new String(message.getBody()));
//
//            }
//        }, "life".getBytes(Charsets.UTF_8));
//
//        while (true) {
//            // 发布
//            template.convertAndSend("life", "from xx: hello");
//            try {
//                TimeUnit.SECONDS.sleep(3L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void testlua() {
        org.springframework.core.io.Resource resource = new ClassPathResource("limit.lua");
        RedisScript<Long> redisScript = RedisScript.of(resource, Long.class);
        String limit = "3"; // 时间窗限流：限制每秒请求数最大为3个
        for (int i = 0; i < 10; i++) {
            String key = "time:window:" + System.currentTimeMillis() / 1000; // 当前秒
            Long retVal = template.execute(redisScript, Collections.singletonList(key), limit);
            if (Objects.equal(retVal, 1L)) {
                System.out.println("acquire success...");
            } else {
                System.err.println("acquire failure，已超过限流大小，拒绝该请求");
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        testlua();
    }
}
