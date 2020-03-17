package com.gocashback.lib_common.network.api

import com.gocashback.lib_common.network.model.ApiModel
import com.gocashback.lib_common.network.model.help.HelpItemModel
import com.gocashback.lib_common.network.model.help.HelpShowIfModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @Author 55HAITAO
 * @Date 2019-06-12 09:31
 */
interface HelpApi {

    /**
     * 帮助中心分类
     *
     * @return Observable&lt;List<HelpItemModel>&gt;
     */
    @GET("/api/help")
    fun help(): Observable<ApiModel<List<HelpItemModel>>>

    /**
     * 帮助详情
     *
     * @return Observable&lt;HelpShowIfModel&gt;
     */
    @GET("/api/help/show/{id}")
    fun helpShow(@Path("id") id: String): Observable<ApiModel<HelpShowIfModel>>
}