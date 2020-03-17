package com.gocashback.module_search.fragment

import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gocashback.lib_common.FRAGMENT_SEARCH_STORE
import com.gocashback.lib_common.base.GcbBaseFragment
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.SearchApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.store.StoreListIfModel
import com.gocashback.lib_common.startStoreDetailActivity
import com.gocashback.module_search.R
import com.gocashback.module_search.SearchKeywordEvent
import com.gocashback.module_search.adapter.SearchStoreAdapter
import kotlinx.android.synthetic.main.fragment_search_store.*
import org.greenrobot.eventbus.Subscribe

/**
 * 搜索结果商家页
 *
 * @Author 55HAITAO
 * @Date 2019-05-21 19:36
 */
@Route(path = FRAGMENT_SEARCH_STORE)
class SearchStoreFragment : GcbBaseFragment() {

    private lateinit var searchStoreAdapter: SearchStoreAdapter
    private var keyWord = ""

    override fun setLayoutId(): Int {
        return R.layout.fragment_search_store
    }

    override fun initVars() {
        registerEventBus()

    }


    override fun initView() {
        rycv_search_store.apply {
            layoutManager = LinearLayoutManager(mActivity)
            searchStoreAdapter = SearchStoreAdapter(mActivity, listOf())
            adapter = searchStoreAdapter
        }

    }

    override fun initEvent() {
        content_view.setOnRefreshListener {
            page = 0
            getSearchData()
        }
        searchStoreAdapter.setOnItemClickListener { adapter, view, position ->
            startStoreDetailActivity(mActivity, searchStoreAdapter.data[position].flag)
        }
        searchStoreAdapter.setOnLoadMoreListener(object : BaseQuickAdapter.RequestLoadMoreListener {
            override fun onLoadMoreRequested() {
                page++
                getSearchData()
            }
        }, rycv_search_store)
        msv.setOnRetryClickListener {
            page = 0
            getSearchData()
        }
    }

    fun getSearchData() {
        createService(SearchApi::class.java)
                .getSearchStore(keyWord, page, perPage)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<StoreListIfModel>(mActivity) {
                    override fun onSuccess(t: StoreListIfModel) {
                        msv.showContent()
                        content_view.isRefreshing = false
                        t.store?.let {
                            if (page == 0) {
                                searchStoreAdapter.setNewData(it)
                            } else {
                                searchStoreAdapter.addData(it)
                            }
                        }

                        if (page + 1 < t.page_count) searchStoreAdapter.loadMoreComplete() else searchStoreAdapter.loadMoreEnd(true)

                        if (searchStoreAdapter.data.isNullOrEmpty()) msv.showEmpty(resources.getString(R.string.msv_no_result))

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