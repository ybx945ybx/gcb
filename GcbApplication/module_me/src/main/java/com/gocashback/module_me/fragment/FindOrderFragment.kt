package com.gocashback.module_me.fragment

import android.support.v7.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.FRAGMENT_FIND_ORDER
import com.gocashback.lib_common.base.GcbBaseFragment
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.LostOrderIfModel
import com.gocashback.lib_common.startLostOrderDetailActivity
import com.gocashback.module_me.R
import com.gocashback.module_me.adapter.MyOrderAdapter
import kotlinx.android.synthetic.main.fragment_find_order.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-10 15:22
 */
@Route(path = FRAGMENT_FIND_ORDER)
class FindOrderFragment : GcbBaseFragment() {

    private lateinit var myOrderAdapter: MyOrderAdapter

    @JvmField
    @Autowired(name = "type")
    var type = 0      //        0全部，1待审核,2待确认，3驳回，4已添加，5关闭

    override fun setLayoutId(): Int {
        return R.layout.fragment_find_order
    }

    override fun initVars() {
        type = arguments?.getInt("type", 0) ?: 0

    }

    override fun initView() {
        rycv_find_order.apply {
            layoutManager = LinearLayoutManager(mActivity)
            myOrderAdapter = MyOrderAdapter(listOf(), 1)
            adapter = myOrderAdapter
        }

    }

    override fun initEvent() {
        // 刷新
        content_view.setOnRefreshListener {
            page = 0
            getOrderList()
        }
        // 加载更多
        myOrderAdapter.setOnLoadMoreListener({
            page++
            getOrderList()
        }, rycv_find_order)
        // item点击事件
        myOrderAdapter.setOnItemClickListener { _, _, position ->
            startLostOrderDetailActivity(mActivity, myOrderAdapter.data[position].id)
        }
        msv.setOnRetryClickListener {
            initData()
        }
    }

    override fun initData() {
        msv.showLoading()
        page = 0
        getOrderList()

    }

    private fun getOrderList() {

        createService(UserApi::class.java)
                .lostOrder(page, perPage, type)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<LostOrderIfModel>(mActivity) {
                    override fun onSuccess(t: LostOrderIfModel) {
                        msv.showContent()
                        content_view.isRefreshing = false
                        t.data?.let {
                            if (page == 0) {
                                myOrderAdapter.setNewData(it)
                            } else {
                                myOrderAdapter.addData(it)
                            }

                            if (page + 1 < t.page_count) myOrderAdapter.loadMoreComplete() else myOrderAdapter.loadMoreEnd(true)
                        }
                        if (myOrderAdapter.data.isNullOrEmpty()) {
                            msv.showEmpty(resources.getString(R.string.msv_no_orders))
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