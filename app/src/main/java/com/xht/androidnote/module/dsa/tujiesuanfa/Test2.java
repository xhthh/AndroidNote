package com.xht.androidnote.module.dsa.tujiesuanfa;

import java.util.Arrays;

/**
 * Created by xht on 2020/1/15.
 */
public class Test2 {


    public static void test(int[] array, int startIndex, int endIndex) {
        if (startIndex >= endIndex) {
            return;
        }

        int pivotIndex = partition2(array, startIndex, endIndex);

        test(array, startIndex, pivotIndex - 1);
        test(array, pivotIndex + 1, endIndex);
    }

    public static int partition(int[] array, int startIndex, int endIndex) {
        int pivot = array[startIndex];
        int left = startIndex;
        int right = endIndex;

        while (left != right) {

            //这里顺序颠倒会有问题，为啥？
            /*while (left < right && array[left] <= pivot) {
                left++;
            }

            while (left < right && array[right] > pivot) {
                right--;
            }*/

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

        array[startIndex] = array[left];
        array[left] = pivot;

        return left;
    }

    //{4, 4, 6, 5, 3, 2, 8, 1}
    public static int partition2(int[] array, int startIndex, int endIndex) {
        int pivot = array[startIndex];

        int mark = startIndex;
        for (int i = startIndex + 1; i <= endIndex; i++) {
            if (array[i] < pivot) {
                mark++;

                int temp = array[i];
                array[i] = array[mark];
                array[mark] = temp;
            }

        }
        array[startIndex] = array[mark];
        array[mark] = pivot;

        return mark;
    }


    public static void main(String[] args) {
        int[] arr = new int[]{4, 4, 6, 5, 3, 2, 8, 1};

        test(arr, 0, arr.length - 1);

        System.out.println(Arrays.toString(arr));
    }

}
