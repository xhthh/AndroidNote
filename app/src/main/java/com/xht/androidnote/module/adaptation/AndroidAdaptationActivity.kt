package com.xht.androidnote.module.adaptation

import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import kotlinx.android.synthetic.main.activity_adaptation.btnAndroid10
import kotlinx.android.synthetic.main.activity_adaptation.btnAndroid11
import kotlinx.android.synthetic.main.activity_adaptation.btnAndroid12
import kotlinx.android.synthetic.main.activity_adaptation.btnAndroid13

class AndroidAdaptationActivity : BaseActivity() {
    private val TAG = "adaptation"
    override fun getLayoutId(): Int {
        return R.layout.activity_adaptation
    }

    override fun initEventAndData() {
        btnAndroid10.setOnClickListener {
            skip2Activity(Adaptation10Activity::class.java)
        }
        btnAndroid11.setOnClickListener {
            skip2Activity(Adaptation10Activity::class.java)
        }
        btnAndroid12.setOnClickListener {
            skip2Activity(Adaptation10Activity::class.java)
        }
        btnAndroid13.setOnClickListener {
            skip2Activity(Adaptation10Activity::class.java)
        }
    }
}