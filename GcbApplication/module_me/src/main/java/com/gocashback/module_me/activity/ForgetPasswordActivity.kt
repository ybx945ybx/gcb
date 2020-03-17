package com.gocashback.module_me.activity

import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_FORGETPWD
import com.gocashback.lib_common.annotation.VerifyMailCodeType
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.LoginApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.startVerifyMailCodeActivity
import com.gocashback.lib_common.utils.*
import com.gocashback.module_me.R
import kotlinx.android.synthetic.main.activity_forget_password.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-03 16:59
 */

@Route(path = ACTIVITY_FORGETPWD)
class ForgetPasswordActivity : GcbBaseActivity() {

    private var newEnable = false
    private var confirmNewEnable = false

    override fun setLayoutId(): Int {
        return R.layout.activity_forget_password
    }

    override fun initVars() {
        // 设置密码需要验证邮箱
        val lastTime = getLong(this, SP_KEY_FORGET_VERIFY_MAIL, 0L) ?: 0L
//        val curTime = System.currentTimeMillis()
//        if (curTime - lastTime > 5 * 60 * 1000) {   // 超过5分钟需要验证

        if (lastTime <= 0) {
            startVerifyMailCodeActivity(this, VerifyMailCodeType.FORGETPWD)
            finish()
        } else {
            put(this, SP_KEY_FORGET_VERIFY_MAIL, 0L)

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
                newEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        et_new_confirm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                confirmNewEnable = !s.isNullOrBlank()
                checkSubmitEnable()

            }
        })

        tv_submit.setOnClickListener {
            createService(LoginApi::class.java)
                    .loginUpdatePwd(getString(this@ForgetPasswordActivity, SP_KEY_FORGET_MAIL, "")
                            ?: ""
                            , Base64.encodeToString(et_new.getText().toString().toByteArray(), Base64.DEFAULT).trim()
                            , Base64.encodeToString(et_new_confirm.getText().toString().toByteArray(), Base64.DEFAULT).trim()
                            , "")
                    .compose(ResponseTransformer.handleResult())
                    .compose(bindToLifecycle())
                    .subscribe(object : BaseObserver<Any>(this) {
                        override fun onSuccess(t: Any) {
                            show(this@ForgetPasswordActivity, R.string.payment_password_success)
                            hideSoftInput()
//                            EventBus.getDefault().post(PaymentPwdUpdateEvent())
                            finishDelay()
                        }
                    })
        }

    }

    private fun checkSubmitEnable() {
        tv_submit.isEnabled = newEnable && confirmNewEnable
    }
}