package com.gocashback.module_home.fragment

import android.support.v7.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.FRAGMENT_HOME_DISCOUNT_CATE_DEAL
import com.gocashback.lib_common.base.GcbBaseFragment
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.DealApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.deal.DealListIfModel
import com.gocashback.lib_common.startDealDetailActivity
import com.gocashback.module_home.R
import com.gocashback.module_home.adapter.DealListAdapter
import kotlinx.android.synthetic.main.fragment_deal_list.*

/**
 * @Author 55HAITAO
 * @Date 2019-05-16 14:24
 */
@Route(path = FRAGMENT_HOME_DISCOUNT_CATE_DEAL)
class HomeDealListFragment : GcbBaseFragment() {

    @JvmField
    @Autowired(name = "id")
    var cateId = ""

    private lateinit var dealListAdapter: DealListAdapter

    override fun setLayoutId(): Int {
        return R.layout.fragment_deal_list
    }

    override fun initVars() {
        cateId = arguments?.getString("id") ?: ""

    }

    override fun initView() {
        rycv_deal.apply {
            layoutManager = LinearLayoutManager(mActivity)
            dealListAdapter = DealListAdapter(mActivity, listOf())
            adapter = dealListAdapter
        }

    }

    override fun initEvent() {
        dealListAdapter.setOnItemClickListener { _, _, position ->
            startDealDetailActivity(mActivity, dealListAdapter.data[position].id)
        }
        dealListAdapter.setOnLoadMoreListener({
            page++
            getDealList()
        }, rycv_deal)
    }

    override fun initData() {
        page = 0
        getDealList()

    }

    private fun getDealList() {
        if (cateId == "-1") {
            createService(DealApi::class.java)
                    .getDealList(page, 50)
                    .compose(ResponseTransformer.handleResult())
                    .compose(bindToLifecycle())
                    .subscribe(object : BaseObserver<DealListIfModel>(mActivity) {
                        override fun onSuccess(t: DealListIfModel) {
                            t.deal?.let {
                                if (page == 0) {
                                    dealListAdapter.setNewData(it)
                                } else {
                                    dealListAdapter.addData(it)
                                }

                                if (page + 1 < t.page_count) dealListAdapter.loadMoreComplete() else dealListAdapter.loadMoreEnd(true)
                            }
                        }
                    })
        } else {
            createService(DealApi::class.java)
                    .getCateDealList(cateId, page, perPage)
                    .compose(ResponseTransformer.handleResult())
                    .compose(bindToLifecycle())
                    .subscribe(object : BaseObserver<DealListIfModel>(mActivity) {
                        override fun onSuccess(t: DealListIfModel) {
                            t.deal?.let {
                                if (page == 0) {
                                    dealListAdapter.setNewData(it)
                                } else {
                                    dealListAdapter.addData(it)
                                }

                                if (page + 1 < t.page_count) dealListAdapter.loadMoreComplete() else dealListAdapter.loadMoreEnd(true)
                            }

                        }
                    })
        }
    }

}