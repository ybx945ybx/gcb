package com.gocashback.lib_common.model

import java.io.Serializable

/**
 * 去购买中间页信息
 *
 * @Author 55HAITAO
 * @Date 2019-06-18 19:45
 */
class TransferExtraIfModel : Serializable {
    var jump_url = ""
    var coupon_code = ""
    var store_logo = ""
    var rebate = "" //  string    返利字段
    var rebate_explain = "" //  string    返利说明字段

}