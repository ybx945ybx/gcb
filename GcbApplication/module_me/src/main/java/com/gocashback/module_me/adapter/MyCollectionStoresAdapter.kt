package com.gocashback.module_me.adapter

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.network.model.user.CollectionItemModel
import com.gocashback.lib_common.widget.GcbGoBuyView
import com.gocashback.module_me.R
import com.gocashback.module_me.event.CheckAllSelectEvent
import org.greenrobot.eventbus.EventBus

/**
 * @Author 55HAITAO
 * @Date 2019-06-12 17:42
 */
class MyCollectionStoresAdapter( private val mActivity: Activity?, list: List<CollectionItemModel>, private var editable: Boolean = false) : BaseQuickAdapter<CollectionItemModel, BaseViewHolder>(R.layout.item_my_collection_stores, list) {
    override fun convert(helper: BaseViewHolder, item: CollectionItemModel) {
        with(item) {
            helper.setText(R.id.tv_store_name, name)
                    .setText(R.id.tv_store_cash_back, rebate)
                    .setText(R.id.tv_store_favourites, collect_num + " " + mActivity?.resources?.getString(R.string.store_collect))
                    .setGone(R.id.iv_select, editable)
                    .setGone(R.id.tv_go_shop, !editable)
//                    .addOnClickListener(R.id.iv_select)
            // gobuy
            helper.getView<GcbGoBuyView>(R.id.tv_go_shop).apply {
                gotobuyUrl = gotobuy_url
                activity = mActivity
            }

            helper.getView<ImageView>(R.id.iv_select).apply {
                isSelected = select
                setOnClickListener {
                    select = !it.isSelected
                    notifyDataSetChanged()
                    checkAllSelect()
                }
            }

            loadImage(originUrl = store_logo, targetView = helper.getView(R.id.iv_store_cover))
            loadImage(originUrl = country_img, targetView = helper.getView(R.id.iv_store_country))


        }

    }

    /**
     * 每次点选以后检查是否是全选，同步底部全选按钮
     */
    private fun checkAllSelect() {
        EventBus.getDefault().post(CheckAllSelectEvent())
    }

    fun setEditable(editable: Boolean) {
        this.editable = editable
        notifyDataSetChanged()
    }

    fun getEditable() = editable

    fun setSelectPosition(position: Int) {
        data[position].select = !data[position].select
        notifyItemChanged(position)
        checkAllSelect()
    }

    fun setAllSelect(allSelect: Boolean) {
        data.forEach { it.select = allSelect }
        notifyDataSetChanged()
    }

    fun getSelectedIds(): String {
        var ids = ""
        data.forEach {
            if (it.select) {
                ids = if (ids.isEmpty()) {
                    it.collect_id
                } else {
                    ids + "," + it.collect_id
                }
            }
        }

        return ids
    }
}