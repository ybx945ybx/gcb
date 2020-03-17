package com.gocashback.lib_common.network.model.user

/**
 * @Author 55HAITAO
 * @Date 2019-06-04 11:01
 */
class UserIfModel {
    var uid = ""           // 用户uid
    var user_name = ""//	string	用户名称
    var email = ""//string	邮箱
    var available = ""//int	可用金额
    var amount = ""//int	总金额
    var avatar = ""//string	用户头像
    var is_pay_pwd = 0//	int	是否已设置支付密码，0否 1是
    var is_payment = 0//int	是否已设置支付方式，0否 1是
}