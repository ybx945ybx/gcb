package com.gocashback.module_me.activity

import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_REGISTER
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.event.LoginChangeEvent
import com.gocashback.lib_common.language.LOCALE_CHINESE
import com.gocashback.lib_common.language.getLanguage
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.LoginApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.login.LoginIfModel
import com.gocashback.lib_common.network.model.user.UserIfModel
import com.gocashback.lib_common.network.setUser
import com.gocashback.lib_common.startLoginActivity
import com.gocashback.lib_common.startWebviewActivity
import com.gocashback.lib_common.utils.SP_KEY_INVITE_REWARD
import com.gocashback.lib_common.utils.isEmail
import com.gocashback.lib_common.utils.show
import com.gocashback.lib_common.widget.GcbHeadView
import com.gocashback.module_me.R
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus

/**
 * @Author 55HAITAO
 * @Date 2019-05-22 20:34
 */
@Route(path = ACTIVITY_REGISTER)
class RegisterActivity : GcbBaseActivity() {

    override fun setLayoutId(): Int {
        return R.layout.activity_register
    }

    override fun isImmersionBarEnabled(): Boolean {
        return false
    }

    override fun initViews() {
//        getImmersionBar()?.fitsSystemWindows(true)?.transparentStatusBar()?.statusBarDarkFont(true)?.init()

        tv_friend_code.text = String.format(getString(R.string.show_friend_code),
                com.gocashback.lib_common.utils.getString(this, SP_KEY_INVITE_REWARD, ""))
    }

    override fun initEvent() {
        gcbhv.onRightClickListener = object : GcbHeadView.OnRightClickListener {
            override fun onRightClick() {
                startLoginActivity(this@RegisterActivity)

            }
        }
        tv_friend_code.setOnClickListener {
            if (tv_friend_code.isSelected) {
                tv_friend_code.isSelected = false
                getv_friend_code.visibility = View.GONE
            } else {
                tv_friend_code.isSelected = true
                getv_friend_code.visibility = View.VISIBLE
            }
        }
        tv_register.setOnClickListener { doRegister() }
        tv_terms_conditions.setOnClickListener { startWebviewActivity(this, if (getLanguage(this) == LOCALE_CHINESE) "https://m.gocashback.com/zh-hans/help/terms" else "https://m.gocashback.com/help/terms") }

        getv_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    tv_register.isEnabled = false
                } else {
                    tv_register.isEnabled = getv_password.getText().isNotBlank()
                }
            }
        })

        getv_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    tv_register.isEnabled = false
                } else {
                    tv_register.isEnabled = getv_email.getText().isNotBlank()
                }
            }
        })
//        getv_vertification.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//        })

    }

//    private fun getVerify() {
//        createService(LoginApi::class.java)
//                .loginVerify()
//                .compose(ResponseTransformer.handleResult())
//                .compose(bindToLifecycle())
//                .subscribe(object : BaseObserver<LoginVerifyIfModel>(this) {
//                    override fun onSuccess(t: LoginVerifyIfModel) {
////                        loadImage(t.url, targetView = iv_verify)
//
//                    }
//                })
//    }

    private fun doRegister() {
        if (!isEmail(getv_email.getText().toString())) {
            show(this, resources.getString(R.string.not_email))
            return
        }
        createService(LoginApi::class.java)
                .login("2"
                        , getv_email.getText().toString()
                        , Base64.encodeToString(getv_password.getText().toString().toByteArray(), Base64.DEFAULT).trim()
                        , "", getv_friend_code.getText().toString())
                .compose(ResponseTransformer.handleLoginResult())
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<LoginIfModel>(this) {
                    override fun onSuccess(t: LoginIfModel) {
                        hideSoftInput()
                        // 保存用户信息
                        UserIfModel().apply {
                            uid = t.uid
                            user_name = t.user_name
                            email = t.user_email
                        }.let {
                            setUser(it)
                        }

                        EventBus.getDefault().post(LoginChangeEvent())
                        finish()
                    }

//                    override fun onFail(code: Int, msg: String) {
//                        show(this@RegisterActivity, msg)
//
//                    }
                })

    }
}