package com.gocashback.module_me.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.network.model.user.InviteItemModel
import com.gocashback.module_me.R

/**
 * @Author 55HAITAO
 * @Date 2019-06-25 11:27
 */
class InviteMyReferralsAdapter(list: List<InviteItemModel>) : BaseQuickAdapter<InviteItemModel, BaseViewHolder>(R.layout.item_invite_my_referrals, list) {
    override fun convert(helper: BaseViewHolder, item: InviteItemModel) {
        with(item) {
            helper.setText(R.id.tv_name, email)
                    .setText(R.id.tv_time, reg_time)
                    .setText(R.id.tv_award, award)

//            loadImage(originUrl = avatar, targetView = helper.getView(R.id.iv_avatar), circle = true)
        }
    }
}