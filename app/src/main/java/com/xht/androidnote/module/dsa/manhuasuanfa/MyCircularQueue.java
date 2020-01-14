package com.xht.androidnote.module.dsa.manhuasuanfa;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

/**
 * Created by xht on 2019/12/26.
 * 循环队列
 * 数组实现
 */
public class MyCircularQueue {

    private int[] array;
    private int front;
    private int rear;

    public MyCircularQueue(int capacity) {
        this.array = new int[capacity];
    }

    /**
     * 入队
     *
     * @param element
     * @throws Exception
     */
    public void enQueue(int element) throws Exception {
        if ((rear + 1) % array.length == front) {
            throw new Exception("队列已满!");
        }

        array[rear] = element;
        rear = (rear + 1) % array.length;
    }

    public int deQueue() throws Exception {
        if (rear == front) {
            throw new Exception("队列已空!");
        }

        int deQueueElement = array[front];

        front = (front + 1) % array.length;

        return deQueueElement;
    }

    public void output() {
        for (int i = front; i != rear; i = (i + 1) % array.length) {
            System.out.print(array[i] + "，");
        }
        System.out.println("\n");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void main(String[] args) throws Exception {
        /*MyCircularQueue myQueue = new MyCircularQueue(6);

        myQueue.enQueue(3);
        myQueue.enQueue(5);
        myQueue.enQueue(6);
        myQueue.enQueue(8);
        myQueue.enQueue(1);
        myQueue.output();

        myQueue.deQueue();
        myQueue.deQueue();

        myQueue.output();

        myQueue.deQueue();
        myQueue.enQueue(2);

        myQueue.output();

        myQueue.enQueue(4);
        myQueue.enQueue(9);
        myQueue.output();*/

        int key = Objects.hashCode("this");

        System.out.println("key=" + key);
    }

}

















