package com.dingjianjun.basetech.mq;

import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Jianjun.Ding
 * @description: 消费者处理器
 * @date 2020/4/30
 */
@Slf4j
public class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        log.info("{} ,sequence:{}, endOfBatch:{}, threadName:{}", event, sequence, endOfBatch, Thread.currentThread().getName());

    }
}
