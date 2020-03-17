package com.gocashback.lib_common.network.model.index

import com.gocashback.lib_common.network.model.store.StoreItemModel

/**
 * @Author 55HAITAO
 * @Date 2019-05-14 17:52
 */
class IndexAdvertItemModel {
    var id = ""// int    广告id
    var type = ""//  string    广告位类型 image=>图片, code=>代码, flash=>Flash, text=>文字
    var click_type = ""//   string    点击类型 web=>网页, activity=>活动, store=>商家, rest=>餐馆, none=>空, aroundRests=>app-发现-周边优惠, inviteFriend=>app-发现-邀请好友
    var name = ""//  string    广告名称
    var url = ""//  string    广告url
    var img_url = ""//  string    广告图片url
    var gotag = ""// string    广告totag
    var banner_title = ""//  string    banner标题第一行
    var banner_title_two = ""//   string    banner标题第二行
    var discount_code = ""//  string    折扣码
    var Intersection_shape = 0//  int    banner交界形状 0=>斜线, 1=>弧形, 2=>不需要
    var deal_id = ""//  int    优惠id
    var store_id = ""// int    商家id
    var is_exclusive = ""//   int    优惠是否为独家，0=>否, 1=>是
    var gotobuy_url = ""//  string    不要返利去看看, 一种是”go/AVRL31?isoutsidelink = 1”, 另一种”https://www.gocashback.net/en/go/TU6qh3?isoutsidelink=1“
    var store: StoreItemModel? = null//    商家信息
}