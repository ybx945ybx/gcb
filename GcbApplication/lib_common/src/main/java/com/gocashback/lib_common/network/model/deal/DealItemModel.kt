package com.gocashback.lib_common.network.model.deal

import com.gocashback.lib_common.network.model.store.StoreItemModel

/**
 * @Author 55HAITAO
 * @Date 2019-05-16 11:38
 */
class DealItemModel {
    var id = ""//int	优惠详情
    var dealam_id = ""//	int	dealam id
    var fmtc_cid = ""//int	fmtc id
    var store_id = ""//int	商家id
    var push_id = ""//int	推送id
    var coupon_code = ""//string	折扣码
    var was_price = ""//string	原价
    var sale_price = ""//string	原价
    var start_time = ""//string	开始时间
    //    end_time	stri= ""//ng	结束时间
    var end_time: EndTimeIfModel? = null //

    var img = ""//string折扣logo
    var url = ""//string商品原始网址
    var track_url = ""//string	可追踪的URl
    var is_hot = ""//int	是否热门
    var is_recommend = ""//	int	是否推荐
    var is_exclusive = ""//	int	是否独家
    var collect_num = ""//int	收藏数量
    var currency_id = ""//int	币种id
    var title = ""//	string	标题
    var store = ""//	string	商家名称
    var discount = ""//string	折扣
    var content = ""//string	内容
    var gotobuy_url = ""//string	跳转url
    var currency = ""//string	币种符号
    var store_info: StoreItemModel? = null// array	商家信息
}