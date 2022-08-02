package com.xht.androidnote.module.hook.click_stat

import android.util.Log
import android.view.View
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

@Aspect
class ClickAspect {
    @Pointcut("execution(* android.view.View.OnClickListener.onClick(..))")
    fun pointcut() {

    }

    @Around("pointcut()")
    @Throws(Throwable::class)
    fun onClickMethodAround(joinPoint: ProceedingJoinPoint) {
        val args: Array<Any> = joinPoint.args
        var view: View? = null
        for (arg in args) {
            if (arg is View) {
                view = arg
            }
        }
        joinPoint.proceed()

        Log.e("xht", "点击了一个按钮---AspectJ---: $view")
    }
}