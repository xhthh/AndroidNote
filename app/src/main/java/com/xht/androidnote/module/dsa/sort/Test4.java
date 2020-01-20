package com.xht.androidnote.module.dsa.sort;

import java.util.Arrays;

/**
 * Created by xht on 2020/1/17.
 */
public class Test4 {


    /*
        自顶向下的归并排序
     */
    private static void sort(int[] array, int low, int high) {

        int mid = (low + high) / 2;

        if (low < high) {
            sort(array, low, mid);
            sort(array, mid + 1, high);
            merge(array, low, mid, high);
        }

    }

    private static void merge(int[] array, int low, int mid, int high) {

        int[] temp = new int[high - low + 1];
        int left = low;
        int right = mid + 1;
        int k = 0;

        while (left <= mid && right <= high) {
            if(array[left] < array[right]) {
                temp[k++] = array[left++];
            } else {
                temp[k++] = array[right++];
            }
        }

        while (left <= mid) {
            temp[k++] = array[left++];
        }

        while (right <= high) {
            temp[k++] = array[right++];
        }

        for (int i = 0; i < temp.length; i++) {
            array[i + low] = temp[i];
        }

    }


    public static void main(String[] args) {
        int[] array = new int[]{5, 8, 6, 3, 9, 2, 1, 7};
        sort(array, 0, array.length - 1);
        System.out.println(Arrays.toString(array));
    }


}
