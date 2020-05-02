package com.dingjianjun.basetech.mq;

import com.dingjianjun.basetech.concurrent.MyThreadFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * @author : Jianjun.Ding
 * @description: Disruptor测试
 * @date 2020/4/30
 */
public class DisruptorDemo {
    public static void main(String[] args) {
        LongEventFactory factory = new LongEventFactory();
        LongEventHandler eventHandler = new LongEventHandler();
        // buffer size
        int ringBufferSize = 1024;
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, ringBufferSize, new MyThreadFactory("disruptorDemo"));
        disruptor.handleEventsWith(eventHandler);
        // 启动 disruptor
        disruptor.start();
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        long sequence = ringBuffer.next();
        try {
            LongEvent longEvent = ringBuffer.get(sequence);
            // 填充数据
            longEvent.setValue(8888L);
        } finally {
            // 发布事件
            ringBuffer.publish(sequence);
        }

    }
}
