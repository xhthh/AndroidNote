package com.xht.androidnote.module.optimize;

import android.view.View;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.module.view.fps.FpsViewActivity;

public class OptTestActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_opt_test;
    }

    @Override
    protected void initEventAndData() {
        findViewById(R.id.btn_fps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果sleep期间没有再发生触摸事件则不会ANR，如果有触摸事件且规定的时间内没有处理则会ANR
//                try {
//                    Thread.sleep(6000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                skip2Activity(FpsViewActivity.class);
            }
        });

        findViewById(R.id.btn_block_detect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip2Activity(BlockDetectActivity.class);
            }
        });
    }
}