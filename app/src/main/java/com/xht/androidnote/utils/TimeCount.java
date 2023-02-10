package com.xht.androidnote.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.xht.androidnote.R;
import com.xht.androidnote.base.App;

/**
 * Author:Shaojian
 * DATA:2016/11/15.
 * ACTION:计时器控件逻辑层
 * TYPE:控件
 */
public class TimeCount extends CountDownTimer {
    private TextView tv;
    public static int totalTime = 60000;
    public static int timeInterval = 1000;
    private int fromType = 0;
    private OnTimeFinishListener mOnTimeFinishListener;
    public TimeCount(long millisInFuture, long countDownInterval, TextView tv) {
        this(millisInFuture, countDownInterval, tv, 0);
    }

    public TimeCount(long millisInFuture, long countDownInterval, TextView tv, int fromType) {
        super(millisInFuture, countDownInterval);
        this.fromType = fromType;
        this.tv = tv;
    }

    //计时过程显示
    @Override
    public void onTick(long millisUntilFinished) {
        if(mOnTimeFinishListener != null){
            mOnTimeFinishListener.onTimeGoingOn();
        }
        tv.setClickable(false);
        if (fromType == 1 || fromType == 2 || fromType == 3 || fromType == 4 || fromType == 6) {
            tv.setTextColor(ContextCompat.getColor(tv.getContext(), R.color.text_hint_color));
            tv.setText(millisUntilFinished / 1000 + "s");
        } else {
            tv.setText(App.getAppContext().getString(R.string.timer_resend) + millisUntilFinished / 1000 + "s");
        }
    }

    //计时完成触发
    @Override
    public void onFinish() {
        if(mOnTimeFinishListener != null){
            mOnTimeFinishListener.onTimeFinished();
        }
        tv.setClickable(true);
        if (fromType == 1 || fromType == 2 || fromType == 3 || fromType == 6) {//注册短信验证 || 修改手机号
            tv.setTextColor(ContextCompat.getColor(tv.getContext(), R.color.houyan));
            tv.setText(App.getAppContext().getString(R.string.timer_resend));
        } else if (fromType == 4) {//账户注销
            tv.setText(App.getAppContext().getString(R.string.timer_resend));
        } else {
            tv.setText(App.getAppContext().getString(R.string.timer_free_register));
        }

    }
    public interface OnTimeFinishListener{
        void onTimeFinished();
        void onTimeGoingOn();
    }
    public void setOnTimeFinishListener(OnTimeFinishListener onTimeFinishListener){
        this.mOnTimeFinishListener = onTimeFinishListener;
    }
}
