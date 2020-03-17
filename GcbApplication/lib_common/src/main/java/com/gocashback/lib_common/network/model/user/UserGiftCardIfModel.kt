package com.gocashback.lib_common.network.model.user

/**
 * @Author 55HAITAO
 * @Date 2019-06-15 14:37
 */
class UserGiftCardIfModel{
    var total = 0       // 总条数
    var page_count = 0  // 总页数
    var data: List<UserGiftCardItemModel>? = listOf()
}