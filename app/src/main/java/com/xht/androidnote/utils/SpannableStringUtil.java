package com.xht.androidnote.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpannableStringUtil {

    public static SpannableString zhuanHuanTelUrl(final Context context, final String strTel) {
        SpannableString ss = new SpannableString(strTel);
        final List<String> list = getNumbers(strTel);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                final int finalI = i;
                ss.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.BLUE);       //设置文件颜色
                        ds.setUnderlineText(false);      //设置下划线
                    }

                    @Override
                    public void onClick(View widget) {
                        String tel = list.get(finalI);
                        //context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel)));
                        copyContentToClipboard(tel, context);
                        Toast.makeText(context, tel + " 已复制到剪切板", Toast.LENGTH_SHORT).show();
                    }
                }, strTel.indexOf(list.get(i)), strTel.indexOf(list.get(i)) + 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }
        return ss;
    }

    /**
     * 将字符串中的电话号码设置点击事件和下划线
     *
     * @param context
     * @param tv
     * @param strTel
     */
    public static void setTelUrl(Context context, TextView tv, String strTel) {
        zhuanHuanTelUrl(context, strTel);
        tv.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
        tv.setText(zhuanHuanTelUrl(context, strTel));
        tv.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
    }


    /**
     * 从字符串中查找电话号码字符串
     */
    public static List<String> getNumbers(String content) {
        List<String> digitList = new ArrayList<String>();
        Pattern p = Pattern.compile("(\\d{11})");
        Matcher m = p.matcher(content);
        while (m.find()) {
            String find = m.group(1).toString();
            digitList.add(find);
        }
        return digitList;
    }

    /**
     * 复制内容到剪贴板
     *
     * @param content
     * @param context
     */
    public static void copyContentToClipboard(String content, Context context) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", content);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }
}    