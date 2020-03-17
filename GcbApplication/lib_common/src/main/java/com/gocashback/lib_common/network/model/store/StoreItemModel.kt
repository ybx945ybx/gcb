package com.gocashback.lib_common.network.model.store

import com.gocashback.lib_common.network.model.deal.DealItemModel

/**
 * @Author 55HAITAO
 * @Date 2019-05-09 10:32
 */
class StoreItemModel {
    var id = ""    // int	商家ID
    var flag = ""    // string	商家名称简写
    var rebate = ""    // string	返利比例
    var is_upto = 0 // int	返利数值是否显示最高(up to) 0不显示 1显示
    var country_img = "" // string	国家旗帜
    var name = "" //	string	商家名称
    var oneword = "" //	string	简短描述
    var store_logo = "" //	string	商家logo
    var luxury_logo = "" //	string	背景logo
    var rebate_view = "" //	string	返利比例携带 最高或up to或Coupons Only
    var jump_url = "" //	string	跳转url
    var gotobuy_url = "" //	string	不要返利去看看url
    var collect_num = "" //int	商家收藏数量
    var deal : List<DealItemModel>? = null
}