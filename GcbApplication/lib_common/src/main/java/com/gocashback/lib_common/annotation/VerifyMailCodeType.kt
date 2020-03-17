package com.gocashback.lib_common.annotation

import android.support.annotation.IntDef
import com.gocashback.lib_common.annotation.VerifyMailCodeType.Companion.FORGETPAYMENTPWD
import com.gocashback.lib_common.annotation.VerifyMailCodeType.Companion.FORGETPWD
import com.gocashback.lib_common.annotation.VerifyMailCodeType.Companion.PAYMENTPWD
import com.gocashback.lib_common.annotation.VerifyMailCodeType.Companion.UPDATEPWD

/**
 * @Author 55HAITAO
 * @Date 2019-06-19 14:33
 */
@IntDef(PAYMENTPWD, UPDATEPWD, FORGETPWD, FORGETPAYMENTPWD)
@Retention(AnnotationRetention.SOURCE)
annotation class VerifyMailCodeType {
    companion object {
        const val PAYMENTPWD = 0        // 设置支付密码
        const val FORGETPAYMENTPWD = 3  // 忘记支付密码
        const val UPDATEPWD = 1         // 修改登录密码
        const val FORGETPWD = 2         // 忘记登录密码
    }
}