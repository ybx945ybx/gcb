package com.gocashback.module_me.activity

import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_PAYMENT_METHOD_EDIT_PAYPAL_SELECT_AREA
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.AppConfigApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.appConfig.PaypalAreaModel
import com.gocashback.module_me.R
import com.gocashback.module_me.adapter.PayPalAreaSelectAdapter
import com.gocashback.module_me.event.PaypalAreaSelectEvent
import kotlinx.android.synthetic.main.activity_paypal_area_select.*
import org.greenrobot.eventbus.EventBus

/**
 * @Author 55HAITAO
 * @Date 2019-12-02 10:28
 */
@Route(path = ACTIVITY_PAYMENT_METHOD_EDIT_PAYPAL_SELECT_AREA)
class PayPalAreaSelectActivity : GcbBaseActivity() {

    private lateinit var payPalAreaSelectAdapter: PayPalAreaSelectAdapter

    override fun setLayoutId(): Int {
        return R.layout.activity_paypal_area_select
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        rycv_area.apply {
            layoutManager = LinearLayoutManager(this@PayPalAreaSelectActivity)
            payPalAreaSelectAdapter = PayPalAreaSelectAdapter(this@PayPalAreaSelectActivity, listOf())
            adapter = payPalAreaSelectAdapter
        }

    }

    override fun initEvent() {
        gcbSearchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (payPalAreaSelectAdapter != null) {
                    if (TextUtils.isEmpty(s)) {
                        payPalAreaSelectAdapter.filter.filter(null)
                    } else {
                        payPalAreaSelectAdapter.filter.filter(s)
                    }
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        payPalAreaSelectAdapter.setOnItemClickListener { adapter, view, position ->
            EventBus.getDefault().post(PaypalAreaSelectEvent(payPalAreaSelectAdapter.data[position]))
            finishDelay(300)
        }
    }


    override fun initData() {
        createService(AppConfigApi::class.java)
                .getCountry()
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<Map<String, String>>(this) {
                    override fun onSuccess(t: Map<String, String>) {
                        var list = arrayListOf<PaypalAreaModel>()
                        for (key in t.keys) {
                            list.add(PaypalAreaModel().apply {
                                simple_nme = key
                                name = t[key] ?: ""
                            })
                        }
                        payPalAreaSelectAdapter.setNewData(list)
                        payPalAreaSelectAdapter.setOrigionData(list)

                    }
                })
    }

    fun setSearchResultView(list: List<PaypalAreaModel>) {
        payPalAreaSelectAdapter.setNewData(list)

    }
}