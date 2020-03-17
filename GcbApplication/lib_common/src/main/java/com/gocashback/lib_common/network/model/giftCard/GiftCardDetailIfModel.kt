package com.gocashback.lib_common.network.model.giftCard

/**
 * @Author 55HAITAO
 * @Date 2019-06-17 14:26
 */
class GiftCardDetailIfModel{
   var  id	= "" // int	礼卡id
   var  card_id	= "" //int	礼卡父级id
   var  image_url= "" //	string	礼卡父级id
   var  brand_name	= "" //string	商家名称
   var  reward_name	= "" //string	礼卡名称
   var  value_type	= "" //string	面值类型，FIXED_VALUE固定面值，VARIABLE_VALUE可选面值
   var  disclaimer	= "" //string	免责声明
   var  description	= "" //string	描述
   var  terms= "" //	string	条款
   var  bonus_percent	= 0 //string	奖励百分比
   var  discount	= "" //string	奖励百分比
   var  available	= "" //string	奖励百分比
   var  face_values	:List<GiftCardDetailFaceIfModel> ?= null //  listOf()array	可选面值
}