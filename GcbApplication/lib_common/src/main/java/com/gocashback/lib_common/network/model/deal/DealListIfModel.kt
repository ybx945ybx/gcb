package com.gocashback.lib_common.network.model.deal

/**
 * @Author 55HAITAO
 * @Date 2019-05-16 11:37
 */
class DealListIfModel {
    var total = 0       // 总条数
    var page_count = 0  // 总页数
    var deal: List<DealItemModel>? = listOf()
}