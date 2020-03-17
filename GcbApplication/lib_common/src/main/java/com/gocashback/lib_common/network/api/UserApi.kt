package com.gocashback.lib_common.network.api

import com.gocashback.lib_common.network.model.ApiModel
import com.gocashback.lib_common.network.model.user.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-04 10:50
 */
interface UserApi {
    /**
     * 个人中心
     *
     * @return Observable&lt;UserIfModel&gt;
     */
    @GET("/api/user")
    fun user(): Observable<ApiModel<UserIfModel>>

    /**
     * 支付方式
     *
     * @return Observable&lt;PaymentMethodIfModel&gt;
     */
    @GET("/api/user/payment_method")
    fun paymentMethod(): Observable<ApiModel<PaymentMethodIfModel>>

    /**
     * 修改支付方式
     *
     * payment_type	是	int	支付方式，1paypal, 3check, 5alipay, 7 helipay
     * paypal	否	string	paypal, payment_type=1
     * post	否	string	邮编, payment_type=3
     * city	否	string	城市, payment_type=3
     * state	否	string	州名, payment_type=3
     * street	否	string	地址, payment_type=3
     * street_two	否	string	地址2, payment_type=3
     * lastname	否	string	姓, payment_type=3
     * firstname	否	string	名, payment_type=3
     * alipay	否	string	支付宝账号, payment_type=5
     * alipay_name	否	string	支付宝名称, payment_type=5
     * bank_id 银行id,  payment_type=7
     * id_card_no 身份证,  payment_type=7
     * bank_account_name_cn 账户姓名,  payment_type=7
     * bank_account_no 账号卡号,  payment_type=7
     * link_phone 手机号码,  payment_type=7
     * @return Observable&lt;any&gt;
     */
    @FormUrlEncoded
    @POST("/api/user/payment_method_update")
    fun paymentMethodPaypalUpdate(@Field("payment_type") payment_type: Int = 1
                                  , @Field("paypal_firstname") paypal_firstname: String
                                  , @Field("paypal_lastname") paypal_lastname: String
                                  , @Field("paypal") paypal: String
                                  , @Field("paypal_area") paypal_area: String): Observable<ApiModel<Any>>

    @FormUrlEncoded
    @POST("/api/user/payment_method_update")
    fun paymentMethodCheckUpdate(@Field("payment_type") payment_type: Int = 3
                                 , @Field("post") post: String
                                 , @Field("city") city: String
                                 , @Field("state") state: String
                                 , @Field("street") street: String
                                 , @Field("street_two") street_two: String
                                 , @Field("lastname") lastname: String
                                 , @Field("firstname") firstname: String): Observable<ApiModel<Any>>

    @FormUrlEncoded
    @POST("/api/user/payment_method_update")
    fun paymentMethodAliUpdate(@Field("payment_type") payment_type: Int = 5
                               , @Field("alipay") alipay: String
                               , @Field("alipay_name") alipay_name: String): Observable<ApiModel<Any>>


    @FormUrlEncoded
    @POST("/api/user/payment_method_update")
    fun paymentMethodHeliUpdate(@Field("payment_type") payment_type: Int = 7
                                , @Field("bank_id") bank_id: String
                                , @Field("bank_account_no") bank_account_no: String
                                , @Field("link_phone") link_phone: String
                                , @Field("bank_account_name_cn") bank_account_name_cn: String
                                , @Field("id_card_no") id_card_no: String): Observable<ApiModel<Any>>

    @FormUrlEncoded
    @POST("/api/user/remove_helipay")
    fun paymentMethodRemoveHeli(@Field("ids") ids: String): Observable<ApiModel<Any>>
//    @FormUrlEncoded
//    @POST("/api/user/payment_method_update")
//    fun paymentMethodUpdate(@Field("payment_type") payment_type: Int
//                            , @Field("paypal") paypal: String
//                            , @Field("post") post: String
//                            , @Field("city") city: String
//                            , @Field("state") state: String
//                            , @Field("street") street: String
//                            , @Field("street_two") street_two: String
//                            , @Field("lastname") lastname: String
//                            , @Field("firstname") firstname: String
//                            , @Field("alipay") alipay: String
//                            , @Field("alipay_name") alipay_name: String): Observable<ApiModel<Any>>

    /**
     * 我的礼卡
     *
     * page	否	int	当前页，从0开始，默认0
     * per_page	否	int	每页显示条数，默认30条
     *
     * @return Observable&lt;CollectionIfModel&gt;
     */
    @GET("/api/user/gift_card")
    fun giftCard(@Query("page") page: Int, @Query("per_page") per_page: Int): Observable<ApiModel<UserGiftCardIfModel>>

    /**
     * 我的礼卡使用
     *
     * id	是	int	我的礼卡id，注：不是礼卡id
     *
     * @return Observable&lt;CollectionIfModel&gt;
     */
    @GET("/api/user/gift_card_use/{id}")
    fun giftCardUse(@Path("id") id: String): Observable<ApiModel<Any>>

    /**
     * 我的礼卡发送邮件
     *
     * id	是	int	我的礼卡id，注：不是礼卡id
     *
     * @return Observable&lt;CollectionIfModel&gt;
     */
    @GET("/api/user/gift_card_send_email/{id}")
    fun giftCardSendEmail(@Path("id") id: String): Observable<ApiModel<Any>>

    /**
     * 我的收藏
     *
     * type	是	string	“store”，“deal”
     * page	否	int	当前页，从0开始，默认0
     * per_page	否	int	每页显示条数，默认30条
     *
     * @return Observable&lt;CollectionIfModel&gt;
     */
    @GET("/api/user/collection")
    fun collection(@Query("type") type: String, @Query("page") page: Int, @Query("per_page") per_page: Int): Observable<ApiModel<CollectionIfModel>>

    /**
     * 取消收藏
     *
     * ids	是	string	collection_id
     * @return Observable&lt;LoginVerifyIfModel&gt;
     */
    @FormUrlEncoded
    @POST("/api/user/collection_del")
    fun collectionDelete(@Field("ids") ids: String): Observable<ApiModel<Any>>

    /**
     * 添加收藏
     *
     * type	是	string	“store”，“deal”
     *  id	是	int	商家id或折扣id
     * @return Observable&lt;LoginVerifyIfModel&gt;
     */
    @FormUrlEncoded
    @POST("/api/user/collection_add")
    fun collectionAdd(@Field("type") type: String, @Field("id") id: String): Observable<ApiModel<CollectionAddIfModel>>

    /**
     * 浏览记录
     *
     * type	是	int	1商家，2折扣
     * page	否	int	当前页，从0开始，默认0
     * per_page	否	int	每页显示条数，默认30条
     *
     * @return Observable&lt;BrowsingHistoryIfModel&gt;
     */
    @GET("/api/user/transfer")
    fun browsingHistory(@Query("type") type: Int, @Query("page") page: Int, @Query("per_page") per_page: Int): Observable<ApiModel<BrowsingHistoryIfModel>>

    /**
     * 消息通知
     *
     * page	否	int	当前页，从0开始，默认0
     * per_page	否	int	每页显示条数，默认30条
     *
     * @return Observable&lt;BrowsingHistoryIfModel&gt;
     */
    @GET("/api/user/notice")
    fun notice(@Query("page") page: Int, @Query("per_page") per_page: Int): Observable<ApiModel<NoticeIfModel>>

    /**
     * 消息通知详情
     * id	是	int	通知id
     *
     * @return Observable&lt;NoticeDetailIfModel&gt;
     */
    @GET("/api/user/notice_detail")
    fun noticeDetail(@Query("id") id: String): Observable<ApiModel<NoticeDetailIfModel>>

    /**
     * 返利记录
     * page	否	int	当前页，从0开始，默认0
     * per_page	否	int	每页显示条数，默认30条
     * status	否	int	0全部 1待生效 2已生效
     * type	否	int	0全部 1订单返利，2朋友订单返利，3注册奖励，4邀请奖励，6活动奖励，7线下活动
     *
     * @return Observable&lt;NoticeDetailIfModel&gt;
     */
    @GET("/api/user/rebate_record")
    fun rebateRecord(@Query("page") page: Int, @Query("per_page") per_page: Int, @Query("status") status: Int, @Query("type") type: Int): Observable<ApiModel<RebateRecordlfModel>>

    /**
     * 提现记录
     * page	否	int	当前页，从0开始，默认0
     * per_page	否	int	每页显示条数，默认30条
     * status	否	0全部 1支付中 2已支取 3提现失败 4提现驳回
     * pay_type	否	int	0全部 1paypal 3支票 5支付宝 6礼品卡
     * @return Observable&lt;NoticeDetailIfModel&gt;
     */
    @GET("/api/user/withdraw_record")
    fun withdrawRecord(@Query("page") page: Int, @Query("per_page") per_page: Int, @Query("status") status: Int, @Query("pay_type") pay_type: Int): Observable<ApiModel<WithdrawRecordIfModel>>

    /**
     * 返利记录+提现记录详情
     * id	是	int	返利记录或提现记录id
     *
     * @return Observable&lt;NoticeDetailIfModel&gt;
     */
    @GET("/api/user/rebate_detail")
    fun rebateDetail(@Query("id") id: String): Observable<ApiModel<RebateDetailIfModel>>

    /**
     * 我的订单
     * page	否	int	当前页，从0开始，默认0
     * per_page	否	int	每页显示条数，默认30条
     * type	是	int	0全部，1已生效,2待生效
     * @return Observable&lt;NoticeDetailIfModel&gt;
     */
    @GET("/api/user/order")
    fun order(@Query("page") page: Int, @Query("per_page") per_page: Int, @Query("type") type: Int): Observable<ApiModel<OrderIfModel>>

    /**
     * 订单详情
     * id	是	int	订单id
     *
     * @return Observable&lt;NoticeDetailIfModel&gt;
     */
    @GET("/api/user/order_detail")
    fun orderDetail(@Query("id") id: String): Observable<ApiModel<OrderDetailIfModel>>

    /**
     * 丢单列表
     * page	否	int	当前页，从0开始，默认0
     * per_page	否	int	每页显示条数，默认30条
     * type	是	int		0全部，1待审核,2待确认，3驳回，4已添加，5关闭
     * @return Observable&lt;NoticeDetailIfModel&gt;
     */
    @GET("/api/user/lost_order")
    fun lostOrder(@Query("page") page: Int, @Query("per_page") per_page: Int, @Query("type") type: Int): Observable<ApiModel<LostOrderIfModel>>

    /**
     * 丢单详情
     * id	是	int	订单id
     *
     * @return Observable&lt;NoticeDetailIfModel&gt;
     */
    @GET("/api/user/lost_order_detail")
    fun lostOrderDetail(@Query("id") id: String): Observable<ApiModel<LostOrderDetailIfModel>>

    /**
     * 丢单页面数据
     * page	否	int	当前页，从0开始，默认0
     * per_page	否	int	每页显示条数，默认30条
     * keyword	否	string
     *
     * @return Observable&lt;NoticeDetailIfModel&gt;
     */
    @GET("/api/user/lost_order_show")
    fun lostOrderShow(@Query("page") page: Int, @Query("per_page") per_page: Int, @Query("keyword") keyword: String): Observable<ApiModel<LostOrderShowIfModel>>

    /**
     * 找回丢单
     * store_id	是	int	商家id
     * order_date	是	string	订单时间
     * order_id	是	string	订单号
     * currency	是	string	币种 例“USD”
     * amount	是	int	丢单金额
     * img	是	string	图片
     * @return Observable&lt;NoticeDetailIfModel&gt;
     */
    @FormUrlEncoded
    @POST("/api/user/retrieve_lost_order")
    fun retrieveostOrderShow(@Field("store_id") store_id: String, @Field("order_date") order_date: String, @Field("order_id") order_id: String, @Field("currency") currency: String, @Field("amount") amount: String, @Field("img") img: String): Observable<ApiModel<Any>>

    /**
     * 提现
     *
     * payment_type	是	int	支付方式，1paypal, 3check, 5alipay，6giftCard
     * amount	是	string	提现金额
     * gift_id	否	int	礼卡id ，payment_type=6需传
     * helipay_id   合利宝id ，payment_type=7需传
     *
     * @return Observable&lt;NoticeDetailIfModel&gt;
     */
    @FormUrlEncoded
    @POST("/api/user/withdraw")
    fun withdraw(@Field("payment_type") payment_type: Int, @Field("amount") amount: String, @Field("gift_id") gift_id: String, @Field("helipay_id") helipay_id: String): Observable<ApiModel<Any>>

    /**
     * 设置支付密码
     *
     * password	是	string	需要base64,支付密码
     * confirm_password	是	string	需要base64, 确认支付密码
     *
     * @return Observable&lt;NoticeDetailIfModel&gt;
     */
    @FormUrlEncoded
    @POST("/api/user/payment_pwd")
    fun paymentPwd(@Field("password") password: String, @Field("confirm_password") confirm_password: String): Observable<ApiModel<Any>>

    /**
     * 支付密码验证
     *
     * payment_pwd	是	string	需要base64，支付密码
     *
     * @return Observable&lt;NoticeDetailIfModel&gt;
     */
    @FormUrlEncoded
    @POST("/api/user/payment_pwd_verify")
    fun paymentPwdVerify(@Field("payment_pwd") payment_pwd: String): Observable<ApiModel<Any>>

    /**
     * 邀请好友
     *
     * @return Observable&lt;InviteIfModel&gt;
     */
    @GET("/api/user/invite")
    fun invite(): Observable<ApiModel<InviteIfModel>>

    /**
     * 邀请好友发送邮件
     *
     * @return Observable&lt;InviteIfModel&gt;
     */
    @FormUrlEncoded
    @POST("/api/user/invite_email")
    fun inviteEmail(@Field("email") email: String): Observable<ApiModel<Any>>

    /**
     * 未读消息数
     *
     * @return Observable&lt;InviteIfModel&gt;
     */
    @GET("/api/user/notice_unread")
    fun noticeUnread(): Observable<ApiModel<NoticeUnreadIfModel>>

    /**
     * 独家福利
     *
     * @return Observable&lt;InviteIfModel&gt;
     * type	是	int	0为不开启，1为开启
     */
    @GET("/api/user/exclusive_benefits")
    fun exclusiveBenefits(@Query("type") type: Int): Observable<ApiModel<ExclusiveBenefitsIfModel>>


}