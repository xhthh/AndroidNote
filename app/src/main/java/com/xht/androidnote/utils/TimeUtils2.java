package com.xht.androidnote.utils;


import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class TimeUtils2 {
    /*倒计时时长  单位：秒*/
    public static int COUNT = 20 * 60;
    /*当前做*/
    private static int CURR_COUNT = 0;
    /*预计结束的时间*/
    private static long TIME_END = 0;
    /*计时器*/
    private static Timer countdownTimer;
    /*显示倒计时的textView*/
    private static TextView txtCountdown;

    /**
     * 开始倒计时
     *
     * @param isFirst  标识是否是第一次进入
     * @param second   倒计时时长  单位：秒
     * @param textView 显示倒计时的textView
     */
    public static void startCountdown(boolean isFirst, int second, TextView textView) {
        COUNT = second;
        long data = System.currentTimeMillis();
        long time = TIME_END;
        //第一次进入时，重新赋值
        if (isFirst) {
            CURR_COUNT = COUNT;
            time = data + COUNT * 1000;
            TIME_END = time;
        } else {
            int the_difference = ((int) (time - data)) / 1000;
            CURR_COUNT = the_difference;
        }
        //开始倒计时
        txtCountdown = textView;
        if (countdownTimer == null) {
            countdownTimer = new Timer();
            countdownTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = CURR_COUNT--;
                    handler.sendMessage(msg);
                }
            }, 0, 1000);
            //第二个参数delay:"0"的意思是:无延迟
            //第三个参数period:"1000"的意思是:每隔多长时间调用一次（单位毫秒）
        }
    }

    /**
     * 结束倒计时
     */
    public static void stopCountdown() {
        //发送消息，结束倒计时
        Message message = new Message();
        message.what = 0;
        handler.sendMessage(message);
    }

    private static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what <= 0) {
                if (countdownTimer != null) {
                    countdownTimer.cancel();
                    countdownTimer = null;
                }
                txtCountdown.setText("剩余：00:00:00");
                txtCountdown.setEnabled(true);
            } else {
                //倒计时效果展示
                String txtTime = formatSecondTime(msg.what);
                txtCountdown.setText(txtTime);
                txtCountdown.setEnabled(false);
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 时间格式化方法
     *
     * @param second 秒数
     * @return
     */
    private static String formatSecondTime(int second) {
        int hour = 0;
        int minute = 0;
        if (second > 60) {
            minute = second / 60;   //取整
            second = second % 60;   //取余
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String strtime = "剩余：" + hour + "小时" + minute + "分" + second + "秒";
        return strtime;
    }
}
