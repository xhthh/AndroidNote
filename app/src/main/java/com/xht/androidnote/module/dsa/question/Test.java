package com.xht.androidnote.module.dsa.question;


import com.xht.androidnote.module.dsa.Node;

/**
 * Created by xht on 2020/1/7.
 */
public class Test {


    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
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


    public static int cycleLength(Node head) {

        Node p1 = head;
        Node p2 = head;

        int length = 0;

        while (p2 != null && p2.next != null) {
            p1 = p1.next;
            p2 = p2.next.next;

            if (p1 == p2) {
                while (true) {
                    p1 = p1.next;
                    p2 = p2.next.next;

                    length++;

                    if (p1 == p2) {
                        return length;
                    }
                }
            }
        }

        return length;
    }


    public static Node findPoint(Node head) {

        Node p1 = head;
        Node p2 = head;

        while (p2 != null && p2.next != null) {

            p1 = p1.next;
            p2 = p2.next.next;

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
