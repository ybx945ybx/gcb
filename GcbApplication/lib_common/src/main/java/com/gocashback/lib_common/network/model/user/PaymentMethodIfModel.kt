package com.gocashback.lib_common.network.model.user

/**
 * @Author 55HAITAO
 * @Date 2019-06-04 11:08
 */
class PaymentMethodIfModel {
    var is_pay_pwd = 0 // int	是否已设置过支付密码，0否 1是
    var withdraw_successful_count = 0 //
    var low_withdraw_limit = "20" //
    var low_withdraw_limit_for_second = "10" //
    var last_payment = 0 // : 7
    var last_payment_bank_account_no = "" // : "6222620110024380140"
    var payment: PaymentIfModel? = null //支付方式信息
    var helipay: List<HeliPaymentIfModel>? = listOf() //合利宝

}