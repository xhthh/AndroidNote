package com.xht.androidnote.summarize.test;

import java.util.Arrays;

public class DSATest {

    public static void main(String[] args) {
        int[] array = new int[]{5, 8, 6, 3, 9, 2, 1, 7};

        System.out.println(Arrays.toString(array));
        bubble(array);
        System.out.println(Arrays.toString(array));

        int binarySearch = binarySearch(array, 3, 0, array.length - 1);
        System.out.println(binarySearch);
    }


    public static void quickSort(int[] arr, int left, int right) {

    }


    public static int binarySearch(int[] arr, int key, int low, int high) {
        if (low > high || arr[key] < arr[low] || arr[key] > arr[high]) {
            return -1;
        }

        int mid = (low + high) / 2;
        if (arr[key] < arr[mid]) {
            return binarySearch(arr, key, low, mid - 1);
        } else if (arr[key] > arr[mid]) {
            return binarySearch(arr, key, mid + 1, high);
        } else {
            return arr[mid];
        }
    }


    /**
     * 冒泡排序
     *
     * @param arr
     */
    public static void bubble(int[] arr) {
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
