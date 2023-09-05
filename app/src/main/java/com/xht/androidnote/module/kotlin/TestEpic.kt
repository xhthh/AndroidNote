package com.xht.androidnote.module.kotlin

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.util.Log
import com.google.gson.Gson
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * 运行报错，可能得Android10以下
 */
object TestEpic {

    private val TAG = "TestEpic"

    @JvmStatic
    fun readConfig(context: Context) {
        val inputStream = context.resources.assets.open("privacy_methods.json")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val result = StringBuilder()
        var line: String? = ""
        while (reader.readLine().also { line = it } != null) {
            result.append(line)
        }

        val configEntity = Gson().fromJson(result.toString(), PrivacyMethod::class.java)
        configEntity.methods.forEach {
            hookPrivacyMethod(it)
        }
    }

    @JvmStatic
    fun getProcessName(context: Context): String? {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = am.runningAppProcesses ?: return null
        for (proInfo in runningApps) {
            if (proInfo.pid == Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName
                }
            }
        }
        return null
    }

    @JvmStatic
    fun hookPrivacyMethod(entity: PrivacyMethodData) {
        if (entity.name_regex.isNotEmpty()) {
            val methodName = entity.name_regex.substring(entity.name_regex.lastIndexOf(".") + 1)
            val className = entity.name_regex.substring(0, entity.name_regex.lastIndexOf("."))
            try {
                val lintClass = Class.forName(className)

                XposedBridge.hookAllMethods(lintClass, methodName, object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        super.beforeHookedMethod(param)
                        Log.i(TAG, "beforeHookedMethod $className.$methodName")
                        Log.d(TAG, "stack= " + Log.getStackTraceString(Throwable()))
                    }
                })
            } catch (e: Exception) {
                Log.w(TAG, "hookPrivacyMethod:$className.$methodName,e=${e.message}")
            }
        }
    }
}