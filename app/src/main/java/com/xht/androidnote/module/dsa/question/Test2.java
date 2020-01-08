package com.xht.androidnote.module.dsa.question;

import java.util.Stack;

/**
 * Created by xht on 2020/1/8.
 *
 * 实现一个栈，该栈带有出栈（pop）、入栈（push）、取最小元素（getMin）3
 * 个方法。要保证这3个方法的时间复杂度都是O(1)。
 */
public class Test2 {

    private Stack<Integer> mainStack = new Stack<>();
    private Stack<Integer> minStack = new Stack<>();


    /**
     * 入栈
     * @param element
     */
    public void push(int element) {

        mainStack.push(element);

        //如果辅助栈为空，或者新元素小于或等于辅助栈栈顶，则将新元素压入辅助栈
        if (minStack.isEmpty() || element <= minStack.peek()) {
            minStack.push(element);
        }

    }


    /**
     * 出栈
     * @return
     */
    public int pop() {
        //如果出栈元素和辅助栈栈顶元素值相等，辅助栈出栈
        if (mainStack.peek().equals(minStack.peek())) {
            minStack.pop();
        }

        return mainStack.pop();
    }


    /**
     * 获取栈的最小元素
     * @return
     * @throws Exception
     */
    public int getMin() throws Exception {
        if (minStack.isEmpty()) {
            throw new Exception("stack is empty");
        }

        return minStack.peek();
    }


    public static void main(String[] args) throws Exception {
        Test2 test2 = new Test2();

        test2.push(4);
        test2.push(9);
        test2.push(7);
        test2.push(3);
        test2.push(8);
        test2.push(5);
        test2.pop();
        test2.pop();
        test2.pop();

        System.out.println("最小值 = " + test2.getMin());
    }
}
