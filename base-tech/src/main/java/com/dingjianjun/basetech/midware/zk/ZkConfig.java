package com.dingjianjun.basetech.midware.zk;

/**
 * @author : Jianjun.Ding
 * @description: ZK配置
 * @date 2020/5/7
 */
public class ZkConfig {
    private String connectString;
    private int sessionTimeOut;

    public ZkConfig(String connectString, int sessionTimeOut) {
        this.connectString = connectString;
        this.sessionTimeOut = sessionTimeOut;
    }

    public String getConnectString() {
        return connectString;
    }

    public int getSessionTimeOut() {
        return sessionTimeOut;
    }
}
