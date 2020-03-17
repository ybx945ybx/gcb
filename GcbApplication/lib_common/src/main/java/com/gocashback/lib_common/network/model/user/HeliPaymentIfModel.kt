package com.gocashback.lib_common.network.model.user

import com.gocashback.lib_common.model.interfaces.IpaymentModel
import java.io.Serializable

/**
 * @Author 55HAITAO
 * @Date 2020-02-17 14:55
 */
class HeliPaymentIfModel : Serializable, IpaymentModel {
    var bank_account_name_cn = "" // : "李婷"
    var bank_account_no = "" //: "6222620110024380140"
    var bank_id = "" //: 5
    var code = "" //: "BOCO"
    var create_time = "" //: 1581299556
    var icon = "" //: "https://img.gocashback.com/data/upload/point_missions/5e2500409a229.png"
    var id = "" //: 28
    var id_card_no = "" //: "320324199304090627"
    var link_phone = "" //: "17721791954"
    var name = "" //: "BOCO"
    var name_en = "" //: "Bank of Communications"
    var uid = "" //: 1731270
    var update_time = "" //: 1581299556

    var last_payment = 0 // 上次提现方式  1paypal, 3check, 5alipay, 7 helipay
    var last_payment_bank_account_no = ""//

    var isSelected = false

}