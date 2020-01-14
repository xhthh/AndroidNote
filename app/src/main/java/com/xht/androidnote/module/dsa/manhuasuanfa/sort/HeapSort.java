package com.xht.androidnote.module.dsa.manhuasuanfa.sort;

import java.util.Arrays;

/**
 * Created by xht on 2020/1/7.
 */
public class HeapSort {

    public static void main(String[] args) {
        HeapSort heapSort = new HeapSort();
        int[] arr = new int[]{1, 3, 2, 6, 5, 7, 8, 9, 10, 0};

        System.out.println(Arrays.toString(arr) + "\n");

        //        test.test1(arr);
        heapSort.test2(arr);

        System.out.println(Arrays.toString(arr));
    }


    /*
                        1
                   3           2
                6       5    7    8
              9   10  0

                        0
                   1           2
                6       3    7    8
              9   10  5

              需要从大到小排序，先构建最小堆，然后循环删除栈顶元素，替换到二叉堆末尾
     */
    public void test2(int[] array) {
        for (int i = array.length / 2 - 1; i > 0; i--) {
            upAdjust(array, i);
        }

        System.out.println(Arrays.toString(array) + "\n");

        //todo 最小堆排序
    }


    /*
                        1
                   3           2
                6       5    7    8
              9   10  0



                        10
                   9           8
               6      5     7    2
             3   1  0


             需要从小到大排序，先构建最大堆，然后循环删除栈顶元素，替换到二叉堆末尾
     */
    public void test1(int[] array) {
        //(array.length - 2) / 2 ===》定位到最后一个子树的父节点位置
        for (int i = (array.length - 2) / 2; i >= 0; i--) {
            downAdjust(array, i, array.length);
        }

        System.out.println(Arrays.toString(array) + "\n");

        for (int i = array.length - 1; i > 0; i--) {
            int temp = array[i];
            array[i] = array[0];
            array[0] = temp;
            downAdjust(array, 0, i);


            System.out.println("排序" + Arrays.toString(array) + "\n");
        }
    }


    /*
                        1
                   3           2
                6       5    7    8
              9   10  0



                        10
                   9           8
               6      5     7    2
             3   1  0
     */
    private void downAdjust(int[] array, int parentIndex, int length) {
        //保存父节点的值，用于最后的赋值
        int temp = array[parentIndex];

        int childIndex = parentIndex * 2 + 1;

        while (childIndex < length) {
            //如果有右孩子，且右孩子的值大于左孩子，则定位到右孩子
            if (childIndex + 1 < length && array[childIndex] < array[childIndex + 1]) {
                childIndex++;
            }

            //如果父节点大于任何一个孩子的值，则跳出
            if (temp >= array[childIndex])
                break;
            array[parentIndex] = array[childIndex];
            parentIndex = childIndex;
            childIndex = 2 * parentIndex + 1;
        }

        array[parentIndex] = temp;
    }



    /*
                        1
                   3           2
                6       5    7    8
              9   10  0
     */
    private void upAdjust(int[] array, int childIndex) {
        int parentIndex = (childIndex - 1) / 2;

        //temp 保存插入的叶子节点值，用于最后的赋值
        int temp = array[childIndex];

        while (childIndex > 0 && temp < array[parentIndex]) {
            array[childIndex] = array[parentIndex];

            childIndex = parentIndex;

            parentIndex = (childIndex - 1) / 2;
        }

        array[childIndex] = temp;
    }



    private void heapify(int[] array, int i) {
        //获取左右节点的数组下标:除数组第一个元素，其余元素将按照顺序2个分为1组，第一个节点的左右节点对应第一组元素，第二个节点的左右节点对应第二组元素。。。。
        //第i个节点的左右节点数组下标为 (i+1)*2-1 (i+1)*2
        int left = (i + 1) * 2 - 1;
        int right = (i + 1) * 2;
        int minIndex = i;//最小值的坐标
        //存在左结点，且左节点的值小于根节点的值
        if (left < array.length && array[left] < array[i])
            minIndex = left;
        //存在右节点，且右节点的子小于以上比较的较小值
        if (right < array.length && array[right] < array[minIndex])
            minIndex = right;
        //若该跟节点就是最小值，那么不用操作
        if (i == minIndex)
            return;
        //否则进行交换
        int temp = array[i];
        array[i] = array[minIndex];
        array[minIndex] = temp;
        //由于替换后的左右子树会被影响，所有要对受影响的子树在进行heapify
        heapify(array, minIndex);
    }

}
