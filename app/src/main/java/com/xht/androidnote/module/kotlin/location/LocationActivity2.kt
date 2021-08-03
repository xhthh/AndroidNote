package com.xht.androidnote.module.kotlin.location

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import kotlinx.android.synthetic.main.activity_location.*

class LocationActivity2 : BaseActivity() {

    private val REQUEST_LOCATION_PERMISSION_CODE = 100


    override fun getLayoutId(): Int {
        return R.layout.activity_location
    }

    override fun initEventAndData() {
        btnGetLocation.setOnClickListener {
            showLocation()
        }
    }

    private fun showLocation() {
        PermissionUtils.permission(PermissionConstants.LOCATION)
            .callback(object : PermissionUtils.FullCallback {
                override fun onGranted(permissionsGranted: List<String>) {
                    if (GPSUtils.getInstance(this@LocationActivity2).isLocationProviderEnabled) {
                        showLocationWithToast()
                    } else {
                        requestLocation()
                    }
                }

                override fun onDenied(
                    permissionsDeniedForever: List<String>,
                    permissionsDenied: List<String>
                ) {
                    Toast.makeText(this@LocationActivity2, "未获得地理位置权限", Toast.LENGTH_LONG).show()
                }
            })
            .request()
    }

    private fun showLocationWithToast() {
        val location = GPSUtils.getInstance(this@LocationActivity2).location
        Toast.makeText(
            this@LocationActivity2,
            "地理位置：lon:${location.longitude};lat:${location.latitude}",
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * 检查地理位置开关是否打开，如果未打开，则提示用户打开地理位置开关。
     * 如果已打开，则显示地理位置；如果被拒绝，直接关闭窗口。
     */
    private fun requestLocation() {
        val message = "本应用需要获取地理位置，请打开获取位置的开关"
        val alertDialog = AlertDialog.Builder(this).setMessage(message).setCancelable(false)
            .setPositiveButton(android.R.string.ok)
            { dialog, _ ->
                dialog.dismiss()
                gotoSysLocationSettingsPage()
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .create()
        alertDialog.show()
    }

    private fun gotoSysLocationSettingsPage() {
        val intent = Intent()
        intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
        startActivityForResult(intent, REQUEST_LOCATION_PERMISSION_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION_CODE -> showLocation()
            else -> {
            }
        }
    }

}