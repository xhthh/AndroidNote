package com.xht.androidnote.module.constraint

import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity

class LayoutTestActivity:BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_layout_test
    }

    override fun initEventAndData() {
        /**
         * FrameLayout 对子 View 可以通过 layout_gravity 设置位置 上下左右中
         */
    }
}