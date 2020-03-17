package com.gocashback.module_me.fragment

import android.support.v7.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.FRAGMENT_MY_COLLECTION_DEALS
import com.gocashback.lib_common.base.GcbBaseFragment
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.CollectionIfModel
import com.gocashback.lib_common.startDealDetailActivity
import com.gocashback.module_me.R
import com.gocashback.module_me.adapter.MyCollectionDealsAdapter
import kotlinx.android.synthetic.main.fragment_my_collection_deals.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-12 17:33
 */
@Route(path = FRAGMENT_MY_COLLECTION_DEALS)
class MyCollectionDealsFragment : GcbBaseFragment() {
    lateinit var myCollectionDealsAdapter: MyCollectionDealsAdapter

    override fun setLayoutId(): Int {
        return R.layout.fragment_my_collection_deals
    }

    override fun initView() {
        rycv_collection_deals.apply {
            layoutManager = LinearLayoutManager(mActivity)
            myCollectionDealsAdapter = MyCollectionDealsAdapter(mActivity, listOf())
            adapter = myCollectionDealsAdapter
        }

    }

    override fun initEvent() {
        // 刷新
        content_view.setOnRefreshListener {
            page = 0
            getDealsList()
        }
        // 加载更多
        myCollectionDealsAdapter.setOnLoadMoreListener({
            page++
            getDealsList()
        }, rycv_collection_deals)
        // item点击事件
        myCollectionDealsAdapter.setOnItemClickListener { _, _, position ->
            if (myCollectionDealsAdapter.getEditable()) {
                myCollectionDealsAdapter.setSelectPosition(position)
            } else {
                startDealDetailActivity(mActivity, myCollectionDealsAdapter.data[position].id)
            }
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
                .collection("deal", page, perPage)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<CollectionIfModel>(mActivity) {
                    override fun onSuccess(t: CollectionIfModel) {
                        msv.showContent()
                        content_view.isRefreshing = false
                        t.data?.let {
                            if (page == 0) {
                                myCollectionDealsAdapter.setNewData(it)
                            } else {
                                myCollectionDealsAdapter.addData(it)
                            }

                            if (page + 1 < t.page_count) myCollectionDealsAdapter.loadMoreComplete() else myCollectionDealsAdapter.loadMoreEnd(true)
                        }
                        if (myCollectionDealsAdapter.data.isNullOrEmpty()) {
                            msv.showEmpty(resources.getString(R.string.msv_no_favorites))
                        }

                    }

                    override fun onFail(code: Int, msg: String) {
                        super.onFail(code, msg)
                        msv.showError()
                        content_view.isRefreshing = false
                    }
                })
    }


    fun setEditable(editable: Boolean) {
        myCollectionDealsAdapter.setEditable(editable)
    }

    fun setAllSelect(allSelect: Boolean) {
        myCollectionDealsAdapter.setAllSelect(allSelect)

    }

    fun getSelectedIds() = myCollectionDealsAdapter.getSelectedIds()

    fun refresh() {
        page = 0
        getDealsList()
    }

}