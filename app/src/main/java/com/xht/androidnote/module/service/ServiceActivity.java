package com.xht.androidnote.module.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.ProgressBar;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.module.ContentReviewActivity;
import com.xht.androidnote.utils.L;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2018/6/6.
 * 1、service的两种启动方式
 * startService()
 * bindService()
 * 2、前台服务
 * 3、IntentService
 * 原理
 * 优点
 * 4、Service与Activity中间的通信方式
 * 1）通过Binder对象
 * 通过serviceConnected()返回的IBinder对象得到service对象
 * 2）通过broadcast(广播)的形式
 */

public class ServiceActivity extends BaseActivity {

    @BindView(R.id.pb_service_download)
    ProgressBar mProgressBar;
    private Intent intent;

    private boolean isServiceStarted;
    private boolean isBound;

    private MyService mService;

    private MsgReceiver msgReceiver;

    private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder myBinder = (MyService.MyBinder) service;
            myBinder.showTip();

            mService = ((MyService.MyBinder) service).getService();

            mService.setOnProgressListener(new OnProgressListener() {
                @Override
                public void onProgress(int progress) {
                    mProgressBar.setProgress(progress);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_service;
    }

    @Override
    protected void initEventAndData() {
        intent = new Intent(mContext, MyService.class);

        //动态注册广播接收器
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.xht.androidnote.module.service.RECEIVER");
        registerReceiver(msgReceiver, intentFilter);


        Intent intent = new Intent(mContext, MyService.class);
        startService(intent);

    }

    @OnClick({R.id.btn_start_service, R.id.btn_stop_service, R.id.btn_bind_service, R.id
            .btn_unbind_service, R.id.btn_content_review, R.id.btn_start_fore_service, R.id
            .btn_stop_fore_service, R.id.btn_service_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start_service:
                startService();
                break;
            case R.id.btn_stop_service:
                stopService();
                break;
            case R.id.btn_bind_service:
                bindService();
                break;
            case R.id.btn_unbind_service:
                unBindService();
                break;
            case R.id.btn_start_fore_service:
                startForeService();
                break;
            case R.id.btn_stop_fore_service:
                stopForeService();
                break;
            case R.id.btn_content_review:
                startActivity(new Intent(mContext, ContentReviewActivity.class).putExtra
                        ("content", "https://github" +
                                ".com/LRH1993/android_interview/blob/master/android/basis/service" +
                                ".md"));
                break;
            case R.id.btn_service_download:
                //开始下载
                mService.startDownLoad();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销广播
        if (msgReceiver != null) {
            unregisterReceiver(msgReceiver);
        }
    }

    //=======================================================//

    private void stopForeService() {
        if (intent != null && isServiceStarted) {
            intent.putExtra("type", 1);
            startService(intent);
            isServiceStarted = false;
        }
    }

    private void startForeService() {
        if (intent != null) {
            isServiceStarted = true;
            intent.putExtra("type", 0);
            startService(intent);
        }
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

    private void stopService() {
        if (intent != null && isServiceStarted) {
            stopService(intent);
            isServiceStarted = false;
        }
    }

    private void startService() {
        if (intent != null) {
            isServiceStarted = true;
            startService(intent);
        }
    }

    //===========================================================//

    /**
     * 广播接收器
     */
    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //拿到进度，更新UI
            int progress = intent.getIntExtra("progress", 0);
            L.i("onReceive()---progress==" + progress);
            mProgressBar.setProgress(progress);
        }

    }
}
