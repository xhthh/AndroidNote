package com.xht.androidnote;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;

import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.module.activity.ATestActivity;
import com.xht.androidnote.module.animation.AnimationActivity;
import com.xht.androidnote.module.asynctask.AsyncTaskActivity;
import com.xht.androidnote.module.bitmap.BitmapActivity;
import com.xht.androidnote.module.broadcastreceiver.BroadCastActivity;
import com.xht.androidnote.module.contentprovider.ContentProviderActivity;
import com.xht.androidnote.module.eventbus.EventBusHelper;
import com.xht.androidnote.module.eventbus.TestEvent;
import com.xht.androidnote.module.eventdispatch.EventDispatchActivity;
import com.xht.androidnote.module.fragment.FragmentTestActivity;
import com.xht.androidnote.module.glide.GlideActivity;
import com.xht.androidnote.module.handler.HandlerActivity;
import com.xht.androidnote.module.ipc.IPCActivity;
import com.xht.androidnote.module.java.JavaTestActivity;
import com.xht.androidnote.module.okhttp.OkHttpActivity;
import com.xht.androidnote.module.retrofit.RetrofitActivity;
import com.xht.androidnote.module.service.ServiceActivity;
import com.xht.androidnote.module.window.DialogWindowActivity;
import com.xht.androidnote.utils.L;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.OnClick;

/**
 * 创建dev分支
 * git merge dev 在master分支上合并dev
 * git branch -d dev 删除dev分支
 * <p>
 * 创建feature1分支 合并 解决冲突
 * <p>
 * <p>
 * 创建dev 推送dev到远程
 * <p>
 * 修改dev，pull 远程分支---hh
 * <p>
 * 修改ssh测试
 */
public class MainActivity extends BaseActivity {

    private static final String ACTIVITY_ALIAS_1 = "com.xht.androidnote.MainActivity_rect";
    private static final String ACTIVITY_ALIAS_2 = "com.xht.androidnote.MainActivity_circle";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {

        EventBusHelper.getInstance().register(this);

        /*Singleton instance = Singleton.getInstance();
        instance.doSomething();*/

    }

    @OnClick({R.id.btn_test, R.id.btn_activity, R.id.btn_service, R.id.btn_broadcast_receiver, R.id
            .btn_content_provider, R.id.btn_fragment, R.id.btn_okhttp, R.id.btn_retrofit, R.id
            .btn_glide, R.id.btn_handler, R.id.btn_async_task, R.id.btn_event_dispatch, R.id
            .btn_window, R.id.btn_ipc, R.id.btn_bitmap, R.id.btn_animation, R.id.btn_java, R.id.btn_icon_replace})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_test:
                skip2Activity(TestActivity.class);
                break;
            case R.id.btn_activity:
                skip2Activity(ATestActivity.class);
                break;
            case R.id.btn_service:
                skip2Activity(ServiceActivity.class);
                break;
            case R.id.btn_broadcast_receiver:
                skip2Activity(BroadCastActivity.class);
                break;
            case R.id.btn_content_provider:
                skip2Activity(ContentProviderActivity.class);
                break;
            case R.id.btn_fragment:
                skip2Activity(FragmentTestActivity.class);
                break;
            case R.id.btn_okhttp:
                skip2Activity(OkHttpActivity.class);
                break;
            case R.id.btn_retrofit:
                skip2Activity(RetrofitActivity.class);
                break;
            case R.id.btn_glide:
                skip2Activity(GlideActivity.class);
                break;
            case R.id.btn_handler:
                skip2Activity(HandlerActivity.class);
                break;
            case R.id.btn_async_task:
                skip2Activity(AsyncTaskActivity.class);
                break;
            case R.id.btn_event_dispatch:
                skip2Activity(EventDispatchActivity.class);
                break;
            case R.id.btn_window:
                skip2Activity(DialogWindowActivity.class);
                break;
            case R.id.btn_ipc:
                skip2Activity(IPCActivity.class);
                break;
            case R.id.btn_bitmap:
                skip2Activity(BitmapActivity.class);
                break;
            case R.id.btn_animation:
                skip2Activity(AnimationActivity.class);
                break;
            case R.id.btn_java:
                skip2Activity(JavaTestActivity.class);
                break;
            case R.id.btn_icon_replace:
                if(getComponentName().getClassName().equals(ACTIVITY_ALIAS_1)) {
                    setIcon(ACTIVITY_ALIAS_2);
                } else {
                    setIcon(ACTIVITY_ALIAS_1);
                }
                break;
        }
    }

    private void setIcon(String activity_alias) {
        Context ctx = getApplication();
        PackageManager pm = ctx.getPackageManager();
        // 使ACTIVITY_ALIAS_1失效
        pm.setComponentEnabledSetting(
                new ComponentName(ctx, ACTIVITY_ALIAS_1),
                ACTIVITY_ALIAS_1.equals(activity_alias) ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        // 使ACTIVITY_ALIAS_2生效
        pm.setComponentEnabledSetting(
                new ComponentName(ctx, ACTIVITY_ALIAS_2),
                ACTIVITY_ALIAS_2.equals(activity_alias) ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        Log.e("xht", "setIcon----success!!-------alias==" + activity_alias);

        //重启桌面（有问题，图标会消失）
        /*ActivityManager am = (ActivityManager) ctx.getSystemService(Activity.ACTIVITY_SERVICE);
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> resolves = pm.queryIntentActivities(i, 0);
        for (ResolveInfo res : resolves) {
            if (res.activityInfo != null) {
                am.killBackgroundProcesses(res.activityInfo.packageName);
            }
        }*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(TestEvent event) {
        L.i("MainActivity---onEventMainThread()---event==" + event.getFlag());
    }

    @Override
    protected void onDestroy() {
        EventBusHelper.getInstance().unregister(this);
        super.onDestroy();
    }
}
