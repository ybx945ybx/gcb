package com.gocashback.lib_common.network.model.giftCard

/**
 * @Author 55HAITAO
 * @Date 2019-06-15 14:50
 */
class GiftCardIfModel{
    var total = 0       // 总条数
    var page_count = 0  // 总页数
    var data: List<GiftCardItemModel>? = listOf()
}