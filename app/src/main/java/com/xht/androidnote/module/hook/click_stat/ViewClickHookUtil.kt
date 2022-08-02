package com.xht.androidnote.module.hook.click_stat

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import java.lang.reflect.Field
import java.lang.reflect.Method

object ViewClickHookUtil {

    fun hookAllChildView(viewGroup: ViewGroup) {
        val count = viewGroup.childCount
        for (i in 0 until count) {
            if (viewGroup.getChildAt(i) is ViewGroup) {
                hookAllChildView(viewGroup.getChildAt(i) as ViewGroup)
            } else {
                hook(viewGroup.getChildAt(i))
            }
        }
    }

    @SuppressLint("DiscouragedPrivateApi", "PrivateApi")
    private fun hook(view: View?) {
        try {
            val getListenerInfo: Method = View::class.java.getDeclaredMethod("getListenerInfo")
            getListenerInfo.isAccessible = true

            //获取当前View的listenerInfo对象
            val mListenerInfo: Any = getListenerInfo.invoke(view)
            try {
                val listenerInfoClazz = Class.forName("android.view.View\$ListenerInfo")

                try {
                    //获取mOnClickListener参数
                    val mOnClickListener: Field =
                        listenerInfoClazz.getDeclaredField("mOnClickListener")
                    mOnClickListener.isAccessible = true
                    var oldListener: View.OnClickListener? =
                        mOnClickListener.get(mListenerInfo) as? View.OnClickListener
                    if (oldListener != null && oldListener !is MyOnClickListener) {
                        //替换OnClickListener
                        val proxyOnClick = MyOnClickListener(oldListener)
                        mOnClickListener.set(mListenerInfo, proxyOnClick)
                    }
                } catch (e: NoSuchFieldException) {
                    e.printStackTrace()
                }

            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }
    }

}