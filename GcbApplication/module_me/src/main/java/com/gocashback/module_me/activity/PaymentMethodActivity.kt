package com.gocashback.module_me.activity

import android.app.Dialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Base64
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gocashback.lib_common.*
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.HeliPaymentIfModel
import com.gocashback.lib_common.network.model.user.PaymentIfModel
import com.gocashback.lib_common.network.model.user.PaymentMethodIfModel
import com.gocashback.lib_common.utils.show
import com.gocashback.lib_common.widget.BsListDlg
import com.gocashback.lib_common.widget.CheckPwdDlg
import com.gocashback.lib_common.widget.GcbConfirmDlg
import com.gocashback.lib_common.widget.GcbPaymentMethodItem
import com.gocashback.module_me.R
import com.gocashback.module_me.adapter.PaymentMethodHeliAdapter
import com.gocashback.module_me.event.PaymentMethodUpdateEvent
import com.gocashback.module_me.event.PaymentPwdUpdateEvent
import kotlinx.android.synthetic.main.activity_payment_method.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * @Author 55HAITAO
 * @Date 2019-06-13 17:24
 */
@Route(path = ACTIVITY_PAYMENT_METHOD, extras = LOGIN_EXTRA)
class PaymentMethodActivity : GcbBaseActivity() {

    private var paymentType = 1  //  1paypal, 3check, 5alipay, 7heili
    private var paymentIfModel: PaymentIfModel? = null
    private var heliPaymentList: List<HeliPaymentIfModel>? = listOf()
    private lateinit var bsListDlg: BsListDlg
    private lateinit var setPaymentPwdDlg: GcbConfirmDlg
    private lateinit var checkPwdDlg: CheckPwdDlg

    private lateinit var paymentMethodHeliAdapter: PaymentMethodHeliAdapter
    private var heliPosition = 0

    override fun setLayoutId(): Int {
        return R.layout.activity_payment_method
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        rycv_heli.apply {
            layoutManager = LinearLayoutManager(this@PaymentMethodActivity)
            paymentMethodHeliAdapter = PaymentMethodHeliAdapter(listOf())
            adapter = paymentMethodHeliAdapter
        }

        registerEventBus()
    }

    override fun initEvent() {
        gpm_ali.onRightClickListener = object : GcbPaymentMethodItem.OnRightClickListener {
            override fun onRightClick() {
                paymentType = 5
                showBslistDlg(gpm_ali.getSubText().isNotEmpty())
            }
        }
        gpm_paypal.onRightClickListener = object : GcbPaymentMethodItem.OnRightClickListener {
            override fun onRightClick() {
                paymentType = 1
                showBslistDlg(gpm_paypal.getSubText().isNotEmpty())
            }
        }
        gpm_check.onRightClickListener = object : GcbPaymentMethodItem.OnRightClickListener {
            override fun onRightClick() {
                paymentType = 3
                showBslistDlg(gpm_check.getSubText().isNotEmpty())
            }
        }
        paymentMethodHeliAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.iv_more) {
                paymentType = 7
                heliPosition = position
                showBslistDlg(true)
            }
        }
        tv_add_heli.setOnClickListener {
            startPaymentMethodEditHeliActivity(this, null)
        }

    }

    override fun initData() {
        getPaymentMethod()

    }

    private fun getPaymentMethod(refresh: Boolean = false) {

        createService(UserApi::class.java)
                .paymentMethod()
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<PaymentMethodIfModel>(this) {
                    override fun onSuccess(t: PaymentMethodIfModel) {
                        paymentIfModel = t.payment
                        heliPaymentList = t.helipay
                        if (t.is_pay_pwd == 0) {    // 未设置过支付密码
                            if (!refresh) {
                                setPaymentPwdDlg = GcbConfirmDlg.Builder(this@PaymentMethodActivity)
                                        .setMessage(R.string.payment_method_not_set_pwd)
                                        .setConfirmListener("", object : GcbConfirmDlg.OnConfirmListener {
                                            override fun onConfirm(dlg: GcbConfirmDlg) {
                                                startPaymentPasswordActivity(this@PaymentMethodActivity)
                                            }
                                        })
                                        .setCancelListener("", object : GcbConfirmDlg.OnCancelListener {
                                            override fun onCancel(dlg: GcbConfirmDlg) {
                                                finish()
                                                dlg.dismiss()
                                            }
                                        })
                                        .setAutoDismiss(false)
                                        .create()
                                setPaymentPwdDlg.show()
                            }
                        } else {     // 设置过支付密码的需要验证
                            //  验证密码弹窗
                            if (!refresh) {
                                checkPwdDlg = CheckPwdDlg(this@PaymentMethodActivity,
                                        object : CheckPwdDlg.CheckPwdListener {
                                            override fun onInputCompleteL(pwd: String) {
                                                checkPwd(pwd)

                                            }
                                        })
                                checkPwdDlg.show()
                            }

                            // 界面渲染
                            gpm_ali.setSubText(t.payment?.alipay ?: "")
                            gpm_check.setSubText((t.payment?.firstname ?: "") + (t.payment?.lastname
                                    ?: ""))
                            gpm_paypal.setSubText(t.payment?.paypal ?: "")
                            paymentMethodHeliAdapter.setNewData(heliPaymentList)
                        }

                    }
                })
    }

    private fun checkPwd(pwd: String) {
        createService(UserApi::class.java)
                .paymentPwdVerify(Base64.encodeToString(pwd.toByteArray(), Base64.DEFAULT).trim())
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<Any>(this) {
                    override fun onSuccess(t: Any) {
                        hideSoftInputDely(gcbHeadView)
                        checkPwdDlg.checkSuccess = true
                        checkPwdDlg.dismiss()

                    }
                })


    }

    fun showBslistDlg(showDelete: Boolean) {
        if (showDelete) {
            bsListDlg = BsListDlg.Builder(this)
                    .addData(R.mipmap.ic_bsdlg_edit, R.string.payment_method_edit)
                    .addData(R.mipmap.ic_bsdlg_delete, R.string.payment_method_remove)
                    .setListener(object : BsListDlg.OnDlgItemClickListener {
                        override fun onDlgItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int, dlg: Dialog) {
                            if (position == 0) {
                                // 编辑
                                goEdit()
                            } else {
                                // 删除
                                paymentMethodUpdate()
                            }
                            dlg.dismiss()

                        }
                    })
                    .create()
        } else {
            bsListDlg = BsListDlg.Builder(this)
                    .addData(R.mipmap.ic_bsdlg_edit, R.string.payment_method_edit)
                    .setListener(object : BsListDlg.OnDlgItemClickListener {
                        override fun onDlgItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int, dlg: Dialog) {
                            // 编辑
                            goEdit()
                            dlg.dismiss()
                        }
                    }).create()
        }
        bsListDlg.show()
    }

    private fun goEdit() {
        //  1paypal, 3check, 5alipay, 7heli
        when (paymentType) {
            1 -> {
                startPaymentMethodEditPaypalActivity(this, paymentIfModel)
            }
            3 -> {
                startPaymentMethodEditCheckActivity(this, paymentIfModel)
            }
            5 -> {
                startPaymentMethodEditAliActivity(this, paymentIfModel)
            }
            7 -> {
                startPaymentMethodEditHeliActivity(this, heliPaymentList?.get(heliPosition))
            }
        }

    }

    /**
     * 删除
     */
    private fun paymentMethodUpdate() {
        //  1paypal, 3check, 5alipay, 7heli
        when (paymentType) {
            1 -> {
                paymentIfModel?.run {
                    createService(UserApi::class.java)
                            .paymentMethodPaypalUpdate(1, "", "", "", "")
                            .compose(ResponseTransformer.handleResult())
                            .compose(bindToLifecycle())
                            .subscribe(object : BaseObserver<Any>(this@PaymentMethodActivity) {
                                override fun onSuccess(t: Any) {
                                    show(this@PaymentMethodActivity, R.string.payment_method_remove_success)
//                                    gpm_paypal.setSubText("")
//                                    paypal = ""
                                    EventBus.getDefault().post(PaymentMethodUpdateEvent())
                                }
                            })
                }

            }
            3 -> {
                paymentIfModel?.run {
                    createService(UserApi::class.java)
                            .paymentMethodCheckUpdate(3, "", "", "", "", "", "", "")
                            .compose(ResponseTransformer.handleResult())
                            .compose(bindToLifecycle())
                            .subscribe(object : BaseObserver<Any>(this@PaymentMethodActivity) {
                                override fun onSuccess(t: Any) {
                                    show(this@PaymentMethodActivity, R.string.payment_method_remove_success)
                                    EventBus.getDefault().post(PaymentMethodUpdateEvent())

//                                    gpm_check.setSubText("")
//                                    post = ""
//                                    city = ""
//                                    state = ""
//                                    street = ""
//                                    street_two = ""
//                                    lastname = ""
//                                    firstname = ""

                                }
                            })
                }
            }
            5 -> {
                paymentIfModel?.run {
                    createService(UserApi::class.java)
                            .paymentMethodAliUpdate(5, "", "")
                            .compose(ResponseTransformer.handleResult())
                            .compose(bindToLifecycle())
                            .subscribe(object : BaseObserver<Any>(this@PaymentMethodActivity) {
                                override fun onSuccess(t: Any) {
                                    show(this@PaymentMethodActivity, R.string.payment_method_remove_success)
                                    EventBus.getDefault().post(PaymentMethodUpdateEvent())

//                                    gpm_ali.setSubText("")
//                                    alipay = ""
//                                    alipay_name = ""

                                }
                            })
                }
            }
            7 -> {
                paymentIfModel?.run {
                    createService(UserApi::class.java)
                            .paymentMethodRemoveHeli(paymentMethodHeliAdapter.data[heliPosition].id)
                            .compose(ResponseTransformer.handleResult())
                            .compose(bindToLifecycle())
                            .subscribe(object : BaseObserver<Any>(this@PaymentMethodActivity) {
                                override fun onSuccess(t: Any) {
                                    show(this@PaymentMethodActivity, R.string.payment_method_remove_success)
                                    EventBus.getDefault().post(PaymentMethodUpdateEvent())

                                }
                            })
                }
            }
        }


    }

    @Subscribe
    fun paymentMethodUpdate(paymentMethodUpdateEvent: PaymentMethodUpdateEvent) {
        getPaymentMethod(true)
    }

    @Subscribe
    fun paymentPwdUpdate(paymentPwdUpdateEvent: PaymentPwdUpdateEvent) {
        if (paymentPwdUpdateEvent.forgetPwd) {   // 修改了支付密码清空已填密码
            checkPwdDlg.clearContent()
        } else {//  设置完支付密码
            setPaymentPwdDlg.dismiss()

        }
    }


}