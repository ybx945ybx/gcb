package com.gocashback.lib_common.network.api

import com.gocashback.lib_common.network.model.ApiModel
import com.gocashback.lib_common.network.model.store.StoreCateItemModel
import com.gocashback.lib_common.network.model.store.StoreCateStoreIfModel
import com.gocashback.lib_common.network.model.store.StoreDetailIfModel
import com.gocashback.lib_common.network.model.store.StoreListIfModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Author 55HAITAO
 * @Date 2019-05-09 10:25
 */
interface StoreApi {

    /**
     * 商家列表
     *
     * @param     type        是	int	获取列表方式 1首页热门列表 2推荐商家
     * @param     page        否	int	当前页，从0开始，默认0
     * @param     per_page    否	int	每页显示条数 默认是30条 首页热门不传
     * @return Observable&lt;StoreListIfModel&gt;
     */
    @GET("/api/store")
    fun getStoreList(@Query("type") type: Int, @Query("page") page: Int, @Query("per_page") per_page: Int): Observable<ApiModel<StoreListIfModel>>

    /**
     * 商家分类
     *
     * @return Observable&lt;List<StoreCateItemModel>&gt;
     */
    @GET("/api/store/cate")
    fun getStoreCateList(): Observable<ApiModel<List<StoreCateItemModel>>>


    /**
     * 分类商家
     *
     * @param     cate_id    是	int	分类id
     * @param     page       否	int	当前页，从0开始，默认0
     * @param     per_page   否	int	每页显示条数，默认30条
     * @param     order_by   否	string	排序方式，默认”orders desc”, 返利”rebate desc”，热门”is_hot desc”
     * @return Observable&lt;StoreCateStoreIfModel&gt;
     */
    @GET("/api/store/store")
    fun getCateStoreList(@Query("cate_id") cate_id: String, @Query("page") page: Int, @Query("per_page") per_page: Int, @Query("order_by") order_by: String): Observable<ApiModel<StoreCateStoreIfModel>>

    /**
     * 商家详情
     *
     * @param     flag        是	string	商家标识
     * @return Observable&lt;StoreDetailIfModel&gt;
     */
    @GET("/api/store/show")
    fun getStoreDetail(@Query("flag") flag: String ): Observable<ApiModel<StoreDetailIfModel>>


}