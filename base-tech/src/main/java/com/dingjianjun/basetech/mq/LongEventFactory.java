package com.dingjianjun.basetech.mq;

import com.lmax.disruptor.EventFactory;

/**
 * @author : Jianjun.Ding
 * @description: 事件工厂
 * @date 2020/4/30
 */
public class LongEventFactory implements EventFactory<LongEvent> {

    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
