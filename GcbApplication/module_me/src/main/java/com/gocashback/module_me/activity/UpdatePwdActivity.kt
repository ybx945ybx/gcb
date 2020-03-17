package com.gocashback.module_me.activity

import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_UPDATEPWD
import com.gocashback.lib_common.LOGIN_EXTRA
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.event.LoginChangeEvent
import com.gocashback.lib_common.network.*
import com.gocashback.lib_common.network.api.LoginApi
import com.gocashback.lib_common.utils.show
import com.gocashback.module_me.R
import kotlinx.android.synthetic.main.activity_update_pwd.*
import org.greenrobot.eventbus.EventBus

/**
 * @Author 55HAITAO
 * @Date 2019-06-19 15:03
 */
@Route(path = ACTIVITY_UPDATEPWD, extras = LOGIN_EXTRA)
class UpdatePwdActivity : GcbBaseActivity() {

    private var oldEnable = false
    private var newEnable = false
    private var confirmNewEnable = false

    override fun setLayoutId(): Int {
        return R.layout.activity_update_pwd
    }

    override fun initVars() {
        // 设置密码需要验证邮箱
//        val lastTime = getLong(this, SP_KEY_FORGET_VERIFY_MAIL, 0) ?: 0
//        val curTime = System.currentTimeMillis()
//        if (curTime - lastTime > 5 * 60 * 1000) {   // 超过5分钟需要验证
//            startVerifyMailCodeActivity(this, VerifyMailCodeType.FORGETPWD)
//            finish()
//
//        }

    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

    }

    override fun initEvent() {
        et_old.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                oldEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })
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
                createService(LoginApi::class.java)
                        .loginUpdatePwd(getUser()?.email ?: ""
                                , Base64.encodeToString(et_new.getText().toString().toByteArray(), Base64.DEFAULT).trim()
                                , Base64.encodeToString(et_new_confirm.getText().toString().toByteArray(), Base64.DEFAULT).trim()
                                , Base64.encodeToString(et_old.getText().toString().toByteArray(), Base64.DEFAULT).trim()
                        )
                        .compose(ResponseTransformer.handleResult())
                        .compose(bindToLifecycle())
                        .subscribe(object : BaseObserver<Any>(this) {
                            override fun onSuccess(t: Any) {
                                show(this@UpdatePwdActivity, R.string.settings_change_password_success)
                                hideSoftInput()
                                clearUser()
                                EventBus.getDefault().post(LoginChangeEvent(false))
                                finishDelay()
                            }

                            override fun onFail(code: Int, msg: String) {
                                super.onFail(code, msg)
                                tv_submit.isEnabled = true

                            }
                        })
            } else {
                show(this@UpdatePwdActivity, R.string.change_password_not_match)

            }
        }

    }

    private fun checkSubmitEnable() {
        tv_submit.isEnabled = oldEnable && newEnable && confirmNewEnable
    }
}