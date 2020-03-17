package com.gocashback.module_home.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_DEAL_DETAIL
import com.gocashback.lib_common.adapter.RebateExplainAdapter
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.event.LoginChangeEvent
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.DealApi
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.isLogin
import com.gocashback.lib_common.network.model.deal.DealDetailIfModel
import com.gocashback.lib_common.network.model.deal.DealListIfModel
import com.gocashback.lib_common.network.model.user.CollectionAddIfModel
import com.gocashback.lib_common.share.ShareUtils
import com.gocashback.lib_common.share.ShareUtils.showShareBoard
import com.gocashback.lib_common.startDealDetailActivity
import com.gocashback.lib_common.startLoginActivity
import com.gocashback.lib_common.startStoreDetailActivity
import com.gocashback.lib_common.utils.*
import com.gocashback.module_home.R
import com.gocashback.module_home.adapter.DealListAdapter
import com.zzhoujay.richtext.RichText
import kotlinx.android.synthetic.main.activity_deal_detail.*
import org.greenrobot.eventbus.Subscribe

/**
 * 优惠详情页
 *
 * @Author 55HAITAO
 * @Date 2019-06-18 20:29
 */
@Route(path = ACTIVITY_DEAL_DETAIL)
class DealDetailActivity : GcbBaseActivity() {

    private var dealDetailIfModel: DealDetailIfModel? = null
    private lateinit var dealListAdapter: DealListAdapter

    @JvmField
    @Autowired(name = "id")
    var id = ""

    override fun setLayoutId(): Int {
        return R.layout.activity_deal_detail
    }

    override fun initVars() {
        registerEventBus()
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        rycv_eyeconic_deals.apply {
            layoutManager = LinearLayoutManager(this@DealDetailActivity)
            dealListAdapter = DealListAdapter(this@DealDetailActivity, listOf())
            adapter = dealListAdapter
            isNestedScrollingEnabled = false
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
        // 折扣码复制到剪切板
        tv_deal_coupon_code_copy.setOnClickListener {
            copyToClipboard(this, tv_deal_coupon_code.text.toString(), resources.getString(R.string.go_buy_code_copied))
        }
        clyt_store.setOnClickListener {
            startStoreDetailActivity(this, dealDetailIfModel?.store_info?.flag ?: "")
        }
        dealListAdapter.setOnItemClickListener { _, _, position ->
            startDealDetailActivity(this, dealListAdapter.data[position].id)
        }

        tv_deal_share.setOnClickListener {
            showShareBoard(this,
                    dealDetailIfModel?.title ?: "",
                    dealDetailIfModel?.content ?: "",
                    dealDetailIfModel?.share_url ?: "",
                    dealDetailIfModel?.img ?: "")
        }
        tv_deal_save.setOnClickListener {
            if (dealDetailIfModel == null) return@setOnClickListener
            if (isLogin()) {
                if (tv_deal_save.isSelected) {
                    doUnSave()
                } else {
                    doSave()
                }
            } else {
                startLoginActivity(this)
            }

        }

        tv_shop.setOnClickListener {
            goToBuy(this, dealDetailIfModel?.gotobuy_url ?: "", dealDetailIfModel?.coupon_code
                    ?: "")
        }

        msv.setOnRetryClickListener {
            initData()
        }

    }

    /**
     * 取消收藏
     */
    private fun doUnSave() {
        tv_deal_save.isEnabled = false
        createService(UserApi::class.java)
                .collectionDelete(dealDetailIfModel?.collect_id ?: "")
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<Any>(this) {
                    override fun onSuccess(t: Any) {
                        tv_deal_save.isSelected = false
                        tv_deal_save.isEnabled = true
                        show(this@DealDetailActivity, resources.getString(R.string.cancle_save))

                    }

                    override fun onFail(code: Int, msg: String) {
                        super.onFail(code, msg)
                        tv_deal_save.isEnabled = true

                    }
                })

    }

    /**
     * 收藏
     */
    private fun doSave() {
        tv_deal_save.isEnabled = false
        createService(UserApi::class.java)
                .collectionAdd("deal", dealDetailIfModel?.id ?: "")
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<CollectionAddIfModel>(this) {
                    override fun onSuccess(t: CollectionAddIfModel) {
                        tv_deal_save.isSelected = true
                        dealDetailIfModel?.collect_id = t.id
                        tv_deal_save.isEnabled = true

                        show(this@DealDetailActivity, resources.getString(R.string.save))

                    }

                    override fun onFail(code: Int, msg: String) {
                        super.onFail(code, msg)
                        tv_deal_save.isEnabled = true

                    }

                })
    }

    override fun initData() {
        msv.showLoading()
        getDealDetail()

    }

    private fun getDealDetail(refreshBottomView: Boolean = false) {
        createService(DealApi::class.java)
                .getDealDetail(id)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<DealDetailIfModel>(this) {
                    override fun onSuccess(t: DealDetailIfModel) {
                        msv.showContent()
                        dealDetailIfModel = t
                        with(t) {
                            // collect state
                            tv_deal_save.isSelected = is_collect == 1
                            if (refreshBottomView) {
                                return
                            }
                            // cover
                            loadImage(originUrl = if (TextUtils.isEmpty(img)) "" else img, targetView = iv_deal_cover)
                            // end time
                            val endTime = end_time?.time ?: 0
                            val timePattern = "yyyy-MM-dd"
                            if (endTime > 0) {  // 有有效期
                                clyt_end_time.visibility = View.VISIBLE
                                tv_end_time.text = dateFormat(endTime * 1000, timePattern, true, this@DealDetailActivity)

                                if (System.currentTimeMillis() / 1000 > end_time?.time ?: 0L) {  // 已过期
                                    tv_deal_over_time.visibility = View.VISIBLE
                                    group_bottom.visibility = View.INVISIBLE
                                } else {    // 未过期
                                    tv_deal_over_time.visibility = View.GONE
                                    group_bottom.visibility = View.VISIBLE
                                }
                            } else {    // 无有效期
                                clyt_end_time.visibility = View.GONE

                            }


                            // title
                            tv_deal_title.text = title
                            // discount
                            if (TextUtils.isEmpty(discount) && TextUtils.isEmpty(store_info?.rebate)) {
                                tv_deal_discount.visibility = View.GONE
                            } else {
                                if (!TextUtils.isEmpty(discount) && !TextUtils.isEmpty(store_info?.rebate)) {
                                    tv_deal_discount.text = discount + " + " + store_info?.rebate
                                } else if (TextUtils.isEmpty(discount)) {
                                    tv_deal_discount.text = store_info?.rebate
                                } else {
                                    store_info?.rebate
                                }
                                tv_deal_discount.visibility = View.VISIBLE
                            }
                            // description
                            RichText.fromHtml(content).into(tv_description_simple)
                            RichText.fromHtml(content).into(tv_description_whole)
                            // code
                            if (TextUtils.isEmpty(coupon_code)) {
                                group_code.visibility = View.GONE
                            } else {
                                tv_deal_coupon_code.text = coupon_code
                                group_code.visibility = View.VISIBLE
                            }
                            // rebate terms
//                            tv_cash_back_explain.text = store_info?.rebate_explain?.let {
//                                "-" + it.replaceFirst("-", "").replace("-", "\n-")
//                            }

                            if (!TextUtils.isEmpty(rebate_explain)) {
//                                group_rebate.visibility = View.GONE
//                            } else {
//                                group_rebate.visibility = View.VISIBLE

                                // 处理格式  换行缩进
                                val rebates = arrayListOf<String>()
                                rebate_explain = "\n" + rebate_explain
                                val splits = rebate_explain.split("\n·")
                                for (string in splits) {
                                    if (!TextUtils.isEmpty(string)) {
                                        rebates.add(TirmUtils.trimStart(string))
                                    }
                                }
                                rycv_cash_back_explain.apply {
                                    layoutManager = LinearLayoutManager(this@DealDetailActivity)
                                    adapter = RebateExplainAdapter(rebates)
                                }
//                                tv_cash_back_explain.text = rebate_explain
                            }

                            // store
                            tv_store_name.text = store_info?.name
                            tv_store_cash_back.text = store_info?.rebate
                            tv_store_favourites.text = store_info?.collect_num + " " + resources?.getString(R.string.store_collect)
                            loadImage(originUrl = store_info?.country_img
                                    ?: "", targetView = iv_store_country)
                            loadImage(originUrl = store_info?.store_logo
                                    ?: "", targetView = iv_store_cover)
                            // deals
                            getdeals(store_id, id)

                        }
                    }

                    override fun onFail(code: Int, msg: String) {
                        super.onFail(code, msg)
                        if (!refreshBottomView)
                            msv.showError()
                    }
                })
    }

    private fun getdeals(store_id: String, id: String) {
        createService(DealApi::class.java)
                .getDealMore(store_id, 0, 10, id)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<DealListIfModel>(this) {
                    override fun onSuccess(t: DealListIfModel) {
                        if (t.deal.isNullOrEmpty()) {
                            createService(DealApi::class.java)
                                    .getHotDeal(store_id, 0, 10)
                                    .compose(ResponseTransformer.handleResult())
                                    .compose(bindToLifecycle())
                                    .subscribe(object : BaseObserver<DealListIfModel>(this@DealDetailActivity) {
                                        override fun onSuccess(t: DealListIfModel) {
                                            if (t.deal.isNullOrEmpty()) {
                                                group_deals.visibility = View.GONE
                                            } else {
                                                dealListAdapter.setNewData(t.deal)
                                            }
                                        }
                                    })
                        } else {
                            dealListAdapter.setNewData(t.deal)
                        }
                    }
                })
    }


    @Subscribe
    fun onLoginChange(loginChangeEvent: LoginChangeEvent) {
        if (loginChangeEvent.isLogin) {
            getDealDetail(true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ShareUtils.onActivityResult(this, requestCode, resultCode, data)
    }

}