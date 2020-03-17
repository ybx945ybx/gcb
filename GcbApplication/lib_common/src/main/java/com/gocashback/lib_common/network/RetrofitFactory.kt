package com.gocashback.lib_common.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

var IS_DEBUG = false

// 测试环境地址
const val DEBUG_URL = "https://api-new.gocashback.net"
// 测试环境地址
const val DEBUG_DOMAIN = "m.gocashback.net"
// 正式环境地址
//const val RELEASE_URL = "https://api-new.gocashback.com"
const val RELEASE_URL = "https://api01.gocashback.com"
// 正式环境域名
const val RELEASE_DOMAIN = "m.gocashback.com"

val BASE_URL = if (IS_DEBUG) DEBUG_URL else RELEASE_URL
val DEFAULT_DOMAIN = if (IS_DEBUG) DEBUG_DOMAIN else RELEASE_DOMAIN

val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .addNetworkInterceptor(StethoInterceptor())
        .addInterceptor(RequestInterceptor())
        .build()

val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()

fun <T> createService(clazz: Class<T>): T = retrofit.create(clazz)

