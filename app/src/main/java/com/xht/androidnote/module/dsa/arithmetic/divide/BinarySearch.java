package com.xht.androidnote.module.dsa.arithmetic.divide;

/**
 * 二分查找是典型的分治算法的应用。需要注意的是，二分查找的前提是查找的数列是有序的。
 */
public class BinarySearch {


    public static void main(String[] args) {


        int[] arr = new int[]{1, 3, 4, 6, 9, 10, 23, 34, 55};

        int index = binarySearch(arr, 3);

        System.out.println("index==" + index);


        int index2 = search(arr, 0, arr.length - 1, 3);
        System.out.println("index2==" + index2);

    }

    public static int binarySearch(int[] arr, int key) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (key < arr[mid]) {
                high = mid - 1;
            } else if (key > arr[mid]) {
                low = mid + 1;
            } else {
                return mid;
            }
        }

        return -1;
    }

    public static int search(int[] arr, int low, int high, int key) {
        if (key < arr[low] || key > arr[high] || low > high) {
            return -1;
        }


        int mid = (low + high) / 2;

        if (key < arr[mid]) {
            return search(arr, low, mid - 1, key);
        } else if (key > arr[mid]) {
            return search(arr, mid + 1, high, key);
        } else {
            return mid;
        }
    }


}
