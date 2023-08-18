package com.xht.androidnote.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Insets;
import android.os.Build;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;

import androidx.annotation.ColorInt;

public class StatusBarUtil {

    /** Use default color {@link #defaultColor_21} between 5.0 and 6.0.*/
    public static final int USE_DEFAULT_COLOR = -1;

    /** Use color {@link #setUseStatusBarColor} between 5.0 and 6.0.*/
    public static final int USE_CUR_COLOR = -2;

    /**
     * Default status bar color between 21(5.0) and 23(6.0).
     * If status color is white, you can set the color outermost.
     * */
    public static int defaultColor_21 = Color.parseColor("#33000000");

    /**
     * Setting the status bar color.
     * It must be more than 21(5.0) to be valid.
     *
     * @param color Status color.
     */
    public static void setUseStatusBarColor(Activity activity, @ColorInt int color) {
        setUseStatusBarColor(activity, color, USE_CUR_COLOR);
    }

    /**
     * It must be more than 21(5.0) to be valid.
     * Setting the status bar color.Supper between 21 and 23.
     *
     * @param color         Status color.
     * @param surfaceColor  Between 21 and 23,if surfaceColor == {@link #USE_DEFAULT_COLOR},the status color is defaultColor_21,
     *                      else if surfaceColor == {@link #USE_CUR_COLOR}, the status color is color,
     *                      else the status color is surfaceColor.
     */
    public static void setUseStatusBarColor(Activity activity, @ColorInt int color, int surfaceColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || (surfaceColor == USE_CUR_COLOR)) ? color : surfaceColor == USE_DEFAULT_COLOR ? defaultColor_21 : surfaceColor);
        }
    }



    /**
     * Get the height of the state bar by reflection.
     *
     * @return Status bar height if it is not equal to -1,
     */
    public static int getStatusBarHeight(Context context) {
        return getSizeByReflection(context, "status_bar_height");
    }




    public static int getSizeByReflection(Context context, String field) {
        int size = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField(field).get(object).toString());
            size = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (size <= 0) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                size = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        if (size == -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                WindowMetrics windowMetrics = wm.getCurrentWindowMetrics();
                WindowInsets windowInsets = windowMetrics.getWindowInsets();
                Insets insets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout());
                return insets.top;
            }
        }
        return size;
    }


    public static void fitsStatusBarPadding(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setPadding(
                    view.getPaddingLeft(),
                    view.getPaddingTop() + StatusBarUtil.getStatusBarHeight(view.getContext()),
                    view.getPaddingRight(),
                    view.getPaddingBottom());
        }
    }

    /**
     * 首页后台配置普通图背景下的间距或者无配图
     *
     * @param view
     */
    public static void fitsHomeStatusBarPadding(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setPadding(
                    view.getPaddingLeft(),
                    StatusBarUtil.getStatusBarHeight(view.getContext()),
                    view.getPaddingRight(),
                    view.getPaddingBottom());
        }
    }

    /**
     * 首页后台配置大图背景下的间距
     *
     * @param view
     * @param topDistance
     */
    public static void fitsHomeStatusBarPadding(View view, int topDistance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setPadding(
                    view.getPaddingLeft(),
                    StatusBarUtil.getStatusBarHeight(view.getContext()) + topDistance,
                    view.getPaddingRight(),
                    view.getPaddingBottom());
        }
    }

    /**
     * 导航栏和状态栏
     * @param show
     */
    public static void setSystemUIVisible(Activity activity,boolean show) {
        if (show) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiFlags |= 0x00001000;
            activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            uiFlags |= 0x00001000;
            activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }
}