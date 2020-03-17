package com.gocashback.module_home.activity

import android.support.v7.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_HOT_DEAL
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.DealApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.deal.DealListIfModel
import com.gocashback.lib_common.startDealDetailActivity
import com.gocashback.module_home.R
import com.gocashback.module_home.adapter.DealListAdapter
import kotlinx.android.synthetic.main.avtivity_hot_deal.*

/**
 * @Author 55HAITAO
 * @Date 2019-05-16 16:22
 */
@Route(path = ACTIVITY_HOT_DEAL)
class HotDealActivity : GcbBaseActivity() {

    private lateinit var dealListAdapter: DealListAdapter


    override fun setLayoutId(): Int {
        return R.layout.avtivity_hot_deal

    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        rycv_hot_deals.apply {
            layoutManager = LinearLayoutManager(this@HotDealActivity)
            dealListAdapter = DealListAdapter(this@HotDealActivity, listOf())
            adapter = dealListAdapter
        }

    }

    override fun initEvent() {
        content_view.setOnRefreshListener {
            page = 0
            getDealList()
        }
        dealListAdapter.setOnItemClickListener { _, _, position ->
            startDealDetailActivity(this, dealListAdapter.data[position].id)
        }
        dealListAdapter.setOnLoadMoreListener({
            page++
            getDealList()
        }, rycv_hot_deals)

    }

    override fun initData() {
        msv.showLoading()
        page = 0
        getDealList()
    }

    private fun getDealList() {
        createService(DealApi::class.java)
                .getDealPopularityList(page, perPage)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<DealListIfModel>(this) {
                    override fun onSuccess(t: DealListIfModel) {
                        msv.showContent()
                        content_view.isRefreshing = false
                        t.deal?.let {
                            if (page == 0) {
                                dealListAdapter.setNewData(it)
                            } else {
                                dealListAdapter.addData(it)
                            }

                            if (page + 1 < t.page_count) dealListAdapter.loadMoreComplete() else dealListAdapter.loadMoreEnd(true)

                        }
                        if (dealListAdapter.data.isNullOrEmpty()) {
                            msv.showEmpty()
                        }
                    }

                    override fun onFail(code: Int, msg: String) {
                        msv.showError()
                        content_view.isRefreshing = false

                    }
                })

    }

}
