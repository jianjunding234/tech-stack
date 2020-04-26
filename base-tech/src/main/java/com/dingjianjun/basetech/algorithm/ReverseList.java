package com.dingjianjun.basetech.algorithm;

import java.util.ArrayList;

/**
 * @author : Jianjun.Ding
 * @description: 链表逆序
 * @date 2020/4/25
 */
public class ReverseList {

    /**
     * 单链表反转
     * @param head 原单链表头结点
     * @return 反转后的单链表头结点
     */
    public static Node reverseLinkedList(Node head) {
        // pre 新的单链表头结点
        Node pre = null, next = null;
        while (head != null) {
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }

        return pre;
    }

    public static Node testReverseLinkedList(Node head) {
        if (head == null) {
            return null;
        }
        ArrayList<Node> list = new ArrayList<>();
        while (head != null) {
            list.add(head);
            head = head.next;
        }
        list.get(0).next = null;
        int N = list.size();
        for (int i = 1; i < N; i++) {
            list.get(i).next = list.get(i - 1);
        }
        return list.get(N - 1);
    }

    public static DNode reverseDoubleList(DNode head) {
        DNode pre = null, next = null;
        while (head != null) {
            next = head.next;
            head.next = pre;
            head.prev = next;
            pre = head;
            head = next;
        }
        return pre;
    }

    public static DNode testReverseDoubleList(DNode head) {
        if (head == null) {
            return null;
        }
        ArrayList<DNode> list = new ArrayList<>();
        while (head != null) {
            list.add(head);
            head = head.next;
        }
        list.get(0).next = null;
        DNode pre = list.get(0);
        int N = list.size();
        for (int i = 1; i < N; i++) {
            DNode cur = list.get(i);
            cur.prev = null;
            cur.next = pre;
            pre.prev = cur;
            pre = cur;
        }
        return list.get(N - 1);
    }


    public static Node generateRandomLinkedList(int len, int value) {
        int size = (int) (Math.random() * (len + 1));
        if (size == 0) {
            return null;
        }
        size--;
        Node head = new Node((int) (Math.random() * (value + 1)));
        Node pre = head;
        while (size != 0) {
            Node cur = new Node((int) (Math.random() * (value + 1)));
            pre.next = cur;
            pre = cur;
            size--;
        }
        return head;
    }

    public static DNode generateRandomDoubleList(int len, int value) {
        int size = (int) (Math.random() * (len + 1));
        if (size == 0) {
            return null;
        }
        size--;
        DNode head = new DNode((int) (Math.random() * (value + 1)));
        DNode pre = head;
        while (size != 0) {
            DNode cur = new DNode((int) (Math.random() * (value + 1)));
            pre.next = cur;
            cur.prev = pre;
            pre = cur;
            size--;
        }
        return head;
    }

    // 要求无环，有环别用这个函数
    public static boolean checkLinkedListEqual(Node head1, Node head2) {
        while (head1 != null && head2 != null) {
            if (head1.value != head2.value) {
                return false;
            }
            head1 = head1.next;
            head2 = head2.next;
        }
        return head1 == null && head2 == null;
    }

    // 要求无环，有环别用这个函数
    public static boolean checkDoubleListEqual(DNode head1, DNode head2) {
        boolean null1 = head1 == null;
        boolean null2 = head2 == null;
        if (null1 && null2) {
            return true;
        }
        if (null1 ^ null2) {
            return false;
        }
        if (head1.prev != null || head2.prev != null) {
            return false;
        }
        DNode end1 = null;
        DNode end2 = null;
        while (head1 != null && head2 != null) {
            if (head1.value != head2.value) {
                return false;
            }
            end1 = head1;
            end2 = head2;
            head1 = head1.next;
            head2 = head2.next;
        }
        if (head1 != null || head2 != null) {
            return false;
        }
        while (end1 != null && end2 != null) {
            if (end1.value != end2.value) {
                return false;
            }
            end1 = end1.prev;
            end2 = end2.prev;
        }
        return end1 == null && end2 == null;
    }

    /**
     * 删除单链表中等于给定值的结点
     * @param head
     * @param num
     * @return
     */
    public static Node removeValue(Node head, int num) {
        while (head != null) {
            if (head.value != num) {
                break;
            }
            head = head.next;
        }
        // head来到 第一个不需要删的位置
        Node pre = head;
        Node cur = head;
        //
        while (cur != null) {
            if (cur.value == num) {
                pre.next = cur.next;
            } else {
                pre = cur;
            }
            cur = cur.next;
        }
        return head;
    }

    public static void main(String[] args) {
        int len = 50;
        int value = 100;
        int testTime = 100000;
        for (int i = 0; i < testTime; i++) {
            Node node1 = generateRandomLinkedList(len, value);
            Node reverse1 = reverseLinkedList(node1);
            Node back1 = testReverseLinkedList(reverse1);
            if (!checkLinkedListEqual(node1, back1)) {
                System.out.println("oops!");
                break;
            }
            DNode node2 = generateRandomDoubleList(len, value);
            DNode reverse2 = reverseDoubleList(node2);
            DNode back2 = testReverseDoubleList(reverse2);
            if (!checkDoubleListEqual(node2, back2)) {
                System.out.println("oops!");
                break;
            }
        }
        System.out.println("finish!");

    }
}
