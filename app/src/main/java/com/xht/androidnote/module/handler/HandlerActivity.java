package com.xht.androidnote.module.handler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.L;

import java.lang.ref.WeakReference;

import butterknife.OnClick;

/**
 * Created by xht on 2018/5/30.
 * 1、Handler源码
 * 2、子线程之间的通信
 * 3、HandlerThread
 * <p>
 * 4、Handler的延迟消息原理
 * 5、Looper的无限循环为何不会阻塞主线程
 */

public class HandlerActivity extends BaseActivity {


    private static class MyHandler extends Handler {
        private final WeakReference<HandlerActivity> mActivity;

        public MyHandler(HandlerActivity activity) {
            this.mActivity = new WeakReference<HandlerActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            HandlerActivity handlerActivity = mActivity.get();
            if (handlerActivity == null) {
                return;
            }
            // todo

            L.i("HandlerActivity---static+弱引用 处理handler内存泄漏");
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    L.i("handler---handleMessage()---thread.name==" + Thread.currentThread()
                            .getName());
                    break;
            }
        }
    };


    private Handler handler1;

    private Handler handler2;

    private HandlerThread mHandlerThread;
    private TextView tvResult;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_handler;
    }

    @Override
    protected void initEventAndData() {

        /*handlerTest();

        handlerAndThreadTest();

        handlerThreadTest();*/

        /*new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        });*/

        tvResult = findViewById(R.id.tv_result);


        Handler handlerCallbackTest = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what == 233) {
                    L.i("Handler CallBack 的使用");
                }
                return false;
            }
        });

        handlerCallbackTest.sendEmptyMessageDelayed(233,3000);

        handlerCallbackTest.handleMessage(null);

    }

    private void handlerTest() {
        //发送一个Runnable投递到Handler内部的Looper中去处理。最终也是通过send()方法来完成。即此Runnable并没有创建线程
        handler.post(new Runnable() {
            @Override
            public void run() {
                //主线程
                L.i("handlerTest()---post---run()---thread.name==" + Thread.currentThread()
                        .getName());
            }
        });

        //子线程中发送消息，主线程更新UI
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 子线程thread，发送消息到主线程
                L.i("handlerTest()---thread---run()---thread.name==" + Thread.currentThread()
                        .getName());
                handler.sendEmptyMessage(1);
            }
        });
        thread.start();
    }

    /**
     * 子线程间通信
     * handler + thread
     */
    private void handlerAndThreadTest() {
        new Thread("Thread#1") {
            @Override
            public void run() {
                Looper.prepare();

                handler1 = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case 2:
                                L.i("handlerAndThreadTest()---handler1-----handleMessage()" +
                                        "---thread.name==" + Thread.currentThread().getName());
                                break;
                        }
                    }
                };

                Looper.loop();
            }
        }.start();

        new Thread("Thread#2") {
            @Override
            public void run() {
                super.run();
                L.i("handlerAndThreadTest()---thread2---run()---thread.name==" + Thread
                        .currentThread()
                        .getName());
                handler1.sendEmptyMessage(2);
            }
        }.start();
    }

    private void handlerThreadTest() {

        mHandlerThread = new HandlerThread("WorkThread") {
            //做一些执行前的初始化工作
            @Override
            protected void onLooperPrepared() {
                super.onLooperPrepared();
            }
        };
        mHandlerThread.start();

        handler2 = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                // 当前为子线程---WorkThread
                L.i("handler2---当前线程===" + Thread.currentThread().getName());
                // 执行耗时操作
                timeConsuming();

            }
        };

        handler2.sendEmptyMessage(3);

        new Thread(new Runnable() {
            @Override
            public void run() {
                L.i("handlerThreadTest()---run()---当前线程===" + Thread.currentThread().getName());
                handler2.sendEmptyMessage(4);
            }
        }).start();

    }

    private void timeConsuming() {
        try {
            //模拟耗时
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void intentServiceTest() {
        //==============================测试IntentService=============================//
        /**
         * 将MyIntentService写为内部类时报错
         * java.lang.RuntimeException: Unable to instantiate service com.xht.androidnote.module.service.ServiceActivity$MyIntentService: java.lang.InstantiationException: can't instantiate class com.xht.androidnote.module.service.ServiceActivity$MyIntentService; no empty constructor
         *
         *  Caused by: java.lang.InstantiationException: can't instantiate class com.xht.androidnote.module.service.ServiceActivity$MyIntentService; no empty constructor
         */
        //同一服务只会开启一个工作线程
        //在onHandleIntent函数里依次处理intent请求。使用IntentService执行耗时任务
        Intent i = new Intent(this, MyIntentService.class);
        Bundle bundle = new Bundle();
        bundle.putString("taskName", "task1");
        i.putExtras(bundle);
        startService(i);

        Intent i2 = new Intent(this, MyIntentService.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("taskName", "task2");
        i2.putExtras(bundle2);
        startService(i2);

        startService(i);  //多次启动
    }


    @OnClick({R.id.btn_handler, R.id.btn_handler_thread, R.id.btn_handler_and_thread, R.id
            .btn_handler_intent_service, R.id.btn_handler_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_handler:
                handlerTest();
                break;
            case R.id.btn_handler_and_thread:
                handlerAndThreadTest();
                break;
            case R.id.btn_handler_thread:
                handlerThreadTest();
                break;
            case R.id.btn_handler_intent_service:
                intentServiceTest();
                break;
            case R.id.btn_handler_test:
                MyHandler handler = new MyHandler(HandlerActivity.this);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText("熄屏后处理");
                    }
                }, 10000);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                Log.i("xht", "HandlerActivity---queueIdle()");
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("xht", "HandlerActivity---onResume()");


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放
        if (mHandlerThread != null) {
            mHandlerThread.quit();
        }
    }
}
