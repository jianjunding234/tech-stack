package com.dingjianjun.basetech.mq;

/**
 * @author : Jianjun.Ding
 * @description: 事件
 * @date 2020/4/30
 */
public class LongEvent {
    private Long value;

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LongEvent{" +
                "value=" + value +
                '}';
    }
}
