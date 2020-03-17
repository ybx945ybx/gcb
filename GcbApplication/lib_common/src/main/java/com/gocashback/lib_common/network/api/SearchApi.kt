package com.gocashback.lib_common.network.api

import com.gocashback.lib_common.network.model.ApiModel
import com.gocashback.lib_common.network.model.deal.DealListIfModel
import com.gocashback.lib_common.network.model.store.StoreListIfModel
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @Author 55HAITAO
 * @Date 2019-05-16 18:05
 */

interface SearchApi {
    /**
     * 搜索商家+搜索页面商家
     *
     * @param     keyword    是	string 关键字，搜索时需传，搜索页面商家不传
     * @param     page       否	int	当前页，从0开始，默认0
     * @param     per_page   否	int	每页显示条数，默认30条
     * @return Observable&lt;StoreListIfModel&gt;
     */
    @FormUrlEncoded
    @POST("/api/search")
    fun getSearchStore(@Field("keyword") keyword: String, @Field("page") page: Int, @Field("per_page") per_page: Int): Observable<ApiModel<StoreListIfModel>>

    /**
     * 搜索商家+搜索页面商家
     *
     * @param     keyword    是	string 关键字
     * @param     page       否	int	当前页，从0开始，默认0
     * @param     per_page   否	int	每页显示条数，默认30条
     * @return Observable&lt;DealListIfModel&gt;
     */
    @FormUrlEncoded
    @POST("/api/search/deal")
    fun getSearchDeal(@Field("keyword") keyword: String, @Field("page") page: Int, @Field("per_page") per_page: Int): Observable<ApiModel<DealListIfModel>>


}