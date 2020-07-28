package com.dingjianjun.basetech.algorithm.tree;

import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/22
 */
public class TreeTraverse {

    /**
     * 先序遍历 根左右
     * @param root
     * @param <T>
     */
    public static <T> void preOrder(TreeNode<T> root) {
        if (root == null) {
            return;
        }

        System.out.print(root.value + "->");
        preOrder(root.left);
        preOrder(root.right);
    }

    /**
     * 非递归实现先序遍历
     * @param root
     * @param <T>
     */
    public static <T> void preOrder2(TreeNode<T> root) {
        if (root == null) {
            return;
        }
        Stack<TreeNode<T>> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode<T> node = stack.pop();
            System.out.print(node.value + "->");
            if (null != node.right) {
                stack.push(node.right);
            }
            if (null != node.left) {
                stack.push(node.left);
            }
        }
    }

    /**
     * 中序遍历 左根右
     * @param root
     * @param <T>
     */
    public static <T> void inOrder(TreeNode<T> root) {
        if (root == null) {
            return;
        }
        inOrder(root.left);
        System.out.print(root.value + "->");
        inOrder(root.right);
    }

    public static <T> void inOrder2(TreeNode<T> root) {
        if (null == root) {
            return;
        }

        Stack<TreeNode<T>> stack = new Stack<>();
        // 保存已遍历的节点
        Set<TreeNode<T>> traversedSet = new HashSet<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            // 窥视栈顶元素
            TreeNode<T> node = stack.peek();
            TreeNode<T> cur = node;
            while (null != cur.left && !traversedSet.contains(cur.left)) {
                stack.push(cur.left);
                cur = cur.left;
            }

            node = stack.pop();
            System.out.print(node.value + "->");
            traversedSet.add(node);
            if (null != node.right) {
                stack.push(node.right);
            }
        }
    }

    /**
     * 后序遍历 左右根
     *
     * @param root
     * @param <T>
     */
    public static <T> void postOrder(TreeNode<T> root) {
        if (root == null) {
            return;
        }
        postOrder(root.left);
        postOrder(root.right);
        System.out.print(root.value + "->");
    }

    public static <T> void postOrder2(TreeNode<T> root) {
        if (null == root) {
            return;
        }

        Stack<TreeNode<T>> stack = new Stack<>();
        // 保存已遍历的节点
        Set<TreeNode<T>> traversedSet = new HashSet<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            // 窥视栈顶元素
            TreeNode<T> node = stack.peek();
            TreeNode<T> cur = node;
            while (null != cur.left && !traversedSet.contains(cur.left)) {
                stack.push(cur.left);
                cur = cur.left;
            }

            node = stack.peek();
            if (null == node.right || traversedSet.contains(node.right)) {
                stack.pop();
                System.out.print(node.value + "->");
                traversedSet.add(node);
            } else {
                stack.push(node.right);
            }
        }
    }


    public static <T> void hierarchy(TreeNode<T> root) {
        if (null == root) {
            return;
        }

        LinkedList<TreeNode<T>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            // 窥视队头元素
            TreeNode<T> node = queue.peek();
            if (null != node.left) {
                queue.offer(node.left);
            }

            if (null != node.right) {
                queue.offer(node.right);
            }

            node = queue.poll();
            System.out.print(node.value + "->");
        }
    }

    public static <T> TreeNode<T> buildTree(T[] elems) {
        if (ArrayUtils.isEmpty(elems)) {
            return null;
        }

        TreeNode<T> root = new TreeNode<>(elems[0], null, null, null);
        if (elems.length == 1) {
            return root;
        }

        TreeNode<T> parent = root;
        for (int i = 1; i < elems.length; i++) {
            TreeNode<T> node = new TreeNode<>(elems[i], null, null, parent);
            if (null == parent.left) {
                parent.left = node;
            } else if (null == parent.right) {
                parent.right = node;
                parent = parent.left;
            }
        }

        return root;
    }



    public static void main(String[] args) {
        Integer[] elems = new Integer[] {1,3,5,7,9,11,13};
        TreeNode<Integer> root = buildTree(elems);
        System.out.println("preOrder----------------");
        preOrder(root);
        System.out.println();
        System.out.println("preOrder2----------------");
        preOrder2(root);
        System.out.println();

        System.out.println("inOrder----------------");
        inOrder(root);
        System.out.println();
        System.out.println("inOrder2----------------");
        inOrder2(root);
        System.out.println();


        System.out.println("postOrder----------------");
        postOrder(root);
        System.out.println();
        System.out.println("postOrder2----------------");
        postOrder2(root);
        System.out.println();

        System.out.println("hierarchy----------------");
        hierarchy(root);
        System.out.println();



    }
}
