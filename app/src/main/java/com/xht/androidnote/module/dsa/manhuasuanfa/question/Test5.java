package com.xht.androidnote.module.dsa.manhuasuanfa.question;

import java.util.Stack;

/**
 * Created by xht on 2020/1/9.
 * <p>
 * 用栈来模拟一个队列，要求实现队列的两个基本操作：入队、出队。
 */
public class Test5 {

    private Stack<Integer> stackA = new Stack<>();
    private Stack<Integer> stackB = new Stack<>();


    private void enqueue(int element) {
        stackA.push(element);
    }


    private Integer dequeue() {
        if(stackB.isEmpty()) {
            if(stackA.isEmpty()) {
                return null;
            }

            while(!stackA.isEmpty()) {
                stackB.push(stackA.pop());
            }
        }

        return stackB.pop();
    }

    public static void main(String[] args) {
        Test5 test = new Test5();

        test.enqueue(1);
        test.enqueue(3);
        test.enqueue(5);

        System.out.println("出队 " + test.dequeue());

        test.enqueue(7);
        System.out.println("出队 " + test.dequeue());
        System.out.println("出队 " + test.dequeue());
        System.out.println("出队 " + test.dequeue());
        test.enqueue(2);
        System.out.println("出队 " + test.dequeue());

    }


}
