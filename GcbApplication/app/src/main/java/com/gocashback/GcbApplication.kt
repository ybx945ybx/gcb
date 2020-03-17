package com.gocashback

import android.content.Context
import com.gocashback.lib_common.base.GcbBaseApplication
import com.gocashback.lib_common.language.*
import java.util.*

class GcbApplication : GcbBaseApplication() {

    override fun onCreate() {
        super.onCreate()
        if (isMainProcess(this)) {
            // 初始化语言
            initLanguage(this)
        }
    }

    private fun initLanguage(context: Context) {
        var language = getLanguage(context)
        if (language.isNullOrBlank()) {  // 新安装未设置过语言
            language = if (LOCALE_CHINESE == getSysLocale(context).language) {  //  非中文的全部设成英文
                LOCALE_CHINESE
            } else {
                LOCALE_ENGLISH
            }
        }
        updateLocale(context, Locale(language))

    }
}