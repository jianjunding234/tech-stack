package com.dingjianjun.basetech.util;

import com.dingjianjun.basetech.midware.zk.DefaultWatcher;
import com.dingjianjun.basetech.midware.zk.ZkConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author : Jianjun.Ding
 * @description: Zookeeper工具类
 * @date 2020/5/6
 */
@Slf4j
public class ZkUtils {
    private static ZooKeeper zk;
    private static CountDownLatch latch = new CountDownLatch(1);

    /**
     * 获取Zookeeper实例，等待客户端与zookeeper建立TCP连接
     * @param zkConfig zk配置
     * @return
     */
    public static ZooKeeper getZk(ZkConfig zkConfig) {
        DefaultWatcher watcher = new DefaultWatcher(latch);
        /**
         * 完成客户端初始化工作后返回ZooKeeper实例，
         * 接着异步创建客户端与Zookeeper服务端的连接
         * 注册session级别的Watcher监听器（默认的），与path、znode无关
         */
        try {
            zk = new ZooKeeper(zkConfig.getConnectString(), zkConfig.getSessionTimeOut(), watcher);
            latch.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zk;
    }

    public static void close() {
        if (null != zk) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
