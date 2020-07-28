package com.dingjianjun.basetech;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/20
 */
public class ResourceLoadTest {

    @Test
    public void test() throws IOException, InterruptedException {
//        ClassLoader classLoader = ResourceLoadTest.class.getClassLoader();
//        InputStream resourceAsStream = classLoader.getResourceAsStream("application.properties");
//        Properties properties = new Properties();
//        properties.load(resourceAsStream);
//        System.out.println(properties);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("exit...")));
        Thread.sleep(100000L);




    }
}
