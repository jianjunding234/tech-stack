package com.dingjianjun.basetech.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/17
 */
@SpringBootTest
public class CircularReferenceTest {
    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    @Test
    public void test() {

    }


}
