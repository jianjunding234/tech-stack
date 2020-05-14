package com.dingjianjun.basetech.algorithm.hash;

import java.util.Objects;

/**
 * @author : Jianjun.Ding
 * @description: 虚拟节点，作为物理节点的副本
 * @date 2020/5/14
 */
public class VirtualNode<T extends Node> implements Node {
    /**
     * 所属物理节点
     */
    private T physicalNode;
    /**
     * 所属物理节点的副本索引
     */
    private int replicaIndex;

    public VirtualNode(T physicalNode, int replicaIndex) {
        this.physicalNode = physicalNode;
        this.replicaIndex = replicaIndex;
    }

    @Override
    public String getKey() {
        return physicalNode.getKey() + "-" + replicaIndex;
    }

    /**
     * 当前对象是否属于指定物理节点的虚拟节点
     * @param pNode
     * @return
     */
    public boolean isVirtualNodeOf(T pNode) {
        return Objects.equals(physicalNode.getKey(), pNode.getKey());
    }

    public T getPhysicalNode() {
        return physicalNode;
    }
}
