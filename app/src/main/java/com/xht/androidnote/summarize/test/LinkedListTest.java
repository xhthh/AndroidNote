package com.xht.androidnote.summarize.test;

/**
 * 链表相关测试
 */
public class LinkedListTest {

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
        //        testMerge();

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
        ListNode reverseKGroup = reverseKGroup(node1, 2);
        traversalList(reverseKGroup);
    }

    /**
     * 给你一个链表，每 k 个节点一组进行翻转，请你返回翻转后的链表。
     * pointer
     *
     * @param head 1    2       3       4       5
     * @param k
     */
    private static ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || head.next == null || k == 1) {
            return head;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode pointer = dummy;
        while (pointer != null) {
            //记录上一个子链表的尾
            ListNode lastGroup = pointer;
            int i = 0;
            for (; i < k; i++) {
                pointer = pointer.next;
                if (pointer == null) {
                    break;
                }
            }
            if (pointer != null) {
                System.out.println("pointer = " + pointer.val);
            }
            // 如果当前子链表的节点数满足 k, 就进行反转
            // 反之，说明程序到尾了，节点数不够，不用反转
            if (i == k) {
                System.out.println("lastGroup = " + lastGroup.val);
                //记录下一个子链表的头
                ListNode nextGroup = pointer.next;
                System.out.println("nextGroup = " + nextGroup.val);
                //反转当前子链表，revers函数返回反转后的子链表的头
                System.out.println("lastGroup.next = " + lastGroup.next.val);
                ListNode reversedList = reverse(lastGroup.next, nextGroup);
                // lastGroup 是上一个子链表的尾，其 next 指向当前反转子链表的头
                // 但是因为当前链表已经被反转，所以它指向的是反转后的链表的尾
                traversalList(reversedList);
                pointer = lastGroup.next;
                System.out.println("pointer = " + pointer.val);
                // 将上一个链表的尾连向反转后链表的头
                lastGroup.next = reversedList;
                // 当前反转后的链表的尾连向下一个子链表的头
                pointer.next = nextGroup;
            }
            System.out.println("-------------");
            traversalList(dummy.next);
            System.out.println("=============");
        }
        return dummy.next;
    }

    private static ListNode reverseKGroup2(ListNode head, int k) {
        if (head == null || head.next == null || k <= 1) {
            return head;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode pointer = dummy;
        while (pointer != null) {
            ListNode lastGroup = pointer;
            int i = 0;
            for (; i < k; i++) {
                pointer = pointer.next;
                if (pointer == null) {
                    break;
                }
            }
            if (i == k) {
                ListNode nextGroup = pointer.next;
                ListNode reversedList = reverse(lastGroup.next, nextGroup);
                pointer = lastGroup.next;
                lastGroup.next = reversedList;
                pointer.next = nextGroup;
            }
        }

        return dummy.next;
    }

    /**
     * 1    2   3   4
     *
     * @param head
     * @param tail
     * @return
     */
    private static ListNode reverse(ListNode head, ListNode tail) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode prev = null, temp = null;
        while ((head != null) && (head != tail)) {
            temp = head.next;
            head.next = prev;
            prev = head;
            head = temp;
        }
        return prev;
    }


    /**
     * 删除中间节点
     *
     * @param head
     * @return
     */
    private static ListNode deleteMiddleNode(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        ListNode temp = new ListNode(-1);
        temp.next = head;
        ListNode prev = temp;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            prev = slow;
            slow = slow.next;
        }
        prev.next = slow.next;
        return temp.next;
    }

    /**
     * 查链表中间节点
     *
     * @param head
     * @return
     */
    private static ListNode getMiddleNode(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
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
     * 合并两个有序链表
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

    /**
     * 测试有环链表
     * 1    2   3   4   5
     * 7       6
     */
    private static void testCircleList() {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        ListNode node6 = new ListNode(6);
        ListNode node7 = new ListNode(7);
        node1.next = node2;
        //                node2.next = node1;
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

    /**
     * 求入环切点
     *
     * @param head
     * @return
     */
    private static ListNode getTangentPoint(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (slow == fast) {
                ListNode p1 = head;
                while (p1 != slow) {
                    p1 = p1.next;
                    slow = slow.next;
                }
                return p1;
            }
        }
        return null;
    }

    /**
     * 求环长
     *
     * @param head
     * @return
     */
    private static int getCircleLength(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                int length = 0;
                do {
                    fast = fast.next.next;
                    slow = slow.next;
                    length++;
                } while (fast != slow);
                return length;
            }
        }
        return 0;
    }

    /**
     * 判断是否有环
     * 快慢指针
     *
     * @param head
     * @return
     */
    private static boolean isCircleList(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取倒数第 k 个值
     * 1    2   3   4   5
     *
     * @param head
     * @param k
     * @return
     */
    private static ListNode getKthFromEnd(ListNode head, int k) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && k > 0) {
            fast = fast.next;
            k--;
        }
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }

    /**
     * 反转链表
     *
     * @param head
     * @return
     */
    private static ListNode reverseLinkedList(ListNode head) {
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

    /**
     * 遍历链表
     *
     * @param head
     */
    private static void traversalList(ListNode head) {
        while (head != null) {
            System.out.print(head.val + "   ");
            head = head.next;
        }
        System.out.println();
    }


}
