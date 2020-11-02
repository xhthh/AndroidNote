package com.xht.androidnote.module.activity;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import com.xht.androidnote.MainActivity;
import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.L;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2018/5/8.
 * 1、Activity的生命周期
 *      1）正常情况下
 *      2）异常情况下
 *          横竖屏
 * 2、启动模式
 * 3、数据保存
 *      onSaveInstanceState()
 *      onRestoreInstanceState()
 */

public class ATestActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    /*
     横竖屏切换
     Activity A------onConfigurationChanged()
     Activity A------onPause()
     Activity A------onSaveInstanceState()
     Activity A------onStop()
     Activity A------onDestroy()
     Activity A------onCreate()
     Activity A------onStart()
     Activity A------onRestoreInstanceState()
     Activity A------onResume()

     */

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch_mode_test;
    }

    @Override
    protected void initEventAndData() {
        mTvTitle.setText("Activity A");
        L.i("Activity A------onCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        L.i("Activity A------onStart()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        L.i("Activity A------onSaveInstanceState()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        L.i("Activity A------onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.i("Activity A------onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.i("Activity A------onPause()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        L.i("Activity A------onRestoreInstanceState()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.i("Activity A------onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.i("Activity A------onDestroy()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        L.i("Activity A------onConfigurationChanged()");
    }

    @OnClick(R.id.btn_start)
    public void onViewClicked() {
        skip2Activity(BTestActivity.class);

        //弹出Dialog 不会影响Activity的生命周期
//        AlertDialog dialog = new AlertDialog.Builder(ATestActivity.this)
//                .setIcon(R.mipmap.ic_launcher_round)
//                .setCancelable(false)
//                .setMessage("Error")
//                .setTitle("Warring")
//                .show();


        /*Intent intent = new Intent();
        intent.setClass(mContext, BTestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/

        //skip2Activity(SingleTopActivity.class);
    }

}
