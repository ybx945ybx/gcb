package com.gocashback.module_search

import android.content.Context
import android.text.TextUtils
import com.gocashback.lib_common.utils.*
import com.gocashback.lib_common.utils.JsonUtils.convertToArrayList
import com.gocashback.lib_common.utils.JsonUtils.toJson
import java.util.*
import kotlin.collections.ArrayList

/**
 * @Author 55HAITAO
 * @Date 2019/4/9 5:13 PM
 */
const val SP_KEY_SEARCH = "SP_KEY_SEARCH"

fun getSearchHistory(context: Context): ArrayList<String> {
    getString(context, SP_KEY_SEARCH, "")?.let {
        if (!TextUtils.isEmpty(it))
            return JsonUtils.convertToArrayList<String>(it, String::class.java)
    }

    return arrayListOf()
}

// 历史记录保存最新的10个

fun addSearchHistory(context: Context, keyword: String) {

}

fun setSearchHistory(context: Context, arrayList: ArrayList<String>) {
    put(context, SP_KEY_SEARCH, toJson(arrayList))
}
//
//fun update(context: Context, locale: Locale) {
//    if (needUpdate(context, locale)) {
//        context.resources.apply {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) configuration.setLocale(locale) else configuration.locale = locale
//            val displayMetrics = displayMetrics
//            updateConfiguration(configuration, displayMetrics)
//            setLocale(context, locale)
//            // 发消息更新页面
//            EventBus.getDefault().post(UpdateLocaleEvent())
//        }
//    }
//
//}