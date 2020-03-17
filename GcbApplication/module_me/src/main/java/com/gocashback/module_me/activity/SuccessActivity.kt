package com.gocashback.module_me.activity

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_SUCCESS
import com.gocashback.lib_common.annotation.SuccessType
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.module_me.R
import kotlinx.android.synthetic.main.activity_success.*

/**
 * @Author 55HAITAO
 * @Date 2019-07-07 15:40
 */
@Route(path = ACTIVITY_SUCCESS)
class SuccessActivity : GcbBaseActivity() {

    @JvmField
    @Autowired(name = "successType")
    var successType = 0

    override fun setLayoutId(): Int {
        return R.layout.activity_success
    }

    override fun initViews() {
        getImmersionBar()!!.fitsSystemWindows(true).statusBarColor(com.gocashback.lib_common.R.color.white).statusBarDarkFont(true).init()

        when (successType) {
            SuccessType.WITHDRAW -> {
                gcbHeadView.setCenterText(resources.getString(R.string.withdraw))
                tv_success_title.text = resources.getString(R.string.success_withdraw_title)
                tv_success_content.text = resources.getString(R.string.success_withdraw_content)
                tv_button.text = resources.getString(R.string.success_withdraw_button)
                tv_button.setOnClickListener { finish() }
            }
            SuccessType.GIFTCARD -> {
                gcbHeadView.setCenterText(resources.getString(R.string.gift_cards))
                tv_success_title.text = resources.getString(R.string.success_giftcard_title)
                tv_success_content.text = resources.getString(R.string.success_giftcard_content)
                tv_button.text = resources.getString(R.string.success_giftcard_button)
                tv_button.setOnClickListener {
//                    startRedeemGiftCardActivity(this)
                    finish()
                }
            }
            SuccessType.FINDORDER -> {
                gcbHeadView.setCenterText(resources.getString(R.string.withdraw))
                tv_success_title.text = resources.getString(R.string.success_withdraw_title)
                tv_success_content.text = resources.getString(R.string.success_withdraw_content)
                tv_button.text = resources.getString(R.string.success_withdraw_button)
                tv_button.setOnClickListener { finish() }
            }
        }
    }
}