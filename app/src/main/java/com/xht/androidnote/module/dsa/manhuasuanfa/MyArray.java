package com.xht.androidnote.module.dsa.manhuasuanfa;

/**
 * Created by xht on 2019/12/24.
 */
public class MyArray {

    private int[] array;
    private int size;

    public MyArray(int capacity) {
        this.array = new int[capacity];
        size = 0;
    }

    public int delete(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("超出数组实际元素范围!");
        }

        int deleteElement = array[index];

        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }

        size--;
        return deleteElement;
    }

    /**
     * 插入
     * [3]
     * [3, 7]
     * [3, 7, 9]
     * [3, 7, 9, 5]
     *
     * @param element
     * @param index
     */
    public void insert(int element, int index) {
        //判断访问下标是否超出范围
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("超出数组实际元素范围!");
        }

        //如果实际元素达到数组容量上限，则对数组进行扩容
        if (size >= array.length) {
            resize();
        }

        for (int i = size - 1; i >= index; i--) {
            System.out.println("size=" + size + "  index=" + index + "  i=" + i);

            array[i + 1] = array[i];
        }

        array[index] = element;
        size++;
    }

    private void resize() {
        int[] newArray = new int[array.length * 2];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }

        //从旧数组复制到新数组
        //        System.arraycopy(array, 0, newArray, 0, array.length);

        array = newArray;
    }

    public void output() {
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                System.out.print("[");
            }
            System.out.print(array[i] + "，");
            if (i == size - 1) {
                System.out.print("]");
            }
        }
    }

    public static void main(String[] args) {
        MyArray myArray = new MyArray(4);

        myArray.insert(3, 0);
        myArray.insert(7, 1);
        myArray.insert(9, 2);
        myArray.insert(5, 3);
        //        myArray.insert(-1, 4);
        //        myArray.insert(4, 5);
        //        myArray.insert(2, 6);
        //        myArray.insert(0, 7);
        //        myArray.insert(1, 8);
        //        myArray.insert(8, 9);
        myArray.output();
        System.out.println("\n");
        myArray.insert(6, 1);
        myArray.output();

        myArray.delete(2);

        System.out.println("\n");
        myArray.output();
    }

}
