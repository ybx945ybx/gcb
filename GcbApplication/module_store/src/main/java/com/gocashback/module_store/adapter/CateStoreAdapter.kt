package com.gocashback.module_store.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.network.model.store.StoreCateStoreItemModel
import com.gocashback.module_store.R

/**
 * 分类商家列表适配器
 *
 * @Author 55HAITAO
 * @Date 2019-05-13 14:35
 */
class CateStoreAdapter(private val context: Context?, list: List<StoreCateStoreItemModel>) : BaseQuickAdapter<StoreCateStoreItemModel, BaseViewHolder>(R.layout.item_cate_store, list) {

    override fun convert(helper: BaseViewHolder, item: StoreCateStoreItemModel) {
        with(item) {
            helper.setText(R.id.tv_store_name, name)
                    .setText(R.id.tv_store_cash_back, rebate)
                    .setText(R.id.tv_store_favourites, collect_num + context?.resources?.getString(R.string.store_collect))
                    .setGone(R.id.tv_go_shop, false)

            loadImage(originUrl = country_img, targetView = helper.getView(R.id.iv_store_country))
            loadImage(originUrl = store_logo, targetView = helper.getView(R.id.iv_store_cover))

        }

    }
}