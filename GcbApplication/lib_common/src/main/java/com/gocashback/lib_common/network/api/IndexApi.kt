package com.gocashback.lib_common.network.api

import com.gocashback.lib_common.network.model.ApiModel
import com.gocashback.lib_common.network.model.index.*
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * @Author 55HAITAO
 * @Date 2019-05-14 17:39
 */
interface IndexApi {
    /**
     * 公共信息
     * @return Observable&lt;StoreDetailIfModel&gt;
     */
    @GET("/api/index")
    fun getIndex(): Observable<ApiModel<IndexIfModel>>

    /**
     * 合利宝银行
     * @return Observable&lt;StoreDetailIfModel&gt;
     */
    @GET("/api/index/helipay_bank")
    fun getHelipayBank(): Observable<ApiModel<List<IndexHelipayBankItemModel>>>

    /**
     * 公共广告位信息
     *
     * @param     id    是	int	广告位id，具体id请找后端确定    首页banner广告位id中文：50，英文49
     *                                                          超级返利广告位id中文：56，英文55
     *                                                          发现 ，英文63，中文62
     * @return Observable&lt;StoreDetailIfModel&gt;
     */
    @GET("/api/index/advert/{id}")
    fun getIndexAdvert(@Path("id") id: Int): Observable<ApiModel<IndexAdvertIfModel>>

    /**
     * 公共广告位信息
     *
     * @return Observable&lt;List<IndexChannelItemModel>&gt;
     */
    @GET("/api/index/channel")
    fun getIndexChannel(): Observable<ApiModel<List<IndexChannelItemModel>>>

    /**
     * 发送邮箱验证码
     * email	是	string	邮箱
     * @return Observable&lt;List<IndexChannelItemModel>&gt;
     */
    @FormUrlEncoded
    @POST("/api/index/send_validate_code")
    fun sendValidateCode(@Field("email") email: String): Observable<ApiModel<Any>>

    /**
     * 验证邮箱验证码
     * email	是	string	邮箱
     * code	是	int	验证码，4-6位
     * @return Observable&lt;List<IndexChannelItemModel>&gt;
     */
    @FormUrlEncoded
    @POST("/api/index/verification_mail_code")
    fun verificationMailCode(@Field("email") email: String, @Field("code") code: String): Observable<ApiModel<Any>>

    /**
     * 上传图片
     * img	是	file	图片
     * path	是	string	上传文件夹名，例丢单图片：inquiry，请与后端确认
     * @return Observable&lt;List<upLoadItemModel>&gt;
     */
//    @FormUrlEncoded
    @Multipart
    @POST("/api/index/upload")
//    fun upLoad(@Field("img") img: File, @Field("path") path: String = "inquiry"): Observable<ApiModel<List<upLoadItemModel>>>
    fun upLoad(@Part file: MultipartBody.Part, @Part("path") description: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"), "inquiry")
    ): Observable<ApiModel<List<upLoadItemModel>>>

}