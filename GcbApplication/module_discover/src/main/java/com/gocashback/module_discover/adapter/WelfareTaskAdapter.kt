package com.gocashback.module_discover.adapter

import android.app.Activity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.language.LOCALE_CHINESE
import com.gocashback.lib_common.language.getLanguage
import com.gocashback.lib_common.language.getLocale
import com.gocashback.module_discover.R
import com.gocashback.module_discover.model.WelfareTaskItemModel

/**
 * @Author 55HAITAO
 * @Date 2019-08-01 15:05
 */
class WelfareTaskAdapter(private val mActivity: Activity?, list: List<WelfareTaskItemModel>) : BaseQuickAdapter<WelfareTaskItemModel, BaseViewHolder>(R.layout.item_welfare_task, list) {
    override fun convert(helper: BaseViewHolder, item: WelfareTaskItemModel) {
        with(item) {
            helper.setVisible(R.id.iv_task_foreground, isFinish)
                    .setImageResource(R.id.iv_task_foreground, if (LOCALE_CHINESE == getLanguage(mActivity)) R.mipmap.ic_welfare_foreground else R.mipmap.ic_welfare_foreground_en)

            when (type) {
                0 -> {
                    helper.setText(R.id.tv_task_title, mActivity?.resources?.getString(R.string.welfare_task_1))
                            .setText(R.id.tv_task_finish, if (isFinish) mActivity?.resources?.getString(R.string.welfare_task_finish) else mActivity?.resources?.getString(R.string.welfare_task_go_1))
                            .setVisible(R.id.iv_task_go, !isFinish)
                            .setImageResource(R.id.iv_task_type, R.mipmap.ic_welfare_register)

                }
                1 -> {
                    helper.setText(R.id.tv_task_title, mActivity?.resources?.getString(R.string.welfare_task_2))
                            .setText(R.id.tv_task_finish, if (isFinish) mActivity?.resources?.getString(R.string.welfare_task_finish) else mActivity?.resources?.getString(R.string.welfare_task_go_2))
                            .setVisible(R.id.iv_task_go, !isFinish)
                            .setImageResource(R.id.iv_task_type, R.mipmap.ic_welfare_shop)
                }
                2 -> {
                    helper.setText(R.id.tv_task_title, mActivity?.resources?.getString(R.string.welfare_task_3))
                            .setText(R.id.tv_task_finish, if (isFinish) mActivity?.resources?.getString(R.string.welfare_task_finish) else mActivity?.resources?.getString(R.string.welfare_task_go_3))
                            .setVisible(R.id.iv_task_go, !isFinish)
                            .setImageResource(R.id.iv_task_type, R.mipmap.ic_welfare_invite)
                }
                else -> {
                }
            }
        }

    }
}