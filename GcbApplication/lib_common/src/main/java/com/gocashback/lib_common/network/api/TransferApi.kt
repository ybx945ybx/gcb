package com.gocashback.lib_common.network.api

import com.gocashback.lib_common.network.model.ApiModel
import com.gocashback.lib_common.network.model.transfer.JumpTransferIfModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Author 55HAITAO
 * @Date 2019-06-18 16:00
 */
interface TransferApi {
    /**
     * 浏览记录
     *
     * type	是	string	商家“store”，折扣“deal” ，“code”， aliexpress “alie”
     * id	是	int	商家id、折扣id、code
     * gotag	否	string	gotag
     * store_id	否	int	type为alie时传 值为802
     * @return Observable&lt;BrowsingHistoryIfModel&gt;
     */
    @GET("/api/transfer")
    fun jumpTransfer(@Query("type") type: String, @Query("id") id: String, @Query("gotag") gotag: String, @Query("store_id") store_id: String): Observable<ApiModel<JumpTransferIfModel>>

}