package com.xht.androidnote

import android.content.Intent
import android.os.Handler
import com.xht.androidnote.base.BaseActivity

class SplashActivity : BaseActivity() {

    private val handler = Handler()
    private var mCountNum = 60


    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initEventAndData() {

        handler.postDelayed({
            startActivity(Intent(mContext, MainActivity::class.java))
            finish()
        }, 500)
    }


}