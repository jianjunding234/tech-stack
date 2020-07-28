package com.dingjianjun.basetech.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/25
 */
public class DaemonTest {
    public static void main(String[] args) throws InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
           System.out.println("ShutdownHook execute start...");
            System.out.println("Netty NioEventLoopGroup shutdownGracefully...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("ShutdownHook execute end...");


        }, "hook-thread"));

        TimeUnit.SECONDS.sleep(7);
        System.exit(0);
    }
}
