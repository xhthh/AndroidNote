package com.xht.androidnote.module.broadcastreceiver;

import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Build;
import android.view.View;
import android.widget.Button;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2018/6/11.
 * 1、广播接收器实现原理
 * 1）广播接收者 通过 Binder机制在 AMS 注册
 * 2）广播发送者 通过 Binder 机制向 AMS 发送广播
 * 3）AMS 根据 广播发送者 要求，在已注册列表中，寻找合适的广播接收者
 * 寻找依据：IntentFilter / Permission
 * 4）AMS将广播发送到合适的广播接收者相应的消息循环队列中；
 * 5）广播接收者通过 消息循环 拿到此广播，并回调 onReceive()
 * 2、使用
 * 1）定义广播接收器
 * 2）注册广播接收器
 * ①静态注册
 * 在AndroidManifest.xml文件中进行注册，当App退出后，Receiver仍然可以接收到广播并且进行相应的处理
 * ②动态注册
 * 在代码中动态注册，当App退出后，也就没办法再接受广播了
 * <p>
 * 3、BroadcastReceiver和LocalBroadCast的区别
 */

public class BroadCastActivity extends BaseActivity {

    private MyBroadCastReceiver mReceiver;

    private LocalBroadcastManager mLocalBroadcastManager;//本地广播

    @BindView(R.id.btn_receiver_register)
    Button mBtnReceiverRegister;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_broadcast;
    }

    @Override
    protected void initEventAndData() {

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*//动态注册
        mReceiver = new MyBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mReceiver, intentFilter);*/

        // 本地广播
        //mLocalBroadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }

        if (mLocalBroadcastManager != null && mReceiver != null) {
            mLocalBroadcastManager.unregisterReceiver(mReceiver);
        }
    }

    @OnClick({R.id.btn_receiver_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_receiver_register:
                //动态注册
                mReceiver = new MyBroadCastReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    registerReceiver(mReceiver, intentFilter, RECEIVER_EXPORTED);
                }
                break;
        }
    }
}
