package com.dingjianjun.basetech.reactive;

import java.util.function.Consumer;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/22
 */
public class PromiseDemo {

    public static void main(String[] args) {
        new Thread(new Worker<Integer>(System.out::println) ,"TT").start();
    }


    static class Worker<T> implements Runnable {
        Consumer<T> consumer;
        Integer a = 1;
        Worker(Consumer<T> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void run() {
            try {
                System.out.println("do something...");
            } finally {
                consumer.accept((T) a);
            }
        }
    }

}
