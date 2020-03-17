package com.gocashback.lib_common.network;

import android.text.TextUtils;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author 55HAITAO
 * @Date 2020-02-21 11:18
 */
public class RetrofitManager {

//    var IS_DEBUG = false
//
//// 测试环境地址
//const val DEBUG_URL = "https://api-new.gocashback.net"
//// 测试环境地址
//const val DEBUG_DOMAIN = "m.gocashback.net"
//// 正式环境地址
////const val RELEASE_URL = "https://api-new.gocashback.com"
//const val RELEASE_URL = "https://api01.gocashback.com"
//// 正式环境域名
//const val RELEASE_DOMAIN = "m.gocashback.com"
//
//    val BASE_URL = if (IS_DEBUG) DEBUG_URL else RELEASE_URL
//    val DEFAULT_DOMAIN = if (IS_DEBUG) DEBUG_DOMAIN else RELEASE_DOMAIN
//
//    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
//            .connectTimeout(20, TimeUnit.SECONDS)
//        .readTimeout(20, TimeUnit.SECONDS)
//        .writeTimeout(20, TimeUnit.SECONDS)
//        .addNetworkInterceptor(StethoInterceptor())
//            .addInterceptor(RequestInterceptor())
//            .build()
//
//    val retrofit: Retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .client(okHttpClient)
//        .build()

//    private              String   mBasePath             = Constant.SWAGGER_URL; // 接口根地址
//    private static final int      DEFAULT_TIME_OUT      = 20;
//    private static final int      DEFAULT_READ_TIME_OUT = 20;
//    private              Retrofit mRetrofit;
//
//    private RetrofitManager() {
//    }
//
//    public void init(String baseUrl) {
//        // 创建 OKHttpClient
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.retryOnConnectionFailure(true)
//                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)//连接超时时间
//                .writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)//写操作 超时时间
//                .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)//读操作超时时间
//                .protocols(Collections.unmodifiableList(Arrays.asList(Protocol.HTTP_1_1, Protocol.HTTP_2)))
//                .addInterceptor(new RequestInterceptor()) // 添加公共参数
//                .addNetworkInterceptor(new StethoInterceptor()); // stetho调试用
//        if (!TextUtils.isEmpty(baseUrl)) {
//            mBasePath = baseUrl;
//        }
//        // 创建Retrofit
//        mRetrofit = new Retrofit.Builder()
//                .client(builder.build())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(mBasePath)
//                .build();
//    }
//
//    private static class SingletonHolder {
//        private static final RetrofitManager INSTANCE = new RetrofitManager();
//    }
//
//    /**
//     * 获取RetrofitServiceManager
//     *
//     * @return
//     */
//    public static RetrofitManager getInstance() {
//        return SingletonHolder.INSTANCE;
//    }
//
//    /**
//     * 获取对应的Service
//     *
//     * @param service Service 的 class
//     * @param <T>
//     * @return
//     */
//    public <T> T create(Class<T> service) {
//        return mRetrofit.create(service);
//    }
//
//    public String getBasePath() {
//        return mBasePath;
//    }
}
