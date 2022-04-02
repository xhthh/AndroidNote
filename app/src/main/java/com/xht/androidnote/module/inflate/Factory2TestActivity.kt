package com.xht.androidnote.module.inflate

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import com.xht.androidnote.R

class Factory2TestActivity : AppCompatActivity() {
    private val TAG = "Factory2"
    override fun onCreate(savedInstanceState: Bundle?) {

        LayoutInflaterCompat.setFactory2(
            LayoutInflater.from(this),
            object : LayoutInflater.Factory2 {
                override fun onCreateView(
                    name: String,
                    context: Context,
                    attrs: AttributeSet
                ): View? {
                    return null
                }

                override fun onCreateView(
                    parent: View?,
                    name: String,
                    context: Context,
                    attrs: AttributeSet
                ): View? {
                    var name = name
                    if (name == "TextView" || name == "ImageView") {
                        name = "Button"
                    }
                    return delegate.createView(parent, name, context, attrs)
                }
            })
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_factory2_test)
    }

    private var skinPackageName: String? = null

    private var skinPkgPath: String = ""

    /**
     * 获取 Resource 对象
     */
    private fun getResourceIns() {
        val packageManager = packageManager
        val packageArchiveInfo =
            packageManager.getPackageArchiveInfo("", PackageManager.GET_ACTIVITIES)
        skinPackageName = packageArchiveInfo?.packageName

        val assetManager = AssetManager::class.java.newInstance()
        val addAssetPath = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
        addAssetPath.invoke(assetManager, skinPkgPath)

        val superRes = resources
        val skinResource = Resources(assetManager, superRes.displayMetrics, superRes.configuration)

        val identifier = skinResource.getIdentifier("resName", "color", skinPackageName)
    }
}