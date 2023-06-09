package com.xht.androidnote.module.kotlin.widget

import com.xht.androidnote.R
import com.xht.androidnote.base.BaseViewActivity
import com.xht.androidnote.databinding.ActivityClockBinding

class ClockActivity : BaseViewActivity<ActivityClockBinding>() {
    override fun getViewBinding(): ActivityClockBinding {
        return ActivityClockBinding.inflate(layoutInflater)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_clock

    }

    override fun initEventAndData() {

        binding.clock.format24Hour = "HH:mm:ss"

    }
}