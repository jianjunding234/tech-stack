package com.dingjianjun.basetech.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * @author : Jianjun.Ding
 * @description: CompletableFuture剖析 （如何管理各种Future结果）
 * @date 2020/4/21
 */
@Slf4j
public class CompletableFutureDemo {
    public static void main(String[] args) {
        test();

    }


    public static void test() {
        CompletableFuture<Long> cf = invokeRpc().thenApply(Function.identity());
        CompletableFuture<Long> cf2 = cf.whenComplete((result, t) -> {
            if (null == t) {
                System.out.println(result);
            } else {
                t.printStackTrace();
                System.err.println("errorMsg: " + t.getMessage());
            }
        });

        CompletableFuture<Long> cf3 = cf2.handle((result, t) -> {
            Long ret = 0L;
            if (null == t) {
                System.out.println(result);
                ret = result * 10;
            } else {
                t.printStackTrace();
                System.err.println("errorMsg: " + t.getMessage());
            }
            return ret;
        });

        try {
            Long r = cf3.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


    }

    public static CompletableFuture<Long> invokeRpc() {
        return CompletableFuture.supplyAsync(() ->
                (long)IntStream.range(0, 10000).sum()
        );
    }


    public static void test2() {
        // 异步计算
        CompletableFuture<Double> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double price = Math.random();
            log.info("计算商品A的价格{}", price);
            return price;
        });

        CompletableFuture<Double> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double price = Math.random();
            log.info("计算商品B的价格{}", price);
            return price;
        });

        CompletableFuture<Double> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double price = Math.random();
            log.info("计算商品C的价格{}", price);
            return price;
        });

        // 管理所有的future结果
        CompletableFuture.allOf(future, future2, future3).join();

        log.info("end---");

        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double price = Math.random();
            log.info("计算商品B的价格{}", price);
            return price;
        }).thenApply(String::valueOf)
                .thenApply((s) -> "play" + s)
                .thenAccept(System.out::println);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
