package com.gocashback.lib_common.language

import android.content.Context
import android.os.Build
import android.text.TextUtils
import com.gocashback.lib_common.event.UpdateLocaleEvent
import com.gocashback.lib_common.utils.*
import com.orhanobut.logger.Logger
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * @Author 55HAITAO
 * @Date 2019-07-11 11:54
 */
const val LOCALE_CHINESE = "zh"
const val LOCALE_ENGLISH = "en"

fun getLocale(context: Context?): Locale {
    return if (TextUtils.isEmpty(getString(context, SP_KEY_LOCALE, ""))) {
        getSysLocale(context!!)
    } else {
        JsonUtils.convertToObject(getString(context, SP_KEY_LOCALE, ""), Locale::class.java)
    }
}

fun setLocale(context: Context, locale: Locale) {
    put(context, SP_KEY_LOCALE, JsonUtils.toJson(locale))
}

fun getLanguage(context: Context?) = getString(context, SP_KEY_LANGUAGE, "")

fun setLanguage(context: Context?, language: String) {
    put(context, SP_KEY_LANGUAGE, language)
}

fun updateLocale(context: Context, locale: Locale) {
    setLocale(context, locale)
    setLanguage(context, locale.language)
    // 发消息更新页面
    EventBus.getDefault().post(UpdateLocaleEvent())
    Logger.d("Local------" + getSysLocale(context))
    Logger.d("Local------language:" + locale.language)
}

/**
 * 获取当前手机系统local
 * @param context Context
 * @return Locale
 */
fun getSysLocale(context: Context): Locale =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) context.resources.configuration.locales.get(0) else context.resources.configuration.locale
