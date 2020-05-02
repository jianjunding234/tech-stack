package com.dingjianjun.basetech.mq;

import com.dingjianjun.basetech.concurrent.MyThreadFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Jianjun.Ding
 * @description: disruptor支持lambda
 * @date 2020/4/30
 */
@Slf4j
public class DisruptorDemo2 {
    public static void main(String[] args) {
        // buffer size
        int ringBufferSize = 1024;
        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, ringBufferSize,
                new MyThreadFactory("disruptorDemo"));
        disruptor.handleEventsWith((event, sequence, endOfBatch) ->
                log.info("{} ,sequence:{}, endOfBatch:{}, threadName:{}", event, sequence, endOfBatch, Thread.currentThread().getName()),
                (event, sequence, endOfBatch) ->
                        log.info("{} ,sequence:{}, endOfBatch:{}, threadName:{}", event, sequence, endOfBatch, Thread.currentThread().getName()));
        // 启动 disruptor
        disruptor.start();

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent((event, sequence) -> {
            event.setValue(666L);
        });

        ringBuffer.publishEvent((event, sequence, arg0) -> event.setValue(arg0), 999L);
        ringBuffer.publishEvent((event, sequence, arg0, arg1) -> event.setValue(arg0 + arg1), 1000L, 2000L);


    }
}
