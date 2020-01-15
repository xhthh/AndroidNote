package com.xht.androidnote.module.dsa.tujiesuanfa;

import java.util.Arrays;

/**
 * Created by xht on 2020/1/15.
 * 二分查找
 * 需要有序数组
 */
public class Test1 {


    public static void sort(int[] array) {

        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }

    }

    /**
     * 循环实现
     * @param array
     * @param num
     * @return
     */
    public static Integer binarySearch(int[] array, int num) {
        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;//可以用位移提高效率

            int guess = array[mid];

            if (guess < num) {
                low = mid + 1;
            } else if (guess > num) {
                high = mid - 1;
            } else {
                return mid;
            }
        }

        return null;
    }

    /**
     * 递归实现
     * @param array
     * @param key
     * @param low
     * @param high
     * @return
     */
    public static int binarySortRecursion(int[] array, int key, int low, int high) {
        if (low <= high) {
            int mid = (low + high) >> 1;
            if (array[mid] < key) {
                low = mid + 1;
                binarySortRecursion(array, key, low, high);
            } else if (array[mid] > key) {
                high = mid - 1;
                binarySortRecursion(array, key, low, high);
            } else {
                return mid;
            }
        }

        return -1;
    }


    public static void main(String[] args) {
        int[] array = new int[]{1, 3, 6, 5, 9, 2, 8};

        sort(array);

        System.out.println(Arrays.toString(array));

        int index = binarySearch(array, 8);
        System.out.println("index1=" + index);

        int index2 = binarySortRecursion(array, 8, array[0], array[array.length - 1]);
        System.out.println("index2=" + index2);
    }

}
