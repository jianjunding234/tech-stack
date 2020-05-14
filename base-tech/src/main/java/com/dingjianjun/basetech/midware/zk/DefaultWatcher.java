package com.dingjianjun.basetech.midware.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * @author : Jianjun.Ding
 * @description: zookeeper默认监听器  Session级别
 * @date 2020/5/6
 */
@Slf4j
public class DefaultWatcher implements Watcher {
    private CountDownLatch latch;
    public DefaultWatcher(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void process(WatchedEvent event) {
        log.info("default watcher监听事件通知 {}", event);
        switch (event.getState()) {
            case Unknown:
                break;
            case Disconnected:
                break;
            case NoSyncConnected:
                break;
            case SyncConnected:
                latch.countDown();
                break;
            case AuthFailed:
                break;
            case ConnectedReadOnly:
                break;
            case SaslAuthenticated:
                break;
            case Expired:
                break;
            case Closed:
                break;
        }
    }
}
