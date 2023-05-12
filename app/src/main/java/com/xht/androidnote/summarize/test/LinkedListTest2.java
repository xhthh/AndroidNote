package com.xht.androidnote.summarize.test;

/**
 * 链表相关测试
 */
public class LinkedListTest2 {

    public static void main(String[] args) {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;


        //反转链表
        //        traversalList(node1);
        //        ListNode newNode = reverseLinkedList(node1);
        //        traversalList(newNode);

        //倒数第k个节点
        //        ListNode kthFromEnd = getKthFromEnd(node1, 3);
        //        System.out.println(kthFromEnd.val);

        //判断链表是否有环
        //        testCircleList();

        //合并两个有序链表
        testMerge();

        //找出链表中间节点
        //        ListNode middleNode = getMiddleNode(node1);
        //        System.out.println(middleNode.val);

        //删除链表中间节点
        //        traversalList(deleteMiddleNode(node1));

        //反转链表，传入头和尾
        //        ListNode reverse = reverse(node1, node5);
        //        System.out.println(reverse.val);
        //        traversalList(reverse);

        //K个一组反转链表
        //ListNode reverseKGroup = reverseKGroup(node1, 2);
        //traversalList(reverseKGroup);

        //        ListNode listNode = reverseList(node1);
        //        traversalList(listNode);
    }

    private static void testMerge() {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        ListNode node6 = new ListNode(6);
        ListNode node7 = new ListNode(7);
        node1.next = node3;
        node2.next = node4;
        node3.next = node5;
        node4.next = node6;
        node5.next = node7;
        ListNode lists = mergeTwoLists(node1, node2);
        traversalList(lists);
    }

    /**
     * prev
     * 1    2   3   4
     * 2    4   5   6
     *
     * @param l1
     * @param l2
     * @return
     */
    private static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode prevHead = new ListNode(-1);
        ListNode prev = prevHead;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                prev.next = l1;
                l1 = l1.next;
            } else {
                prev.next = l2;
                l2 = l2.next;
            }
            prev = prev.next;
        }
        prev.next = l1 == null ? l2 : l1;

        return prevHead.next;
    }

    private static void testCircleList() {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        ListNode node6 = new ListNode(6);
        ListNode node7 = new ListNode(7);
        node1.next = node2;
        //        node2.next = node1;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node6;
        node6.next = node7;
        node7.next = node3;

        boolean isCircle = isCircleList(node1);
        System.out.println("isCircle = " + isCircle);

        int length = getCircleLength(node1);
        System.out.println("length = " + length);

        ListNode node = getTangentPoint(node1);
        System.out.println("切点 = " + node.val);
    }

    private static ListNode getTangentPoint(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                ListNode l1 = head;
                while (l1 != slow) {
                    l1 = l1.next;
                    slow = slow.next;
                }
                return l1;
            }
        }
        return null;
    }

    private static int getCircleLength(ListNode head) {
        ListNode p1 = head;
        ListNode p2 = head;
        while (p2 != null && p2.next != null) {
            p1 = p1.next;
            p2 = p2.next.next;
            if (p1 == p2) {
                int length = 0;
                do {
                    p1 = p1.next;
                    p2 = p2.next.next;
                    length++;
                } while (p1 != p2);
                return length;
            }
        }
        return 0;
    }

    private static boolean isCircleList(ListNode head) {
        ListNode l1 = head;
        ListNode l2 = head;
        while (l2 != null && l2.next != null) {
            l1 = l1.next;
            l2 = l2.next.next;
            if (l1 == l2) {
                return true;
            }
        }
        return false;
    }

    /**
     * cur
     * prev 1    2   3   4   5   6   7
     *
     * @param head
     * @return
     */
    private static ListNode reverse(ListNode head) {
        ListNode prev = null;
        ListNode cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        return prev;
    }

    private static ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode newHead = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    private static void traversalList(ListNode head) {
        while (head != null) {
            System.out.print(head.val + "   ");
            head = head.next;
        }
        System.out.println();
    }
}
