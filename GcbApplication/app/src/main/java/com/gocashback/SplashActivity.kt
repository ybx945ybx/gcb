package com.gocashback

import android.content.Context
import android.os.Build
import android.os.Process
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.language.LOCALE_CHINESE
import com.gocashback.lib_common.language.getLanguage
import com.gocashback.lib_common.model.AgreeObject
import com.gocashback.lib_common.startGuideActivity
import com.gocashback.lib_common.startMainActivity
import com.gocashback.lib_common.utils.JsonUtils
import com.gocashback.lib_common.utils.SP_KEY_FIRST_OPEN
import com.gocashback.lib_common.utils.getBoolean
import com.gocashback.lib_common.utils.show
import com.gocashback.lib_common.widget.GcbPrivacyPolicyDlg
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_privacy.*
import java.util.concurrent.TimeUnit

/**
 * @Author 55HAITAO
 * @Date 2019-08-01 16:24
 */
class SplashActivity : GcbBaseActivity() {

    private lateinit var gcbPrivacyPolicyDlg: GcbPrivacyPolicyDlg

    override fun setLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initViews() {
        Logger.d("开启App----------SplashActivity")
        if (getBoolean(this@SplashActivity, SP_KEY_FIRST_OPEN, true) == true) {
//            startActivity(Intent(this, PrivacyActivity::class.java))
//            gcbPrivacyPolicyDlg = GcbPrivacyPolicyDlg(this,
//                    object : GcbPrivacyPolicyDlg.OnConfirmListener {
//                        override fun onConfirm(dlg: GcbPrivacyPolicyDlg) {
//                            startApp()
//                        }
//                    },
//                    object : GcbPrivacyPolicyDlg.OnCancelListener {
//                        override fun onCancel(dlg: GcbPrivacyPolicyDlg) {
//                            finish()
//                            Process.killProcess(Process.myPid())
//                        }
//                    })
//            gcbPrivacyPolicyDlg.show()


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

                    super.onPageFinished(view, url)
                }
            }

            webView.loadUrl(if (getLanguage(this) == LOCALE_CHINESE) "https://m.gocashback.com/zh-hans/android-privacy-policy/welcome"
            else "https://m.gocashback.com/android-privacy-policy/welcome")

            Observable.timer(2000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterTerminate {
                        view_top.visibility = View.VISIBLE
                        webView.visibility = View.VISIBLE
                    }
                    .subscribe()
        } else {
            startApp()
        }

    }

    private fun startApp() {
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate {
                    if (getBoolean(this@SplashActivity, SP_KEY_FIRST_OPEN, true) == true) {
                        startGuideActivity(this)
                        finish()
                    } else {
                        startMainActivity(this@SplashActivity)
                        finish()

                    }
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
        fun startGoCashback(agree: String) {
//            show(this@SplashActivity, agree)
            val agreeObject: AgreeObject = JsonUtils.convertToObject(agree, AgreeObject::class.java)
            if (agreeObject.agree) {
                startGuideActivity(this@SplashActivity)
                finish()
            } else {
                finish()
                Process.killProcess(Process.myPid())
            }

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