package com.xht.androidnote.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.view.View;

import com.xht.androidnote.utils.L;

import java.util.ArrayList;
import java.util.List;

import leakcanary.AppWatcher;
import leakcanary.LeakCanary;

/**
 * Created by xht on 2018/4/28.
 */

public class App extends Application {

    private static Context mContext;

    public App() {
        mContext = this;
    }

    public static Context getAppContext() {
        return mContext;
    }

    public List<View> leakedViews = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();


        // 多进程导致Application多次创建
        String processName = getProcessNameByPID(this, Process.myPid());
        L.i("processName==" + processName);

        // LeakCanary 自定义config
//        AppWatcher.Config.Builder builder = new AppWatcher.Config().newBuilder();
//        builder.watchActivities(false);
//        AppWatcher.Config config = builder.build();
//        AppWatcher.setConfig(config);
    }


    public static String getProcessNameByPID(Context context, int pid) {
        ActivityManager manager
                = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return "";
    }
}
