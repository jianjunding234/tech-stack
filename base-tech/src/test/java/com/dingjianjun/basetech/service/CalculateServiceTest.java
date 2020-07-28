package com.dingjianjun.basetech.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/16
 */
@SpringBootTest
public class CalculateServiceTest {
    @Autowired
    private CalculateService calculateService;

    @Test
    public void testAdd() {
        Integer result = calculateService.add(1, 2);
        Assertions.assertEquals(3, result);
    }
}
