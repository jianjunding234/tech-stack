package com.dingjianjun.basetech.algorithm.tree;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/22
 */
public class TreeNode<T> {
    T value;
    TreeNode<T> left;
    TreeNode<T> right;
    TreeNode<T> parent;

    public TreeNode(T value, TreeNode<T> left, TreeNode<T> right, TreeNode<T> parent) {
        this.value = value;
        this.left = left;
        this.right = right;
        this.parent = parent;
    }
}
