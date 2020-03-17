package com.gocashback.lib_common.network.model.store

/**
 * @Author 55HAITAO
 * @Date 2019-05-09 19:57
 */
class StoreCateStoreIfModel{
    var total = 0       // 总条数
    var page_count = 0  // 总页数
    var store: List<StoreCateStoreItemModel>? = listOf()
}