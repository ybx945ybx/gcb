package com.gocashback.module_me.fragment

import android.support.v7.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.FRAGMENT_CLICK_HISTORY_DEALS
import com.gocashback.lib_common.base.GcbBaseFragment
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.BrowsingHistoryIfModel
import com.gocashback.lib_common.startDealDetailActivity
import com.gocashback.module_me.R
import com.gocashback.module_me.adapter.ClickHistoryDealsAdapter
import kotlinx.android.synthetic.main.fragment_click_history_deals.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-13 09:59
 */
@Route(path = FRAGMENT_CLICK_HISTORY_DEALS)
class ClickHistoryDealsFragment : GcbBaseFragment() {

    private lateinit var clickHistoryDealsAdapter: ClickHistoryDealsAdapter

    override fun setLayoutId(): Int {
        return R.layout.fragment_click_history_deals
    }

    override fun initView() {
        rycv_click_history_deals.apply {
            layoutManager = LinearLayoutManager(mActivity)
            clickHistoryDealsAdapter = ClickHistoryDealsAdapter(listOf(), mActivity)
            adapter = clickHistoryDealsAdapter
        }

    }

    override fun initEvent() {
        // 刷新
        content_view.setOnRefreshListener {
            page = 0
            getDealsList()
        }
        // 加载更多
        clickHistoryDealsAdapter.setOnLoadMoreListener({
            page++
            getDealsList()
        }, rycv_click_history_deals)
        // item点击事件
        clickHistoryDealsAdapter.setOnItemClickListener { _, _, position ->
            startDealDetailActivity(mActivity, clickHistoryDealsAdapter.data[position].id)
        }
        msv.setOnRetryClickListener {
           initData()
        }

    }

    override fun initData() {
        msv.showLoading()
        page = 0
        getDealsList()

    }

    private fun getDealsList() {
        createService(UserApi::class.java)
                .browsingHistory(2, page, perPage)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<BrowsingHistoryIfModel>(mActivity) {
                    override fun onSuccess(t: BrowsingHistoryIfModel) {
                        msv.showContent()
                        content_view.isRefreshing = false
                        t.data?.let {
                            if (page == 0) {
                                clickHistoryDealsAdapter.setNewData(it)
                            } else {
                                clickHistoryDealsAdapter.addData(it)
                            }

                            if (page + 1 < t.page_count) clickHistoryDealsAdapter.loadMoreComplete() else clickHistoryDealsAdapter.loadMoreEnd(true)
                        }
                        if (clickHistoryDealsAdapter.data.isNullOrEmpty()) {
                            msv.showEmpty(resources.getString(R.string.msv_no_click_history))
                        }


                    }

                    override fun onFail(code: Int, msg: String) {
                        super.onFail(code, msg)
                        msv.showError()
                        content_view.isRefreshing = false
                    }
                })
    }
}