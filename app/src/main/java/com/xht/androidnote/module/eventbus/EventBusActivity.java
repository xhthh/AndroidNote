package com.xht.androidnote.module.eventbus;

import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by xht on 2019/8/16.
 */
public class EventBusActivity extends BaseActivity implements View.OnClickListener {

    private Button mBtnSkip;
    private Button mBtnRegist;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_eventbus;
    }

    @Override
    protected void initEventAndData() {
        mBtnRegist = findViewById(R.id.btn_regist_eventbus);
        mBtnSkip = findViewById(R.id.btn_to_another);

        mBtnRegist.setOnClickListener(this);
        mBtnSkip.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_regist_eventbus:
                regist();
                break;
            case R.id.btn_to_another:
                startActivity(new Intent(EventBusActivity.this, EventBusAnotherActivity.class));
                break;
        }
    }

    private void regist() {
        EventBusHelper.getInstance().register(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void test1(TestEvent event) {
        Log.i("xht", "EventBusActivity---当前线程 " + Thread.currentThread().getName() + " 是否为主线程 = " + (Looper.getMainLooper() == Looper.myLooper()));
        Log.i("xht", "EventBusActivity---POSTING---message=" + event.getFlag());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void test2(TestEvent event) {
        Log.i("xht", "EventBusActivity---当前线程 " + Thread.currentThread().getName() + " 是否为主线程 = " + (Looper.getMainLooper() == Looper.myLooper()));
        Log.i("xht", "EventBusActivity---MAIN---message=" + event.getFlag());

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void test3(TestEvent event) {
        Log.i("xht", "EventBusActivity---当前线程 " + Thread.currentThread().getName() + " 是否为主线程 = " + (Looper.getMainLooper() == Looper.myLooper()));
        Log.i("xht", "EventBusActivity---MAIN---BACKGROUND=" + event.getFlag());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void test4(TestEvent event) {
        Log.i("xht", "EventBusActivity---当前线程 " + Thread.currentThread().getName() + " 是否为主线程 = " + (Looper.getMainLooper() == Looper.myLooper()));
        Log.i("xht", "EventBusActivity---ASYNC---message=" + event.getFlag());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.getInstance().unregister(this);
    }

}
