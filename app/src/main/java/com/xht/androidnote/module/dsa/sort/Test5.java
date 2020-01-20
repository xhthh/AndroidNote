package com.xht.androidnote.module.dsa.sort;

import java.util.Arrays;

/**
 * Created by xht on 2020/1/20.
 */
public class Test5 {


    public static void sort(int[] array, int low, int high) {
        if (low >= high) {
            return;
        }

        int pivotIndex = partition(array, low, high);

        sort(array, low, pivotIndex - 1);
        sort(array, pivotIndex + 1, high);
    }

    private static int partition(int[] array, int low, int high) {
        int pivot = array[low];

        int left = low;
        int right = high;


        while (left != right) {
            while (left < right && array[right] > pivot) {
                right--;
            }

            while (left < right && array[left] <= pivot) {
                left++;
            }

            if (left < right) {
                int temp = array[left];
                array[left] = array[right];
                array[right] = temp;
            }
        }

        array[low] = array[left];
        array[left] = pivot;

        return left;
    }


    public static void main(String[] args) {
        int[] array = new int[]{5, 8, 6, 3, 9, 2, 1, 7};
        System.out.println(Arrays.toString(array));
        sort(array, 0, array.length - 1);
        System.out.println(Arrays.toString(array));
    }

}
