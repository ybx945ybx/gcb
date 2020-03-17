package com.gocashback.module_me.activity

import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.*
import com.gocashback.lib_common.annotation.SuccessType
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.imageload.loadMipmapImg
import com.gocashback.lib_common.language.LOCALE_CHINESE
import com.gocashback.lib_common.language.getLanguage
import com.gocashback.lib_common.language.getLocale
import com.gocashback.lib_common.model.interfaces.IpaymentModel
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.getUser
import com.gocashback.lib_common.network.model.user.HeliPaymentIfModel
import com.gocashback.lib_common.network.model.user.PaymentIfModel
import com.gocashback.lib_common.network.model.user.PaymentMethodIfModel
import com.gocashback.lib_common.utils.getFuzzyString
import com.gocashback.lib_common.utils.moneyFormat
import com.gocashback.lib_common.widget.BsSelectPaymentMethodDlg
import com.gocashback.lib_common.widget.CheckPwdDlg
import com.gocashback.lib_common.widget.GcbConfirmDlg
import com.gocashback.module_me.R
import com.gocashback.module_me.event.PaymentMethodUpdateEvent
import com.gocashback.module_me.event.PaymentPwdUpdateEvent
import com.gocashback.module_me.event.WithdrawSuccessEvent
import kotlinx.android.synthetic.main.activity_withdraw.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * @Author 55HAITAO
 * @Date 2019-06-19 15:51
 */
@Route(path = ACTIVITY_WITHDRAW, extras = LOGIN_EXTRA)
class WithdrawActivity : GcbBaseActivity() {

    private var paymentIfModel: PaymentIfModel? = null
    private var heliPaymentList: List<HeliPaymentIfModel>? = listOf()

    private lateinit var paymentList: ArrayList<IpaymentModel>     // 选择提现方式列表

    private lateinit var setPaymentPwdDlg: GcbConfirmDlg
    private lateinit var repairPaymentPayDlg: GcbConfirmDlg
    private lateinit var checkPwdDlg: CheckPwdDlg
    private lateinit var bsSelectPaymentMethodDlg: BsSelectPaymentMethodDlg
//    private var available = 0

    private var amountEnable = false
    private var paymentMethodEnable = false

    private var paymentType = -1   //1paypal, 3check, 5alipay，6giftCard 7heli
    private var lowWithdrawLimit = "20"
    private var heliPayId = ""
//    private var aliEnable = false
//    private var checkEnable = false
//    private var paypalEnable = false

    override fun setLayoutId(): Int {
        return R.layout.activity_withdraw
    }

    override fun initVars() {
        registerEventBus()

    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        iv_gift_card.setImageResource(if (getLanguage(this) == LOCALE_CHINESE) R.mipmap.ic_gift_card else R.mipmap.ic_gift_card_en)
        et_payment_amount.hint = resources.getString(R.string.withdraw_payable) + " " + moneyFormat(getUser()?.available
                ?: "")
    }

    override fun initEvent() {
        iv_gift_card.setOnClickListener {
            startRedeemGiftCardActivity(this@WithdrawActivity)
        }

        llyt_payment_method.setOnClickListener {
            bsSelectPaymentMethodDlg = BsSelectPaymentMethodDlg(this@WithdrawActivity, paymentList,
                    object : BsSelectPaymentMethodDlg.OnDlgItemClickListener {
                        override fun onDlgItemClick(ipaymentModel: IpaymentModel) {
                            if (ipaymentModel is HeliPaymentIfModel) {
                                paymentType = 7
                                heliPayId = ipaymentModel.id
                                et_payment_method.setText(ipaymentModel.name + " · " + getFuzzyString(ipaymentModel.bank_account_no))
                                loadImage(originUrl = ipaymentModel.icon, targetView = iv_payment)
                            }
                            if (ipaymentModel is PaymentIfModel) {
                                ipaymentModel.run {
                                    if (paymentType == 1) {
                                        et_payment_method.setText(resources.getString(com.gocashback.lib_common.R.string.payment_method_paypal) + " · " + getFuzzyString(paypal))
                                        loadMipmapImg(R.mipmap.ic_pay_type_paypal, iv_payment)

                                    } else {
                                        et_payment_method.setText(resources.getString(com.gocashback.lib_common.R.string.payment_method_check) + " · " + getFuzzyString(firstname + lastname))
                                        loadMipmapImg(R.mipmap.ic_pay_type_check, iv_payment)

                                    }
                                }

                            }

                        }
                    })
            bsSelectPaymentMethodDlg.show()
        }

        et_payment_method.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                paymentMethodEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        et_payment_amount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(s)) {
                    amountEnable = false
                    tv_tips.text = String.format(resources.getString(R.string.withdraw_amount_tips), lowWithdrawLimit)
                    tv_tips.setTextColor(ContextCompat.getColor(this@WithdrawActivity, R.color.grey666666))
                } else {
                    if (java.lang.Float.valueOf(s.toString()) < java.lang.Float.valueOf(lowWithdrawLimit)) {
                        amountEnable = false
                        tv_tips.text = String.format(resources.getString(R.string.withdraw_amount_error_tips), lowWithdrawLimit)
                        tv_tips.setTextColor(ContextCompat.getColor(this@WithdrawActivity, R.color.redFF4E4E))
                    } else {
                        amountEnable = true
                        tv_tips.text = String.format(resources.getString(R.string.withdraw_amount_tips), lowWithdrawLimit)
                        tv_tips.setTextColor(ContextCompat.getColor(this@WithdrawActivity, R.color.grey666666))

                    }
                }
                checkSubmitEnable()

            }
        })

//        clyt_check.setOnClickListener {
//
//            if (iv_check_select.isSelected) {
//                iv_check_select.isSelected = false
//                checkEnable = false
//            } else {
//                if (TextUtils.isEmpty(tv_payment_check_account.text)) {
//                    goPaymentEdit(2)
//                } else {
//                    aliEnable = false
//                    paypalEnable = false
//                    checkEnable = true
//                    iv_check_select.isSelected = true
//                    iv_ali_select.isSelected = false
//                    iv_palpay_select.isSelected = false
//
//                }
//            }
//            checkSubmitEnable()
//
//        }
//        clyt_ali.setOnClickListener {
//            if (iv_ali_select.isSelected) {
//                iv_ali_select.isSelected = false
//                aliEnable = false
//            } else {
//                if (TextUtils.isEmpty(tv_payment_ali_account.text)) {
//                    goPaymentEdit(1)
//                } else {
//                    aliEnable = true
//                    paypalEnable = false
//                    checkEnable = false
//                    iv_ali_select.isSelected = true
//                    iv_check_select.isSelected = false
//                    iv_palpay_select.isSelected = false
//                }
//            }
//            checkSubmitEnable()
//
//        }
//        clyt_palpay.setOnClickListener {
//            if (iv_palpay_select.isSelected) {
//                iv_palpay_select.isSelected = false
//                paypalEnable = false
//            } else {
//                if (TextUtils.isEmpty(tv_payment_palpay_account.text)) {
//                    goPaymentEdit(0)
//                } else {
//                    aliEnable = false
//                    paypalEnable = true
//                    checkEnable = false
//                    iv_palpay_select.isSelected = true
//                    iv_ali_select.isSelected = false
//                    iv_check_select.isSelected = false
//                }
//            }
//            checkSubmitEnable()
//
//        }

        tv_withdraw_recorder.setOnClickListener { startWithdrawRecordActivity(this) }
        tv_withdraw_method.setOnClickListener { startPaymentMethodActivity(this) }

        tv_submit.setOnClickListener {
            if (paymentType == 1) {
                if (TextUtils.isEmpty(paymentIfModel?.paypal_firstname) || TextUtils.isEmpty(paymentIfModel?.paypal_lastname) || TextUtils.isEmpty(paymentIfModel?.paypal_area)) {
                    repairPaymentPayDlg = GcbConfirmDlg.Builder(this@WithdrawActivity)
                            .setMessage(R.string.payment_method_paypal_not_complete)
                            .setConfirmListener("", object : GcbConfirmDlg.OnConfirmListener {
                                override fun onConfirm(dlg: GcbConfirmDlg) {
                                    dlg.dismiss()
                                    checkPwdDlg = CheckPwdDlg(this@WithdrawActivity,
                                            object : CheckPwdDlg.CheckPwdListener {
                                                override fun onInputCompleteL(pwd: String) {
                                                    createService(UserApi::class.java)
                                                            .paymentPwdVerify(Base64.encodeToString(pwd.toByteArray(), Base64.DEFAULT).trim())
                                                            .compose(ResponseTransformer.handleResult())
                                                            .compose(bindToLifecycle())
                                                            .subscribe(object : BaseObserver<Any>(this@WithdrawActivity) {
                                                                override fun onSuccess(t: Any) {
                                                                    hideSoftInput()
                                                                    checkPwdDlg.dismiss()

                                                                    startPaymentMethodEditPaypalActivity(this@WithdrawActivity, paymentIfModel)

                                                                }
                                                            })

                                                }
                                            }, false)
                                    checkPwdDlg.show()
                                }
                            })
                            .setCancelListener("", object : GcbConfirmDlg.OnCancelListener {
                                override fun onCancel(dlg: GcbConfirmDlg) {
                                    dlg.dismiss()
                                }
                            })
                            .setAutoDismiss(false)
                            .create()
                    repairPaymentPayDlg.show()
                } else {
                    //  验证密码弹窗
                    checkPwdDlg = CheckPwdDlg(this@WithdrawActivity,
                            object : CheckPwdDlg.CheckPwdListener {
                                override fun onInputCompleteL(pwd: String) {
                                    checkPwd(pwd)

                                }
                            }, false)
                    checkPwdDlg.show()
                }

            } else {
                //  验证密码弹窗
                checkPwdDlg = CheckPwdDlg(this@WithdrawActivity,
                        object : CheckPwdDlg.CheckPwdListener {
                            override fun onInputCompleteL(pwd: String) {
                                checkPwd(pwd)

                            }
                        }, false)
                checkPwdDlg.show()
            }
        }

    }

//    private fun goPaymentEdit(payType: Int) {  // 0paypal 1ali 2check
//        GcbConfirmDlg.Builder(this@WithdrawActivity)
//                .setMessage(R.string.payment_method_not_set)
//                .setConfirmListener("", object : GcbConfirmDlg.OnConfirmListener {
//                    override fun onConfirm(dlg: GcbConfirmDlg) {
//                        checkPwdDlg = CheckPwdDlg(this@WithdrawActivity,
//                                object : CheckPwdDlg.CheckPwdListener {
//                                    override fun onInputCompleteL(pwd: String) {
//                                        createService(UserApi::class.java)
//                                                .paymentPwdVerify(Base64.encodeToString(pwd.toByteArray(), Base64.DEFAULT).trim())
//                                                .compose(ResponseTransformer.handleResult())
//                                                .compose(bindToLifecycle())
//                                                .subscribe(object : BaseObserver<Any>(this@WithdrawActivity) {
//                                                    override fun onSuccess(t: Any) {
//                                                        hideSoftInput()
//                                                        checkPwdDlg.dismiss()
//
//                                                        when (payType) {
//                                                            0 -> startPaymentMethodEditPaypalActivity(this@WithdrawActivity, paymentIfModel)
//                                                            1 -> startPaymentMethodEditAliActivity(this@WithdrawActivity, paymentIfModel)
//                                                            2 -> startPaymentMethodEditCheckActivity(this@WithdrawActivity, paymentIfModel)
//                                                        }
//
//                                                    }
//                                                })
//
//                                    }
//                                }, false)
//                        checkPwdDlg.show()
//                    }
//                }).create().show()
//
//
//    }

    override fun initData() {
        createService(UserApi::class.java)
                .paymentMethod()
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<PaymentMethodIfModel>(this) {
                    override fun onSuccess(t: PaymentMethodIfModel) {
                        paymentIfModel = t.payment
                        heliPaymentList = t.helipay
                        if (t.is_pay_pwd == 0) {    // 未设置过支付密码
                            setPaymentPwdDlg = GcbConfirmDlg.Builder(this@WithdrawActivity)
                                    .setMessage(R.string.payment_method_not_set_pwd)
                                    .setConfirmListener("", object : GcbConfirmDlg.OnConfirmListener {
                                        override fun onConfirm(dlg: GcbConfirmDlg) {
                                            startPaymentPasswordActivity(this@WithdrawActivity)
                                        }
                                    })
                                    .setCancelListener("", object : GcbConfirmDlg.OnCancelListener {
                                        override fun onCancel(dlg: GcbConfirmDlg) {
                                            dlg.dismiss()
                                            finish()
                                        }
                                    })
                                    .setAutoDismiss(false)
                                    .create()
                            setPaymentPwdDlg.show()
                        }
//                        else {
                        // 界面渲染
                        t.run {
                            lowWithdrawLimit = if (withdraw_successful_count > 0) low_withdraw_limit_for_second else low_withdraw_limit
                            tv_tips.text = String.format(resources.getString(R.string.withdraw_amount_tips), lowWithdrawLimit)
                            // 默认  上次>支票 > PayPal > 支付宝（现在是合利宝）
                            if (last_payment == 1 || last_payment == 3 || last_payment == 7) { // 有提现记录
                                paymentType = last_payment
                            } else {
                                paymentType = if (TextUtils.isEmpty(t.payment?.firstname) || TextUtils.isEmpty(t.payment?.lastname)) {
                                    if (TextUtils.isEmpty(t.payment?.paypal)) {
                                        if (t.helipay.isNullOrEmpty()) {
                                            -1
                                        } else {
                                            7
                                        }
                                    } else {
                                        1
                                    }
                                } else {
                                    3
                                }
                            }
                            initPaymentMethodList(t)

                            renderView(this)
                        }

//                        if (TextUtils.isEmpty(t.payment?.firstname) || TextUtils.isEmpty(t.payment?.lastname)) {
//                            tv_payment_check_account.visibility = View.GONE
//                        } else {
//                            tv_payment_check_account.visibility = View.VISIBLE
//                            tv_payment_check_account.text = (t.payment?.firstname
//                                    ?: "") + (t.payment?.lastname ?: "")
//
//                        }
//                        if (TextUtils.isEmpty(t.payment?.paypal)) {
//                            tv_payment_palpay_account.visibility = View.GONE
//                        } else {
//                            tv_payment_palpay_account.visibility = View.VISIBLE
//                            tv_payment_palpay_account.text = t.payment?.paypal ?: ""
//
//                        }
//                        if (TextUtils.isEmpty(t.payment?.alipay)) {
//                            tv_payment_ali_account.visibility = View.GONE
//                        } else {
//                            tv_payment_ali_account.visibility = View.VISIBLE
//                            tv_payment_ali_account.text = t.payment?.alipay ?: ""
//
////                            }
//                        }

                    }
                })
    }

    private fun renderView(t: PaymentMethodIfModel) {

        when (paymentType) {
            -1 -> {
                loadMipmapImg(R.mipmap.ic_payment_method_default, iv_payment)
            }
            1 -> {
                et_payment_method.setText(resources.getString(com.gocashback.lib_common.R.string.payment_method_paypal)+ " · " + getFuzzyString(t.payment?.paypal ?: ""))
                loadMipmapImg(R.mipmap.ic_pay_type_paypal, iv_payment)
            }
            3 -> {
                et_payment_method.setText(resources.getString(com.gocashback.lib_common.R.string.payment_method_check)+ " · " + getFuzzyString(t.payment?.firstname + t.payment?.lastname))
                loadMipmapImg(R.mipmap.ic_pay_type_check, iv_payment)
            }
            7 -> {
                var match = false
                for (heli in t.helipay!!) {
                    if (heli.bank_account_no == t.last_payment_bank_account_no) {
                        et_payment_method.setText(heli.name + " · " + getFuzzyString(heli.bank_account_no))
                        loadImage(originUrl = heli.icon, targetView = iv_payment)
                        heliPayId = heli.id
                        match = true
                        break
                    }
                }
                if (!match){
                    val heli = t.helipay!![0]
                    et_payment_method.setText(heli.name + " · " + getFuzzyString(heli.bank_account_no))
                    loadImage(originUrl = heli.icon, targetView = iv_payment)
                    heliPayId = heli.id
                }
            }
        }

    }

    private fun initPaymentMethodList(t: PaymentMethodIfModel) {
        paymentList = arrayListOf()
        if (!t.helipay.isNullOrEmpty()) {
            t.helipay?.let { paymentList.addAll(it) }
        }
        if (!TextUtils.isEmpty(t.payment?.paypal)) {
            t.payment?.let {
                paymentList.add(PaymentIfModel().apply {
                    paymentType = 1
                    paypal = it.paypal
                })
            }
        }
        if (!TextUtils.isEmpty(t.payment?.firstname) && !TextUtils.isEmpty(t.payment?.lastname)) {
            t.payment?.let {
                paymentList.add(PaymentIfModel().apply {
                    paymentType = 3
                    lastname = it.lastname
                    firstname = it.firstname
                })
            }
        }
        paymentList.add(PaymentIfModel().apply { paymentType = -1 })
    }

    fun checkSubmitEnable() {
        tv_submit.isEnabled = paymentMethodEnable && amountEnable
    }

    /**
     * 验证支付密码
     * @param pwd String
     */
    private fun checkPwd(pwd: String) {
        createService(UserApi::class.java)
                .paymentPwdVerify(Base64.encodeToString(pwd.toByteArray(), Base64.DEFAULT).trim())
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<Any>(this) {
                    override fun onSuccess(t: Any) {
                        hideSoftInput()
                        checkPwdDlg.checkSuccess = true
                        checkPwdDlg.dismiss()

                        doWithdraw()

                    }
                })


    }


    /**
     * 提现
     */
    private fun doWithdraw() {
        createService(UserApi::class.java)
                .withdraw(paymentType, et_payment_amount.text.toString(), "", if (paymentType == 7) heliPayId else "")
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<Any>(this) {
                    override fun onSuccess(t: Any) {
                        EventBus.getDefault().post(WithdrawSuccessEvent())
                        startSuccessActivity(this@WithdrawActivity, SuccessType.WITHDRAW)
                        finish()
                    }
                })

    }

    @Subscribe
    fun paymentMethodUpdate(paymentMethodUpdateEvent: PaymentMethodUpdateEvent) {
        createService(UserApi::class.java)
                .paymentMethod()
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<PaymentMethodIfModel>(this) {
                    override fun onSuccess(t: PaymentMethodIfModel) {
                        paymentIfModel = t.payment
                        heliPaymentList = t.helipay
                        // 更新选支付方式列表
                        initPaymentMethodList(t)
                        // 界面渲染
//                        if (TextUtils.isEmpty(t.payment?.firstname) || TextUtils.isEmpty(t.payment?.lastname)) {
//                            tv_payment_check_account.visibility = View.GONE
//                            checkEnable = false
//                            iv_check_select.isSelected = false
//                        } else {
//                            tv_payment_check_account.visibility = View.VISIBLE
//                            tv_payment_check_account.text = (t.payment?.firstname
//                                    ?: "") + (t.payment?.lastname ?: "")
//
//                        }
//                        if (TextUtils.isEmpty(t.payment?.paypal)) {
//                            tv_payment_palpay_account.visibility = View.GONE
//                            paypalEnable = false
//                            iv_palpay_select.isSelected = false
//                        } else {
//                            tv_payment_palpay_account.visibility = View.VISIBLE
//                            tv_payment_palpay_account.text = t.payment?.paypal ?: ""
//
//                        }
//                        if (TextUtils.isEmpty(t.payment?.alipay)) {
//                            tv_payment_ali_account.visibility = View.GONE
//                            aliEnable = false
//                            iv_ali_select.isSelected = false
//
//                        } else {
//                            tv_payment_ali_account.visibility = View.VISIBLE
//                            tv_payment_ali_account.text = t.payment?.alipay ?: ""
//
//                        }
//
//                        checkSubmitEnable()

                    }
                })
    }

    @Subscribe
    fun paymentPwdUpdate(paymentPwdUpdateEvent: PaymentPwdUpdateEvent) {
        if (paymentPwdUpdateEvent.forgetPwd) {  // 忘记支付密码
            checkPwdDlg.clearContent()
        } else {//  已设置支付密码
            setPaymentPwdDlg.dismiss()

        }
    }
}