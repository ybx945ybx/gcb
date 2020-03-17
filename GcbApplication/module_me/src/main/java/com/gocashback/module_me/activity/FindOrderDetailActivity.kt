package com.gocashback.module_me.activity

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_FINDORDER_DETAIL
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.LostOrderDetailIfModel
import com.gocashback.lib_common.widget.GcbStatusView
import com.gocashback.module_me.R
import kotlinx.android.synthetic.main.activity_order_detail.*

/**
 * 丢单详情
 *
 * @Author 55HAITAO
 * @Date 2019-06-10 15:40
 */
@Route(path = ACTIVITY_FINDORDER_DETAIL)
class FindOrderDetailActivity : GcbBaseActivity() {
    @JvmField
    @Autowired(name = "id")
    var id = ""

    override fun setLayoutId(): Int {
        return R.layout.activity_find_order_detail
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

    }

    override fun initData() {
        createService(UserApi::class.java)
                .lostOrderDetail(id)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<LostOrderDetailIfModel>(this) {
                    override fun onSuccess(t: LostOrderDetailIfModel) {
                        with(t) {
                            gsitv_order_id.setSubText(order_id)
                            gsitv_store.setSubText(store)
                            gsitv_amount.setSubText(amount)
                            gsitv_date.setSubText(order_date)
//                            gsitv_date.setSubText(dateFormat(inputtime*1000))
                            gsitv_status.setSubText(GcbStatusView.transferStatus(this@FindOrderDetailActivity, GcbStatusView.StatusCategory.FINDORDER, status))

                        }

                    }
                })
    }
}