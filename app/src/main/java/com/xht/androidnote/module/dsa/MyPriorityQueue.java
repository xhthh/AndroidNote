package com.xht.androidnote.module.dsa;

import java.util.Arrays;

/**
 * Created by xht on 2019/12/31.
 */
public class MyPriorityQueue {


    private int[] array;

    private int size;

    public MyPriorityQueue() {
        //队列初始长度为32
        array = new int[8];
    }


    /*
        [3，5，10，2，7]

        3                                        [3]


                    3               5
        3、5      5      =>       3              [5, 3]

                        5               10
        5、3、10      3   10     =》   3   5     [10, 3, 5]


                            10
        10、3、5、2       3     5                [10, 3, 5, 2]
                       2


                                    10                              10
        10、3、5、2、7           3        5     =》          7               5
                              2    7                    2       3

     */
    public void upAdjust() {
        int childIndex = size - 1;
        int parentIndex = (childIndex - 1) / 2;

        // temp 保存插入的叶子节点值，用于最后的赋值
        int temp = array[childIndex];
        while (childIndex > 0 && temp > array[parentIndex]) {

            array[childIndex] = array[parentIndex];

            childIndex = parentIndex;

            parentIndex = parentIndex / 2;
        }

        array[childIndex] = temp;
    }


    public void downAdjust() {
        int parentIndex = 0;

        // temp 保存父节点的值，用于最后的赋值
        int temp = array[parentIndex];

        int childIndex = 1;

        while (childIndex < size) {
            // 如果有右孩子，且右孩子大于左孩子的值，则定位到右孩子
            if (childIndex + 1 < size && array[childIndex + 1] > array[childIndex]) {
                childIndex++;
            }

            // 如果父节点大于任何一个孩子节点的值，直接跳出
            if (temp >= array[childIndex])
                break;

            //无需真正交换，单向赋值即可
            array[parentIndex] = array[childIndex];
            parentIndex = childIndex;
            childIndex = 2 * parentIndex + 1;
        }

        array[parentIndex] = temp;
    }


    public void enQueue(int key) {
        if (size >= array.length) {
            reSize();
        }

        array[size++] = key;
        upAdjust();
    }

    public int deQueue() throws Exception {
        if (size <= 0) {
            throw new Exception("this queue is empty!");
        }

        //获取堆顶元素
        int head = array[0];
        //让最后一个元素移到堆顶
        array[0] = array[--size];
        downAdjust();

        return head;
    }


    public void reSize() {
        int newSize = this.size * 2;
        this.array = Arrays.copyOf(this.array, newSize);
    }


    public static void main(String[] args) throws Exception {
        MyPriorityQueue priorityQueue = new MyPriorityQueue();

        priorityQueue.enQueue(3);
        System.out.println(Arrays.toString(priorityQueue.array));
        priorityQueue.enQueue(5);
        System.out.println(Arrays.toString(priorityQueue.array));
        priorityQueue.enQueue(10);
        System.out.println(Arrays.toString(priorityQueue.array));
        priorityQueue.enQueue(2);
        System.out.println(Arrays.toString(priorityQueue.array));
        priorityQueue.enQueue(7);
        System.out.println(Arrays.toString(priorityQueue.array));

        System.out.println();

        System.out.println(" 出队元素：" + priorityQueue.deQueue());
        System.out.println(" 出队元素：" + priorityQueue.deQueue());
    }
}
