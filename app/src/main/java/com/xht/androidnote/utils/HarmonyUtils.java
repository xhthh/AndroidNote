package com.xht.androidnote.utils;

import android.text.TextUtils;

import java.lang.reflect.Method;

public class HarmonyUtils {
    /**
     * 是否为鸿蒙系统
     *
     * @return true为鸿蒙系统
     */
    public static boolean isHarmonyOs() {
        try {
            Class<?> buildExClass = Class.forName("com.huawei.system.BuildEx");
            Object osBrand = buildExClass.getMethod("getOsBrand").invoke(buildExClass);
            return "Harmony".equalsIgnoreCase(osBrand.toString());
        } catch (Throwable x) {
            return false;
        }
    }

    /**
     * 获取鸿蒙系统版本号（大版本号）
     *
     * @return 版本号
     */
    public static String getHarmonyVersion() {
        return getProp("hw_sc.build.platform.version", "");
    }

    /**
     * 通过反射获取属性
     *
     * @param property
     * @param defaultValue
     * @return
     */
    private static String getProp(String property, String defaultValue) {
        try {
            Class spClz = Class.forName("android.os.SystemProperties");
            Method method = spClz.getDeclaredMethod("get", String.class);
            String value = (String) method.invoke(spClz, property);
            if (TextUtils.isEmpty(value)) {
                return defaultValue;
            }
            return value;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    /**
     * 获得鸿蒙系统版本号（含小版本号，实际上同Android的android.os.Build.DISPLAY）
     *
     * @return 版本号
     */
    public static String getHarmonyDisplayVersion() {
        return android.os.Build.DISPLAY;
    }
}

