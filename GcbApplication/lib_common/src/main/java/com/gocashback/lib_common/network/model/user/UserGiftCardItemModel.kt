package com.gocashback.lib_common.network.model.user

import android.os.CountDownTimer
import com.gocashback.lib_common.R
import com.gocashback.lib_common.base.GcbBaseApplication

/**
 * @Author 55HAITAO
 * @Date 2019-06-15 14:38
 */
class UserGiftCardItemModel {
    var id = "" // int	我的礼卡id
    var amount_bonus = "" // i	string	我的礼卡id
    var add_time = "" // istring	添加时间
    var reward_name = "" // istring	礼卡名称
    var is_available = 0 // iint	使用状态 0未使用，1已使用
    var brand_name = ""

    // 倒计时用
    var isOnCount = false
    var time = GcbBaseApplication.application?.getString(R.string.gift_cards_resend)
}