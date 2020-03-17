package com.gocashback.lib_common.utils

/**
 * @Author 55HAITAO
 * @Date 2019-06-10 14:09
 */
fun moneyFormat(money: String, currency: String = "\$"): String {
    return currency + if (money.contains(".")) java.lang.Float.valueOf(money) else money
}