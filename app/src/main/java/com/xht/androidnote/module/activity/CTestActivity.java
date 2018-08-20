package com.xht.androidnote.module.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.L;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2018/5/8.
 */

public class CTestActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initEventAndData() {
        mTvTitle.setText("Activity C");
        L.i("Activity C------onCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        L.i("Activity C------onStart()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        L.i("Activity C------onSaveInstanceState()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        L.i("Activity C------onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.i("Activity C------onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.i("Activity C------onPause()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        L.i("Activity C------onRestoreInstanceState()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.i("Activity C------onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.i("Activity C------onDestroy()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        L.i("Activity C------onConfigurationChanged()");
    }

    @OnClick(R.id.btn_start)
    public void onViewClicked() {
        skip2Activity(BTestActivity.class);
    }
}
