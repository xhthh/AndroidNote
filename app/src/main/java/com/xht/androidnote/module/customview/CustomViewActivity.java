package com.xht.androidnote.module.customview;

import android.support.v4.content.ContextCompat;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

/**
 * Created by xht on 2019/4/4.
 */

public class CustomViewActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_customview;
    }

    @Override
    protected void initEventAndData() {
        CarPkView pkView = findViewById(R.id.car_pk_view);

        pkView.setData(ContextCompat.getColor(CustomViewActivity.this, R.color.wzui_c_e5004d_a1),
                ContextCompat.getColor(CustomViewActivity.this, R.color.wzui_c_f9712c_a2), 35, 65);
    }
}
