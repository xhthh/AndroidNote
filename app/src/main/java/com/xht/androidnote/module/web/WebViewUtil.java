package com.xht.androidnote.module.web;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebView;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.List;

/**
 * 07-18 17:09:14.972 E/AndroidRuntime(15723): FATAL EXCEPTION: main
 * 07-18 17:09:14.972 E/AndroidRuntime(15723): Process: com.hbxj.haidai:pushcore, PID: 15723
 * 07-18 17:09:14.972 E/AndroidRuntime(15723): java.lang.RuntimeException: Using WebView from more than one process at once with the same data directory is not supported. https://crbug.com/558377 : Current process com.hbxj.haidai:pushcore (pid 15723), lock owner com.hbxj.haidai (pid 13798)
 * 07-18 17:09:14.972 E/AndroidRuntime(15723): 	at org.chromium.android_webview.AwDataDirLock.b(HwWebview-12.0.4.307.4397:27)
 * 07-18 17:09:14.972 E/AndroidRuntime(15723): 	at org.chromium.android_webview.AwBrowserProcess.k(HwWebview-12.0.4.307.4397:5)
 * 07-18 17:09:14.972 E/AndroidRuntime(15723): 	at pE.h(HwWebview-12.0.4.307.4397:31)
 * 07-18 17:09:14.972 E/AndroidRuntime(15723): 	at oE.run(HwWebview-12.0.4.307.4397:2)
 * 07-18 17:09:14.972 E/AndroidRuntime(15723): 	at android.os.Handler.handleCallback(Handler.java:966)
 * 07-18 17:09:14.972 E/AndroidRuntime(15723): 	at android.os.Handler.dispatchMessage(Handler.java:110)
 * 07-18 17:09:14.972 E/AndroidRuntime(15723): 	at android.os.Looper.loopOnce(Looper.java:205)
 * 07-18 17:09:14.972 E/AndroidRuntime(15723): 	at android.os.Looper.loop(Looper.java:293)
 * 07-18 17:09:14.972 E/AndroidRuntime(15723): 	at android.app.ActivityThread.main(ActivityThread.java:9596)
 * 07-18 17:09:14.972 E/AndroidRuntime(15723): 	at java.lang.reflect.Method.invoke(Native Method)
 * 07-18 17:09:14.972 E/AndroidRuntime(15723): 	at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:586)
 * 07-18 17:09:14.972 E/AndroidRuntime(15723): 	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1204)
 * <p>
 * <p>
 * Android官方：
 * https://developer.android.google.cn/about/versions/pie/android-9.0-changes-28?hl=zh-cn#framework-security-changes
 * <p>
 * 解决方案：
 * https://www.jianshu.com/p/d09ceb7d4c78
 * https://www.yisu.com/jc/445583.html
 *
 * 在 application 里调用
 */
public class WebViewUtil {

    public static void handleWebViewDir(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return;
        }
        try {
            String suffix = "";
            String processName = getCurProcessName(context);
            if (!TextUtils.equals(context.getPackageName(), processName)) {//判断不等于默认进程名称
                suffix = TextUtils.isEmpty(processName) ? context.getPackageName() : processName;
                WebView.setDataDirectorySuffix(suffix);
                suffix = "_" + suffix;
            }
            tryLockOrRecreateFile(context, suffix);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.P)
    private static void tryLockOrRecreateFile(Context context, String suffix) {
        String sb = context.getDataDir().getAbsolutePath() +
                "/app_webview" + suffix + "/webview_data.lock";
        File file = new File(sb);
        if (file.exists()) {
            try {
                FileLock tryLock = new RandomAccessFile(file, "rw").getChannel().tryLock();
                if (tryLock != null) {
                    tryLock.close();
                } else {
                    createFile(file, file.delete());
                }
            } catch (Exception e) {
                e.printStackTrace();
                boolean deleted = false;
                if (file.exists()) {
                    deleted = file.delete();
                }
                createFile(file, deleted);
            }
        }
    }

    private static void createFile(File file, boolean deleted) {
        try {
            if (deleted && !file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess == null) {
                continue;
            }
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

}
