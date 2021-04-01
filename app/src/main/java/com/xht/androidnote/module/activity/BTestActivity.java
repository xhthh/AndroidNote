package com.xht.androidnote.module.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.module.service.MyService;
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

    private boolean isBound;

    private MyService mService;

    private Intent intent;

    private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder myBinder = (MyService.MyBinder) service;
            myBinder.showTip();

            mService = ((MyService.MyBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch_mode_test2;
    }

    @Override
    protected void initEventAndData() {
        sActivity = this;

        //mTvTitle.setText("Activity B");
        L.i("Activity B------onCreate()");

        AppWatcher.INSTANCE.getObjectWatcher().watch(sActivity, "静态变量持有Activity引用");

        Intent intentForResult = new Intent();
        intentForResult.putExtra("name", "xht");
        setResult(666, intentForResult);


        intent = new Intent(mContext, MyService.class);
        intent.putExtra("clientName", "BTestActivity");
        findViewById(R.id.btn_bind_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindService();
            }
        });

        findViewById(R.id.btn_unbind_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unBindService();
            }
        });
    }

    private void unBindService() {
        if (isBound) {
            L.i("ActivityB 执行 unbindService");
            unbindService(con);
            isBound = false;
        }
    }

    private void bindService() {
        if (intent != null && con != null) {
            isBound = true;
            L.i("Activity B 执行 bindService");
            bindService(intent, con, BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        L.i("Activity B------onStart()");
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

        //子线程也可以更新UI，只要在 onReusme() 中或之前，即ViewRootImpl实例创建之前就不会抛出异常
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                String name = Thread.currentThread().getName();
//                L.e("threadName = " + name);
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mTvTitle.setText("Activity B");
//                    }
//                }).start();
//            }
//        },3000);
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
                skip2Activity(ATestActivity.class);
                break;
            case R.id.btn_finish:
                finish();
                break;
        }
    }
    /*
    Activity A------onCreate()、onStart()、onResume()、onPause()
    Activity B------onCreate()、onStart()、onResume()
    Activity A------onStop()、onSaveInstanceState()
    Activity B------onPause()a
    Activity A------onRestart()、onStart()、onNewIntent()、onResume()
    Activity B------onStop()、onDestroy()
     */
}
