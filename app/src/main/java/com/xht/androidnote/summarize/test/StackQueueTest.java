package com.xht.androidnote.summarize.test;

import java.util.Stack;

/**
 * 测试栈和队列
 */
public class StackQueueTest {
    public static void main(String[] args) {
        testQueueStack();
    }

    private static void testQueueStack() {
        QueueStack stack = new QueueStack();
        stack.push(2);
        stack.push(4);
        stack.push(7);
        stack.push(5);
        System.out.println(stack.pop());
        System.out.println(stack.pop());

        stack.push(1);
        System.out.println(stack.pop());
    }

    //===================================用栈实现队列================================================

    /**
     * 测试栈和队列
     */
    private static void testStackQueue() {

        StackQueue stackQueue = new StackQueue();
        stackQueue.enQueue(1);
        stackQueue.enQueue(2);
        stackQueue.enQueue(3);
        System.out.println(stackQueue.deQueue());
        System.out.println(stackQueue.deQueue());
        stackQueue.enQueue(4);
        System.out.println(stackQueue.deQueue());
        System.out.println(stackQueue.deQueue());
    }

    static class StackQueue {
        private Stack<Integer> stackA = new Stack<>();
        private Stack<Integer> stackB = new Stack<>();

        private void enQueue(int n) {
            stackA.push(n);
        }

        private Integer deQueue() {
            if (stackB.isEmpty()) {
                if (stackA.isEmpty()) {
                    return null;
                }
                while (!stackA.isEmpty()) {
                    stackB.push(stackA.pop());
                }
            }
            return stackB.pop();
        }
    }

    //===================================用栈实现队列================================================


}
