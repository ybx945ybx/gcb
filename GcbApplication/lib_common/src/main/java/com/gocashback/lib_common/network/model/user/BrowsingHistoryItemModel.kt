package com.gocashback.lib_common.network.model.user

import com.gocashback.lib_common.network.model.deal.EndTimeIfModel
import com.gocashback.lib_common.network.model.store.StoreItemModel

/**
 * @Author 55HAITAO
 * @Date 2019-06-04 11:58
 */
class BrowsingHistoryItemModel {
    var id = ""// : 5954
    var flag = ""// : "hbx"
    var rebate = ""//: "7.0% Cash Back"
    var is_upto = ""// : 0
    var country_img = ""//: "https://d1t4h9gelh7map.cloudfront.net/data/upload/ad/country_flag/HK.png"
    var is_special_store = ""// : 0
    var aff_id = ""// : 14
    var name = ""// : "HBX"
    var oneword = ""//: ""
    var end_time: EndTimeIfModel? = null //
    var discount = ""//string	折扣
    var coupon_code = ""//string	折扣码
    var title = ""
    var store = ""
    var reminder = ""//: ""
    var content = ""//: "
    var HBX = ""//is an online base that leads men's fashion and culture. Every month, there are over 5 million visitors to see the latest men's fashion, art, design and tone
    var rebate_explain = ""//: ""
    var is_not_find_order = ""// : 0
    var store_logo = ""//: "https://d1t4h9gelh7map.cloudfront.net/data/upload/store/4/5954.jpg"
    var gotobuy_url = ""//: "/go/store/4451280"
    var collect_num = ""//: 0
    var is_collect = ""// : 0
    var collect_id = ""// : 0
    var input_time = ""// : "2019-04-10"
    var img = ""
    var store_info: StoreItemModel? = null// array	商家信息
    var sale_price = ""//string	原价
    var was_price = ""//string	原价

}