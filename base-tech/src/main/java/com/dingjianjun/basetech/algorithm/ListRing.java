package com.dingjianjun.basetech.algorithm;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/29
 */
public class ListRing {

    /**
     * 获取两个单链表的第一个公共节点（考虑有环、无环情况）
     * @param head1
     * @param head2
     * @return
     */
    public static Node getIntersectNode(Node head1, Node head2) {
        if (head1 == null || head2 == null) {
            return null;
        }

        // 找出单链表中环的入口节点，如果没有返回null
        Node loopNode1 = getLoopNode(head1);
        Node loopNode2 = getLoopNode(head2);
        // 如果都是无环单链表
        if (null == loopNode1 && null == loopNode2) {
            return noLoop(head1, head2);
        }

        // 如果都是有环单链表
        if (null != loopNode1 && null != loopNode2) {
            return bothLoop(head1, loopNode1, head2, loopNode2);
        }

        return null;
    }

    /**
     * 找出两个无环单链表的第一个相交节点，如果不相交返回null
     * @param head1
     * @param head2
     * @return
     */
    private static Node noLoop(Node head1, Node head2) {
        if (null == head1 || null == head2) {
            return null;
        }
        int n = 0;
        Node cur1 = head1;
        Node cur2 = head2;
        while (cur1 != null) {
            n++;
            cur1 = cur1.next;
        }

        while (cur2 != null) {
            n--;
            cur2 = cur2.next;
        }

        // 两个单链表的尾节点不是同一个节点，则不相交
        if (cur1 != cur2) {
            return null;
        }

        cur1 = n > 0 ? head1 : head2; // 谁长谁的头结点给cur1
        cur2 = cur1 == head1 ? head2 : head1;
        n = Math.abs(n);
        // 长的先移动n个节点
        while (n != 0) {
            n--;
            cur1 = cur1.next;
        }

        // 一定相交
        while (cur1 != cur2) {
            cur1 = cur1.next;
            cur2 = cur2.next;
        }

        return cur1;
    }

    /**
     * 两个有环链表，返回第一个相交节点，如果不相交返回null
     * @param head1
     * @param loop1
     * @param head2
     * @param loop2
     * @return
     */
    private static Node bothLoop(Node head1, Node loop1, Node head2, Node loop2) {
        if (loop1 == loop2) {
            int n = 0;
            Node cur1 = head1;
            Node cur2 = head2;
            while (cur1 != loop1) {
                n++;
                cur1 = cur1.next;
            }

            while (cur2 != loop2) {
                n--;
                cur2 = cur2.next;
            }

            cur1 = n > 0 ? head1 : head2; // 谁长谁的头结点给cur1
            cur2 = cur1 == head1 ? head2 : head1;
            n = Math.abs(n);
            // 长的先移动n个节点
            while (n != 0) {
                n--;
                cur1 = cur1.next;
            }

            // 一定相交
            while (cur1 != cur2) {
                cur1 = cur1.next;
                cur2 = cur2.next;
            }

            return cur1;
        } else {
            Node cur = loop1.next;
            while (cur != loop1) {
                if (cur == loop2) {
                    return loop1;
                }

                cur = cur.next;
            }

            return null;
        }


    }

    /**
     * 找出单链表中环的入口节点，如果没有返回null
     * 快慢指针
     * @param head
     * @return
     */
    public static Node getLoopNode(Node head) {
        if (null == head || null == head.next || null == head.next.next) {
            return null;
        }

        // 至少3个节点
        Node slow = head.next;
        Node fast = head.next.next;
        // 等到快慢指针第一次相遇
        while (slow != fast) {
            if (fast.next == null || fast.next.next == null) {
                return null;
            }
            slow = slow.next;
            fast = fast.next.next;
        }

        // 快指针从头开始走，每次移动一步
        fast = head;
        // 等到第二次相遇
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }

        return slow;
    }

    /**
     * 如果是奇数个节点，返回中间节点，否则返回靠前的中间节点
     * @param head
     * @return
     */
    public static Node midOrUpNode(Node head) {
        if (null == head || null == head.next || null == head.next.next) {
            return head;
        }

        // 至少3个节点
        Node slow = head.next;
        Node fast = head.next.next;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }


    public static void main(String[] args) {
        Node head = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        Node node5 = new Node(5);
        head.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node3;

        Node loopNode = getLoopNode(head);
        System.out.println((loopNode != null) ? loopNode.value : null);
    }
}
