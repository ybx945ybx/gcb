package com.gocashback.lib_common.base

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.baichuan.android.trade.AlibcTradeSDK
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.facebook.stetho.Stetho
import com.gocashback.lib_common.BuildConfig
import com.gocashback.lib_common.imageload.UnsafeOkHttpClient.getUnsafeOkHttpClient
import com.gocashback.lib_common.share.*
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig


open class GcbBaseApplication : Application() {

    companion object {
        var application: GcbBaseApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        application = this@GcbBaseApplication
        if (isMainProcess(this)) {

            // 初始化ARouter
            initArouter()
            // 初始化Logger
            Logger.addLogAdapter(AndroidLogAdapter())
            // 初始化Stetho
            Stetho.initializeWithDefaults(this)
            // 初始化分享
            initShare()
            // 初始化Fresco
            initFresco()
            // 初始化阿里百川
            initAlibc()
        }

    }

    private fun initAlibc() {
        AlibcTradeSDK.asyncInit(this, object : AlibcTradeInitCallback {
            override fun onSuccess() {
                //初始化成功，设置相关的全局配置参数
                Logger.d("初始化阿里百川电商服务成功")
            }

            override fun onFailure(code: Int, msg: String) {
                //初始化失败，可以根据code和msg判断失败原因，详情参见错误说明
                Logger.d("初始化阿里百川电商服务失败：$msg$code")
            }
        })

    }

    private fun initShare() {
        UMConfigure.init(this, UM_APPID, "umeng", UMConfigure.DEVICE_TYPE_PHONE, "")

        PlatformConfig.setWeixin(WX_APPID, WX_APPSECRET)
        PlatformConfig.setQQZone(QQ_APPID, QQ_APPKEY)
        PlatformConfig.setSinaWeibo(Sina_APPID, Sina_APPSECRET, Sina_Redirect_Url)
        PlatformConfig.setTwitter(Twitter_APPID, Twitter_APPSECRET)
        // facebook 在manifests中配置


//        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0")
//        //豆瓣RENREN平台目前只能在服务器端配置
//        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com")
//        PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf")
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba")
//        PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO")
//        PlatformConfig.setAlipay("2015111700822536")
//        PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e")
//        PlatformConfig.setPinterest("1439206")
//        PlatformConfig.setKakao("e4f60e065048eb031e235c806b31c70f")
//        PlatformConfig.setDing("dingoalmlnohc0wggfedpk")
//        PlatformConfig.setVKontakte("5764965", "5My6SNliAaLxEm3Lyd9J")
//        PlatformConfig.setDropbox("oz8v5apet3arcdy", "h7p2pjbzkkxt02a")
    }

    private fun initArouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(application)
    }

    /**
     * 包名判断是否为主进程
     *
     * @param context
     * @return
     */
    fun isMainProcess(context: Context) = context.packageName == getProcessName(context)

    /**
     * 获取进程名称
     *
     * @param context
     * @return
     *
     */
    private fun getProcessName(context: Context): String {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        am.runningAppProcesses?.forEach {
            it.processName?.apply {
                if (it.pid == android.os.Process.myPid()) {
                    return it.processName
                }
            }
        }
        return ""
    }

    private fun initFresco() {
        Fresco.initialize(this,
                OkHttpImagePipelineConfigFactory
                        .newBuilder(this, getUnsafeOkHttpClient())
                        .setDownsampleEnabled(true) // 向下采样
                        .setBitmapsConfig(Bitmap.Config.RGB_565)
                        .setResizeAndRotateEnabledForNetwork(true) // 对网络图进行自动旋转和压缩
                        .build()
        )
    }
}