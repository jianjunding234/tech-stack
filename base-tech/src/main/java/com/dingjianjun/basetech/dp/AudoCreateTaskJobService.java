package com.dingjianjun.basetech.dp;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : Jianjun.Ding
 * @description: 自动创建任务job
 * @date 2020/4/21
 */
@Slf4j
public class AudoCreateTaskJobService implements JobService {
    @Override
    public void execute() {
        log.info("AudoCreateTaskJob executing >>>");
    }
}
