package com.xht.androidnote.module.kotlin.widget

import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.NonNull
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import com.xht.androidnote.utils.SpannableStringUtil
import kotlinx.android.synthetic.main.activity_text_test.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class TextTestActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return com.xht.androidnote.R.layout.activity_text_test
    }

    override fun initEventAndData() {


        val list = "13436628362；15112311231；15210383702；15311647653；19512311241；19520418576"

        val spannableString = SpannableStringUtil.zhuanHuanTelUrl(this, list)

//        tvText.text = spannableString
//        tvText.isClickable = true
//        tvText.isEnabled = true
//        tvText.movementMethod = LinkMovementMethod.getInstance()

        SpannableStringUtil.setTelUrl(this, tvText, list)
    }


//    private fun setNote(note: String, textView: TextView) {
//        if (TextUtils.isEmpty(note)) {
//            return
//        }
//        val spannableString = SpannableString(note)
//        var strNote = note
//        var phoneNum: String = StringUtils.getPhoneNum(strNote)
//        while (!TextUtils.isEmpty(phoneNum)) {
//            setClick(spannableString, phoneNum, textView, note)
//            strNote = strNote.substring(strNote.indexOf(phoneNum) + 12)
//            phoneNum = StringUtils.getPhoneNum(strNote)
//        }
//        textView.text = spannableString
//        textView.movementMethod = LinkMovementMethod.getInstance()
//    }

    private fun setClick(
        spannableString: SpannableString, phoneNum: String, textView: TextView,
        note: String
    ) {
        spannableString.setSpan(object : ClickableSpan() {
            override fun updateDrawState(@NonNull ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = resources.getColor(com.xht.androidnote.R.color.colorPrimary) //设置电话号码字体颜色
                ds.isUnderlineText = false //设置电话号码下划线
            }

            override fun onClick(@NonNull widget: View) {
                //电话号码点击事件
            }
        }, note.indexOf(phoneNum), note.indexOf(phoneNum) + 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(
            ForegroundColorSpan(resources.getColor(com.xht.androidnote.R.color.color_333333)),
            note.indexOf(phoneNum), note.indexOf(phoneNum) + 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }


    private val REGEX_MOBILE = "^[1][3|4|5|6|7|8|9][0-9]{9}"
    fun isPhoneNum(str: String?): Boolean {
        val p: Pattern = Pattern.compile(REGEX_MOBILE)
        val m: Matcher = p.matcher(str)
        return m.matches()
    }
}