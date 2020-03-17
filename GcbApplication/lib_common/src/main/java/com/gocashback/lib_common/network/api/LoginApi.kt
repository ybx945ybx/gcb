package com.gocashback.lib_common.network.api

import com.gocashback.lib_common.network.model.ApiModel
import com.gocashback.lib_common.network.model.login.LoginIfModel
import com.gocashback.lib_common.network.model.login.LoginVerifyIfModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


/**
 * @Author 55HAITAO
 * @Date 2019-05-08 17:38
 */
interface LoginApi {

    /**
     * 图形验证码
     *
     * @return Observable&lt;LoginVerifyIfModel&gt;
     */
    @GET("/api/login/verify")
    fun loginVerify(): Observable<ApiModel<LoginVerifyIfModel>>

    /**
     * 登录+注册
     *
     * @param     type            是   1登录，2注册
     * @param     email           是   邮箱
     * @param     password        是   密码 6-20位
     * @param     captcha         是   图形验证码
     * @param     referrer_email  否   推荐人邮箱
     * @return Observable&lt;LoginIfModel&gt;
     */
    @FormUrlEncoded
    @POST("/api/login")
    fun login(@Field("type") type: String, @Field("email") email: String, @Field("password") password: String, @Field("captcha") captcha: String, @Field("referrer_email") referrer_email: String): Observable<Response<ApiModel<LoginIfModel>>>

    /**
     * 忘记+修改密码
     * @param     email           是   邮箱
     * @param     password        是   密码 需base64加密
     * @param     confirm_password         是   确认密码 需base64加密
     * @param     old_password  否   旧密码，修改密码时需传 需base64加密
     * @return Observable&lt;Any&gt;
     */
    @FormUrlEncoded
    @POST("/api/login/update_pwd")
    fun loginUpdatePwd(@Field("email") email: String, @Field("password") password: String, @Field("confirm_password") confirm_password: String, @Field("old_password") old_password: String): Observable<ApiModel<Any>>
}