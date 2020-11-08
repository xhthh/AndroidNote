package com.xht.androidnote.summarize.test;

import java.util.Stack;

/**
 * Created by xht on 2020/11/7
 */
public class StackQueue {
    private Stack<Integer> stackA = new Stack<>();
    private Stack<Integer> stackB = new Stack<>();

    public void enqueue(int element) {
        stackA.push(element);
    }

    public Integer dequeue() {
        if (stackB.isEmpty()) {
            if (stackA.isEmpty()) {
                return null;
            }
            transfer();
        }
        return stackB.pop();
    }

    private void transfer() {
        while (!stackA.isEmpty()) {
            stackB.push(stackA.pop());
        }
    }
}
