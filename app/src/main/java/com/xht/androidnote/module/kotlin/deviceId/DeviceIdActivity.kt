package com.xht.androidnote.module.kotlin.deviceId

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import kotlinx.android.synthetic.main.activity_device_id.*
import java.net.NetworkInterface
import java.util.*

class DeviceIdActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_device_id
    }

    override fun initEventAndData() {
        btnGetDeviceId.setOnClickListener {
            val wifiMac = getWifiMac()
            tvDeviceId.text = wifiMac + "  " + getAndroidId(this) + "  " + generateId()
        }

        //921018fb881
    }

    fun generateId():String {
        val strRand = java.lang.StringBuilder("android")
        for (i in 0..7) {
            strRand.append((Math.random() * 10).toInt())
        }
        return strRand.toString()
    }

    fun getWifiMac(): String? {
        try {
            val enumeration: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
                ?: return ""
            while (enumeration.hasMoreElements()) {
                val netInterface: NetworkInterface = enumeration.nextElement()
                if (netInterface.getName().equals("wlan0")) {
                    return formatMac(netInterface.getHardwareAddress())
                }
            }
        } catch (e: Exception) {
            Log.e("tag", e.message, e)
        }
        return ""
    }

    private fun formatMac(hardwareAddress: ByteArray?): String? {
        var builder = StringBuilder()
        if (hardwareAddress != null) {
            for (byte in hardwareAddress) {
                //builder.append(':').append(Integer.toHexString(0xFF and byte.toInt()))
                builder.append(Integer.toHexString(0xFF and byte.toInt()))
            }
        }
        return builder.toString()
    }

    fun getAndroidId(context: Context): String? {
        return Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID)
    }
}