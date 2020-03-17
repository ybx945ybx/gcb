package com.gocashback.lib_common.network.model.user

import com.gocashback.lib_common.model.interfaces.IpaymentModel
import java.io.Serializable

/**
 * @Author 55HAITAO
 * @Date 2019-06-04 11:12
 */
class PaymentIfModel : Serializable, IpaymentModel {
    var id = ""// int	支付方式id
    var uid = ""// int	uid
    var type = ""// 	int	‘paypal’,’check’,’alipay’
    var charity_id = ""// int	慈善机构id
    var firstname = ""// string	名
    var lastname = ""// string	姓
    var city = ""// string	城市
    var state = ""// string	州名
    var street = ""// string	地址
    var street_two = ""// string	地址2
    var post = ""// string	邮编
    var card = ""// string	银行卡号码
    var month = ""// int	月
    var year = ""// int	年
    var paypal = ""// string	Paypal帐号
    var paypal_firstname = ""// string	Paypal帐号
    var paypal_lastname = ""// string	Paypal帐号
    var paypal_area = ""// string	Paypal帐号
    var paypal_area_value = ""// string	Paypal帐号
    var alipay = ""// string	支付宝账号
    var alipay_name = ""// string	支付宝账号名称
    var inputtime = ""// int	创建时间
    var updatetime = ""// int	修改时间
    var payment_type = ""// array	支付方式对应type
    var check = ""// int	支票

    var paymentType = -1
    var isSelected = false


}