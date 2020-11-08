package com.xht.androidnote.summarize.test;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by xht on 2020/11/8
 */
public class QueueStack {
    private Queue<Integer> queue1 = new ArrayDeque<>();

    private Queue<Integer> queue2 = new ArrayDeque<>();

    /**
     * 向栈中压入数据
     *
     * @param element
     */
    public void push(Integer element) {
        //两个队列为空时，优先考虑queue1
        if (queue1.isEmpty() && queue2.isEmpty()) {
            queue1.add(element);
            return;
        }

        //如果queue1为空，queue2有数据，直接放入queue2
        if (queue1.isEmpty()) {
            queue2.add(element);
            return;
        }

        //如果queue2为空，queue1有数据，直接放入queue1
        if (queue2.isEmpty()) {
            queue1.add(element);
            return;
        }
    }

    /**
     * 取出栈中的数据
     *
     * @return
     */
    public Integer pop() {
        //两个队列为空时，直接抛出异常
        if (queue1.isEmpty() && queue2.isEmpty()) {
            throw new RuntimeException("stack is empty");
        }

        //如果queue1为空，将queue2中的元素依次加入到 queue1, 弹出最后一个元素
        if (queue1.isEmpty()) {
            while (queue2.size() > 1) {
                queue1.add(queue2.poll());
            }
            return queue2.poll();
        }

        //如果queue2为空，将queue1中的元素依次加入到 queue2, 弹出最后一个元素
        if (queue2.isEmpty()) {
            while (queue1.size() > 1) {
                queue2.add(queue1.poll());
            }
            return queue1.poll();
        }
        return null;
    }
}
