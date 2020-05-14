package com.dingjianjun.basetech.midware.zk.curator;

import com.dingjianjun.basetech.concurrent.MyThreadFactory;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.*;

/**
 * @author : Jianjun.Ding
 * @description: curator异步回调
 * @date 2020/5/7
 */
@Slf4j
public class CuratorCallBackDemo {
    private static final String PATH = "/demo";
    private static final ExecutorService executor = new ThreadPoolExecutor(2, 2,
            0L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(4),
            new MyThreadFactory("demo"),
            new ThreadPoolExecutor.AbortPolicy());
    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.1.102:2181")
                .sessionTimeoutMs(6000).connectionTimeoutMs(60000)
                .retryPolicy(retryPolicy)
                .namespace("app")
                .build();
        client.start();
        // 创建节点
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
//                .inBackground(new BackgroundCallback() {
//                    @Override
//                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
//                        log.info("event[code: {}, type: {}]", curatorEvent.getResultCode(), curatorEvent.getType());
//                        log.info("processResult thread name:{}", Thread.currentThread().getName());
//                    }
//                }, executor)
                .forPath(PATH, "init".getBytes(Charsets.UTF_8));

    }

}
