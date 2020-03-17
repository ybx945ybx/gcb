package com.gocashback.module_me.activity

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_MYREBATE_DETAIL
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.RebateDetailIfModel
import com.gocashback.lib_common.utils.moneyFormat
import com.gocashback.lib_common.widget.GcbStatusView.Companion.transferRebateType
import com.gocashback.lib_common.widget.GcbStatusView.Companion.transferStatus
import com.gocashback.lib_common.widget.GcbStatusView.StatusCategory.Companion.REBATE
import com.gocashback.module_me.R
import kotlinx.android.synthetic.main.activity_rebate_detail.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-06 15:58
 */
@Route(path = ACTIVITY_MYREBATE_DETAIL)
class RebateDetailActivity : GcbBaseActivity() {

    @JvmField
    @Autowired(name = "id")
    var id = ""

    override fun setLayoutId(): Int {
        return R.layout.activity_rebate_detail
    }

    override fun initVars() {


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
                            gsitv_cash.setSubText(moneyFormat(amount))
                            gsitv_status.setSubText(transferStatus(this@RebateDetailActivity, REBATE, status))
                            gsitv_type.setSubText(transferRebateType(this@RebateDetailActivity, type))

                        }

                    }
                })
    }
}