package com.xht.androidnote.module.dsa.question;


import com.xht.androidnote.module.dsa.Node;

/**
 * Created by xht on 2020/1/7.
 *
 * 判断一个链表是否有环？环的长度？入环点
 */
public class Test1 {


    public static void main(String[] args) {
        Node node1 = new Node(5);
        Node node2 = new Node(3);
        Node node3 = new Node(7);
        Node node4 = new Node(2);
        Node node5 = new Node(6);
        Node node6 = new Node(8);
        Node node7 = new Node(1);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node6;
        node6.next = node7;
        node7.next = node4;


        boolean isCycle = isCycle(node1);

        System.out.println("isCycle=" + isCycle);

        int cycleLength = cycleLength(node1);

        System.out.println("cycleLength=" + cycleLength);


        Node point = findPoint(node1);
        System.out.println("入环点==" + point.data);
    }


    /**
     * 判断链表是否有环
     *
     * @param head
     * @return
     */
    public static boolean isCycle(Node head) {

        Node p1 = head;
        Node p2 = head;

        while (p2 != null && p2.next != null) {
            p1 = p1.next;
            p2 = p2.next.next;

            if (p1 == p2) {
                return true;
            }
        }

        return false;
    }


    /**
     * 第一次相遇肯定是在环上，因为p2步速是p1的2倍，那么第二次相遇肯定是第一次相遇的点上
     *
     * @param head
     * @return
     */
    public static int cycleLength(Node head) {

        Node p1 = head;
        Node p2 = head;
        int v1 = 1;
        int v2 = 2;

        int length = 0;

        while (p2 != null && p2.next != null) {
            p1 = p1.next;
            p2 = p2.next.next;

            if (p1 == p2) {
                System.out.println("第一次相遇 data=" + p1.data);
                while (true) {
                    p1 = p1.next;
                    p2 = p2.next.next;

                    length++;

                    if (p1 == p2) {
                        System.out.println("第二次相遇 data=" + p2.data);
                        return (v2 - v1) * length;
                    }
                }
            }
        }

        return length;
    }


    /**
     * 从链表头结点到入环点的距离，等于从首次相遇点绕环n-1圈再回到入环点的距离。
     * 这样一来，只要把其中一个指针放回到头节点位置，另一个指针保持在首次相遇点，两个指针都是每次向
     * 前走1步。那么，它们最终相遇的节点，就是入环节点。
     *
     * @param head
     * @return
     */
    public static Node findPoint(Node head) {

        Node p1 = head;
        Node p2 = head;

        while (p2 != null && p2.next != null) {

            p1 = p1.next;
            p2 = p2.next.next.next;

            if (p1 == p2) {
                p1 = head;
                while (true) {
                    p1 = p1.next;
                    p2 = p2.next;

                    if (p1 == p2) {
                        return p1;
                    }
                }
            }
        }

        return null;
    }


}
