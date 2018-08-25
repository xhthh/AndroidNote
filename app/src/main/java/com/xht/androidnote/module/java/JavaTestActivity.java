package com.xht.androidnote.module.java;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xht on 2018/8/15.
 */

public class JavaTestActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_java_test;
    }

    @Override
    protected void initEventAndData() {
        /*int[] array = {3, 1, 2, 5, 4};

        BubbleSort(array);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("=========");
        }

        int[] array2 = {3, 1, 2, 5, 4};
        BubbleSort2(array2);*/


        //        arrayListTest();


        //        linkedListTest();

        //        hashMapTest();

        //test();

        threadTest();
    }

    /**
     * t.join()方法阻塞调用此方法的线程(calling thread)，直到线程t完成
     */
    private void threadTest() {
        final Thread thread1 = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("t1 begin");
            }
        }, "t1");

        final Thread thread2 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    thread1.join(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t2 begin");
            }
        }, "t2");

        Thread thread3 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    thread2.join(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t3 begin");
            }
        }, "t3");

        thread1.start();
        thread2.start();
        thread3.start();
    }

    private void test() {
        Integer a = 3;
        int b = 3;

        Integer c = 223;
        Integer d = 223;

        L.i("1---a==b：" + (a == b));
        L.i("2---c==d：" + (c == d));

        /**
         * Integer.Java 类，有一个内部私有类，IntegerCache.java，它缓存了从-128到127之间的所有的整数对象。
         */
    }


    private void hashMapTest() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("语文", 1);
        map.put("数学", 2);
        map.put("英语", 3);
        map.put("历史", 4);
        map.put("政治", 5);
        map.put("地理", 6);
        map.put("生物", 7);
        map.put("化学", 8);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * ArrayList
     * 底层是以数组实现
     * 节约空间，但数组有容量限制。超出限制时会增加50%容量，用System.arraycopy()复制到新的数组，
     * 因此最好能给出数组大小的预估值。默认第一次插入元素时创建大小为10的数组。
     */
    private void arrayListTest() {
        ArrayList<String> list = new ArrayList<>();
        list.add("语文: 99");
        list.add("数学: 98");
        list.add("英语: 100");
        list.remove(0);

        list.set(0, "92");
        list.get(0);
    }

    /**
     * LinkedList
     * 以双向链表实现。链表无容量限制，但双向链表本身使用了更多空间，也需要额外的链表指针操作。
     */
    private void linkedListTest() {
        LinkedList<String> list = new LinkedList<>();
        list.add("语文: 1");
        list.add("数学: 2");
        list.add("英语: 3");

        list.set(0, "历史");
        list.get(0);
    }


    public void mapTest() {
        Day day1 = new Day(1, 2, 3);
        Day day2 = new Day(2, 3, 4);
        Map<String, Day> map = new HashMap<String, Day>();
        //成对放入key-value对
        map.put("第一个", day1);
        map.put("第二个", day2);
        //判断是否包含指定的key
        System.out.println(map.containsKey("第一个"));
        //判断是否包含指定的value
        System.out.println(map.containsValue(day1));
        //循环遍历
        //1.获得Map中所有key组成的set集合
        Set<String> keySet = map.keySet();
        //2.使用foreach进行遍历
        for (String key : keySet) {
            //根据key获得指定的value
            System.out.println(map.get(key));
        }
        //根据key来移除key-value对
        map.remove("第一个");
        System.out.println(map);
    }

    class Day {

        public Day(int i, int j, int k) {

        }
    }


    /**
     * [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
     * <p>
     * [99, 99, 99, 99, 99, 99, 99, 99, 99, 99]
     */
    public void iteratorTest() {
        List<MyObject> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new MyObject(i));
        }

        System.out.println(list.toString());

        Iterator<MyObject> iterator = list.iterator();//集合元素的值传给了迭代变量，仅仅传递了对象引用。保存的仅仅是指向对象内存空间的地址
        while (iterator.hasNext()) {
            MyObject next = iterator.next();
            next.num = 99;
        }

        System.out.println(list.toString());
    }

    static class MyObject {
        int num;

        MyObject(int num) {
            this.num = num;
        }

        @Override
        public String toString() {
            return String.valueOf(num);
        }
    }


    /**
     * 快速排序
     * <p>
     * 先从数列中取出一个数作为key值；
     * 将比这个数小的数全部放在它的左边，大于或等于它的数全部放在它的右边；
     * 对左右两个小数列重复第二步，直至各区间只有1个数。
     *
     * @param a
     * @param l
     * @param r
     */
    public static void quickSort(int a[], int l, int r) {
        if (l >= r)
            return;

        int i = l;
        int j = r;
        int key = a[l];//选择第一个数为key

        while (i < j) {

            while (i < j && a[j] >= key)//从右向左找第一个小于key的值
                j--;
            if (i < j) {
                a[i] = a[j];
                i++;
            }

            while (i < j && a[i] < key)//从左向右找第一个大于key的值
                i++;

            if (i < j) {
                a[j] = a[i];
                j--;
            }
        }
        //i == j
        a[i] = key;
        quickSort(a, l, i - 1);//递归调用
        quickSort(a, i + 1, r);//递归调用
    }


    /**
     * 选择排序
     * <p>
     * 在长度为N的无序数组中，第一次遍历n-1个数，找到最小的数值与第一个元素交换；
     * 第二次遍历n-2个数，找到最小的数值与第二个元素交换；
     * 。。。
     * 第n-1次遍历，找到最小的数值与第n-1个元素交换，排序完成。
     *
     * @param array
     * @param length
     */
    public static void select_sort(int array[], int length) {
        for (int i = 0; i < length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < length; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                int temp = array[i];
                array[i] = array[minIndex];
                array[minIndex] = temp;
            }
        }
    }


    /**
     * 冒泡排序
     * <p>
     * 比较相邻的两个数据，如果第二个数小，就交换位置。
     * 从后向前两两比较，一直到比较最前两个数据。最终最小数被交换到起始的位置，这样第一个最小数的位置就排好了。
     * 继续重复上述过程，依次将第2、3...n-1个最小数排好位置。
     *
     * @param arr
     */
    public static void BubbleSort(int[] arr) {

        int temp;//临时变量
        for (int i = 0; i < arr.length - 1; i++) {   //表示趟数，一共arr.length-1次。
            for (int j = arr.length - 1; j > i; j--) {

                if (arr[j] < arr[j - 1]) {
                    temp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = temp;
                }
            }
            for (int k = 0; k < arr.length; k++) {
                System.out.print(arr[k] + " ");
            }

            System.out.println("\n");
        }
    }


    /**
     * 冒泡优化
     * <p>
     * 设置标志位flag，如果发生了交换flag设置为true；如果没有交换就设置为false。
     * 这样当一轮比较结束后如果flag仍为false，即：这一轮没有发生交换，说明数据的顺序已经排好，没有必要继续进行下去。
     *
     * @param arr
     */
    public static void BubbleSort2(int[] arr) {

        int temp;//临时变量
        boolean flag;
        for (int i = 0; i < arr.length - 1; i++) {   //表示趟数，一共arr.length-1次。
            flag = false;
            for (int j = arr.length - 1; j > i; j--) {

                if (arr[j] < arr[j - 1]) {
                    temp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = temp;
                    flag = true;
                }
            }

            if (!flag)
                break;


            for (int k = 0; k < arr.length; k++) {
                System.out.print(arr[k] + " ");
            }

            System.out.println("\n");
        }
    }
}
