package com.xht.androidnote.module.dsa.arithmetic.divide;

import java.util.Arrays;

public class MergeSort {

    public static void main(String[] args) {

        int[] arr = new int[]{4, 4, 6, 5, 3, 2, 8, 1};

        System.out.println(Arrays.toString(arr));

        mergeSort(arr, 0, arr.length - 1);

        System.out.println(Arrays.toString(arr));

    }


    public static void mergeSort(int[] arr, int start, int end) {
        if (start < end) {
            int mid = (start + end) / 2;

            mergeSort(arr, start, mid);
            mergeSort(arr, mid + 1, end);
            merge(arr, start, mid, end);
        }
    }

    private static void merge(int[] arr, int left, int mid, int right) {
        int[] temp = new int[arr.length];

        int p1 = left, p2 = mid + 1, k = left;//p1、p2是检测指针，k是存放指针

        while (p1 <= mid && p2 <= right) {
            if (arr[p1] <= arr[p2]) {
                temp[k++] = arr[p1++];
            } else {
                temp[k++] = arr[p2++];
            }
        }


        //这是剩下的，上面比较两个数组元素大小，按顺序放入临时存储数组中，当一个放完以后，剩下的直接放入后面
        while (p1 <= mid) {
            temp[k++] = arr[p1++];
        }

        while (p2 <= right) {
            temp[k++] = arr[p2++];
        }

        for (int i = left; i <= right; i++) {
            arr[i] = temp[i];
        }
    }

}
