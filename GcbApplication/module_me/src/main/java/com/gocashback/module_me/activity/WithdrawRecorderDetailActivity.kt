package com.gocashback.module_me.activity

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_WITHDRAWRECORD_DETAIL
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.RebateDetailIfModel
import com.gocashback.lib_common.widget.GcbStatusView
import com.gocashback.lib_common.widget.GcbStatusView.Companion.transferStatus
import com.gocashback.lib_common.widget.GcbStatusView.Companion.transferWithdrawType
import com.gocashback.module_me.R
import kotlinx.android.synthetic.main.activity_rebate_detail.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-11 19:13
 */
@Route(path = ACTIVITY_WITHDRAWRECORD_DETAIL)
class WithdrawRecorderDetailActivity : GcbBaseActivity() {

    @JvmField
    @Autowired(name = "id")
    var id = ""

    override fun setLayoutId(): Int {
        return R.layout.activity_withdraw_record_detail
    }


    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

    }

    override fun initData() {

        createService(UserApi::class.java)
                .rebateDetail(id)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<RebateDetailIfModel>(this) {
                    override fun onSuccess(t: RebateDetailIfModel) {
                        with(t) {
                            gsitv_date.setSubText(dateline)
                            gsitv_cash.setSubText(amount)
                            gsitv_status.setSubText(transferStatus(this@WithdrawRecorderDetailActivity, GcbStatusView.StatusCategory.WITHDRAW, status))
                            gsitv_type.setSubText(transferWithdrawType(this@WithdrawRecorderDetailActivity, pay_type))

                        }

                    }
                })
    }
}