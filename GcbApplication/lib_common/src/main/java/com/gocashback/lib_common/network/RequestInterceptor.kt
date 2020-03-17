package com.gocashback.lib_common.network

import com.gocashback.lib_common.base.GcbBaseApplication
import com.gocashback.lib_common.language.LOCALE_CHINESE
import com.gocashback.lib_common.language.getLanguage
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @Author 55HAITAO
 * @Date 2019-05-08 10:43
 */
class RequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request().newBuilder()
                .addHeader("platform", "android")
                .addHeader("lang_id", if (LOCALE_CHINESE == getLanguage(GcbBaseApplication.application)) "2" else "1")      // 1英文，2中文，3繁体，4德语，5俄语
                .addHeader("key", getUserKey())
                .addHeader("secret", getUserSecret())
                .build()

        return chain.proceed(request)
    }
}