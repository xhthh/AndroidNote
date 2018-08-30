package com.xht.androidnote.module.broadcastreceiver;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.xht.androidnote.MainActivity;
import com.xht.androidnote.utils.L;

/**
 * Created by xht on 2018/8/29.
 * <p>
 * 有的文章说 BroadCastReceiver中启动Activity需要setFlags()
 * 如果不设置intent的FLAG_ACTIVITY_NEW_TASK属性，就会报这个异常：
 * android.util.AndroidRuntimeException: Calling startActivity() from outside of an Activity  context
 * requires the FLAG_ACTIVITY_NEW_TASK flag.
 * <p>
 * 这里测试不加flag也可以启动Activity，而且context instanceof Activity 返回true。。。。。
 * <p>
 * https://blog.csdn.net/dct8888/article/details/52064160#commentBox
 */

public class MyBroadCastReceiver extends BroadcastReceiver {
    //接收到广播后自动调用该方法
    @Override
    public void onReceive(Context context, Intent intent) {
        //写入接收广播后的操作
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isAvailable()) {
            Toast.makeText(context, "network is available", Toast.LENGTH_SHORT).show();

            //test context.startActivity
            if (context instanceof Activity) {
                L.i("context instanceof Activity-------------true---------------");
            }
            if (context instanceof Application) {
                L.i("context instanceof Application-------------true---------------");
            }
            Intent intentMain = new Intent(context, MainActivity.class);

            //intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentMain);
        } else {
            Toast.makeText(context, "network is unavailable", Toast.LENGTH_SHORT).show();
        }
    }
}
