package com.gocashback.module_me.activity

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_MYORDER_DETAIL
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.OrderDetailIfModel
import com.gocashback.lib_common.utils.dateFormat
import com.gocashback.lib_common.utils.moneyFormat
import com.gocashback.lib_common.widget.GcbStatusView
import com.gocashback.module_me.R
import kotlinx.android.synthetic.main.activity_order_detail.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-10 13:56
 */
@Route(path = ACTIVITY_MYORDER_DETAIL)
class OrderDetailActivity : GcbBaseActivity() {
    @JvmField
    @Autowired(name = "id")
    var id = ""

    override fun setLayoutId(): Int {
        return R.layout.activity_order_detail
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

    }

    override fun initData() {

        createService(UserApi::class.java)
                .orderDetail(id)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<OrderDetailIfModel>(this) {
                    override fun onSuccess(t: OrderDetailIfModel) {
                        with(t) {
                            gsitv_order_id.setSubText(order_id)
                            gsitv_store.setSubText(store)
                            gsitv_amount.setSubText(moneyFormat(amount))
                            gsitv_rebate.setSubText(moneyFormat(rebate))
                            gsitv_date.setSubText(dateFormat(dateline*1000))
                            gsitv_status.setSubText(GcbStatusView.transferStatus(this@OrderDetailActivity, GcbStatusView.StatusCategory.ORDER, status))

                        }

                    }
                })
    }
}