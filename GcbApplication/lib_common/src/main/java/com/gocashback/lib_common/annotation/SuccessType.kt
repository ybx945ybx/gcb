package com.gocashback.lib_common.annotation

import android.support.annotation.IntDef
import com.gocashback.lib_common.annotation.SuccessType.Companion.FINDORDER
import com.gocashback.lib_common.annotation.SuccessType.Companion.GIFTCARD
import com.gocashback.lib_common.annotation.SuccessType.Companion.WITHDRAW

/**
 * @Author 55HAITAO
 * @Date 2019-07-07 15:57
 */
@IntDef(GIFTCARD, WITHDRAW, FINDORDER)
@Retention(AnnotationRetention.SOURCE)
annotation class SuccessType {
    companion object {
        const val GIFTCARD = 0    // 礼品卡兑换
        const val WITHDRAW = 1    // 提现
        const val FINDORDER = 2   // 找回订单
//            const val ORDER = 3     // 我的订单
    }
}