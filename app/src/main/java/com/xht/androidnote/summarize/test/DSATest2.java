package com.xht.androidnote.summarize.test;

import java.util.Arrays;
import java.util.Stack;

public class DSATest2 {

    public static void main(String[] args) {
        int[] array = new int[]{5, 8, 6, 3, 9, 2, 1, 7, 4};

        //        System.out.println(Arrays.toString(array));
        //        //bubble(array);
        //        quickSort(array, 0, array.length - 1);
        //        System.out.println(Arrays.toString(array));
        //
        //        int binarySearch = binarySearch(array, 1, 0, array.length - 1);
        //        System.out.println(binarySearch);

        //        testQueue();

        testReverseList();

        //        testMergeList();

        //testCircleList();

        //testStack();

    }

    private static void testStack() {
        QueueStack stack = new QueueStack();
        stack.push(2);
        stack.push(4);
        stack.push(7);
        stack.push(5);
        System.out.println(stack.pop());
        System.out.println(stack.pop());

        stack.push(1);
        System.out.println(stack.pop());
    }

    private static void testQueue() {
        StackQueue queue = new StackQueue();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
        queue.enqueue(4);
        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
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

    private static ListNode getTangentPoint(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;

        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;

            if (slow == fast) {
                slow = head;
                while (true) {
                    slow = slow.next;
                    fast = fast.next;

                    if (slow == fast) {
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
        int length = 0;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                System.out.println("第一次相遇点 = " + slow.val);
                while (true) {
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


    private static void testMergeList() {
        ListNode node1 = new ListNode(1);
        ListNode node3 = new ListNode(3);
        ListNode node6 = new ListNode(6);
        ListNode node7 = new ListNode(7);
        node1.next = node3;
        node3.next = node6;
        node6.next = node7;

        ListNode node2 = new ListNode(2);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        ListNode node8 = new ListNode(8);
        node2.next = node4;
        node4.next = node5;
        node5.next = node8;

        traversalList(node1);
        traversalList(node2);

        ListNode mergeList = mergeTwoList(node1, node2);
        traversalList(mergeList);
    }

    private static ListNode mergeTwoList(ListNode node1, ListNode node2) {
        ListNode preHead = new ListNode(-1);
        ListNode pre = preHead;

        while (node1 != null && node2 != null) {
            if (node1.val < node2.val) {
                pre.next = node1;
                node1 = node1.next;
            } else {
                pre.next = node2;
                node2 = node2.next;
            }
            pre = pre.next;
        }

        pre.next = node1 == null ? node2 : node1;

        return preHead.next;
    }


    private static void testReverseList() {
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

        traversalList(node1);
        //        ListNode listNode = reverseList(node1);
        //        traversalList(listNode);

        //        ListNode midNode = getMidNode(node1);
        //        System.out.println("midNode = " + midNode.val);

        int[] arr = reverseTraversal(node1);

        System.out.println(Arrays.toString(arr));
    }

    private static int[] reverseTraversal(ListNode head) {
        Stack<Integer> stack = new Stack<>();
        while (head != null) {
            stack.push(head.val);
            head = head.next;
        }

        int size = stack.size();
        int[] arr = new int[size];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = stack.pop();
        }

        return arr;
    }

    private static ListNode getMidNode(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;

        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }

        return slow;
    }

    private static ListNode reverseList(ListNode head) {
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

    private static void traversalList(ListNode head) {
        while (head != null) {
            System.out.print(head.val + " ");
            head = head.next;
        }
        System.out.println();
    }

    private static int binarySearch(int[] arr, int key, int low, int high) {
        if (low > high || key < arr[low] || key > arr[high]) {
            return -1;
        }

        int mid = (low + high) / 2;
        if (key < arr[mid]) {
            return binarySearch(arr, key, low, mid - 1);
        } else if (key > arr[mid]) {
            return binarySearch(arr, key, mid + 1, high);
        } else {
            return mid;
        }
    }


    private static void quickSort(int[] arr, int startIndex, int endIndex) {
        if (startIndex >= endIndex) {
            return;
        }

        int pivotIndex = partition(arr, startIndex, endIndex);
        quickSort(arr, startIndex, pivotIndex - 1);
        quickSort(arr, pivotIndex + 1, endIndex);
    }

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
        }

        arr[startIndex] = arr[left];
        arr[left] = pivot;

        return left;
    }

    private static void bubble(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            boolean isSorted = true;
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
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


    /**
     * 双重检索
     */
    static class SingletonDCL {
        private static volatile SingletonDCL instance;

        private SingletonDCL() {
        }

        public static SingletonDCL getInstance() {
            if (instance == null) {
                synchronized (SingletonDCL.class) {
                    if (instance == null) {
                        instance = new SingletonDCL();
                    }
                }
            }
            return instance;
        }
    }

    /**
     * 静态内部类单例
     */
    static class SingletonSIC {
        private SingletonSIC() {
        }

        public static SingletonSIC getInstance() {
            return Holder.instance;
        }

        private static class Holder {
            private static final SingletonSIC instance = new SingletonSIC();
        }
    }

}
