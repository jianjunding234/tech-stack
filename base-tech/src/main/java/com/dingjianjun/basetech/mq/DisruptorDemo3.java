package com.dingjianjun.basetech.mq;

import com.dingjianjun.basetech.concurrent.MyThreadFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Jianjun.Ding
 * @description: Disruptor ExceptionHandler
 * @date 2020/4/30
 */
@Slf4j
public class DisruptorDemo3 {
    public static void main(String[] args) {
        // buffer size
        int ringBufferSize = 1024;
        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, ringBufferSize,
                new MyThreadFactory("disruptorDemo"),
                ProducerType.MULTI,
                new SleepingWaitStrategy());
        EventHandler eventHandler = (event, sequence, endOfBatch) ->{
            log.info("{} ,sequence:{}, endOfBatch:{}, threadName:{}", event, sequence, endOfBatch, Thread.currentThread().getName());
            throw new Exception("消费者异常");
        };
        disruptor.handleEventsWith(eventHandler);
        disruptor.handleExceptionsFor(eventHandler).with(new ExceptionHandler() {
            @Override
            public void handleEventException(Throwable ex, long sequence, Object event) {
                log.error("{}", ex);

            }

            @Override
            public void handleOnStartException(Throwable ex) {

            }

            @Override
            public void handleOnShutdownException(Throwable ex) {

            }
        });
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
