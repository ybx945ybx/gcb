package com.gocashback.module_store.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_TRANSFER
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.module_store.R

/**
 * @Author 55HAITAO
 * @Date 2019-06-18 16:34
 */
@Route(path = ACTIVITY_TRANSFER)
class GoBuyTransferActivity : GcbBaseActivity() {
    override fun setLayoutId(): Int {
        return R.layout.activity_go_buy_transfer
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

    }
}