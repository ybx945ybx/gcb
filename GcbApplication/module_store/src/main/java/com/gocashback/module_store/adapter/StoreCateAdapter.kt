package com.gocashback.module_store.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.network.model.store.StoreCateItemModel
import com.gocashback.module_store.R

/**
 * 商家分类列表适配器
 *
 * @Author 55HAITAO
 * @Date 2019-05-09 18:33
 */
class StoreCateAdapter(list: MutableList<StoreCateItemModel>) : BaseQuickAdapter<StoreCateItemModel, BaseViewHolder>(R.layout.item_store_cate, list) {
    var curPosition = 0

    override fun convert(helper: BaseViewHolder, item: StoreCateItemModel) {
        with(item) {
            helper.setText(R.id.tv_store_cate, name)
                    .setVisible(R.id.view_line, is_selected)

            helper.getView<TextView>(R.id.tv_store_cate).isSelected = is_selected

        }
    }

    fun setSelectPosition(position: Int) {
        for (element in data) {
            if (element.is_selected) {
                element.is_selected = false
                data[position].is_selected = true
                notifyDataSetChanged()
                curPosition = position
                break
            }
        }

    }
}