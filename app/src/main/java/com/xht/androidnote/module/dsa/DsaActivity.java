package com.xht.androidnote.module.dsa;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

/**
 * Created by xht on 2019/4/9.
 * <p>
 * data structures and algorithms
 */

public class DsaActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_dsa;
    }

    @Override
    protected void initEventAndData() {

        Button btnCompute = findViewById(R.id.btn_compute);
        btnCompute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                int[] array = {1, 2, 4, 6, 7, 9, 10, 13, 15};
                //
                //                int position = binarySearch(array, 1, 6, 4);
                //                //int position = binarySearch(array, 8);
                //                Toast.makeText(mContext, "position=" + position, Toast.LENGTH_SHORT).show();


                //                int[] array = {5, 3, 6, 2, 10, 0};
                //                int[] sorted = selectSort(array);
                //                for (int i = 0; i < sorted.length; i++) {
                //                    Log.i("xht", "i=" + sorted[i]);
                //                }

                int fact = fact(5);
                Log.i("xht", "fact==" + fact);
            }
        });


    }

    private int fact(int x) {
        if (x == 1)
            return 1;
        return x * fact(x - 1);
    }


    private int[] selectSort(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int k = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[k]) {
                    k = j;
                }
            }

            if (i != k) {
                int temp = array[k];
                array[k] = array[i];
                array[i] = temp;
            }
        }
        return array;
    }

    private int[] selectSort1(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = array.length - 1; j > i; j--) {
                if (array[j] < array[i]) {
                    int temp = array[j];
                    array[j] = array[i];
                    array[i] = temp;
                }
            }
        }

        return array;
    }



    /*private int[] selectSort1(int[] array) {
        int[] newArray = new int[array.length];

        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[findSmallest(array)];

        }

        return newArray;
    }*/

    private int findSmallest(int[] array) {
        int smallestIndex = 0;
        int smallest = array[0];
        for (int i = 0; i < array.length; i++) {
            if (array[i] < smallest) {
                smallest = array[i];
                smallestIndex = i;
            }
        }
        return smallestIndex;
    }


    /**
     * 二分查找 普通循环
     * 必须为有序列表
     * [1,2,4,6,7,9,10,13,15]
     *
     * @param array
     * @param key
     * @return
     */
    private int binarySearch(int[] array, int key) {
        int position = -1;

        int low = 0;
        int high = array.length - 1;

        while (low <= high && position == -1) {
            int mid = (low + high) / 2;

            int guess = array[mid];

            if (key == guess) {
                position = mid;
            } else if (key < guess) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return position;
    }


    /**
     * 二分查找，递归方式
     *
     * @param array
     * @param start
     * @param end
     * @param key
     * @return
     */
    private int binarySearch(int[] array, int start, int end, int key) {
        int mid = (end - start) / 2 + start;

        if (array[mid] == key) {
            return mid;
        }

        if (start >= end) {
            return -1;
        } else if (key > array[mid]) {
            return binarySearch(array, mid + 1, end, key);
        } else if (key < array[mid]) {
            return binarySearch(array, start, end - 1, key);
        }

        return -1;
    }


}
