package com.gocashback.module_me.activity

import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_PAYMENT_PASSWORD
import com.gocashback.lib_common.LOGIN_EXTRA
import com.gocashback.lib_common.annotation.VerifyMailCodeType.Companion.FORGETPAYMENTPWD
import com.gocashback.lib_common.annotation.VerifyMailCodeType.Companion.PAYMENTPWD
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.*
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.startVerifyMailCodeActivity
import com.gocashback.lib_common.utils.SP_KEY_PAY_VERIFY_MAIL
import com.gocashback.lib_common.utils.getLong
import com.gocashback.lib_common.utils.put
import com.gocashback.lib_common.utils.show
import com.gocashback.module_me.R
import com.gocashback.module_me.event.PaymentPwdUpdateEvent
import kotlinx.android.synthetic.main.activity_payment_password.*
import org.greenrobot.eventbus.EventBus

/**
 * @Author 55HAITAO
 * @Date 2019-06-14 16:46
 */
@Route(path = ACTIVITY_PAYMENT_PASSWORD, extras = LOGIN_EXTRA)
class PaymentPasswordActivity : GcbBaseActivity() {

    @JvmField
    @Autowired(name = "fromForget")
    var fromForget = false

    private var newEnable = false
    private var confirmNewEnable = false

    override fun setLayoutId(): Int {
        return R.layout.activity_payment_password
    }

    override fun initVars() {
        // 设置密码需要验证邮箱
        val lastTime = getLong(this, SP_KEY_PAY_VERIFY_MAIL, 0L) ?: 0L
//        val curTime = System.currentTimeMillis()
//        if (curTime - lastTime > 5 * 60 * 1000) {   // 超过5分钟需要验证
        if (lastTime <= 0) {
            startVerifyMailCodeActivity(this, if (fromForget) FORGETPAYMENTPWD else PAYMENTPWD)
            finish()
        } else {
            put(this, SP_KEY_PAY_VERIFY_MAIL, 0L)

        }
//        }

    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

    }

    override fun initEvent() {
        et_new.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                newEnable = !s.isNullOrBlank() && s.length > 5
                checkSubmitEnable()
            }
        })

        et_new_confirm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                confirmNewEnable = !s.isNullOrBlank() && s.length > 5
                checkSubmitEnable()

            }
        })

        tv_submit.setOnClickListener {
            if (et_new.getText().toString() == et_new_confirm.getText().toString()) {
                tv_submit.isEnabled = false
                createService(UserApi::class.java)
                        .paymentPwd(Base64.encodeToString(et_new.getText().toString().toByteArray(), Base64.DEFAULT).trim()
                                , Base64.encodeToString(et_new_confirm.getText().toString().toByteArray(), Base64.DEFAULT).trim())
                        .compose(ResponseTransformer.handleResult())
                        .compose(bindToLifecycle())
                        .subscribe(object : BaseObserver<Any>(this) {
                            override fun onSuccess(t: Any) {
                                show(this@PaymentPasswordActivity, R.string.payment_password_success)
                                hideSoftInput()
                                // 保存用户信息
                                getUser()?.apply {
                                    is_pay_pwd = 1
                                }?.let {
                                    setUser(it)
                                }
                                EventBus.getDefault().post(PaymentPwdUpdateEvent(fromForget))
                                finishDelay()
                            }

                            override fun onFail(code: Int, msg: String) {
                                super.onFail(code, msg)
                                tv_submit.isEnabled = true

                            }
                        })
            } else {
                show(this@PaymentPasswordActivity, R.string.change_password_not_match)

            }
        }

    }

    private fun checkSubmitEnable() {
        tv_submit.isEnabled = newEnable && confirmNewEnable
    }

}