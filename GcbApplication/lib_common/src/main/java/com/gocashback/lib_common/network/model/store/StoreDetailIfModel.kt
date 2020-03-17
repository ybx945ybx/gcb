package com.gocashback.lib_common.network.model.store

/**
 * @Author 55HAITAO
 * @Date 2019-05-13 18:16
 */
class StoreDetailIfModel {
    var store = "" //    array    商家信息
    var id = "" // int    商家id
    var flag = "" //   string    商家标识
    var rebate = "" //  string    返利字段
    var is_upto = 0 //   int    是否最高返利 0否 1是
    var country_img = "" // string    商家国家旗帜
    var is_special_store = 0 //  int    是否特殊商家返利 0否 1是
    var aff_id = "" // : 1,
    var name = "" //   string    商家名称
    var oneword = "" //  string    商家简单描述
    var reminder = "" //   string    温馨提醒
    var content = "" //  string    商家介绍
    var rebate_explain = "" //  string    返利说明
    var is_not_find_order = 0 // ,
    var store_logo = "" //  string    商家logo
    var special: List<StoreDetailSpecialItemModel>? = null     // array	商家特殊返利
    var gotobuy_url = "" /// : "/go/store/6671",
    var collect_num = "" //    int    商家收藏数量
    var is_collect = 0 //   int    是否收藏，1已收藏
    var collect_id = "" //   int    收藏id
    var url = "" //   int    收藏id
    var reward = "" // : "13.50%"
    var pure_content = "" // : "13.50%"
    var share_url = "" // : "13.50%"

}