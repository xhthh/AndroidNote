package com.xht.androidnote.module.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

import com.xht.androidnote.R;
import com.xht.androidnote.module.ContentReviewActivity;
import com.xht.androidnote.utils.L;

/**
 * Created by xht on 2018/6/7.
 */

public class MyService extends Service {


    private boolean isForeLive;

    /**
     * id不可设置为0,否则不能设置为前台service？
     */
    private static final int NOTIFICATION_ID = 233;

    /**
     * 进度条的最大值
     */
    public static final int MAX_PROGRESS = 100;
    /**
     * 进度条的进度值
     */
    private int progress = 0;

    /**
     * 更新进度的回调接口
     */
    private OnProgressListener onProgressListener;

    private Intent intent = new Intent("com.xht.androidnote.module.service.RECEIVER");


    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }

        public void showTip() {
            L.i("我是来此服务的提示");
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        L.i("MyService---onCreate()");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        L.i("MyService---onBind()");
        //return null;
        return new MyBinder();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*if (startId == 8){
            stopSelf();
        }*/

        int type = intent.getIntExtra("type", -1);
        L.i("MyService---onStartCommand()---startId==" + startId + "  type==" + type);

        if (type == 0 && !isForeLive) {
            // 开启前台服务
            startForeService();
            isForeLive = true;
        } else if (type == 1) {
            if (isForeLive) {
                isForeLive = false;
                stopForeground(true);
            }
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        L.i("MyService---onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i("MyService---onDestroy()");
    }


    /**
     * 注册回调接口的方法，供外部调用
     *
     * @param onProgressListener
     */
    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    /**
     * 增加get()方法，供Activity调用
     *
     * @return 下载进度
     */
    public int getProgress() {
        return progress;
    }

    /**
     * 模拟下载任务，每秒钟更新一次
     */
    public void startDownLoad() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (progress < MAX_PROGRESS) {
                    progress += 5;

                    //1、使用IBinder与Activity通信，通过回调传递progress
                    //进度发生变化通知调用方
                    /*if (onProgressListener != null) {
                        onProgressListener.onProgress(progress);
                    }*/

                    //2、通过BroadcastReceiver
                    intent.putExtra("progress", progress);
                    sendBroadcast(intent);


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void startForeService() {
        // 自定义通知栏标题
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.item_notification);
        //创建通知
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("前台服务")
                //.setContent(remoteViews)
                .setContentText("My life is getting better.");
        // 创建点跳转的Intent(跳转到通知详情)
        Intent intent = new Intent(this, ContentReviewActivity.class);
        intent.putExtra("content", "https://github" +
                ".com/LRH1993/android_interview/blob/master/android/basis/service.md");


        /**
         * 在4.4模拟器上没效果
         */

        // 创建通知详情页的栈
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //为其添加父栈，当从通知详情页回退时，将退到添加的父栈中
        stackBuilder.addParentStack(ContentReviewActivity.class);
        stackBuilder.addNextIntentWithParentStack(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent
                .FLAG_UPDATE_CURRENT);

        /*Intent intentMain = new Intent(this, MainActivity.class);
        Intent[] intents = new Intent[2];
        intents[0] = intentMain;
        intents[1] = intent;
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, intents, PendingIntent
                .FLAG_UPDATE_CURRENT);*/


        //设置跳转Intent到通知中
        builder.setContentIntent(pendingIntent);
        //获取通知服务
        NotificationManager nm = (NotificationManager) getSystemService(Context
                .NOTIFICATION_SERVICE);
        //构建通知
        Notification notification = builder.build();
        //显示通知
        nm.notify(NOTIFICATION_ID, notification);
        //启动前台服务
        startForeground(NOTIFICATION_ID, notification);
    }
}
