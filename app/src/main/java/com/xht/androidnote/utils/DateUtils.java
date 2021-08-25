package com.xht.androidnote.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by yfeng on 2019/4/1.
 */

public class DateUtils {
    public static String getWeek(String pTime) {
        String Week = "周";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";
        }
        return Week;
    }

    /**
     * 返回年月日
     *
     * @param pTime
     * @return
     */
    public static int[] getYearAndMontAndDay(String pTime) {
        int[] time = new int[3];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(pTime)) {
            try {
                c.setTime(format.parse(pTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //年
        time[0] = c.get(Calendar.YEAR);
        //月
        time[1] = c.get(Calendar.MONTH);
        //日
        time[2] = c.get(Calendar.DAY_OF_MONTH);
        return time;
    }

    public static String getEnglishMonth(int month) {
        month += 1;
        String monthString = "";
        switch (month) {
            case 1:
                //                monthString = "Jan.";
                monthString = "一月";
                break;
            case 2:
                //                monthString = "Feb.";
                monthString = "二月";
                break;
            case 3:
                //                monthString = "Mar.";
                monthString = "三月";
                break;
            case 4:
                //                monthString = "Apr.";
                monthString = "四月";
                break;
            case 5:
                //                monthString = "May.";
                monthString = "五月";
                break;
            case 6:
                //                monthString = "June.";
                monthString = "六月";
                break;
            case 7:
                //                monthString = "July.";
                monthString = "七月";
                break;
            case 8:
                //                monthString = "Aug.";
                monthString = "八月";
                break;
            case 9:
                //                monthString = "Sept.";
                monthString = "九月";
                break;
            case 10:
                //                monthString = "Oct.";
                monthString = "十月";
                break;
            case 11:
                //                monthString = "Nov.";
                monthString = "十一月";
                break;
            case 12:
                //                monthString = "Dec.";
                monthString = "十二月";
                break;
            default:
                break;

        }
        return monthString;
    }

    public static int getCurrentYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DATE);
    }

    /**
     * 把long 转换成 日期 再转换成String类型 年月日 时分秒
     */
    public static String transferLongToDate(Long millSec) {
        String timeFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    public static String transferLongToDate2(Long millSec) {
        String timeFormat = "HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    public static String transferLongToDate3(Long millSec) {
        String timeFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    /**
     * 根据时间戳计算星期几
     *
     * @param millSec 时间戳
     * @return
     */
    public static String getWeekOfDate(Long millSec) {
        Date date = new Date(millSec);
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * @param secondParams 秒数
     * @return 时timer[2]，分timer[1],秒timer[0]
     */
    public static int[] getTimerArray3BySeconds(long secondParams) {
        secondParams = secondParams / 1000;
        int[] timer = new int[3];
        timer[0] = (int) (secondParams % 60);
        secondParams = secondParams / 60;
        timer[1] = (int) (secondParams % 60);
        secondParams = secondParams / 60;
        timer[2] = (int) secondParams;
        //        for (int i = 0; i < 3; i++) {
        //            if (i == 2) {
        //                timer[i] = (int) secondParams;
        //            } else {
        //                timer[i] = (int) secondParams % 60;
        //                secondParams = secondParams / 60;
        //            }
        //        }
        return timer;
    }

    /**
     * @param secondParams 秒数
     * @return 时timer[2]，分timer[1],秒timer[0]
     */
    public static int[] getTimerArrayBySeconds(long secondParams) {
        long time = secondParams / 1000;
        int[] timer = new int[3];
        for (int i = 0; i < 3; i++) {
            if (i == 2) {
                timer[i] = (int) time;
            } else {
                timer[i] = (int) time % 60;
                time = time / 60;
            }
        }
        return timer;
    }


    /**
     * @param secondParams 秒数
     * @return 天timer[3] 时timer[2]，分timer[1],秒timer[0]
     */
    public static int[] getDayTimeArray(long secondParams) {
        long time = secondParams / 1000;
        int[] timer = new int[4];
        for (int i = 0; i < 4; i++) {
            if (i == 3) {
                timer[i] = (int) time;
            } else if (i == 2) {
                timer[i] = (int) time % 24;
                time = time / 24;
            } else {
                timer[i] = (int) time % 60;
                time = time / 60;
            }
        }

        return timer;
    }

    public static String getTimerFormat(int t) {
        if (t < 10) {
            return "0" + t;
        } else {
            return String.valueOf(t);
        }
    }

    public static String getFormateTime(long secondParams) {
        StringBuilder builder = new StringBuilder();
        int[] timeArray = getTimerArrayBySeconds(secondParams);
        String h = getTimerFormat(timeArray[2]);
        String m = getTimerFormat(timeArray[1]);
        String s = getTimerFormat(timeArray[0]);
        return builder.append(h).append(":").append(m).append(":").append(s).toString();
    }


    public static String getDayFormateTime(long secondParams) {
        StringBuilder builder = new StringBuilder();
        int[] timeArray = getDayTimeArray(secondParams);
        String d = String.valueOf(timeArray[3]);
        String h = getTimerFormat(timeArray[2]);
        String m = getTimerFormat(timeArray[1]);
        String s = getTimerFormat(timeArray[0]);
        if ("00".equals(d) || "0".equals(d)) {
            return builder.append(h).append(":").append(m).append(":").append(s).toString();
        } else {
            return builder.append(d).append("天").append(h).append(":").append(m).append(":").append(s).toString();
        }
    }

    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    public static boolean isSameDayOfMillis(final long ms1, final long ms2) {
        final long interval = ms1 - ms2;
        return interval < MILLIS_IN_DAY
                && interval > -1L * MILLIS_IN_DAY
                && toDay(ms1) == toDay(ms2);
    }

    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTodayZeroTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = new Date(getTodayZero());
        return format.format(d1);
    }

    public static long getTodayZero() {
        Date date = new Date();
        long l = 24 * 60 * 60 * 1000; //每天的毫秒数
        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
        //减8个小时的毫秒值是为了解决时区的问题。
        return (date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000);
    }

    /**
     * 获取当天23：59时间
     *
     * @return
     */
    public static String get2359Time() {
        long l = getTodayZero() + 86400000 - 1000;
        return transferLongToDate(l);
    }

    public static boolean IsToday(Long mill) throws ParseException {

        String day = transferLongToDate(mill);

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();
}
