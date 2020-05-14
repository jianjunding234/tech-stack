package com.dingjianjun.basetech.midware.zk;

import com.dingjianjun.basetech.midware.zk.OptimizedZkLock;
import com.dingjianjun.basetech.midware.zk.ZkConfig;
import com.dingjianjun.basetech.util.ZkUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author : Jianjun.Ding
 * @description: test ZK分布式锁
 * @date 2020/5/6
 */
@Slf4j
public class ZkLockTest {
    private ZooKeeper zk;

    @BeforeEach
    public void conn() {
        ZkConfig conf = new ZkConfig("192.168.1.102:2181,192.168.1.103:2181,192.168.1.106:2181/exc_lock", 10000);
        zk = ZkUtils.getZk(conf);
    }

    @Test
    public void testLock() {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                Thread t = Thread.currentThread();
                OptimizedZkLock lock = new OptimizedZkLock(zk, t);
                log.info("{} try lock...", t.getName());
                lock.tryLock();
                log.info("{} working...", t.getName());

                try {
                    TimeUnit.MILLISECONDS.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                lock.unLock();
                log.info("{} release lock...", t.getName());
            }, "ThreadLock-" + (i + 1)).start();
        }

        try {
            TimeUnit.SECONDS.sleep(600L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @AfterEach
    public void close() {
        ZkUtils.close();
    }


}
