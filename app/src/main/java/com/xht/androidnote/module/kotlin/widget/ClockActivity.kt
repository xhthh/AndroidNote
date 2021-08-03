package com.xht.androidnote.module.kotlin.widget

import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import kotlinx.android.synthetic.main.activity_clock.*

class ClockActivity:BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_clock

    }

    override fun initEventAndData() {

        clock.format24Hour = "HH:mm:ss"

    }
}