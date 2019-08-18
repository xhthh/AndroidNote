package com.xht.androidnote.module.thread;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2019/8/18.
 */
public class ThreadTestActivity extends BaseActivity {

    @BindView(R.id.btn_start1)
    Button btnStart1;
    @BindView(R.id.btn_start2)
    Button btnStart2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_thread_test;
    }

    @Override
    protected void initEventAndData() {
    }

    @OnClick({R.id.btn_start1, R.id.btn_start2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start1:
                test1();
                break;
            case R.id.btn_start2:
                test2();
                break;
        }
    }

    class MyThread extends Thread {
        public MyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            Log.i("xht", "MyThread---run()---name==" + Thread.currentThread().getName());
        }
    }

    private void test2() {
        MyThread myThread = new MyThread("线程2");
        myThread.start();
    }

    private void test1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("xht", "当前线程 " + Thread.currentThread().getName());
            }
        }, "线程1").start();
    }

}
