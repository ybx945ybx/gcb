package com.gocashback.module_me.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_PAYMENT_METHOD_EDIT_ALI
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.PaymentIfModel
import com.gocashback.lib_common.utils.show
import com.gocashback.module_me.R
import com.gocashback.module_me.event.PaymentMethodUpdateEvent
import kotlinx.android.synthetic.main.activity_payment_method_edit_ali.*
import org.greenrobot.eventbus.EventBus

/**
 * @Author 55HAITAO
 * @Date 2019-06-13 20:51
 */
@Route(path = ACTIVITY_PAYMENT_METHOD_EDIT_ALI)
class PaymentMethodEditAliActivity : GcbBaseActivity() {

    @JvmField
    @Autowired(name = "paymentIfModel")
    internal var paymentIfModel: PaymentIfModel? = null

    private var nameEnable = false
    private var accountEnable = false

    override fun setLayoutId(): Int {
        return R.layout.activity_payment_method_edit_ali
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        paymentIfModel?.run {
            gpmev_ali_name.setText(if (TextUtils.isEmpty(alipay_name)) "" else alipay_name)
            gpmev_ali_account.setText(if (TextUtils.isEmpty(alipay)) "" else alipay)
            tv_submit.isEnabled = true
        }
    }

    override fun initEvent() {
        gpmev_ali_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                nameEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        gpmev_ali_account.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                accountEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        tv_submit.setOnClickListener {
            hideSoftInput()

            createService(UserApi::class.java)
                    .paymentMethodAliUpdate(5, gpmev_ali_account.getText(), gpmev_ali_name.getText())
                    .compose(ResponseTransformer.handleResult())
                    .compose(bindToLifecycle())
                    .subscribe(object : BaseObserver<Any>(this) {
                        override fun onSuccess(t: Any) {
                            show(this@PaymentMethodEditAliActivity, R.string.payment_method_edit_success)
                            EventBus.getDefault().post(PaymentMethodUpdateEvent())
                            finish()

                        }
                    })
        }
    }

    private fun checkSubmitEnable() {
        tv_submit.isEnabled = nameEnable && accountEnable

    }
}