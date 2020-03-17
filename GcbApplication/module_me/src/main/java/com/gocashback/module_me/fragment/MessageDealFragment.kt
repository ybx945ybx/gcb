package com.gocashback.module_me.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.FRAGMENT_MESSAGE_DEAL
import com.gocashback.lib_common.base.GcbBaseFragment
import com.gocashback.module_me.R

/**
 * @Author 55HAITAO
 * @Date 2019-06-12 16:03
 */
@Route(path = FRAGMENT_MESSAGE_DEAL)
class MessageDealFragment : GcbBaseFragment() {
    override fun setLayoutId(): Int {
        return R.layout.fragment_message_deal
    }
}