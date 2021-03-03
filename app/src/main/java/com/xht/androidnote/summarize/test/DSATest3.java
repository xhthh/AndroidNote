package com.xht.androidnote.summarize.test;

import android.graphics.pdf.PdfRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Stack;

public class DSATest3 {

    public static void main(String[] args) {
        //testSortAndSearch();
        testListNode();
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

        //listNodeTraversal(node1);

        //listNodeTraversal(reverseListNote(node1));

        //System.out.println(Arrays.toString(reversalTraversal(node1)));

        //System.out.println(getMiddleNode(node1).val);


        testMerge();
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
     * prev
     * l1 1 3 5 7
     * l2 2 4 6
     *
     * @param l1
     * @param l2
     * @return
     */
    private static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode preHead = new ListNode(-1);
        ListNode prev = preHead;
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
        return preHead.next;
    }

    private static ListNode getMiddleNode(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }

        return slow;
    }


    private static int[] reversalTraversal(ListNode head) {
        Stack<ListNode> stack = new Stack();
        while (head != null) {
            stack.push(head);
            head = head.next;
        }

        int size = stack.size();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = stack.pop().val;
        }
        return arr;
    }

    /**
     * pre
     * current
     * 1,   2,  3,  4,  5,  6,  7
     *
     * @param head
     */
    private static ListNode reverseListNote(ListNode head) {
        ListNode pre = null;
        ListNode current = head;
        while (current != null) {
            ListNode temp = current.next;
            current.next = pre;
            pre = current;
            current = temp;
        }
        return pre;
    }

    private static void listNodeTraversal(ListNode head) {
        if (head == null) {
            return;
        }
        System.out.print("[" + head.val + "，");

        while (head.next != null) {
            head = head.next;
            if (head.next == null) {
                System.out.println(head.val + "]");
            } else {
                System.out.print(head.val + "，");
            }
        }
    }
















    /*



     */

    private static void testSortAndSearch() {
        int[] array = new int[]{5, 8, 6, 3, 9, 2, 1, 7};

        System.out.println(Arrays.toString(array));

        bubbleSort(array);
        //quickSort(array, 0, array.length - 1);

        System.out.println(Arrays.toString(array));

        //int index = recursionBinarySearch(array, 1, 0, array.length - 1);
        int index = binarySearch(array, 4);
        System.out.println("index = " + index);
    }

    private static int binarySearch(int[] arr, int key) {
        int low = 0;
        int high = arr.length - 1;
        int mid = 0;

        if (arr[low] > key || arr[high] < key) {
            return -1;
        }

        while (low <= high) {
            mid = (low + high) / 2;
            if (arr[mid] > key) {
                high = mid - 1;
            } else if (arr[mid] < key) {
                low = mid + 1;
            } else {
                return mid;
            }
        }

        return -1;
    }

    /**
     * [1, 2, 3, 5, 6, 7, 8, 9]
     *
     * @param arr
     * @param key
     * @param low
     * @param high
     * @return
     */
    private static int recursionBinarySearch(int[] arr, int key, int low, int high) {
        if (key < arr[low] || key > arr[high] || low > high) {
            return -1;
        }

        int mid = (low + high) / 2;

        if (arr[mid] > key) {
            return recursionBinarySearch(arr, key, low, mid - 1);
        } else if (arr[mid] < key) {
            return recursionBinarySearch(arr, key, mid + 1, high);
        } else {
            return mid;
        }
    }


    private static void quickSort(int[] arr, int startIndex, int endIndex) {
        //System.out.println(Arrays.toString(arr));

        if (startIndex >= endIndex) {
            return;
        }

        int pivotIndex = partition(arr, startIndex, endIndex);
        System.out.println("pivotIndex = " + pivotIndex);


        quickSort(arr, startIndex, pivotIndex - 1);
        quickSort(arr, pivotIndex + 1, endIndex);
    }

    /**
     * 5, 8, 6, 3, 9, 2, 1, 7  arr[right]>pivot ritht--  right=6
     * 5, 8, 6, 3, 9, 2, 1, 7  arr[left]<=pivot left++   left =1
     * 5, 1, 6, 3, 9, 2, 8, 7  交换left和right位置的值
     * 5, 1, 6, 3, 9, 2, 8, 7  arr[right]>pivot right--  right=5
     * 5, 1, 6, 3, 9, 2, 8, 7  arr[left]<=pivot left++   left =2
     * 5, 1, 2, 3, 9, 6, 8, 7  交换left和right位置的值
     * ...
     * 3, 1, 2, 5, 9, 6, 8, 7  交换left和right位置的值 left =3 right = 4
     */
    private static int partition(int[] arr, int startIndex, int endIndex) {
        int pivot = arr[startIndex];
        int left = startIndex;
        int right = endIndex;

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

            System.out.println(Arrays.toString(arr));

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
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    isSorted = false;
                }
            }

            if (isSorted) {
                break;
            }
        }
    }


}

class Singleton1 {
    private static volatile Singleton1 instance = null;

    private Singleton1() {

    }

    public static Singleton1 getInstance() {
        if (instance == null) {
            synchronized (Singleton1.class) {
                if (instance == null) {
                    instance = new Singleton1();
                }
            }
        }

        return instance;
    }
}

class Singleton2 {
    private Singleton2() {
    }

    public static Singleton2 getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final Singleton2 instance = new Singleton2();
    }
}

