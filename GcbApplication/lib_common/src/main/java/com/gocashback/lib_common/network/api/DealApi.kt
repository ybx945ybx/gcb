package com.gocashback.lib_common.network.api

import com.gocashback.lib_common.network.model.ApiModel
import com.gocashback.lib_common.network.model.deal.DealCateItemModel
import com.gocashback.lib_common.network.model.deal.DealDetailIfModel
import com.gocashback.lib_common.network.model.deal.DealListIfModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Author 55HAITAO
 * @Date 2019-05-16 11:31
 */
interface DealApi {

    /**
     * 折扣分类
     *
     * @return Observable&lt;List<DealCateItemModel>&gt;
     */
    @GET("/api/deal/cate")
    fun getDealCateList(): Observable<ApiModel<List<DealCateItemModel>>>

    /**
     * 首页热门折扣
     *
     * @param     page        否	int	当前页，从0开始，默认0
     * @param     per_page    否	int	每页显示条数 默认是30条
     * @return Observable&lt;DealListIfModel&gt;
     */
    @GET("/api/deal")
    fun getDealList(@Query("page") page: Int, @Query("per_page") per_page: Int): Observable<ApiModel<DealListIfModel>>

    /**
     * 人气优惠
     *
     * @param     page        否	int	当前页，从0开始，默认0
     * @param     per_page    否	int	每页显示条数 默认是30条
     * @param     day         否	int	天数（几天内），默认3天
     * @return Observable&lt;DealListIfModel&gt;
     */
    @GET("/api/deal/popularity")
    fun getDealPopularityList(@Query("page") page: Int, @Query("per_page") per_page: Int, @Query("day") day: Int = 3): Observable<ApiModel<DealListIfModel>>

    /**
     * 分类折扣
     *
     * @param     cate_id    是	int	分类id
     * @param     page       否	int	当前页，从0开始，默认0
     * @param     per_page   否	int	每页显示条数，默认30条
     * @return Observable&lt;DealListIfModel&gt;
     */
    @GET("/api/deal/deal")
    fun getCateDealList(@Query("cate_id") cate_id: String, @Query("page") page: Int, @Query("per_page") per_page: Int): Observable<ApiModel<DealListIfModel>>

    /**
     * 更多折扣
     *
     * store_id	是	int	商家id
     * page	否	int	当前页，从0开始，默认0
     * per_page	否	int	每页显示条数，默认30条
     * deal_id	否	int	折扣id
     * is_plugin	否	int	是否插件
     * @return Observable&lt;DealListIfModel&gt;
     */
    @GET("/api/deal/more")
    fun getDealMore(@Query("store_id") store_id: String, @Query("page") page: Int, @Query("per_page") per_page: Int, @Query("deal_id") deal_id: String = ""): Observable<ApiModel<DealListIfModel>>

    /**
     * 商家分类热门折扣
     *
     * page	否	int	当前页，从0开始，默认0
     * per_page	否	int	显示条数，默认30条
     * store_id	是	int	商家id
     * @return Observable&lt;DealListIfModel&gt;
     */
    @GET("/api/deal/hot")
    fun getHotDeal(@Query("store_id") store_id: String, @Query("page") page: Int, @Query("per_page") per_page: Int): Observable<ApiModel<DealListIfModel>>

    /**
     *  折扣详情
     *
     * page	否	int	当前页，从0开始，默认0
     * per_page	否	int	显示条数，默认30条
     *  id	是	int	商家id
     * @return Observable&lt;DealListIfModel&gt;
     */
    @GET("/api/deal/show")
    fun getDealDetail(@Query("id") id: String): Observable<ApiModel<DealDetailIfModel>>


}