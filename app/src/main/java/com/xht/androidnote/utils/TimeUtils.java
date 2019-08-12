package com.xht.androidnote.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by jiezi on 4/5/16.
 */
public class TimeUtils {

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
    }

    public static String getCurrentTimeToMillisecond() {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(new Date());
    }

    /**
     * 从20160524 转成 2016-05-24
     *
     * @param date
     * @return
     */
    public static String formatDate(String date) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        try {
            return newFormat.format(oldFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取两个日期之间的间隔天数
     *
     * @author qjd
     * @time 2016/5/20 11:19
     */
    public static int getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 将日期时间字符串转化为Date对象
     *
     * @param dateStr
     * @return
     */
    public static Date convertStrToDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date date = sdf.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取普通的时间格式
     *
     * @param date
     * @return
     */
    public static String getSimpleTime(Date date) {//可根据需要自行截取数据显示
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 获取前n天日期、后n天日期
     *
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    public static String getBeforeOrLaterDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    /**
     * 获取前n天日期、后n天日期
     *
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    public static String getBeforeOrLaterDate(String currentDate, int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");


        //Date beginDate = new Date();
        Date beginDate = TimeUtils.convertStrToDate(currentDate);
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }


    /**
     * 比较两个日期大小
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean compareDate(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                return true;
            } else if (dt1.getTime() <= dt2.getTime()) {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    //获得明天日期
    public static String getTomoData(){

        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
        String[] months_little = { "4", "6", "9", "11" };

        List<String> list_big = Arrays.asList(months_big);
        List<String> list_little = Arrays.asList(months_little);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DATE);
        if(day==30){
            if (list_big.contains(String.valueOf(month))){
                day=31;
            }
            if (list_little.contains(String.valueOf(month))){
                day=1;
                if(month==12){
                    year++;
                    month=1;
                }else{
                    month++;
                }

            }
        }else if(day==31){
            day=1;
            if(month==12){
                year++;
                month=1;
            }else{
                month++;
            }

        }else {
            day++;
        }
        String data;
        if( day<10)
        {

            data=year+"-"+"0"+month+"-"+"0"+day;
        }
        else
        {
            data=year+"-"+"0"+month+"-"+day;
        }

        return data;
    }


    public static Date getTomorrow() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前时间
        Date date = new Date(System.currentTimeMillis() + 86400000);
        return date;
    }
}
