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
        findViewById(R.id.btn_viewpager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip2Activity(CustomViewPagerActivity.class);
            }
        });

        findViewById(R.id.btn_nested_scrollview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip2Activity(NestedActivity.class);
            }
        });
        findViewById(R.id.btn_coordinator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip2Activity(CoordinatorActivity.class);
            }
        });
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
