package com.xht.androidnote.module.dsa.arithmetic.divide;

public class FullPermutation {

    public static void main(String[] args) {

        int[] arr = {1, 2, 3, 4};

        fullSort(arr, 0, arr.length - 1);
    }


    /*
        问题描述：
          有1，2，3，4个数，问你有多少种排列方法，并输出排列。
        问题分析：
          若采用分治思想进行求解，首先需要把大问题分解成很多的子问题，大问题是所有的排列方法。
          那么我们分解得到的小问题就是以 1 开头的排列，以 2 开头的排列，以 3 开头的排列，以 4 开头的排列。
          现在这些问题有能继续分解，比如以 1 开头的排列中，只确定了 1 的位置，没有确定 2 ，3 ，4 的位置，
          把 2，3，4 三个又看成大问题继续分解，2 做第二个，3 做第二个，或者 4 做第二个。

          一直分解下去，直到分解成的子问题只有一个数字的时候，不能再分解。

          只有一个数的序列只有一种排列方式，则子问题求解容易的多。
     */
    private static void fullSort(int[] arr, int start, int end) {

        //递归终止条件
        if (start == end) {
            for (int i : arr) {
                System.out.print(i);
            }
            System.out.println();
            return;
        }

        for (int i = start; i <= end; i++) {
            /*
                调整数字位置，比如数组初始为 1 2 3 4，start=0, end=3，
                从0开始，第一次交换，i = start = 0，数组不变，1 2 3 4，
                递归，start + 1, 再次进入for循环，i = start = 1，
                再递归，再次进入for循环，i = start = 2,
                再次递归，start = end = 3，输出 1 2 3 4，return

                在 i = start = 2 的循环中，i++，i = 3，交换2和3位置的元素，1 2 4 3，输出，交换回去，return
                回到 i = 1 的循环中，i++, i = 2, 交换 start = 1 i = 2位置的元素，1 3 2 4
                递归，i = start = 2，
                递归 star + 1 = 3，输出 1 3 2 4，return

                回到 i = 2的循环， i++，交换 i=3 start=2 位置的元素，1 3 4 2，
                递归 start = 3, 输出 1 3 4 2，return

                回到 i=2 的循环，将交换的元素返回，1 3 2 4
                回到 i=1 的循环，将交换的元素返回，1 2 3 4


                回到 start = 1 的循环，i++  i=3，交换，1 3 位置的元素 1 4 3 2

                。。。。。

             */
            swap(arr, i, start);
            fullSort(arr, start + 1, end);
            swap(arr, i, start);//为了将交换的元素换回原来的样子，保持arr = ｛1 2 3 4｝
        }

    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}