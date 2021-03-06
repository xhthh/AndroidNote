package com.xht.androidnote.summarize.test;


import androidx.appcompat.widget.WithHint;

import java.util.Arrays;

public class DSATest4 {

    public static void main(String[] args) {

        testSort();
        //        testListNode();

    }

    private static void testSort() {
        int[] array = new int[]{5, 8, 6, 3, 9, 2, 1, 7};
        System.out.println(Arrays.toString(array));

        quickSort(array, 0, array.length - 1);
        //        bubbleSort(array);
        System.out.println(Arrays.toString(array));


    }

    private static void quickSort(int[] array, int startIndex, int endIndex) {
        if (startIndex >= endIndex) {
            return;
        }
        int pivotIndex = partition(array, startIndex, endIndex);
    }

    private static int partition(int[] arr, int startIndex, int endIndex) {
        int left = startIndex;
        int right = endIndex;
        int pivot = arr[startIndex];

        while (left != right) {
            while (left < right && arr[right] > pivot) {
                right--;
            }

            while (left < right && arr[left] <= pivot) {
                left++;
            }

            if (left < right) {
                int temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
            }
        }


        arr[startIndex] = arr[left];
        arr[left] = pivot;
        return left;
    }

    private static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            boolean isSorted = true;
            for (int j = 0; j < arr.length - i - 1; j++) {
                int temp = 0;
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                    isSorted = false;
                }
            }
            if (isSorted) {
                break;
            }
        }
    }


    private static void testListNode() {
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

        listNodeTraversal(node1);

        listNodeTraversal(reverseListNote(node1));

        //System.out.println(Arrays.toString(reversalTraversal(node1)));

        //System.out.println(getMiddleNode(node1).val);


        testMerge();
    }

    /**
     *      cur
     * pre  1   3   5   7
     *
     * @param head
     * @return
     */
    private static ListNode reverseListNote(ListNode head) {
        ListNode pre = null;
        ListNode cur = head;
        while (cur != null) {
            ListNode temp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = temp;
        }
        return pre;
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
        node3.next = node5;
        node5.next = node7;

        node2.next = node4;
        node4.next = node6;

        listNodeTraversal(node1);
        listNodeTraversal(node2);

        listNodeTraversal(mergeTwoLists(node1, node2));
    }

    /**
     * pre
     * l1  1   3   5   7
     * L2  2   4   6
     *
     * @param l1
     * @param l2
     * @return
     */
    private static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode preHead = new ListNode(-1);
        ListNode pre = preHead;

        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                pre.next = l1;
                l1 = l1.next;
            } else {
                pre.next = l2;
                l2 = l2.next;
            }
            pre = pre.next;
        }

        pre.next = l1 == null ? l2 : l1;

        return preHead.next;
    }


    private static void listNodeTraversal(ListNode head) {
        if (head == null) {
            return;
        }
        System.out.print("[" + head.val + "，");
        while (head.next != null) {
            head = head.next;
            if (head.next != null) {
                System.out.print(head.val + "，");
            } else {
                System.out.print(head.val + "]");
            }
        }
        System.out.println();
    }


}

class Singleton {
    private static volatile Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}

class Singleton3 {
    private Singleton3() {
    }

    private static Singleton3 getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final Singleton3 instance = new Singleton3();
    }
}