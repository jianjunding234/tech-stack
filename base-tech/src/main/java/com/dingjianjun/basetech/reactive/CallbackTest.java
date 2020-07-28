package com.dingjianjun.basetech.reactive;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/7/3
 */
public class CallbackTest {
    public static void main(String[] args) {
        CallbackService callbackService = new CallbackServiceImpl();
        callbackService.addListener("foo", new CallbackListener() {
            @Override
            public void changed(String msg) {
                System.out.println("changed:" + msg);
            }
        });

    }
}
