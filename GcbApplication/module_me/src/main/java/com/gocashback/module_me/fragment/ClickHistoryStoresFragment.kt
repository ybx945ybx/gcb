package com.gocashback.module_me.fragment

import android.support.v7.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.FRAGMENT_CLICK_HISTORY_STORES
import com.gocashback.lib_common.base.GcbBaseFragment
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.BrowsingHistoryIfModel
import com.gocashback.lib_common.startStoreDetailActivity
import com.gocashback.module_me.R
import com.gocashback.module_me.adapter.ClickHistoryStoresAdapter
import kotlinx.android.synthetic.main.fragment_click_history_stores.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-13 10:02
 */
@Route(path = FRAGMENT_CLICK_HISTORY_STORES)
class ClickHistoryStoresFragment : GcbBaseFragment() {

    private lateinit var clickHistoryStoresAdapter: ClickHistoryStoresAdapter

    override fun setLayoutId(): Int {
        return R.layout.fragment_click_history_stores
    }

    override fun initView() {
        rycv_click_history_stores.apply {
            layoutManager = LinearLayoutManager(mActivity)
            clickHistoryStoresAdapter = ClickHistoryStoresAdapter(listOf(),  mActivity)
            adapter = clickHistoryStoresAdapter
        }

    }

    override fun initEvent() {
        // 刷新
        content_view.setOnRefreshListener {
            page = 0
            getStoresList()
        }
        // 加载更多
        clickHistoryStoresAdapter.setOnLoadMoreListener({
            page++
            getStoresList()
        }, rycv_click_history_stores)
        // item点击事件
        clickHistoryStoresAdapter.setOnItemClickListener { _, _, position ->
            startStoreDetailActivity(mActivity, clickHistoryStoresAdapter.data[position].flag)
        }
        msv.setOnRetryClickListener {
            initData()
        }
    }

    override fun initData() {
        msv.showLoading()
        page = 0
        getStoresList()

    }

    private fun getStoresList() {
        createService(UserApi::class.java)
                .browsingHistory(1, page, perPage)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<BrowsingHistoryIfModel>(mActivity) {
                    override fun onSuccess(t: BrowsingHistoryIfModel) {
                        msv.showContent()
                        content_view.isRefreshing = false
                        t.data?.let {
                            if (page == 0) {
                                clickHistoryStoresAdapter.setNewData(it)
                            } else {
                                clickHistoryStoresAdapter.addData(it)
                            }

                            if (page + 1 < t.page_count) clickHistoryStoresAdapter.loadMoreComplete() else clickHistoryStoresAdapter.loadMoreEnd(true)
                        }
                        if (clickHistoryStoresAdapter.data.isNullOrEmpty()) {
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