package com.dingjianjun.basetech.midware.zk;

import com.dingjianjun.basetech.util.ZkUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author : Jianjun.Ding
 * @description: test ZK分布式配置中心
 * @date 2020/5/7
 */
@Slf4j
public class ZkConfigCenterTest {
    private ZooKeeper zk;

    @BeforeEach
    public void conn() {
        ZkConfig conf = new ZkConfig("192.168.1.102:2181,192.168.1.103:2181,192.168.1.106:2181", 10000);
        zk = ZkUtils.getZk(conf);
    }

    @Test
    public void testConfigCenter() {
        ConfigInfo configInfo = new ConfigInfo();
        String watchPath = "/appConf";
        ZkConfigCenter configCenter = new ZkConfigCenter(zk, configInfo, watchPath);
        configCenter.await();
        while (true) {
            if (StringUtils.isEmpty(configInfo.getConfMsg())) {
                log.info("has no config...");
                configCenter.await();
            } else {
                log.info("confMsg:{}", configInfo.getConfMsg());
            }

            try {
                TimeUnit.SECONDS.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterEach
    public void close() {
        ZkUtils.close();
    }
}
