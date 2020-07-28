package com.dingjianjun.basetech.reactive;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/7/3
 */
public class CallbackServiceImpl implements CallbackService {
    private final ConcurrentMap<String, CallbackListener> listeners = new ConcurrentHashMap<>(64);
    @Override
    public void addListener(String key, CallbackListener listener) {
        listeners.put(key, listener);
    }

    public CallbackServiceImpl() {
        Thread t = new Thread(() -> {
            while(true) {
                for (Map.Entry<String, CallbackListener> e : listeners.entrySet()) {
                    try {
                        e.getValue().changed(getMsg(e.getKey()));
                    } catch (Throwable throwable) {
                        listeners.remove(e.getKey());
                    }
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }, "async");

        t.start();
    }

    private String getMsg(String key) {
        return key + "-idx";
    }
}
