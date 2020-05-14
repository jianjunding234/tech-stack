package com.dingjianjun.basetech.midware.zk;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * @author : Jianjun.Ding
 * @description: ZK实现分布式配置中心
 * @date 2020/5/7
 */
public class ZkConfigCenter implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {
    private ZooKeeper zk;
    private ConfigInfo config;
    private String watchPath;
    private CountDownLatch latch;

    public ZkConfigCenter(ZooKeeper zk, ConfigInfo config, String watchPath) {
        this.zk = zk;
        this.config = config;
        this.watchPath = watchPath;
    }

    /**
     * 等待发现配置数据
     */
    public void await() {
        zk.exists(watchPath, this, this, "initWatch");
        latch = new CountDownLatch(1);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                zk.getData(watchPath, this, this, "getData");
                break;
            case NodeDeleted:
                config.setConfMsg("");
                latch = new CountDownLatch(1);
                break;
            case NodeDataChanged:
                zk.getData(watchPath, this, this, "getData");
                break;
            case NodeChildrenChanged:
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
        }

    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        // 存在数据节点
        if (null != stat) {
            // 取数据
            zk.getData(watchPath, this, this, "getData");
        }

    }

    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        if (null != data) {
            config.setConfMsg(new String(data));
            latch.countDown();
        }

    }
}
