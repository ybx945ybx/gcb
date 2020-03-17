package com.gocashback.module_me.fragment

import android.support.v7.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.FRAGMENT_MY_COLLECTION_STORES
import com.gocashback.lib_common.base.GcbBaseFragment
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.CollectionIfModel
import com.gocashback.lib_common.startStoreDetailActivity
import com.gocashback.module_me.R
import com.gocashback.module_me.adapter.MyCollectionStoresAdapter
import kotlinx.android.synthetic.main.fragment_my_collection_stores.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-12 17:35
 */
@Route(path = FRAGMENT_MY_COLLECTION_STORES)
class MyCollectionStoresFragment : GcbBaseFragment() {

     lateinit var myCollectionStoresAdapter: MyCollectionStoresAdapter

    override fun setLayoutId(): Int {
        return R.layout.fragment_my_collection_stores
    }

    override fun initView() {
        rycv_collection_stores.apply {
            layoutManager = LinearLayoutManager(mActivity)
            myCollectionStoresAdapter = MyCollectionStoresAdapter(mActivity, listOf())
            adapter = myCollectionStoresAdapter
        }

    }

    override fun initEvent() {
        // 刷新
        content_view.setOnRefreshListener {
            page = 0
            getStoreList()
        }
        // 加载更多
        myCollectionStoresAdapter.setOnLoadMoreListener({
            page++
            getStoreList()
        }, rycv_collection_stores)
        // item点击事件
        myCollectionStoresAdapter.setOnItemClickListener { _, _, position ->
            if (myCollectionStoresAdapter.getEditable()){
                myCollectionStoresAdapter.setSelectPosition(position)
            }else {
                startStoreDetailActivity(mActivity, myCollectionStoresAdapter.data[position].flag)
            }
        }
        msv.setOnRetryClickListener {
            initData()
        }

    }

    override fun initData() {
        msv.showLoading()
        page = 0
        getStoreList()

    }

    private fun getStoreList() {
        createService(UserApi::class.java)
                .collection("store", page, perPage)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<CollectionIfModel>(mActivity) {
                    override fun onSuccess(t: CollectionIfModel) {
                        msv.showContent()
                        content_view.isRefreshing = false
                        t.data?.let {
                            if (page == 0) {
                                myCollectionStoresAdapter.setNewData(it)
                            } else {
                                myCollectionStoresAdapter.addData(it)
                            }

                            if (page + 1 < t.page_count) myCollectionStoresAdapter.loadMoreComplete() else myCollectionStoresAdapter.loadMoreEnd(true)
                        }
                        if (myCollectionStoresAdapter.data.isNullOrEmpty()) {
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
        myCollectionStoresAdapter.setEditable(editable)
    }

    fun setAllSelect(allSelect: Boolean) {
        myCollectionStoresAdapter.setAllSelect(allSelect)

    }

    fun getSelectedIds() = myCollectionStoresAdapter.getSelectedIds()

    fun refresh(){
        page = 0
        getStoreList()
    }
}