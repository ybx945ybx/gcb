package com.gocashback.module_me.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_PAYMENT_METHOD_EDIT_PAYPAL
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.PaymentIfModel
import com.gocashback.lib_common.startPaypalSelectAreaActivity
import com.gocashback.lib_common.utils.isEmail
import com.gocashback.lib_common.utils.show
import com.gocashback.module_me.R
import com.gocashback.module_me.event.PaymentMethodUpdateEvent
import com.gocashback.module_me.event.PaypalAreaSelectEvent
import kotlinx.android.synthetic.main.activity_payment_method_edit_paypal.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * @Author 55HAITAO
 * @Date 2019-06-13 20:52
 */
@Route(path = ACTIVITY_PAYMENT_METHOD_EDIT_PAYPAL)
class PaymentMethodEditPaypalActivity : GcbBaseActivity() {

    @JvmField
    @Autowired(name = "paymentIfModel")
    internal var paymentIfModel: PaymentIfModel? = null

    var firstEnable = false
    var lastEnable = false
    var accountEnable = false
    var areaEnable = false

    override fun setLayoutId(): Int {
        return R.layout.activity_payment_method_edit_paypal
    }


    override fun initVars() {
        registerEventBus()
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()
        gpmev_paypal_area.setEditEnable(false)

        paymentIfModel?.run {
            firstEnable = !TextUtils.isEmpty(paypal_firstname)
            gpmev_paypal_first_name.setText(if (firstEnable) paypal_firstname else "")

            lastEnable = !TextUtils.isEmpty(paypal_lastname)
            gpmev_paypal_last_name.setText(if (lastEnable) paypal_lastname else "")

            accountEnable = !TextUtils.isEmpty(paypal)
            gpmev_paypal_account.setText(if (accountEnable) paypal else "")

            areaEnable = !TextUtils.isEmpty(paypal_area)
            gpmev_paypal_area.setText(if (areaEnable) paypal_area else "")

            checkSubmitEnable()
        }

    }

    override fun initEvent() {
        gpmev_paypal_first_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                firstEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        gpmev_paypal_last_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lastEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        gpmev_paypal_account.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                accountEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        gpmev_paypal_area.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                areaEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        gpmev_paypal_area.setOnClickListener {
            startPaypalSelectAreaActivity(this)
        }

        tv_submit.setOnClickListener {
            if (!isEmail(gpmev_paypal_account.getText())) {
                show(this, resources.getString(R.string.not_email))
                return@setOnClickListener
            }
            hideSoftInput()

            createService(UserApi::class.java)
                    .paymentMethodPaypalUpdate(1
                            , gpmev_paypal_first_name.getText()
                            , gpmev_paypal_last_name.getText()
                            , gpmev_paypal_account.getText()
                            , gpmev_paypal_area.getText())
                    .compose(ResponseTransformer.handleResult())
                    .compose(bindToLifecycle())
                    .subscribe(object : BaseObserver<Any>(this) {
                        override fun onSuccess(t: Any) {
                            show(this@PaymentMethodEditPaypalActivity, R.string.payment_method_edit_success)
                            EventBus.getDefault().post(PaymentMethodUpdateEvent())
                            finish()

                        }
                    })
        }
    }

    private fun checkSubmitEnable() {
        tv_submit.isEnabled = firstEnable && lastEnable && accountEnable && areaEnable
    }


    @Subscribe
    fun onAreaSelect(paypalAreaSelectEvent: PaypalAreaSelectEvent) {
        gpmev_paypal_area.setText(paypalAreaSelectEvent.paypalAreaModel.name)
    }
}