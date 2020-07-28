package com.dingjianjun.basetech.algorithm;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/29
 */
public class TreeSolution {

    /**
     * 请完成一个函数，输入一个二叉树，该函数输出它的镜像。
     * 解决思路：交换左右子树，先序遍历（递归调用）
     * @param root
     * @return
     */
    public TreeNode mirrorTree(TreeNode root) {
        if (null == root || (root.left == null && root.right == null)) {
            return root;
        }

        TreeNode tmp = root.left;
        // 交换左右子树
        root.left = root.right;
        root.right = tmp;

        // 先序遍历
        if (root.left != null) {
            mirrorTree(root.left);
        }

        if (root.right != null) {
            mirrorTree(root.right);
        }

        return root;
    }


    /**
     * 请实现一个函数，用来判断一棵二叉树是不是对称的。
     * 如果一棵二叉树和它的镜像一样，那么它是对称的。
     * 例如，二叉树 [1,2,2,3,4,4,3] 是对称的。
     * 但是下面这个 [1,2,2,null,3,null,3] 则不是镜像对称的:
     * 解决思路：定义一种对称前序的遍历方式：根、右、左，遍历结果与前序遍历结果相同，则二叉树是对称的，否则非对称
     * 遍历算法（递归实现）
     * @param root
     * @return
     */
    public boolean isSymmetric(TreeNode root) {
        return isSymmetric(root, root);
    }

    public boolean isSymmetric(TreeNode p1, TreeNode p2) {
        if (p1 == null && p2 == null) {
            return true;
        }

        if (p1 == null || p2 == null) {
            return false;
        }

        if (p1.val != p2.val) {
            return false;
        }

        return isSymmetric(p1.left, p2.right) && isSymmetric(p1.right, p2.left);
    }

    /**
     * 二叉树的最大深度
     * @param root
     * @return
     */
    public int maxDepth(TreeNode root) {
        if (null == root) {
            return 0;
        }

        int lChildDepth = maxDepth(root.left);
        int rChildDepth = maxDepth(root.right);
        int maxDepth = lChildDepth + 1;
        if (rChildDepth > lChildDepth) {
            maxDepth = rChildDepth + 1;
        }
        return maxDepth;
    }

    /**
     * 输入一棵二叉树的根节点，判断该树是不是平衡二叉树。
     * 如果某二叉树中任意节点的左右子树的深度相差不超过1，那么它就是一棵平衡二叉树。
     * @param root
     * @return
     */
    public boolean isBalanced(TreeNode root) {
       boolean[] ans = new boolean[1];
       ans[0] = true;
       process(root, ans);
       return ans[0];
    }

    private int process(TreeNode root, boolean[] ans) {
        if (!ans[0] || root == null) {
            return -1;
        }

        int leftDepth = process(root.left, ans);
        int rightDepth = process(root.right, ans);
        if (Math.abs(leftDepth - rightDepth) > 1) {
            // 非平衡
            ans[0] = false;
        }

        return Math.max(leftDepth, rightDepth) + 1;
    }

    public boolean isBalanced2(TreeNode root) {
        return process2(root).isBalanced;
    }

    private Info process2(TreeNode root) {
        if (null == root) {
            return new Info(0, true);
        }

        Info lChild = process2(root.left);
        Info rChild = process2(root.right);
        boolean isBalanced = true;
        // 不平衡条件
        if (!lChild.isBalanced || !rChild.isBalanced || Math.abs(lChild.height - rChild.height) > 1) {
            isBalanced = false;
        }

        return new Info(Math.max(lChild.height, rChild.height) + 1, isBalanced);
    }

    static class Info {
        int height;
        boolean isBalanced;
        public Info(int height, boolean isBalanced) {
            this.height = height;
            this.isBalanced = isBalanced;
        }
    }

}
