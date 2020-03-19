package com.xht.androidnote.module.dsa.arithmetic.divide;

import java.util.Arrays;

public class QuickSort {


    public static void main(String[] args) {

        int[] arr = new int[]{4, 4, 6, 5, 3, 2, 8, 1};

        System.out.println(Arrays.toString(arr));

        sort(arr, 0, arr.length - 1);

        System.out.println(Arrays.toString(arr));
    }

    public static void sort(int[] arr, int startIndex, int endIndex) {

        if (startIndex >= endIndex) {
            return;
        }

        int pivotIndex = partition(arr, startIndex, endIndex);

        sort(arr, startIndex, pivotIndex - 1);
        sort(arr, pivotIndex + 1, endIndex);
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


}
