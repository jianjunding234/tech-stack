package com.dingjianjun.basetech.spi;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author : Jianjun.Ding
 * @description: 数据库搜索
 * @date 2020/5/2
 */
@Slf4j
public class DatabaseSearch implements Search {

    @Override
    public List<String> search() {
        log.info("DatabaseSearch search---");
        return Lists.newArrayList();
    }
}
