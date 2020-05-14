package com.dingjianjun.basetech.algorithm.hash.sample;

import com.dingjianjun.basetech.algorithm.hash.Node;

/**
 * @author : Jianjun.Ding
 * @description: 物理节点
 * @date 2020/5/14
 */
public class RealNode implements Node {
    private String dci;
    private String ip;
    private int port;

    public RealNode(String dci, String ip, int port) {
        this.dci = dci;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String getKey() {
        return dci + "-" + ip + "-" + port;
    }

    @Override
    public String toString() {
        return "RealNode{" +
                "dci='" + dci + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
