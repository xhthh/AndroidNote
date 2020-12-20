package com.xht.androidnote.module.window;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.L;

import java.lang.reflect.Field;

import butterknife.OnClick;

/**
 * Created by xht on 2018/6/25.
 * <p>
 * AlertDialog源码或者Toast实现的原理都是windowmanager.addView();来添加的， 它们都是一个个view ,因此不会对activity的生命周期有任何影响。
 *
 * Window表示一个窗口的概念，它是一个抽象类，具体实现是PhoneWindow。
 * WindowManager是外界访问Window的入口，Window的具体实现位于WindowManagerService中，
 * WindowManager和WindowManagerService的交互是一个IPC过程。Android中所有的视图透视通过Window来呈现的，
 * 不管是Activity、Dialog还是Toast，它们的视图都是附加在Window上的，因此Window实际是View的直接管理者。
 *
 * Window是一个抽象的概念，每一个Window都对应着一个View和一个ViewRootImpl，Window和View通过ViewRootImpl来建立联系，
 * 因此Window并不是实际存在的，它是以View的形式存在。
 *
 *
 *
 */

public class DialogWindowActivity extends BaseActivity implements View.OnTouchListener {


    private Button mFloatingButton;
    private WindowManager.LayoutParams mLayoutParams;
    private WindowManager mWindowManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_window;
    }

    @Override
    protected void initEventAndData() {

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

    }

    @OnClick({R.id.btn_dialog, R.id.btn_popup_window, R.id.btn_show_window})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog:
                showCustomDialog();
                break;
            case R.id.btn_popup_window:
                showPopupWindow();
                break;
            case R.id.btn_show_window:
                addAWindow();
                break;
        }
    }


    private void addAWindow() {
        mFloatingButton = new Button(this);
        mFloatingButton.setText("我是新添加的Button");

        mLayoutParams = new WindowManager.LayoutParams(ViewGroup
                .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0, 0,
                PixelFormat.TRANSPARENT);
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager
                .LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.x = 500;
        mLayoutParams.y = 500;

        mFloatingButton.setOnTouchListener(this);
        mWindowManager.addView(mFloatingButton, mLayoutParams);
    }

    private void showPopupWindow() {

    }

    /**
     * 自定义Dilaog
     * 自定义布局setView()
     */
    private void showCustomDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("孙悟空")
                .setMessage("卡米哈米哈")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "点击取消", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "点击确定", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
        dialog.show();

        // 在dialog执行show之后才能来设置
        TextView tvMsg = (TextView) dialog.findViewById(android.R.id.message);
        tvMsg.setTextSize(16);
        tvMsg.setTextColor(Color.parseColor("#4E4E4E"));

        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextSize(16);
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#8C8C8C"));
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextSize(16);
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1DA6DD"));

        try {
            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object alertController = mAlert.get(dialog);

            Field mTitleView = alertController.getClass().getDeclaredField("mTitleView");
            mTitleView.setAccessible(true);

            TextView tvTitle = (TextView) mTitleView.get(alertController);
            if (null != tvTitle) {
                tvTitle.setTextSize(16);
                tvTitle.setTextColor(Color.parseColor("#000000"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int x = (int) event.getX();
                int y = (int) event.getY();
                mLayoutParams.x = rawX;
                mLayoutParams.y = rawY;
                mWindowManager.updateViewLayout(mFloatingButton, mLayoutParams);
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
            default:
                break;
        }

        return false;
    }

    //=================================弹框时Activity的生命周期=========================================//

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.i("DialogWindowActivity------onCreate()");

    }

    @Override
    protected void onStart() {
        super.onStart();
        L.i("DialogWindowActivity------onStart()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        L.i("DialogWindowActivity------onSaveInstanceState()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        L.i("DialogWindowActivity------onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.i("DialogWindowActivity------onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.i("DialogWindowActivity------onPause()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        L.i("DialogWindowActivity------onRestoreInstanceState()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.i("DialogWindowActivity------onStop()");
    }

    @Override
    protected void onDestroy() {
        try {
            mWindowManager.removeView(mFloatingButton);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        super.onDestroy();
        L.i("DialogWindowActivity------onDestroy()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        L.i("DialogWindowActivity------onConfigurationChanged()");
    }
}
