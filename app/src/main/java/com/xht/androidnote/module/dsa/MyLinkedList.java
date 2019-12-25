package com.xht.androidnote.module.dsa;

/**
 * Created by xht on 2019/12/24.
 */
public class MyLinkedList {

    //链表实际长度
    private int size;

    //头节点指针
    private Node head;

    //尾节点指针
    private Node last;

    public void insert(int data, int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("超出链表节点范围!");
        }

        Node insertedNode = new Node(data);

        if (size == 0) {
            //空链表
            head = insertedNode;
            last = insertedNode;
        } else if (index == 0) {
            //插入头部
            insertedNode.next = head;
            head = insertedNode;
        } else if (index == size) {
            //插入尾部
            last.next = insertedNode;
            last = insertedNode;
        } else {
            Node prevNode = get(index - 1);

            insertedNode.next = prevNode.next;

            prevNode.next = insertedNode;
        }

        size++;
    }

    public Node remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("超出链表节点范围！");
        }

        Node delete = null;

        if (index == 0) {
            //删除头节点
            delete = head;

            head = head.next;
        } else if (index == size - 1) {
            //删除尾节点
            Node prevNode = get(index - 1);
            delete = prevNode.next;
            prevNode.next = null;
            last = prevNode;
        } else {
            Node prevNode = get(index - 1);
            delete = prevNode.next;

            Node nextNode = prevNode.next.next;

            prevNode.next = nextNode;
        }

        size--;

        return delete;
    }

    /**
     * 查找节点
     *
     * @param index
     * @return
     */
    private Node get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("超出链表节点范围！");
        }

        Node temp = head;

        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }

        return temp;
    }

    public void output() {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data + " ");
            temp = temp.next;
        }
        System.out.println("\n");
    }

    public static void main(String[] args) {
        MyLinkedList myLinkedList = new MyLinkedList();

        myLinkedList.insert(3,0);
        myLinkedList.insert(7,1);
        myLinkedList.insert(9,2);
        myLinkedList.insert(5,3);
        myLinkedList.insert(6,1);
        myLinkedList.output();
        myLinkedList.remove(0);
        myLinkedList.output();

        myLinkedList.insert(8,2);
        myLinkedList.output();
    }

    private static class Node {
        int data;
        Node next;

        public Node(int data) {
            this.data = data;
        }
    }

}
