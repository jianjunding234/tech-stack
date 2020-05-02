package com.dingjianjun.basetech.concurrent;

import org.openjdk.jmh.annotations.*;

/**
 * @author : Jianjun.Ding
 * @description: test
 * @date 2020/4/30
 */
public class ThreadPoolDemoTest {
    @Benchmark
    @Warmup(iterations = 1, time = 3)
    @Fork(5)
    @BenchmarkMode(Mode.Throughput)
    @Measurement(iterations = 1, time = 3)
    public void testUseForJoinPool() {
        ThreadPoolDemo.useForJoinPool();

    }
}
