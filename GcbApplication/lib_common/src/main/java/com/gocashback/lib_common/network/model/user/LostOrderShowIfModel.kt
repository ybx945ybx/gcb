package com.gocashback.lib_common.network.model.user

/**
 * @Author 55HAITAO
 * @Date 2019-06-10 16:49
 */
class LostOrderShowIfModel {
    var total = 0       // 总条数
    var page_count = 0  // 总页数
    var data: List<LostOrderShowStoreItemModel>? = listOf()
    var currency: LostOrderShowCurrencyIfModel? = null
}