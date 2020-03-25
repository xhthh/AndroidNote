package com.xht.androidnote.module.dsa.interview40;

/**
 * Created by xht on 2020/3/25
 * 反转单链表
 */
public class Test2 {

    /*
        反转一个单链表。

        示例:

        输入: 1->2->3->4->5->NULL
        输出: 5->4->3->2->1->NULL
        进阶:
        你可以迭代或递归地反转链表。你能否用两种方法解决这道题？
     */
    public static void main(String[] args) {

        ListNode listNode1 = new ListNode(1);
        ListNode listNode2 = new ListNode(2);
        ListNode listNode3 = new ListNode(3);
        ListNode listNode4 = new ListNode(4);
        ListNode listNode5 = new ListNode(5);

        listNode1.next = listNode2;
        listNode2.next = listNode3;
        listNode3.next = listNode4;
        listNode4.next = listNode5;
        listNode5.next = null;

        traversal(listNode1);

//        ListNode node = reverseList(listNode1);
//        traversal(node);


        ListNode reverseNode = reverse(listNode1);
        traversal(reverseNode);
    }


    /*
        准备两个空结点 pre用来保存先前结点、temp用来做临时变量
        在头结点node遍历的时候此时为1结点
        temp = 1结点.next(2结点)
        1结点.next=pre(null)
        pre = 1结点
        node = 2结点

        进行下一次循环node=2结点
        temp = 2结点.next(3结点)
        2结点.next=pre(1结点)=>即完成2->1
        pre = 2结点
        node = 3结点

        进行循环...
     */
    public static ListNode reverseList(ListNode node) {
        ListNode prev = null;//保存先前的节点
        ListNode temp = null;//临时变量

        while (node != null) {
            temp = node.next;
            node.next = prev;
            prev = node;

            node = temp;
        }

        return prev;
    }


    /*
        递归

        递归实质上就是系统帮你压栈的过程，系统在压栈的时候会保留现场。

        head是1节点，temp是2节点，递归，传入2节点
        head是2节点，temp是3节点，递归，传入3节点
        head是3节点，temp是4节点，递归，传入4节点
        head是4节点，temp是5节点，递归，传入5节点
        head是5节点，return head

        进入head是4节点的现场，temp是5节点，newHead是5节点，
        5节点.next = 4节点，完成5-->4，同时4节点.next=null，return 5节点


        进入head是3节点的现场，temp是4节点，newHead是5节点，
        4节点.next = 3节点，完成5-->4-->3，同时3节点.next=null，return 5节点

        。。。。。

     */
    public static ListNode reverse(ListNode head) {
        if(head == null || head.next == null) {
            return head;
        }

        ListNode temp = head.next;
        ListNode newHead = reverse(head.next);
        temp.next = head;
        head.next = null;

        return newHead;
    }

    private static void traversal(ListNode node) {
        while (node != null) {
            System.out.print(node.val + "   ");
            node = node.next;
        }
        System.out.println();
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

}
