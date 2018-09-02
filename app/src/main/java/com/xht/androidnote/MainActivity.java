package com.xht.androidnote;

import android.view.View;

import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.module.activity.ATestActivity;
import com.xht.androidnote.module.animation.AnimationActivity;
import com.xht.androidnote.module.asynctask.AsyncTaskActivity;
import com.xht.androidnote.module.bitmap.BitmapActivity;
import com.xht.androidnote.module.broadcastreceiver.BroadCastActivity;
import com.xht.androidnote.module.contentprovider.ContentProviderActivity;
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

import butterknife.OnClick;

/**
 * 创建dev分支
 * git merge dev 在master分支上合并dev
 * git branch -d dev 删除dev分支
 */
public class MainActivity extends BaseActivity {

    // 创建feature1分支，开发某个功能

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {

        /*Singleton instance = Singleton.getInstance();
        instance.doSomething();*/

    }

    @OnClick({R.id.btn_activity, R.id.btn_service, R.id.btn_broadcast_receiver, R.id
            .btn_content_provider, R.id.btn_fragment, R.id.btn_okhttp, R.id.btn_retrofit, R.id
            .btn_glide, R.id.btn_handler, R.id.btn_async_task, R.id.btn_event_dispatch, R.id
            .btn_window, R.id.btn_ipc, R.id.btn_bitmap, R.id.btn_animation, R.id.btn_java})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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

        }
    }

}
