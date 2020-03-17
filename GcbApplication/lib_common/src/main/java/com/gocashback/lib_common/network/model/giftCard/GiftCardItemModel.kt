package com.gocashback.lib_common.network.model.giftCard

/**
 * @Author 55HAITAO
 * @Date 2019-06-15 14:51
 */
class GiftCardItemModel {
    var id = "" // int	礼卡id
    var card_id = "" // int	礼卡父级id
    var value_type = "" // string	面值类型，固定面值FIXED_VALUE，可选金额有VARIABLE_VALUE）
    var bonus_percent = "" // string	返利百分比
    var brand_name = "" // string	商家名称
    var image_url = "" // 	string	礼卡logo
    var is_available = 0 // int	是否使用 ，0未使用 1已使用
}