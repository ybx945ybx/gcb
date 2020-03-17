package com.gocashback.module_me.activity

import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_LOGIN
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.event.LoginChangeEvent
import com.gocashback.lib_common.model.TransferExtraIfModel
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.LoginApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.login.LoginIfModel
import com.gocashback.lib_common.network.model.user.UserIfModel
import com.gocashback.lib_common.network.setUser
import com.gocashback.lib_common.startForgetPwdActivity
import com.gocashback.lib_common.startRegisterActivity
import com.gocashback.lib_common.startWebviewActivity
import com.gocashback.lib_common.utils.isEmail
import com.gocashback.lib_common.utils.show
import com.gocashback.lib_common.widget.GcbHeadView
import com.gocashback.module_me.R
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * @Author 55HAITAO
 * @Date 2019-05-22 20:24
 */
@Route(path = ACTIVITY_LOGIN)
class LoginActivity : GcbBaseActivity() {

    @JvmField
    @Autowired(name = "transferExtraIfModel")
    var transferExtraIfModel: TransferExtraIfModel? = null

    override fun setLayoutId(): Int {
        return R.layout.activity_login
    }


    override fun initVars() {
        registerEventBus()
    }


    override fun isImmersionBarEnabled(): Boolean {
        return false
    }
//    override fun initViews() {
//        getImmersionBar()?.fitsSystemWindows(true)?.transparentStatusBar()?.statusBarDarkFont(true)?.init()
//
//    }

    override fun initEvent() {
        gcbhv.onRightClickListener = object : GcbHeadView.OnRightClickListener {
            override fun onRightClick() {
                startRegisterActivity(this@LoginActivity)

            }
        }
        tv_login.setOnClickListener { doLogin() }
        tv_forget.setOnClickListener { startForgetPwdActivity(this) }

        getv_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    tv_login.isEnabled = false
                } else {
                    tv_login.isEnabled = getv_password.getText().isNotBlank()
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
                    tv_login.isEnabled = false
                } else {
                    tv_login.isEnabled = getv_email.getText().isNotBlank()
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

    private fun doLogin() {
        if (!isEmail(getv_email.getText().toString())) {
            show(this, resources.getString(R.string.not_email))
            return
        }
        createService(LoginApi::class.java)
                .login("1"
                        , getv_email.getText().toString()
                        , Base64.encodeToString(getv_password.getText().toString().toByteArray(), Base64.DEFAULT).trim()
                        , "", "")
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
                        // 是去购买跳转过来的 登录后继续跳转
                        transferExtraIfModel?.run {
                            startWebviewActivity(this@LoginActivity, jump_url, this)
                        }
                        EventBus.getDefault().post(LoginChangeEvent())

                    }

//                    override fun onFail(code: Int, msg: String) {
//                        gcbhv.post { show(this@LoginActivity, msg) }
//
//                    }

                })
    }

    @Subscribe
    fun onLoginChange(loginChangeEvent: LoginChangeEvent) {
        finish()
    }
}