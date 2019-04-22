package com.xht.dsa;

import java.util.Arrays;

public class DsaClass {
    public static void main(String[] args) {

        //System.out.println("result==" + fact(3));
        //        int[] array = {4, 2, 3};
        //        int[] sorted = bubbleSort(array);
        //        for(int i : sorted) {
        //            System.out.println("i=" + i);
        //        }

        //        int[] arr = {2, 5, 1, 10, 8, 4, 23};


        //        int partition = partition(arr, 0, arr.length - 1);
        //        System.out.println(partition);
        //        quickSort(arr, 0, arr.length - 1);
        //        for (int i = 0; i < arr.length; i++) {
        //            System.out.println("i=" + arr[i]);
        //        }

                int[] arr = {6, 1, 2, 7, 9, 3, 4, 5, 10, 8};
                bubbleSort(arr);
                System.out.println("arr=" + Arrays.toString(arr));
        //        quickSort(arr, 0, arr.length - 1);


//        hanoi(3, 'A', 'B', 'C');


    }

    /**
     * @param n    共有N个盘子
     * @param from 开始的柱子
     * @param in   中间的柱子
     * @param to   目标柱子
     *             无论有多少个盘子，都认为是只有两个。上面的所有盘子和最下面一个盘子。
     */
    public static void hanoi(int n, char from, char in, char to) {
        if (n == 1) {
            System.out.println("第 1 个盘子从 " + from + " 移到 " + to);
        } else {
            //移动上面所有的盘子到中间位置
            hanoi(n - 1, from, to, in);
            //移动下面的盘子从开始位置到目标位置
            System.out.println("第 " + n + " 个盘子从 " + from + " 移到 " + to);
            //把上面的所有的盘子从中间位置移到目标位置
            hanoi(n - 1, in, from, to);
        }
    }


    public static void quickSort(int[] array, int low, int high) {
        if (low >= high) {
            return;
        }

        int pivotIndex = partition(array, low, high);
        System.out.println("pivotIndex=" + pivotIndex + "   此时数组==" + Arrays.toString(array));
        quickSort(array, low, pivotIndex - 1);
        quickSort(array, pivotIndex + 1, high);
    }

    /**
     * 将数组分割为两个，返回基准值所在的位置
     * <p>
     * {6, 1, 2, 7, 9, 3, 4, 5, 10, 8}
     * 左右两个索引 low high
     * high先动，直到找到一个比基准值小的数，第一次找到5，index为7
     * low再动，直到找到一个比基准值大的数，第一次找到7，index为3
     * 交换位置
     * <p>
     * {6, 1, 2, 5, 9, 3, 4, 7, 10, 8}
     * 此时low = 3，high = 7，满足最外层条件 low < high
     * 继续
     * high先动，找到第二个比基准值小的数，4，index为6
     * low再动，找到第二个比基准值大的数，9，index为4
     * 交换
     * {6, 1, 2, 5, 4, 3, 9, 7, 10, 8}
     * 此时 low = 4，high = 6
     * 继续
     * high此时为6，值为9，满足条件，high--，high = 5，继续循环，不满足arr[high]>=pivot，跳出循环
     * low此时为4，值为4，满足条件，low++，low = 5，继续循环，不满足arr[low]<=pivot，跳出循环
     * <p>
     * low = 5，high = 5
     * <p>
     * 跳出外层循环，交换基准值和最终low（或high）的位置
     * [3, 1, 2, 5, 4, 6, 9, 7, 10, 8]
     *
     * @param array
     * @param low
     * @param high
     * @return
     */
    public static int partition(int[] array, int low, int high) {
        int pivot = array[low];

        int pivotIndex = low;

        while (low < high) {
            System.out.println("low=" + low + "  high=" + high);
            //右侧索引从右到左依次查找小于基准值的数，如果大于，索引递减
            while (low < high && array[high] >= pivot) {
                high--;
                System.out.println("while 1 low=" + low + "  high=" + high);
            }
            while (low < high && array[low] <= pivot) {
                low++;
                System.out.println("while 2 low=" + low + "  high=" + high);
            }

            System.out.println("最终 low=" + low + "  high=" + high);


            //内层两个while走完后，左侧找到一个大于基准值的值，右侧找到一个小于基准值的值，进行交换
            int temp = array[low];
            array[low] = array[high];
            array[high] = temp;
        }

        //当两侧索引相遇，跳出循环，确定最终基准值的位置，与最初基准值的位置进行交换
        int temp = array[pivotIndex];
        array[pivotIndex] = array[low];
        array[low] = temp;

        return low;
    }


    public static int fact(int x) {
        if (x == 1)
            return 1;
        return x * fact(x - 1);
    }


    /**
     * 选择排序
     * <p>
     * {2,5,1,10,8,4,23}
     *
     * @param array
     * @return
     */
    public static int[] selectSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            int k = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[k]) {
                    k = j;//找出最小值的索引
                }
            }

            //将最小值与i交换
            if (i != k) {//提高效率，如果数组本来就是有序的，就不会执行到这里
                int temp = array[k];
                array[k] = array[i];
                array[i] = temp;
            }
        }
        return array;
    }

    /**
     * 冒泡
     * {2, 5, 1, 10, 8, 4, 23}
     *
     * @param array
     * @return
     */
    /*public static int[] bubbleSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = array.length - 1; j > i; j--) {
                if (array[j] < array[j - 1]) {
                    int temp = array[j - 1];
                    array[j - 1] = array[j];
                    array[j] = temp;
                }
            }
        }

        return array;
    }*/
    public static int[] bubbleSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                if (array[j] > array[j +1]) {
                    int temp = array[j + 1];
                    array[j + 1] = array[j];
                    array[j] = temp;
                }
            }
        }

        return array;
    }


    /**
     * 二分查找 普通循环
     * 必须为有序列表
     * [1,2,4,6,7,9,10,13,15]
     *
     * @param array
     * @param key
     * @return
     */
    public static int binarySearch(int[] array, int key) {
        int position = -1;

        int low = 0;
        int high = array.length - 1;

        while (low <= high && position == -1) {
            int mid = (low + high) / 2;

            int guess = array[mid];

            if (key == guess) {
                position = mid;
            } else if (key < guess) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return position;
    }


    /**
     * 二分查找，递归方式
     *
     * @param array
     * @param start
     * @param end
     * @param key
     * @return
     */
    public static int binarySearch(int[] array, int start, int end, int key) {
        int mid = (end - start) / 2 + start;

        if (array[mid] == key) {
            return mid;
        }

        if (start >= end) {
            return -1;
        } else if (key > array[mid]) {
            return binarySearch(array, mid + 1, end, key);
        } else if (key < array[mid]) {
            return binarySearch(array, start, end - 1, key);
        }

        return -1;
    }
}
