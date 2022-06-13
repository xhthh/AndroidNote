package com.xht.androidnote.module.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityTest extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * 给定一个 View 和 id，查找对应 id 的 view
     *
     * @param view
     * @param id
     * @return
     */
    public View findViewById(View view, int id) {
        int viewId = view.getId();
        if (id == viewId) {
            return view;
        }

        ViewGroup viewGroup;
        if (view instanceof ViewGroup) {
            viewGroup = (ViewGroup) view;

            int childCount = viewGroup.getChildCount();
            if (childCount <= 0)
                return null;
            for (int i = 0; i < childCount; i++) {
                View childView = viewGroup.getChildAt(i);
                View v = findViewById(childView, id);
                if (v != null)
                    return v;
            }
        }
        return null;
    }


    private int maxDeep(View view) {
        if (!(view instanceof ViewGroup)) {
            return 1;
        }

        ViewGroup viewGroup = (ViewGroup) view;

        int childCount = viewGroup.getChildCount();

        if (childCount == 0) {
            return 1;
        }

        int max = 1;
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            int deep = maxDeep(child) + 1;
            if (deep > max) {
                max = deep;
            }
        }

        return max;
    }
}
