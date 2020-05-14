package com.dingjianjun.basetech.midware.zk.curator;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @author : Jianjun.Ding
 * @description: curator创建session
 * @date 2020/5/7
 */
@Slf4j
public class CreateSessionDemo {
    private static final String PATH = "/demo";
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
                .forPath(PATH, "init".getBytes(Charsets.UTF_8));

        // 节点状态
        Stat stat = new Stat();
        String nodeData = new String(client.getData().storingStatIn(stat).forPath(PATH));
        log.info("{}", nodeData);
        // 更新数据
        client.setData().withVersion(stat.getVersion()).forPath(PATH, "init001".getBytes(Charsets.UTF_8));
        nodeData = new String(client.getData().storingStatIn(stat).forPath(PATH));
        log.info("{}", nodeData);

        // 删除节点，强制指定版本进行删除
//        client.delete().deletingChildrenIfNeeded()
//                .withVersion(stat.getVersion())
//                .forPath(PATH);




    }
}
