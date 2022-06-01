package com.xht.androidnote;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.module.activity.ATestActivity;
import com.xht.androidnote.module.adaptation.AndroidAdaptationActivity;
import com.xht.androidnote.module.animation.AnimationActivity;
import com.xht.androidnote.module.annotation.AnnotationActivity;
import com.xht.androidnote.module.asynctask.AsyncTaskActivity;
import com.xht.androidnote.module.bitmap.BitmapActivity;
import com.xht.androidnote.module.broadcastreceiver.BroadCastActivity;
import com.xht.androidnote.module.constraint.ConstraintLayoutTestActivity;
import com.xht.androidnote.module.contentprovider.ContentProviderActivity;
import com.xht.androidnote.module.customview.CustomViewActivity;
import com.xht.androidnote.module.dsa.manhuasuanfa.DsaActivity;
import com.xht.androidnote.module.eventbus.EventBusActivity;
import com.xht.androidnote.module.eventdispatch.EventDispatchActivity;
import com.xht.androidnote.module.fragment.FragmentTestActivity;
import com.xht.androidnote.module.glide.GlideActivity;
import com.xht.androidnote.module.handler.HandlerActivity;
import com.xht.androidnote.module.hotfix.HotFixTestActivity;
import com.xht.androidnote.module.inflate.Factory2TestActivity;
import com.xht.androidnote.module.ipc.IPCActivity;
import com.xht.androidnote.module.java.JavaTestActivity;
import com.xht.androidnote.module.kotlin.KotlinTestActivity;
import com.xht.androidnote.module.kotlin.WidgetTestActivity;
import com.xht.androidnote.module.okhttp.OkHttpActivity;
import com.xht.androidnote.module.pickerview.PickerViewActivity;
import com.xht.androidnote.module.proxy.ProxyActivity;
import com.xht.androidnote.module.recyclerview.RecyclerViewActivity;
import com.xht.androidnote.module.retrofit.RetrofitActivity;
import com.xht.androidnote.module.rxjava.RxJavaActivity;
import com.xht.androidnote.module.service.ServiceActivity;
import com.xht.androidnote.module.thread.ThreadTestActivity;
import com.xht.androidnote.module.view.edittext.EditTextActivity;
import com.xht.androidnote.module.view.fps.FpsViewActivity;
import com.xht.androidnote.module.window.DialogWindowActivity;

import butterknife.BindView;
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
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int height = scrollView.getHeight();
        int measuredHeight = scrollView.getMeasuredHeight();

        Log.i("xht", "height=" + height + "  measuredHeight=" + measuredHeight);
        Log.i("xht", "height2=" + llContent.getHeight() + "  measuredHeight2=" + llContent.getMeasuredHeight());
    }

    @Override
    protected void initEventAndData() {

        /*
            llContent持有Activity的引用
            View.mContext references a destroyed activity

         */
        //        App app = (App) getApplication();
        //        app.leakedViews.add(llContent);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                int height = scrollView.getHeight();
                int measuredHeight = scrollView.getMeasuredHeight();

                Log.i("xht", "height=" + height + "  measuredHeight=" + measuredHeight);
                Log.i("xht", "height2=" + llContent.getHeight() + "  measuredHeight2=" + llContent.getMeasuredHeight());
            }
        });

        //        Handler handler = new Handler();
        //        handler.postDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        //                finish();
        //                startActivity(new Intent(mContext, MainActivity.class));
        //            }
        //        }, 3000);
    }

    @OnClick({R.id.btn_fps, R.id.btn_rxjava, R.id.btn_widget_test, R.id.btn_event_custom_view, R.id.btn_thread, R.id.btn_test_pickerview, R.id.btn_activity,
            R.id.btn_service, R.id.btn_broadcast_receiver, R.id.btn_content_provider, R.id.btn_fragment, R.id.btn_okhttp,
            R.id.btn_retrofit, R.id.btn_glide, R.id.btn_handler, R.id.btn_async_task, R.id.btn_event_dispatch,
            R.id.btn_window, R.id.btn_ipc, R.id.btn_bitmap, R.id.btn_animation, R.id.btn_java,
            R.id.btn_icon_replace, R.id.btn_annotation, R.id.btn_eventbus, R.id.btn_icon_hot_fix, R.id.btn_java_dynamic_proxy,
            R.id.btn_recyclerview, R.id.btn_dsa, R.id.btn_edit_text, R.id.btn_constraint, R.id.btnKotlin,
            R.id.btn_adaptation_test, R.id.btn_inflater})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_inflater:
                skip2Activity(Factory2TestActivity.class);
                break;
            case R.id.btn_adaptation_test:
                skip2Activity(AndroidAdaptationActivity.class);
                break;
            case R.id.btnKotlin:
                skip2Activity(KotlinTestActivity.class);
                break;
            case R.id.btn_widget_test:
                skip2Activity(WidgetTestActivity.class);
                break;
            case R.id.btn_fps:
                skip2Activity(FpsViewActivity.class);
                break;
            case R.id.btn_constraint:
                skip2Activity(ConstraintLayoutTestActivity.class);
                break;
            case R.id.btn_event_custom_view:
                skip2Activity(CustomViewActivity.class);
                break;
            case R.id.btn_edit_text:
                skip2Activity(EditTextActivity.class);
                break;
            case R.id.btn_dsa:
                skip2Activity(DsaActivity.class);
                break;
            case R.id.btn_annotation:
                skip2Activity(AnnotationActivity.class);
                break;
            case R.id.btn_rxjava:
                skip2Activity(RxJavaActivity.class);
                break;
            case R.id.btn_thread:
                skip2Activity(ThreadTestActivity.class);
                break;
            case R.id.btn_eventbus:
                skip2Activity(EventBusActivity.class);
                break;
            case R.id.btn_recyclerview:
                skip2Activity(RecyclerViewActivity.class);
                break;
            case R.id.btn_test_pickerview:
                skip2Activity(PickerViewActivity.class);
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
                if (getComponentName().getClassName().equals(ACTIVITY_ALIAS_1)) {
                    setIcon(ACTIVITY_ALIAS_2);
                } else {
                    setIcon(ACTIVITY_ALIAS_1);
                }
                break;
            case R.id.btn_icon_hot_fix:
                skip2Activity(HotFixTestActivity.class);
                break;
            case R.id.btn_java_dynamic_proxy:
                skip2Activity(ProxyActivity.class);
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

    private static final String TAG = "MainActivity";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "---onNewIntent()---");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "---onStart()---");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "---onResume()---");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "---onRestart()---");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
