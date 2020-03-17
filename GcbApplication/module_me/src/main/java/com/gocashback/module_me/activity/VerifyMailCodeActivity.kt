package com.gocashback.module_me.activity

import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_VERIFY_MAIL_CODE
import com.gocashback.lib_common.annotation.VerifyMailCodeType.Companion.FORGETPAYMENTPWD
import com.gocashback.lib_common.annotation.VerifyMailCodeType.Companion.FORGETPWD
import com.gocashback.lib_common.annotation.VerifyMailCodeType.Companion.PAYMENTPWD
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.*
import com.gocashback.lib_common.network.api.IndexApi
import com.gocashback.lib_common.startForgetPwdActivity
import com.gocashback.lib_common.startPaymentPasswordActivity
import com.gocashback.lib_common.utils.*
import com.gocashback.module_me.R
import kotlinx.android.synthetic.main.avtivity_verify_mail_code.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-14 15:53
 */
@Route(path = ACTIVITY_VERIFY_MAIL_CODE)
class VerifyMailCodeActivity : GcbBaseActivity() {

    @JvmField
    @Autowired(name = "verifyMailCodeType")
    var verifyMailCodeType = 0

    private lateinit var mCountDownTimer: CountDownTimer   // 发送验证码倒计时
    private var isOnCount = false

    private var mailEnable = false
    private var codeEnable = false

    override fun setLayoutId(): Int {
        return R.layout.avtivity_verify_mail_code
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        mCountDownTimer = TimerCountDown((60 * 2000).toLong(), 1000)

        // 修改密码和设置支付密码需要验证邮箱
        // 修改密码是已登录直接把邮箱填充
        if (isLogin()) {
            et_mail.isEnabled = false
            et_mail.setText(getUser()?.email)
            mailEnable = true
        }else{
            et_mail.isEnabled = true

        }
    }

    override fun initEvent() {
        et_mail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mailEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        et_code.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                codeEnable = !s.isNullOrBlank()
                checkSubmitEnable()

            }
        })

        get_code.setOnClickListener {
            if (TextUtils.isEmpty(et_mail.text)) {
                show(this, R.string.please_enter_mail)
            } else {
                createService(IndexApi::class.java)
                        .sendValidateCode(et_mail.text.toString())
                        .compose(ResponseTransformer.handleResult())
                        .compose(bindToLifecycle())
                        .subscribe(object : BaseObserver<Any>(this) {
                            override fun onSuccess(t: Any) {
                                startCountDown()
                                show(this@VerifyMailCodeActivity, R.string.code_send_success)

                            }
                        })
            }
        }

        tv_submit.setOnClickListener {
            createService(IndexApi::class.java)
                    .verificationMailCode(et_mail.text.toString(), et_code.text.toString())
                    .compose(ResponseTransformer.handleResult())
                    .compose(bindToLifecycle())
                    .subscribe(object : BaseObserver<Any>(this) {
                        override fun onSuccess(t: Any) {
                            hideSoftInput()
                            when (verifyMailCodeType) {
                                PAYMENTPWD -> {
                                    put(this@VerifyMailCodeActivity, SP_KEY_PAY_VERIFY_MAIL, System.currentTimeMillis())
                                    startPaymentPasswordActivity(this@VerifyMailCodeActivity)
                                }
                                FORGETPAYMENTPWD -> {
                                    put(this@VerifyMailCodeActivity, SP_KEY_PAY_VERIFY_MAIL, System.currentTimeMillis())
                                    startPaymentPasswordActivity(this@VerifyMailCodeActivity, true)
                                }
//                                UPDATEPWD -> {
//                                    put(this@VerifyMailCodeActivity, SP_KEY_UPDATE_VERIFY_MAIL, System.currentTimeMillis())
//                                    startUpdatePwdActivity(this@VerifyMailCodeActivity)
//                                }
                                FORGETPWD -> {
                                    put(this@VerifyMailCodeActivity, SP_KEY_FORGET_VERIFY_MAIL, System.currentTimeMillis())
                                    put(this@VerifyMailCodeActivity, SP_KEY_FORGET_MAIL, et_mail.text.toString())
                                    startForgetPwdActivity(this@VerifyMailCodeActivity)
                                }

                            }
                            finish()
                        }
                    })
        }

    }

    private fun checkSubmitEnable() {
        tv_submit.isEnabled = mailEnable && codeEnable
    }

    /**
     * 发送验证码倒计时
     */
    private fun startCountDown() {
        mCountDownTimer.start()
        isOnCount = true
    }

    internal inner class TimerCountDown(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
            get_code.isEnabled = false
            get_code.text = resources.getString(R.string.verify_mail_get_code_again) + " " + millisUntilFinished / 1000 + "s"

        }

        override fun onFinish() {
            isOnCount = false
            get_code.isEnabled = true
            get_code.text = resources.getString(R.string.verify_mail_get_code)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel()
        }

    }
}