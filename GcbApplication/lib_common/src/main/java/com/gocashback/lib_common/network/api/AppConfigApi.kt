package com.gocashback.lib_common.network.api

import com.gocashback.lib_common.network.model.ApiModel
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * @Author 55HAITAO
 * @Date 2019-11-28 16:37
 */
interface AppConfigApi {
    /**
     * 地区列表
     *
     * @return Observable&lt;List<DealCateItemModel>&gt;
     */
    @GET("/api/app_config/country")
    fun getCountry(): Observable<ApiModel<Map<String,String>>>


    
}