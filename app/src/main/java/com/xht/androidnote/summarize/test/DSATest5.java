package com.xht.androidnote.summarize.test;

public class DSATest5 {

    public static void main(String[] args) {
        int[] array = new int[]{5, 8, 6, 3, 9, 2, 1, 7, 4};
        testCircleList();
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
     * v1 = 1
     * v2 = 2
     *
     * 起点至入环点：      d
     * 入环点至首次相遇点：s1
     * 首次相遇点至入环点：s2
     * l2是l1 速度的两倍，即l2走了l1两倍距离
     * l1所走距离：d+s1
     * l2所走距离：d+s1+(s1+s2)--->s1+s2即环长
     * 2(d + s1) = d + s1 + n(s1+s2)
     * 当 n=1 时，d = s2
     * 即将一个指针放回头，另一个指针在首次相遇点，每次走一步，相遇时，即切点
     * 1    2   3   4   5
     *
     *          7       6
     */
    private static ListNode getTangentPoint(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast!= null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if(fast == slow) {
                fast = head;
                while (true) {
                    fast = fast.next;
                    slow = slow.next;
                    if(fast == slow){
                        return slow;
                    }
                }
            }
        }
        return null;
    }


    private static int getCircleLength(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                int length = 0;
                while (fast != null) {
                    fast = fast.next.next;
                    slow = slow.next;
                    length++;
                    if (fast == slow) {
                        return length;
                    }
                }
            }
        }
        return 0;
    }

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


}
