package com.xht.androidnote.module.kotlin.deviceId

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseViewActivity
import com.xht.androidnote.databinding.ActivityDeviceIdBinding
import com.xht.androidnote.utils.DeviceIdUtils
import java.net.NetworkInterface
import java.util.Enumeration

class DeviceIdActivity : BaseViewActivity<ActivityDeviceIdBinding>() {
    override fun getViewBinding(): ActivityDeviceIdBinding {
        return ActivityDeviceIdBinding.inflate(layoutInflater)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_device_id
    }

    override fun initEventAndData() {
        binding.btnGetDeviceId.setOnClickListener {
//            val wifiMac = getWifiMac()
//            tvDeviceId.text = wifiMac + "  " + getAndroidId(this) + "  " + generateId()
            binding.tvDeviceId.text = DeviceIdUtils.getDeviceId(this)
        }

        //921018fb881
    }

    fun generateId(): String {
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