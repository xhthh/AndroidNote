package com.xht.androidnote.module.dsa;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
                int[] array = {1, 2, 4, 6, 7, 9, 10, 13, 15};

                int position = binarySearch(array, 1, 6, 4);
                //int position = binarySearch(array, 8);
                Toast.makeText(mContext, "position=" + position, Toast.LENGTH_SHORT).show();
            }
        });


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
