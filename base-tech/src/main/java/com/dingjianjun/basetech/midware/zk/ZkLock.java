package com.dingjianjun.basetech.midware.zk;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @author : Jianjun.Ding
 * @description: ZK实现分布式锁
 * @date 2020/5/6
 */
@Slf4j
public class ZkLock implements Watcher, AsyncCallback.Create2Callback, AsyncCallback.StatCallback {
    private final ZooKeeper zk;
    private final Thread thread;
    private CountDownLatch latch = new CountDownLatch(1);
    private static final String pathName = "/lock";

    public ZkLock(ZooKeeper zk, Thread thread) {
        this.zk = zk;
        this.thread = thread;
    }

    /**
     * 获取分布式锁（阻塞等待获取）
     * TODO：锁的可重入
     */
    public void tryLock() {
        // 判断锁是否被其他事务持有
        try {
            Stat stat = zk.exists(pathName, false);
            if (Objects.nonNull(stat)) {
                // 注册Watcher监听事件
                zk.exists(pathName, this, this, "exists");
            } else {
                // 创建临时节点
                zk.create(pathName, thread.getName().getBytes(Charsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
                        this, "tryLock");
            }
            latch.await();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放锁
     */
    public void unLock() {
        try {
            // version:-1 忽略版本
            zk.delete(pathName, -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zk.create(pathName, thread.getName().getBytes(Charsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
                        this, "tryLock");
                break;
            case NodeDataChanged:
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
    public void processResult(int rc, String path, Object ctx, String name, Stat stat) {
        if (!StringUtils.isEmpty(name)) {
            latch.countDown();
            return;
        }

        zk.exists(pathName, this, this, "exists");
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (Objects.isNull(stat)) {
            // 创建临时节点
            zk.create(pathName, thread.getName().getBytes(Charsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
                    this, "tryLock");
        }

    }
}
