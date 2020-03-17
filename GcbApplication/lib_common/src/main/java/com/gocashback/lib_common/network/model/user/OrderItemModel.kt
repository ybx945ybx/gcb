package com.gocashback.lib_common.network.model.user

/**
 * @Author 55HAITAO
 * @Date 2019-06-04 14:33
 */
class OrderItemModel {
    var id = ""//int	订单id
    var progress = ""//int	0 未审核 1 已审核 2 已推送 3 已结算
    var uid = ""//int	用户id
    var record_id = ""//int	流水ID
    var record_rid = ""//int	用户上级流水ID
    var transfer_id = ""//int	跳转ID
    var dateline = 0L//int	创建日期
    var store = ""//string	商家名称
    var merchant_id = ""//string	mid
    var order_id = ""//string	订单号
    var status = 0//string	0,1,5待生效 4已生效
    var amount = ""//string	消费金额
    var rebate = ""//string	返利
    var valid_time = ""//int	生效时间
    var byhand = ""//int	是否为手动提交,0否，1是
    var come_source = ""//	int	初始来源0为正常1为手工
    var transaction_time = ""//	int	订单时间
    var aff_id = "" // : 1,
    var store_logo = ""//	string	商家logo
    var order_date = ""//	string	商家logo
    var inputtime =0L//	丢单创建时间
}