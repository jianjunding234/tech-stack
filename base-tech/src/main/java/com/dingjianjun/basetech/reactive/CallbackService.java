package com.dingjianjun.basetech.reactive;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/7/3
 */
public interface CallbackService {
    void addListener(String key, CallbackListener listener);
}
