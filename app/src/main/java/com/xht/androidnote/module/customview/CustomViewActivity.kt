package com.xht.androidnote.module.customview

import android.view.View
import android.view.ViewGroup
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import kotlinx.android.synthetic.main.activity_custom_view.*

/**
 * Created by xht on 2020/9/2
 */
class CustomViewActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_custom_view
    }

    override fun initEventAndData() {
        findViewById<View>(R.id.btn_viewpager).setOnClickListener {
            skip2Activity(
                CustomViewPagerActivity::class.java
            )
        }
        findViewById<View>(R.id.btn_nested_scrollview).setOnClickListener {
            skip2Activity(
                NestedActivity::class.java
            )
        }
        findViewById<View>(R.id.btn_coordinator).setOnClickListener {
            skip2Activity(
                CoordinatorActivity::class.java
            )
        }

        btn_flowlayout.setOnClickListener {
            skip2Activity(FlowLayoutActivity::class.java)
        }
    }

    fun traverseViewGroup(view: View?): Int {
        var viewCount = 0
        if (view == null) {
            return 0
        }
        if (view is ViewGroup) {
            val viewGroup = view
            val childCount = viewGroup.childCount
            for (i in 0 until childCount) {
                val child = viewGroup.getChildAt(0)
                viewCount += traverseViewGroup(child)
            }
        } else {
            viewCount++
        }
        return viewCount
    }
}