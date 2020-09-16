package com.xht.androidnote;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Created by xht on 2020/8/25
 */
public class DSA {

    public static void main(String[] args) {
        int[] array = new int[]{5, 8, 6, 3, 9, 2, 1, 7};

        //bubbleSort(array);
        quickSort(array, 0, array.length - 1);

        System.out.println(Arrays.toString(array));


        initLinkedList();

        String reverseStr = reverseStr("1234567");
        System.out.println(reverseStr);

        System.out.println();

        System.out.println(reverseStrK("1234567", 3));

        int[] arr = {1, 3, 2, 4, 4, 5, 3, 6, 7};
        int[] newArr = removeElement(arr, 3);
        System.out.println(Arrays.toString(newArr));

        System.out.println();

        System.out.println(findRepeatNumber(arr));
    }


    private static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode preHead = new ListNode(-1);

        ListNode prev = preHead;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                prev.next = l1;
                l1 = l1.next;
            } else {
                prev.next = l2;
                l2 = l2.next;
            }
            prev = prev.next;
        }

        prev.next = l1 == null ? l2 : l1;

        return preHead;
    }


    private static List<Integer> preOrder(TreeNode root) {
        LinkedList<TreeNode> stack = new LinkedList<>();
        LinkedList<Integer> output = new LinkedList<>();

        if (root == null) {
            return output;
        }

        stack.add(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pollFirst();
            output.push(node.val);
            if (node.right != null) {
                stack.add(node.right);
            }
            if (node.left != null) {
                stack.add(node.left);
            }
        }
        return output;
    }


    private static int findRepeatNumber(int[] arr) {
        Set<Integer> set = new HashSet<>();
        int repeat = -1;
        for (int num : arr) {
            if (!set.add(num)) {
                repeat = num;
                break;
            }
        }
        return repeat;
    }

    /**
     * 给定一个数组和值，移除数组中与此值相同的元素，并返回新的数组长度。
     * 数组中的元素顺序可以被改变，只需要获取到新的数组长度，不需要考虑最终数组当中的元素排列状态。
     */
    private static int[] removeElement(int[] arr, int elem) {
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != elem) {
                arr[index] = arr[i];
                index++;
            }
        }
        int[] newArray = Arrays.copyOf(arr, index);
        return newArray;
    }

    /**
     * 给定一个字符串，一个数值k，对其进行反转
     * 例：1234567 k=3 ===>  3216547
     */
    private static String reverseStrK(String str, int k) {
        char[] ch = str.toCharArray();
        int n = ch.length;
        for (int i = 0; i < n; i += k) {
            int left = i;
            int right = (i + k - 1 < n) ? i + k - 1 : n - 1; //判断下标是否越界
            while (left <= right) {
                char temp = ch[left];
                ch[left] = ch[right];
                ch[right] = temp;
                left++;
                right--;
            }
        }
        return new String(ch);
    }

    private static String reverseStr(String str) {
        char[] chars = str.toCharArray();
        StringBuilder reverse = new StringBuilder();
        for (int i = str.length() - 1; i >= 0; i--) {
            reverse.append(chars[i]);
        }
        return reverse.toString();
    }

    private static void initLinkedList() {
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

        //        traversal(node1);
        //        traversal(reverseLinkedList(node1));
        //        int[] arr = reversalTraversal(node1);
        //        System.out.println(Arrays.toString(arr));
        System.out.println("中间节点==" + getMiddleNode(node1).val);
    }


    /**
     * 寻找链表中间节点
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


    /**
     * 从尾到头打印链表
     * 使用栈
     */
    private static int[] reversalTraversal(ListNode head) {
        Stack<ListNode> stack = new Stack<>();

        while (head != null) {
            stack.push(head);
            head = head.next;
        }

        int size = stack.size();

        int[] arr = new int[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = stack.pop().val;
        }
        return arr;
    }

    /**
     * 反转链表
     */
    private static ListNode reverseLinkedList(ListNode head) {
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

    /**
     * 遍历链表
     *
     * @param head
     */
    private static void traversal(ListNode head) {
        System.out.print(head.val + " ");
        while (head.next != null) {
            head = head.next;
            System.out.print(head.val + " ");
        }
        System.out.println();
    }


    /**
     * 二分查找
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

        int middle = (low + high) / 2;
        if (arr[middle] > key) {
            return recursionBinarySearch(arr, key, low, middle - 1);
        } else if (arr[middle] < key) {
            return recursionBinarySearch(arr, key, middle + 1, high);
        } else {
            return middle;
        }
    }

    private static int binarySearch(int[] arr, int key) {
        int low = 0;
        int high = arr.length - 1;
        int middle = 0;

        if (key < arr[low] || key > arr[high] || low > high) {
            return -1;
        }

        while (low <= high) {
            middle = (low + high) / 2;
            if (key < arr[middle]) {
                high = middle - 1;
            } else if (key > arr[middle]) {
                low = middle + 1;
            } else {
                return middle;
            }
        }
        return -1;
    }

    /**
     * 快速排序
     *
     * @param arr
     * @param startIndex
     * @param endIndex
     */
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
            if (left < right && arr[right] > pivot) {
                right--;
            }

            if (left < right && arr[left] <= pivot) {
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


    /**
     * 冒泡排序
     *
     * @param arr
     */
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

/**
 * 双重检索
 */
class Singleton1 {

    private static volatile Singleton1 sInstance;

    private Singleton1() {
        System.out.println("do something");
    }

    public static Singleton1 getInstance() {
        if (sInstance == null) {//避免不必要的同步，如果不加volatile，指令重排后，另一个线程进来，这里已经不为空了，会出问题
            synchronized (Singleton1.class) {
                if (sInstance == null) {//在null的情况下创建实例，防止另一个线程进来重复重复创建实例
                    sInstance = new Singleton1();
                }
            }
        }
        return sInstance;
    }

}

/**
 * 静态内部类单例
 */
class Singleton2 {
    private Singleton2() {
    }

    public static Singleton2 getInstance() {
        return Holder.sInstance;
    }

    //只有在第一次调用getInstance()时才会初始化，jvm类加载保证线程安全
    private static class Holder {
        private static final Singleton2 sInstance = new Singleton2();
    }
}

class ListNode {
    public int val;
    public ListNode next;

    public ListNode(int x) {
        val = x;
    }
}

class TreeNode {
    TreeNode left;
    TreeNode right;
    int val;

    public TreeNode(int val) {
        this.val = val;
    }
}
