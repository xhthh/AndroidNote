package com.xht.androidnote.module.hook.click_stat

import android.util.Log
import android.view.View

class MyOnClickListener(var onClickListener: View.OnClickListener?) : View.OnClickListener {
    override fun onClick(v: View?) {
        Log.e("xht", "点击了一个按钮——$v")
        onClickListener!!.onClick(v)
    }
}