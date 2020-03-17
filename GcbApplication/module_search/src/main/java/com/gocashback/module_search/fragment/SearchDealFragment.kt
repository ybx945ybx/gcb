package com.gocashback.module_search.fragment

import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.FRAGMENT_SEARCH_DEAL
import com.gocashback.lib_common.base.GcbBaseFragment
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.SearchApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.deal.DealListIfModel
import com.gocashback.lib_common.startDealDetailActivity
import com.gocashback.module_search.R
import com.gocashback.module_search.SearchKeywordEvent
import com.gocashback.module_search.adapter.SearchDealAdapter
import kotlinx.android.synthetic.main.fragment_search_deal.*
import org.greenrobot.eventbus.Subscribe

/**
 * @Author 55HAITAO
 * @Date 2019-05-21 19:37
 */
@Route(path = FRAGMENT_SEARCH_DEAL)
class SearchDealFragment : GcbBaseFragment() {

    private lateinit var searchDealAdapter: SearchDealAdapter
    private var keyWord = ""

    override fun setLayoutId(): Int {
        return R.layout.fragment_search_deal
    }

    override fun initVars() {
        registerEventBus()

    }

    override fun initView() {
        rycv_search_deal.apply {
            layoutManager = LinearLayoutManager(mActivity)
            searchDealAdapter = SearchDealAdapter(mActivity, listOf())
            adapter = searchDealAdapter
        }

    }

    override fun initEvent() {
        content_view.setOnRefreshListener {
            page = 0
            getSearchData()
        }
        searchDealAdapter.setOnItemClickListener { _, _, position ->
            startDealDetailActivity(mActivity, searchDealAdapter.data[position].id)
        }
        searchDealAdapter.setOnLoadMoreListener({
            page++
            getSearchData()
        }, rycv_search_deal)
        msv.setOnRetryClickListener {
            page = 0
            getSearchData()
        }
    }

    private fun getSearchData() {
        createService(SearchApi::class.java)
                .getSearchDeal(keyWord, page, perPage)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<DealListIfModel>(mActivity) {
                    override fun onSuccess(t: DealListIfModel) {
                        msv.showContent()
                        content_view.isRefreshing = false
                        t.deal?.let {
                            if (page == 0) {
                                searchDealAdapter.setNewData(it)
                            } else {
                                searchDealAdapter.addData(it)
                            }
                        }

                        if (page + 1 < t.page_count) searchDealAdapter.loadMoreComplete() else searchDealAdapter.loadMoreEnd(true)

                        if (searchDealAdapter.data.isNullOrEmpty()) msv.showEmpty(resources.getString(R.string.msv_no_result))

                    }

                    override fun onFail(code: Int, msg: String) {
                        super.onFail(code, msg)
                        content_view.isRefreshing = false
                        if (page == 0) {
                            msv.showError()
                        }
                    }
                })
    }

    @Subscribe
    fun onSearchKeywordEvent(event: SearchKeywordEvent) {
        if (!TextUtils.equals(keyWord, event.keyWord)) {
            keyWord = event.keyWord
            page = 0
            getSearchData()
        }
    }
}