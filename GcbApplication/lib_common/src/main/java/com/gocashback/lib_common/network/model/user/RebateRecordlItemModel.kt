package com.gocashback.lib_common.network.model.user

/**
 * @Author 55HAITAO
 * @Date 2019-06-04 14:01
 */
class RebateRecordItemModel {
    var id = ""       // int	记录id
    var itemid = ""       // int	记录id
    var type = 0       // int	1订单返利，2朋友订单返利，3注册奖励，4邀请奖励，6活动奖励，7线下活动
    var amount = ""       // int	金额
    var dateline = ""       // int	创建时间
    var validtime = ""       // int	生效时间
    var status = 0       // int	0待生效，1已生效
}