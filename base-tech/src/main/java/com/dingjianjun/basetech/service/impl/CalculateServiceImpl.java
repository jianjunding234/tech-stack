package com.dingjianjun.basetech.service.impl;

import com.dingjianjun.basetech.service.CalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/16
 */
@Service
@Slf4j
public class CalculateServiceImpl implements CalculateService {
    @Override
    @Transactional
    public Integer add(Integer var1, Integer var2) {
        if (null == var1 || null == var2) {
            throw new IllegalArgumentException("[add] parameters cannot be null");
        }
        log.info("add executing...");

        return var1 + var2;
    }
}
