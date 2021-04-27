package com.xht.androidnote.module.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xht.androidnote.MainActivity;
import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.module.service.MyService;
import com.xht.androidnote.module.service.OnProgressListener;
import com.xht.androidnote.utils.L;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2018/5/8.
 * 1、Activity的生命周期
 * 1）正常情况下
 * 2）异常情况下
 * 横竖屏
 * 2、启动模式
 * 3、数据保存
 * onSaveInstanceState()
 * onRestoreInstanceState()
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
        return R.layout.activity_launch_mode_test;
    }

    @Override
    protected void initEventAndData() {
        mTvTitle.setText("Activity A");
        L.i("Activity A------onCreate()");


        //测试ANR
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        findViewById(R.id.btn_app_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开其他App
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.xht.testapp");
                if (intent != null) {
                    intent.putExtra("name", "xht");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        findViewById(R.id.btn_app_jump_spec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转其他App指定页面
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName componentName = new ComponentName("com.xht.testapp", "com.xht.testapp.LoginActivity");
                intent.setComponent(componentName);
                intent.putExtra("source", "AndroidNote");
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_app_jump_url).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setData(Uri.parse("xht://pull.xht.demo/login?type=110"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        intent = new Intent(mContext, MyService.class);
        intent.putExtra("clientName", "ATestActivity");
        findViewById(R.id.btn_bind_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindService();
            }
        });
    }

    private void unBindService() {
        if (isBound) {
            unbindService(con);
            isBound = false;
        }
    }

    private void bindService() {
        if (intent != null && con != null) {
            isBound = true;
            bindService(intent, con, BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        L.i("Activity A------onNewIntent()");
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
        //ATestActivity 启动模式是 singleTask 在该页面再次启动自己 startActivity()  onPause()--->onNewIntent()--->onResume()
        //如果是 startActivityForResult() 则会重新打开一个页面？
//        Intent intent = new Intent(mContext, ATestActivity.class);
//        startActivityForResult(intent, 666);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 666) {
            if (data != null) {
                String name = data.getStringExtra("name");
                L.i("ATestActivity---onActivityResult()---name=" + name);
            }
        }
    }
}
