package com.xht.androidnote.module.eventbus;

import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by xht on 2019/8/16.
 */
public class EventBusActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvReceive;
    private Button mBtnSendMessage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_eventbus;
    }

    @Override
    protected void initEventAndData() {
        EventBusHelper.getInstance().register(this);

        mBtnSendMessage = findViewById(R.id.btn_send_message);
        mTvReceive = findViewById(R.id.tv_receive_message);


        mBtnSendMessage.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_message:
                sendMessage();
                break;
        }
    }

    private void sendMessage() {
        EventBusHelper.getInstance().post(new TestEvent("fuck"));
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    private void test1(TestEvent event) {
        Log.i("xht", "当前线程 " + Thread.currentThread().getName() + " 是否为主线程 = " + (Looper.getMainLooper() == Looper.myLooper()));
        Log.i("xht", "POSTING---message=" + event.getFlag());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    private void test2(TestEvent event) {
                Log.i("xht", "当前线程 " + Thread.currentThread().getName() + " 是否为主线程 = " + (Looper.getMainLooper() == Looper.myLooper()));
        Log.i("xht", "MAIN---message=" + event.getFlag());

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    private void test3(TestEvent event) {
                Log.i("xht", "当前线程 " + Thread.currentThread().getName() + " 是否为主线程 = " + (Looper.getMainLooper() == Looper.myLooper()));
        Log.i("xht", "MAIN---BACKGROUND=" + event.getFlag());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    private void test4(TestEvent event) {
                Log.i("xht", "当前线程 " + Thread.currentThread().getName() + " 是否为主线程 = " + (Looper.getMainLooper() == Looper.myLooper()));
        Log.i("xht", "ASYNC---message=" + event.getFlag());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.getInstance().unregister(this);
    }

}
