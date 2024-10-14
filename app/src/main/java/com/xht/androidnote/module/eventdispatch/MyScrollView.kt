package com.xht.androidnote.module.eventdispatch

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyScrollView(context: Context, attrs: AttributeSet) : ScrollView(context, attrs) {
    private var lastY = 0
    private var isScrollToBottom = false

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setOnScrollChangeListener { v, _, scrollY, _, _ ->
                val viewGroup = v as ViewGroup
                isScrollToBottom = v.getHeight() + scrollY >= viewGroup.getChildAt(0).height
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercept = false
        var actionMarked = ev.let { ev.actionMasked }
        when (actionMarked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL ->
                Unit

            MotionEvent.ACTION_MOVE -> {
                val layout = getChildAt(0) as LinearLayout
                val recyclerView = layout.getChildAt(1) as RecyclerView
                //如果没有滑动到底部，由ScrollView处理，进行拦截
                intercept = if (!isScrollToBottom) {
                    true
                    //如果滑动到底部且listView还没滑动到顶部，不拦截
                } else if (!ifTop(recyclerView)) {
                    false
                } else {
                    //否则判断是否是向下滑
                    ev.y > lastY
                }
            }
        }
        lastY = ev.y.toInt();
        super.onInterceptTouchEvent(ev)
        return intercept
    }

    // 判断listView是否到达顶部
    private fun ifTop(recyclerView: RecyclerView): Boolean {
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
        if (layoutManager != null && layoutManager.findFirstVisibleItemPosition() == 0) {
            val firstChild = recyclerView.getChildAt(0)
            return firstChild != null && firstChild.top >= 0
        }
        return false
    }
}