package com.gocashback.lib_common.network.model.transfer

import com.gocashback.lib_common.network.model.store.StoreDetailIfModel
import com.gocashback.lib_common.network.model.store.StoreItemModel

/**
 * @Author 55HAITAO
 * @Date 2019-06-18 16:04
 */
class JumpTransferIfModel{
   var jump_url	= "" // string	跳转url
   var gotag	= "" // string	如果有gotag的话就拼在gotobuy_url后边
   var store_info	: StoreDetailIfModel? = null // array	商家信息
}