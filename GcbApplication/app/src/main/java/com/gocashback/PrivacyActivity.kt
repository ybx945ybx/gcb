package com.gocashback

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.gocashback.lib_common.event.PrivacyShowEvent
import com.gocashback.lib_common.utils.show
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_privacy.*
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

/**
 * @Author 55HAITAO
 * @Date 2020-02-25 14:58
 */
class PrivacyActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val settings: WebSettings = webView.settings
        settings.builtInZoomControls = true //显示放大缩小 controler

        settings.setSupportZoom(true) //可以缩放

        settings.displayZoomControls = false // 隐藏缩放控件

        settings.defaultZoom = WebSettings.ZoomDensity.CLOSE //默认缩放模式

        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.domStorageEnabled = true
        // useragent后面拼接"(WWHT,Android,V版本号)"
//        settings.setUserAgentString(settings.getUserAgentString() + " " + String.format("(WWHT,Android,V%s)", BuildConfig.VERSION_NAME));
        // useragent后面拼接"(WWHT,Android,V版本号)"
//        settings.setUserAgentString(settings.getUserAgentString() + " " + String.format("(WWHT,Android,V%s)", BuildConfig.VERSION_NAME));
        settings.javaScriptEnabled = true
        settings.loadsImagesAutomatically = true // 支持自动加载图片

        // 允许https http内容混合
        // 允许https http内容混合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 开启调试
            WebView.setWebContentsDebuggingEnabled(true)
        }
        webView.isVerticalScrollBarEnabled = false
        webView.addJavascriptInterface(GcbJavascriptinterface(this), "android")
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                Logger.d("url=" + "shouldOverrideUrlLoading")
                if (url.startsWith("http")) {
                    view.loadUrl(url)
                }
                return true
                //                return super.shouldOverrideUrlLoading(view, url);
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                view_top.visibility = View.VISIBLE
                webView.visibility = View.VISIBLE

                webView.postDelayed(object : Runnable{
                    override fun run() {
                        EventBus.getDefault().post(PrivacyShowEvent())
                    }
                }, 1000)

                super.onPageFinished(view, url)
            }
        }

        webView.loadUrl("https://m.gocashback.com/zh-hans/android-privacy-policy/welcome")

        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate {
                    view_top.visibility = View.VISIBLE
                    webView.visibility = View.VISIBLE
                    webView.postDelayed(object : Runnable{
                        override fun run() {
                            EventBus.getDefault().post(PrivacyShowEvent())
                        }
                    }, 1000)
                }
                .subscribe()

    }

    /**
     * JS交互方法
     *
     * @property mContext Context
     * @constructor
     */
    inner class GcbJavascriptinterface(private val mContext: Context) {

        @JavascriptInterface
        fun startGoCashback() {
            show(this@PrivacyActivity, "来了")
        }


    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}