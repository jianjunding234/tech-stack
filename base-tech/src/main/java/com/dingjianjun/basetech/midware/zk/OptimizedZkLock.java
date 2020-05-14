package com.dingjianjun.basetech.midware.zk;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @author : Jianjun.Ding
 * @description: 优化的ZK分布式锁 (创建临时顺序节点)
 * @date 2020/5/6
 */
@Slf4j
public class OptimizedZkLock implements Watcher, AsyncCallback.StringCallback,
        AsyncCallback.Children2Callback, AsyncCallback.StatCallback {
    private final ZooKeeper zk;
    private final Thread thread;
    private CountDownLatch latch = new CountDownLatch(1);
    /**
     * 保存新建节点的相对路径
     */
    private String pathName;

    public OptimizedZkLock(ZooKeeper zk, Thread thread) {
        this.zk = zk;
        this.thread = thread;
    }

    /**
     * 获取分布式锁（阻塞等待获取）
     *
     */
    public void tryLock() {
        // 创建临时顺序节点
        zk.create("/lock", thread.getName().getBytes(Charsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL, this,"tryLock");
        try {
            latch.await();
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
    public void processResult(int rc, String path, Object ctx, String name) {
        if (!StringUtils.isEmpty(name)) {
            log.info("name:{}", name);
            pathName = name;
            // 异步获取父节点下的孩子列表
            zk.getChildren("/", false, this, "getChildren");
        }

    }

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        log.info("children:{}", children);

        // 对子节点列表按节点名称排序
        Collections.sort(children);
        // 按节点名称查找自己在子节点列表中的位置
        int index = children.indexOf(pathName.substring(1));
        // 如果自己的序号最小
        if (index == 0) {
            latch.countDown();
        } else {
            // 在自己前面一个子节点上注册Watcher
            zk.exists("/" + children.get(index - 1), this, this, "exists");
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
                zk.getChildren("/", false, this, "getChildren");
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
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (Objects.isNull(stat)) {
            // 异步获取父节点下的孩子列表
            zk.getChildren("/", false, this, "getChildren");
        }

    }
}
