package com.xht.androidnote.module.web

import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseViewActivity
import com.xht.androidnote.databinding.ActivityTestWebviewBinding


/**
 * webview测试
 */
class WebViewTestActivity : BaseViewActivity<ActivityTestWebviewBinding>() {

    override fun getViewBinding(): ActivityTestWebviewBinding {
        return ActivityTestWebviewBinding.inflate(layoutInflater)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_test_webview
    }

    override fun initEventAndData() {
//        initWebSettings(binding.webview)
//        binding.webview.webViewClient = this.webViewClient
        //binding.webview.loadUrl("chrome://crash")

        val settings = binding.webview.settings
        settings.javaScriptEnabled = true
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val uri = request?.url
                if (uri?.scheme?.startsWith("alipay") == true) {
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        binding.webview.loadUrl("file:///android_asset/schemetest.html");
    }

    private fun initWebView(webView: WebView) {
        initWebSettings(webView)
        webView.webViewClient = this.webViewClient
        webView.webChromeClient = object : WebChromeClient() {
        }
        webView.loadUrl("https://www.baidu.com")
    }

    private var webViewClient: WebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.e("WebView", "------onPageFinished------url：$url")
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError
        ) {
            super.onReceivedError(view, request, error)
            Log.e("WebView", "Error loading page: " + error.description)
        }

        override fun onRenderProcessGone(
            view: WebView, detail: RenderProcessGoneDetail
        ): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                return false
            }

            Log.e("WebView", "------onRenderProcessGone------detail：${detail.didCrash()}")
            //todo 处理，WebView进程被杀死， 需要重新创建
            val parent = binding.webview.parent as ViewGroup
            val params = binding.webview.layoutParams
            parent.removeView(binding.webview)

            val webView = WebView(this@WebViewTestActivity).apply {
                id = R.id.webview
            }
            parent.addView(webView, params)
            initWebView(webView)
            return true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun initWebSettings(mWebView: WebView) {
        val webSettings = mWebView.getSettings()
        //设置WebView属性，能够执行Javascript脚本
        webSettings.javaScriptEnabled = true
        //设置可以支持缩放
        webSettings.setSupportZoom(true)
        //设置出现缩放工具
        webSettings.builtInZoomControls = true
        //扩大比例的缩放
        webSettings.displayZoomControls = false
        //设置Web页面字体稳定大小
        webSettings.textZoom = 100
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型：
         * 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小
         * 2、LayoutAlgorithm.SINGLE_COLUMN : 适应屏幕，内容将自动缩放
         */
        webSettings.blockNetworkImage = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        //文件访问漏洞处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.allowFileAccessFromFileURLs = false
            webSettings.allowUniversalAccessFromFileURLs = false
        }

        //设置加载进来的页面自适应手机屏幕
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        //设置WebView缓存模式
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        // 开启 DOM storage API 功能
        webSettings.domStorageEnabled = true
        //开启 database storage API 功能
        webSettings.databaseEnabled = true

        //启用地理定位
        webSettings.setGeolocationEnabled(true)

        //允许设置cookie
        CookieManager.getInstance().setAcceptCookie(true)

        //关闭密码保存提醒
        webSettings.savePassword = false
        mWebView.isScrollContainer = true
        mWebView.isVerticalScrollBarEnabled = true
        mWebView.isHorizontalScrollBarEnabled = true
    }
}