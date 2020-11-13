package com.xht.androidnote.module.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.L;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leakcanary.AppWatcher;

/**
 * Created by xht on 2018/5/8.
 */

public class BTestActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    private static Activity sActivity;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch_mode_test;
    }

    @Override
    protected void initEventAndData() {
        sActivity = this;

        mTvTitle.setText("Activity B");
        L.i("Activity B------onCreate()");

        AppWatcher.INSTANCE.getObjectWatcher().watch(sActivity,"静态变量持有Activity引用");
    }

    @Override
    protected void onStart() {
        super.onStart();
        L.i("Activity B------onStart()");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        L.i("Activity B------onSaveInstanceState()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        L.i("Activity B------onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.i("Activity B------onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.i("Activity B------onPause()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        L.i("Activity B------onRestoreInstanceState()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.i("Activity B------onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.i("Activity B------onDestroy()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        L.i("Activity B------onConfigurationChanged()");
    }


    @OnClick({R.id.btn_start, R.id.btn_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                skip2Activity(CTestActivity.class);
                break;
            case R.id.btn_finish:
                finish();
                break;
        }
    }
}
