package com.gocashback.lib_common.model

/**
 * @Author 55HAITAO
 * @Date 2019-06-27 11:42
 */
class JumpIfModel {
    var click_type = ""//   string    点击类型 web=>网页, activity=>活动, store=>商家, rest=>餐馆, none=>空, aroundRests=>app-发现-周边优惠, inviteFriend=>app-发现-邀请好友
    var url = ""//  string
    var gotag = ""// string
    var store_id = ""// int    商家id
    var gotobuy_url = ""//  string    不要返利去看看, 一种是”go/AVRL31?isoutsidelink = 1”, 另一种”https://www.gocashback.net/en/go/TU6qh3?isoutsidelink=1“
}