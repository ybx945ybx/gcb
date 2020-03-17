package com.gocashback.lib_common.network.api

import com.gocashback.lib_common.network.model.ApiModel
import com.gocashback.lib_common.network.model.giftCard.GiftCardDetailIfModel
import com.gocashback.lib_common.network.model.giftCard.GiftCardIfModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @Author 55HAITAO
 * @Date 2019-06-15 14:47
 */
interface GiftCardApi {
    /**
     * 礼卡列表
     * keyword	否	string	关键字
     * page	否	int	当前页，从0开始，默认0
     * per_page	否	int	每页显示条数，默认30条
     *
     * @return Observable&lt;CollectionIfModel&gt;
     */
    @GET("/api/gift_card")
    fun giftCard(@Query("keyword") keyword: String, @Query("page") page: Int, @Query("per_page") per_page: Int): Observable<ApiModel<GiftCardIfModel>>

 /**
     * 礼卡详情
     * keyword	否	string	关键字
     * page	否	int	当前页，从0开始，默认0
     * per_page	否	int	每页显示条数，默认30条
     *
     * @return Observable&lt;GiftCardDetailIfModel&gt;
     */
    @GET("/api/gift_card/detail/{id}")
    fun giftCardDetail(@Path("id") id: String ): Observable<ApiModel<GiftCardDetailIfModel>>


}