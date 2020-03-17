package com.gocashback.module_me.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_PAYMENT_METHOD_EDIT_CHECK
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.PaymentIfModel
import com.gocashback.lib_common.utils.show
import com.gocashback.module_me.R
import com.gocashback.module_me.event.PaymentMethodUpdateEvent
import kotlinx.android.synthetic.main.activity_payment_method_edit_check.*
import org.greenrobot.eventbus.EventBus

/**
 * @Author 55HAITAO
 * @Date 2019-06-13 20:53
 */
@Route(path = ACTIVITY_PAYMENT_METHOD_EDIT_CHECK)
class PaymentMethodEditCheckActivity : GcbBaseActivity() {

    @JvmField
    @Autowired(name = "paymentIfModel")
    internal var paymentIfModel: PaymentIfModel? = null

    private var firstEnable = false
    private var lastEnable = false
    private var streetEnable = false
    private var streetTwoEnable = true   //  这个是选填
    private var cityEnable = false
    private var stateEnable = false
    private var zipcodeEnable = false

    override fun setLayoutId(): Int {
        return R.layout.activity_payment_method_edit_check
    }

    override fun isImmersionBarEnabled(): Boolean {
        return false
    }

    override fun initViews() {
//        getImmersionBar()?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        paymentIfModel?.run {
            gpmev_check_first.setText(if (TextUtils.isEmpty(firstname)) "" else firstname)
            gpmev_check_last.setText(if (TextUtils.isEmpty(lastname)) "" else lastname)
            gpmev_check_street.setText(if (TextUtils.isEmpty(street)) "" else street)
            gpmev_check_street_two.setText(if (TextUtils.isEmpty(street_two)) "" else street_two)
            gpmev_check_city.setText(if (TextUtils.isEmpty(city)) "" else city)
            gpmev_check_state.setText(if (TextUtils.isEmpty(state)) "" else state)
            gpmev_check_zipcode.setText(if (TextUtils.isEmpty(post)) "" else post)
            tv_submit.isEnabled = true

        }
    }

    override fun initEvent() {
        gpmev_check_first.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                firstEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        gpmev_check_last.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lastEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        gpmev_check_street.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                streetEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        gpmev_check_street_two.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                streetTwoEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        gpmev_check_city.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cityEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        gpmev_check_state.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                stateEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        gpmev_check_zipcode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                zipcodeEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        tv_submit.setOnClickListener {
            hideSoftInput()

            createService(UserApi::class.java)
                    .paymentMethodCheckUpdate(3
                            , gpmev_check_zipcode.getText()
                            , gpmev_check_city.getText()
                            , gpmev_check_state.getText()
                            , gpmev_check_street.getText()
                            , gpmev_check_street_two.getText()
                            , gpmev_check_last.getText()
                            , gpmev_check_first.getText()
                    )
                    .compose(ResponseTransformer.handleResult())
                    .compose(bindToLifecycle())
                    .subscribe(object : BaseObserver<Any>(this) {
                        override fun onSuccess(t: Any) {
                            show(this@PaymentMethodEditCheckActivity, R.string.payment_method_edit_success)
                            EventBus.getDefault().post(PaymentMethodUpdateEvent())
                            finish()

                        }
                    })
        }
    }

    private fun checkSubmitEnable() {
        tv_submit.isEnabled = firstEnable && lastEnable && streetEnable && streetTwoEnable && cityEnable && stateEnable && zipcodeEnable

    }
}