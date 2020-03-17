package com.gocashback.module_me.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_PAYMENT_METHOD_EDIT_HELI
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.language.LOCALE_CHINESE
import com.gocashback.lib_common.language.getLanguage
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.IndexApi
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.index.IndexHelipayBankItemModel
import com.gocashback.lib_common.network.model.user.HeliPaymentIfModel
import com.gocashback.lib_common.utils.show
import com.gocashback.lib_common.widget.BsSelectBankDlg
import com.gocashback.module_me.R
import com.gocashback.module_me.event.PaymentMethodUpdateEvent
import kotlinx.android.synthetic.main.activity_payment_method_edit_ali.tv_submit
import kotlinx.android.synthetic.main.activity_payment_method_edit_heli.*
import org.greenrobot.eventbus.EventBus

/**
 * @Author 55HAITAO
 * @Date 2019-06-13 20:51
 */
@Route(path = ACTIVITY_PAYMENT_METHOD_EDIT_HELI)
class PaymentMethodEditHeliActivity : GcbBaseActivity() {

    @JvmField
    @Autowired(name = "heliPaymentIfModel")
    internal var heliPaymentIfModel: HeliPaymentIfModel? = null

    private var bankEnable = false
    private var cardEnable = false
    private var telEnable = false
    private var nameEnable = false
    private var identityEnable = false

    private var bankId = ""

    private lateinit var bsSelectBankDlg: BsSelectBankDlg
    private var bankList: List<IndexHelipayBankItemModel> = listOf()

    override fun setLayoutId(): Int {
        return R.layout.activity_payment_method_edit_heli
    }

    override fun isImmersionBarEnabled(): Boolean {
        return false
    }

    override fun initViews() {
//        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()
        gpmev_heli_bank.setEditEnable(false)
        gpmev_heli_bank.setdisablehint(resources.getString(R.string.edit_payment_method_heli_bank_hint))

        heliPaymentIfModel?.run {
            bank_id = bank_id

            gpmev_heli_bank.setText(if (TextUtils.isEmpty(name)) "" else name)
            gpmev_heli_card_number.setText(if (TextUtils.isEmpty(bank_account_no)) "" else bank_account_no)
            gpmev_heli_tel.setText(if (TextUtils.isEmpty(link_phone)) "" else link_phone)
            gpmev_heli_name.setText(if (TextUtils.isEmpty(bank_account_name_cn)) "" else bank_account_name_cn)
            gpmev_heli_identity.setText(if (TextUtils.isEmpty(id_card_no)) "" else id_card_no)
            tv_submit.isEnabled = true
        }
    }

    override fun initEvent() {
        gpmev_heli_bank.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                bankEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        gpmev_heli_bank.setOnClickListener {
            if (bankList.isNullOrEmpty()) {
                createService(IndexApi::class.java).getHelipayBank()
                        .compose(ResponseTransformer.handleResult())
                        .compose(bindToLifecycle())
                        .subscribe(object : BaseObserver<List<IndexHelipayBankItemModel>>(this) {
                            override fun onSuccess(t: List<IndexHelipayBankItemModel>) {
                                bankList = t
                                bsSelectBankDlg = BsSelectBankDlg(this@PaymentMethodEditHeliActivity, bankList,
                                        object : BsSelectBankDlg.OnDlgItemClickListener {
                                            override fun onDlgItemClick(indexHelipayBankItemModel: IndexHelipayBankItemModel) {
                                                with(indexHelipayBankItemModel) {
                                                    bankId = id
//                                                    todo
                                                    gpmev_heli_bank.setText(if (getLanguage(this@PaymentMethodEditHeliActivity) == LOCALE_CHINESE) name else name_en)
                                                }


                                            }
                                        })
                                bsSelectBankDlg.show()

                            }
                        })
            } else {
                bsSelectBankDlg.show()
            }
        }

        gpmev_heli_card_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cardEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        gpmev_heli_tel.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                telEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        gpmev_heli_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                nameEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        gpmev_heli_identity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                identityEnable = !s.isNullOrBlank()
                checkSubmitEnable()
            }
        })

        tv_submit.setOnClickListener {
            hideSoftInput()

            createService(UserApi::class.java)
                    .paymentMethodHeliUpdate(7, bankId, gpmev_heli_card_number.getText(), gpmev_heli_tel.getText(), gpmev_heli_name.getText(), gpmev_heli_identity.getText())
                    .compose(ResponseTransformer.handleResult())
                    .compose(bindToLifecycle())
                    .subscribe(object : BaseObserver<Any>(this) {
                        override fun onSuccess(t: Any) {
                            show(this@PaymentMethodEditHeliActivity, R.string.payment_method_edit_success)
                            EventBus.getDefault().post(PaymentMethodUpdateEvent())
                            finish()

                        }
                    })
        }
    }

    private fun checkSubmitEnable() {
        tv_submit.isEnabled = bankEnable && cardEnable && telEnable && nameEnable && identityEnable

    }
}