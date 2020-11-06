package com.xht.androidnote.module.customview;

import android.view.View;
import android.view.ViewGroup;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

/**
 * Created by xht on 2020/9/2
 */
public class CustomViewActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_view;
    }

    @Override
    protected void initEventAndData() {

    }

    public int traverseViewGroup(View view) {
        int viewCount = 0;

        if (view == null) {
            return 0;
        }

        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = viewGroup.getChildAt(0);
                viewCount += traverseViewGroup(child);
            }
        } else {
            viewCount++;
        }

        return viewCount;
    }
}
