package com.gocashback.module_me.activity

import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.widget.LinearLayout.HORIZONTAL
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_GIFTCARD_DETAIL
import com.gocashback.lib_common.LOGIN_EXTRA
import com.gocashback.lib_common.annotation.SuccessType
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.GiftCardApi
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.getUser
import com.gocashback.lib_common.network.model.giftCard.GiftCardDetailIfModel
import com.gocashback.lib_common.startPaymentPasswordActivity
import com.gocashback.lib_common.startSuccessActivity
import com.gocashback.lib_common.utils.moneyFormat
import com.gocashback.lib_common.widget.CheckPwdDlg
import com.gocashback.lib_common.widget.GcbConfirmDlg
import com.gocashback.module_me.R
import com.gocashback.module_me.adapter.GiftCardDetailAmountSelectAdapter
import com.gocashback.module_me.event.PaymentPwdUpdateEvent
import com.gocashback.module_me.event.WithdrawSuccessEvent
import com.zzhoujay.richtext.RichText
import kotlinx.android.synthetic.main.activity_gift_card_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.text.DecimalFormat

/**
 * @Author 55HAITAO
 * @Date 2019-06-15 15:23
 */
@Route(path = ACTIVITY_GIFTCARD_DETAIL, extras = LOGIN_EXTRA)
class GiftCardDetailActivity : GcbBaseActivity() {
    private lateinit var giftCardDetailAmountSelectAdapter: GiftCardDetailAmountSelectAdapter
    private lateinit var checkPwdDlg: CheckPwdDlg
    private lateinit var setPaymentPwdDlg: GcbConfirmDlg
    private lateinit var tipsDlg: GcbConfirmDlg

    @JvmField
    @Autowired(name = "id")
    var id = ""

    private var bonusPercent = 0
    private var redemptionAmount = 0f
    private var cardId = ""

    private var needCheckPwd = false

    override fun setLayoutId(): Int {
        return R.layout.activity_gift_card_detail
    }

    override fun isImmersionBarEnabled(): Boolean {
        return false
    }

    override fun initVars() {
        registerEventBus()
    }

    override fun initViews() {

        rycv_amount_selected.apply {
            layoutManager = LinearLayoutManager(this@GiftCardDetailActivity).apply { orientation = HORIZONTAL }
            giftCardDetailAmountSelectAdapter = GiftCardDetailAmountSelectAdapter(listOf())
            adapter = giftCardDetailAmountSelectAdapter
        }
    }

    override fun initEvent() {
        iv_more.setOnClickListener {
            if (tv_description_simple.visibility == View.VISIBLE) {
                tv_description_simple.visibility = View.GONE
                tv_description_whole.visibility = View.VISIBLE
                iv_more.setImageResource(R.mipmap.ic_more_up)
            } else {
                tv_description_simple.visibility = View.VISIBLE
                tv_description_whole.visibility = View.GONE
                iv_more.setImageResource(R.mipmap.ic_more_down)

            }
        }
        et_amount_selected.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_currency.visibility = if (TextUtils.isEmpty(s)) View.GONE else View.VISIBLE
                val moneyStr = if (!TextUtils.isEmpty(s)) s.toString() else "0"

                tv_get.text = moneyFormat(java.lang.String.valueOf(DecimalFormat("0.00").format(java.lang.Float.valueOf(moneyStr) * (1 + bonusPercent / 100f))))
                tv_redemption_amount.text = moneyFormat(moneyStr)
                redemptionAmount = java.lang.Float.valueOf(moneyStr) ?: 0f
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        giftCardDetailAmountSelectAdapter.setOnItemClickListener { _, _, position ->

            giftCardDetailAmountSelectAdapter.setSelectPosition(position)

            tv_reward_name.text = tv_reward_name.text.split("\$")[0] + moneyFormat(giftCardDetailAmountSelectAdapter.data[position].face_value)

            tv_redemption_amount.text = moneyFormat(giftCardDetailAmountSelectAdapter.data[position].amount)
            redemptionAmount = java.lang.Float.valueOf(giftCardDetailAmountSelectAdapter.data[position].face_value)
            cardId = giftCardDetailAmountSelectAdapter.data[position].id

        }

        tv_redeem_now.setOnClickListener {
            if (!TextUtils.isEmpty(tv_redemption_amount.text)) {
                tipsDlg = GcbConfirmDlg.Builder(this@GiftCardDetailActivity)
                        .setMessage(R.string.gift_cards_tips)
                        .setConfirmListener(R.string.gift_cards_tips_confirm, object : GcbConfirmDlg.OnConfirmListener {
                            override fun onConfirm(dlg: GcbConfirmDlg) {
                                //  验证密码弹窗
                                if (getUser()?.is_pay_pwd == 1) {
                                    checkPwdDlg = CheckPwdDlg(this@GiftCardDetailActivity,
                                            object : CheckPwdDlg.CheckPwdListener {
                                                override fun onInputCompleteL(pwd: String) {
                                                    checkPwd(pwd)
                                                }
                                            }, false)
                                    checkPwdDlg.show()
                                } else {   // 未设置过支付密码的去设置
                                    setPaymentPwdDlg = GcbConfirmDlg.Builder(this@GiftCardDetailActivity)
                                            .setMessage(R.string.payment_method_not_set_pwd)
                                            .setConfirmListener("", object : GcbConfirmDlg.OnConfirmListener {
                                                override fun onConfirm(dlg: GcbConfirmDlg) {
                                                    startPaymentPasswordActivity(this@GiftCardDetailActivity)
                                                }
                                            })
                                            .create()
                                    setPaymentPwdDlg.show()

                                }
                            }
                        })
                        .create()
                tipsDlg.show()

            }
        }
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

    private fun doWithdraw() {
        createService(UserApi::class.java)
                .withdraw(6, redemptionAmount.toString(), cardId, "")
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<Any>(this) {
                    override fun onSuccess(t: Any) {
                        EventBus.getDefault().post(WithdrawSuccessEvent())
                        startSuccessActivity(this@GiftCardDetailActivity, SuccessType.GIFTCARD)
                        finish()
                    }
                })

    }

    override fun initData() {
        createService(GiftCardApi::class.java)
                .giftCardDetail(id)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<GiftCardDetailIfModel>(this) {
                    override fun onSuccess(t: GiftCardDetailIfModel) {
                        with(t) {
                            bonusPercent = bonus_percent
                            loadImage(originUrl = image_url, targetView = iv_img)

                            tv_reward_name.text = reward_name
                            tv_discount.text = discount
                            RichText.fromHtml(description + disclaimer).into(tv_description_whole)
                            RichText.fromHtml(description + disclaimer).into(tv_description_simple)

                            if ("VARIABLE_VALUE" == value_type) {
                                cardId = id
                                et_amount_selected.visibility = View.VISIBLE
                                rycv_amount_selected.visibility = View.GONE
                                tv_get_title.visibility = View.VISIBLE
                                tv_get.visibility = View.VISIBLE
                            } else {
                                et_amount_selected.visibility = View.GONE
                                rycv_amount_selected.visibility = View.VISIBLE
                                tv_get_title.visibility = View.GONE
                                tv_get.visibility = View.GONE

                                giftCardDetailAmountSelectAdapter.setNewData(face_values?.apply {
                                    get(0).selected = true
                                    cardId = get(0).id
                                    tv_reward_name.text = reward_name.split("\$")[0] + moneyFormat(get(0).face_value)

                                    tv_redemption_amount.text = moneyFormat(get(0).amount)
                                    redemptionAmount = java.lang.Float.valueOf(get(0).face_value)

                                })

                            }
                            tv_available.text = moneyFormat(available)
                        }

                    }
                })

    }

    override fun onResume() {
        super.onResume()
        if (needCheckPwd) {
            needCheckPwd = false
            checkPwdDlg = CheckPwdDlg(this@GiftCardDetailActivity,
                    object : CheckPwdDlg.CheckPwdListener {
                        override fun onInputCompleteL(pwd: String) {
                            checkPwd(pwd)
                        }
                    }, false)
            checkPwdDlg.show()
        }
    }


    @Subscribe
    fun paymentPwdUpdate(paymentPwdUpdateEvent: PaymentPwdUpdateEvent) {
        if (paymentPwdUpdateEvent.forgetPwd) {   // 修改了支付密码清空已填密码
            checkPwdDlg.clearContent()
        } else {//  设置完支付密码
            needCheckPwd = true
        }
    }
}