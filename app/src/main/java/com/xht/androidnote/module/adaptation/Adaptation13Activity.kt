package com.xht.androidnote.module.adaptation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.ext.SdkExtensions.getExtensionVersion
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import pub.devrel.easypermissions.EasyPermissions

class Adaptation13Activity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_adaptation13
    }

    override fun initEventAndData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            adaptNotification()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun adaptNotification() {
        val hasPermissions =
            EasyPermissions.hasPermissions(this, Manifest.permission.POST_NOTIFICATIONS)
        if (!hasPermissions && !NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            EasyPermissions.requestPermissions(
                this,
                "",
                123,
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    //compileSdkVersion 需要至少为 33 才可以调用此方法
    fun isPhotoPickerAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getExtensionVersion(Build.VERSION_CODES.R) >= 2
        } else {
            false
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You must allow all the permissions.", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }
        }
    }
}