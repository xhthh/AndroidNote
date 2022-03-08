package com.xht.androidnote.summarize.test;

import java.util.Arrays;

public class SortAndSearchTest {
    public static void main(String[] args) {
        int[] array = new int[]{5, 8, 6, 3, 9, 2, 1, 7, 4};

        System.out.println(Arrays.toString(array));
        //bubbleSort(array);
        quickSort(array, 0, array.length - 1);
        System.out.println(Arrays.toString(array));

        int result = binarySearch2(array, 3);
        System.out.println("result = " + result);
    }

    private static int binarySearch2(int[] arr, int key) {
        int low = 0;
        int high = arr.length - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
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

    /**
     * 二分查找
     * 递归
     *
     * @param arr
     * @param key
     * @param low
     * @param high
     * @return
     */
    private static int binarySearch1(int[] arr, int key, int low, int high) {
        if (low > high || key < arr[low] || key > arr[high]) {
            return -1;
        }
        int mid = (low + high) / 2;
        if (key < arr[mid]) {
            return binarySearch1(arr, key, low, mid - 1);
        } else if (key > arr[mid]) {
            return binarySearch1(arr, key, mid + 1, high);
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
        int left = startIndex;
        int right = endIndex;
        int pivot = arr[startIndex];

        while (left != right) {
            while (left < right && pivot < arr[right]) {
                right--;
            }
            while (left < right && pivot >= arr[left]) {
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
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean isSorted = true;
            for (int j = 0; j < n - i - 1; j++) {
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
}
