package com.xht.androidnote.module.handler;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;

import com.xht.androidnote.utils.L;

/**
 * Created by xht on 2018/6/20.
 *
 * 需要在清单文件注册
 */

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("xht");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // 执行耗时操作，如下载。。。
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //若启动IntentService多次，那么每个耗时操作则以队列的方式在IntentService的onHandleIntent回调方法中依次执行，执行完自动结束。
        //根据Intent的不同进行不同的事务处理
        String taskName = intent.getExtras().getString("taskName");
        switch (taskName) {
            case "task1":
                L.i("MyIntentService", "do task1");
                break;
            case "task2":
                L.i("MyIntentService", "do task2");
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.i("MyIntentService", "onCreate()");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        L.i("MyIntentService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i("MyIntentService", "onDestroy()");
    }

}
