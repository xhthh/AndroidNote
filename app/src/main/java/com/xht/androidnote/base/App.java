package com.xht.androidnote.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;

import com.xht.androidnote.utils.L;

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

    @Override
    public void onCreate() {
        super.onCreate();


        // 多进程导致Application多次创建
        String processName = getProcessNameByPID(this, Process.myPid());
        L.i("processName==" + processName);
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
