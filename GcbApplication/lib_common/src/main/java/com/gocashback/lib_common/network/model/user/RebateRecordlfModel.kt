package com.gocashback.lib_common.network.model.user

/**
 * @Author 55HAITAO
 * @Date 2019-06-04 13:59
 */
class RebateRecordlfModel {
    var total = 0       // 总条数
    var page_count = 0  // 总页数
    var effective_amount = ""       //	int	已生效金额
    var pending_amount = ""       //int	待生效金额
    var data: List<RebateRecordItemModel>? = listOf()

}