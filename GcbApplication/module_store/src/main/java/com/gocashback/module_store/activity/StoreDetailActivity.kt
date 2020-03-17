package com.gocashback.module_store.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_STORE_DETAIL
import com.gocashback.lib_common.adapter.RebateExplainAdapter
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.event.LoginChangeEvent
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.DealApi
import com.gocashback.lib_common.network.api.StoreApi
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.isLogin
import com.gocashback.lib_common.network.model.deal.DealListIfModel
import com.gocashback.lib_common.network.model.store.StoreDetailIfModel
import com.gocashback.lib_common.network.model.user.CollectionAddIfModel
import com.gocashback.lib_common.share.ShareUtils
import com.gocashback.lib_common.share.ShareUtils.showShareBoard
import com.gocashback.lib_common.startDealDetailActivity
import com.gocashback.lib_common.startLoginActivity
import com.gocashback.lib_common.utils.TirmUtils
import com.gocashback.lib_common.utils.goToBuy
import com.gocashback.lib_common.utils.show
import com.gocashback.module_store.R
import com.gocashback.module_store.adapter.CashBackCateAdapter
import com.gocashback.module_store.adapter.StoreDealListAdapter
import com.zzhoujay.richtext.RichText
import kotlinx.android.synthetic.main.activity_store_detail.*
import org.greenrobot.eventbus.Subscribe

/**
 * 商家详情页面
 *
 * @Author 55HAITAO
 * @Date 2019-05-13 17:56
 */
@Route(path = ACTIVITY_STORE_DETAIL)
class StoreDetailActivity : GcbBaseActivity() {

    private var storeDetailIfModel: StoreDetailIfModel? = null
    private lateinit var cashBackCateAdapter: CashBackCateAdapter
    private lateinit var storeDealListAdapter: StoreDealListAdapter

    @JvmField
    @Autowired(name = "flag")
    var flag = ""

    override fun setLayoutId(): Int {
        return R.layout.activity_store_detail
    }

    override fun initVars() {
        registerEventBus()
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        rycv_cash_back_categories.apply {
            layoutManager = LinearLayoutManager(this@StoreDetailActivity)
            cashBackCateAdapter = CashBackCateAdapter(listOf())
            adapter = cashBackCateAdapter
        }

        rycv_eyeconic_deals.apply {
            layoutManager = LinearLayoutManager(this@StoreDetailActivity)
            storeDealListAdapter = StoreDealListAdapter(this@StoreDetailActivity, listOf())
            adapter = storeDealListAdapter
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

        storeDealListAdapter.setOnItemClickListener { _, _, position ->
            startDealDetailActivity(this, storeDealListAdapter.data[position].id)
        }

        tv_store_share.setOnClickListener {
            showShareBoard(this,
                    (storeDetailIfModel?.name ?: "") + " " + storeDetailIfModel?.rebate,
                    storeDetailIfModel?.pure_content ?: "",
                    storeDetailIfModel?.share_url ?: "",
                    storeDetailIfModel?.store_logo ?: "")
        }
        tv_store_save.setOnClickListener {
            if (storeDetailIfModel == null) return@setOnClickListener
            if (isLogin()) {
                if (tv_store_save.isSelected) {
                    doUnSave()
                } else {
                    doSave()
                }
            } else {
                startLoginActivity(this)
            }

        }

        tv_shop.setOnClickListener {
            goToBuy(this, storeDetailIfModel?.gotobuy_url ?: "")
        }

        msv.setOnRetryClickListener {
            initData()
        }

    }

    /**
     * 取消收藏
     */
    private fun doUnSave() {
        tv_store_save.isEnabled = false
        createService(UserApi::class.java)
                .collectionDelete(storeDetailIfModel?.collect_id ?: "")
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<Any>(this) {
                    override fun onSuccess(t: Any) {
                        tv_store_save.isSelected = false
                        tv_store_save.isEnabled = true
                        show(this@StoreDetailActivity, resources.getString(R.string.cancle_save))

                    }

                    override fun onFail(code: Int, msg: String) {
                        super.onFail(code, msg)
                        tv_store_save.isEnabled = true

                    }
                })

    }

    /**
     * 收藏
     */
    private fun doSave() {
        tv_store_save.isEnabled = false
        createService(UserApi::class.java)
                .collectionAdd("store", storeDetailIfModel?.id ?: "")
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<CollectionAddIfModel>(this) {
                    override fun onSuccess(t: CollectionAddIfModel) {
                        tv_store_save.isSelected = true
                        storeDetailIfModel?.collect_id = t.id
                        tv_store_save.isEnabled = true
                        show(this@StoreDetailActivity, resources.getString(R.string.save))

                    }

                    override fun onFail(code: Int, msg: String) {
                        super.onFail(code, msg)
                        tv_store_save.isEnabled = true

                    }
                })
    }

    override fun initData() {
        msv.showLoading()
        getStoreDetail()

    }

    private fun getStoreDetail(refreshBottomView: Boolean = false) {
        createService(StoreApi::class.java)
                .getStoreDetail(flag)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<StoreDetailIfModel>(this) {
                    override fun onSuccess(t: StoreDetailIfModel) {
                        msv.showContent()
                        storeDetailIfModel = t
                        with(t) {
                            // collect state
                            tv_store_save.isSelected = is_collect == 1
                            if (refreshBottomView) {
                                return
                            }
                            // store
                            tv_store_name.text = name
                            tv_store_cash_back.text = rebate
                            tv_store_favourites.text = collect_num + " " + resources?.getString(R.string.store_collect)
                            loadImage(originUrl = country_img, targetView = iv_store_country)
                            loadImage(originUrl = store_logo, targetView = iv_store_cover)
                            RichText.fromHtml(content).into(tv_description_simple)
                            RichText.fromHtml(content).into(tv_description_whole)
                            // specials
                            if (special.isNullOrEmpty()) {
                                group_categories.visibility = View.GONE
                            } else {
                                group_categories.visibility = View.VISIBLE
                                cashBackCateAdapter.setNewData(special)
                            }
                            // rebate terms
                            if (TextUtils.isEmpty(rebate_explain)) {
                                group_rebate.visibility = View.GONE
                            } else {
                                group_rebate.visibility = View.VISIBLE

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
                                    layoutManager = LinearLayoutManager(this@StoreDetailActivity)
                                    adapter = RebateExplainAdapter(rebates)
                                }
//                                tv_cash_back_explain.text = rebate_explain
                            }
                            // deals
                            getdeals(id)

                        }
                    }

                    override fun onFail(code: Int, msg: String) {
                        super.onFail(code, msg)
                        if (!refreshBottomView)
                            msv.showError()
                    }
                })
    }

    private fun getdeals(id: String) {
        createService(DealApi::class.java)
                .getDealMore(id, 0, 10)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<DealListIfModel>(this) {
                    override fun onSuccess(t: DealListIfModel) {
                        if (t.deal.isNullOrEmpty()) {
                            createService(DealApi::class.java)
                                    .getHotDeal(id, 0, 10)
                                    .compose(ResponseTransformer.handleResult())
                                    .compose(bindToLifecycle())
                                    .subscribe(object : BaseObserver<DealListIfModel>(this@StoreDetailActivity) {
                                        override fun onSuccess(t: DealListIfModel) {
                                            if (t.deal.isNullOrEmpty()) {
                                                group_deals.visibility = View.GONE
                                            } else {
                                                storeDealListAdapter.setNewData(t.deal)
                                            }
                                        }
                                    })
                        } else {
                            storeDealListAdapter.setNewData(t.deal)
                        }
                    }
                })
    }

    @Subscribe
    fun onLoginChange(loginChangeEvent: LoginChangeEvent) {
        if (loginChangeEvent.isLogin) {
            getStoreDetail(true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ShareUtils.onActivityResult(this, requestCode, resultCode, data)
    }

}