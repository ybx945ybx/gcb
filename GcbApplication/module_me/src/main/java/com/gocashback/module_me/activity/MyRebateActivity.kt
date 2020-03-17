package com.gocashback.module_me.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_MYREBATE
import com.gocashback.lib_common.LOGIN_EXTRA
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.RebateRecordlfModel
import com.gocashback.lib_common.startRebateDetailActivity
import com.gocashback.lib_common.startWithdrawActivity
import com.gocashback.lib_common.utils.moneyFormat
import com.gocashback.lib_common.widget.GcbFilterPopupWindow
import com.gocashback.lib_common.widget.GcbHeadView
import com.gocashback.module_me.R
import com.gocashback.module_me.adapter.MyRebateAdapter
import kotlinx.android.synthetic.main.activity_my_rebate.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-04 17:40
 */
@Route(path = ACTIVITY_MYREBATE, extras = LOGIN_EXTRA)
class MyRebateActivity : GcbBaseActivity() {
    private lateinit var myRebateAdapter: MyRebateAdapter
    private lateinit var tvAvailable: TextView
    private lateinit var tvPending: TextView
    private var status = 0   // 	0全部 1待生效 2已生效
    private var type = 0    //  0全部 1订单返利，2朋友订单返利，3注册奖励，4邀请奖励，6活动奖励，7线下活动
    private lateinit var filterPopupWindow: GcbFilterPopupWindow

    override fun setLayoutId(): Int {
        return R.layout.activity_my_rebate
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        // 头部数据展示
        val head = LayoutInflater.from(this@MyRebateActivity).inflate(R.layout.view_my_rebate_head, null)
        tvAvailable = head.findViewById(R.id.tv_available_count)
        tvPending = head.findViewById(R.id.tv_pending_count)
        myRebateAdapter = MyRebateAdapter(this, listOf())
        myRebateAdapter.addHeaderView(head)
        rycv_my_rebate.apply {
            layoutManager = LinearLayoutManager(this@MyRebateActivity)
            adapter = myRebateAdapter
        }

        // 筛选弹窗
        val listStatus = listOf(GcbFilterPopupWindow.FilterTagModel(getString(R.string.rebate_filter_all), 0, true)
                , GcbFilterPopupWindow.FilterTagModel(getString(R.string.status_cash_pending), 1)
                , GcbFilterPopupWindow.FilterTagModel(getString(R.string.status_cash_available), 2))

        //  0全部 1订单返利，2朋友订单返利，3注册奖励，4邀请奖励，6活动奖励，7线下活动
        val listType = listOf(GcbFilterPopupWindow.FilterTagModel(getString(R.string.rebate_filter_all), 0, true)
                , GcbFilterPopupWindow.FilterTagModel(getString(R.string.rebate_type_order), 1)
                , GcbFilterPopupWindow.FilterTagModel(getString(R.string.rebate_type_friend_order), 2)
                , GcbFilterPopupWindow.FilterTagModel(getString(R.string.rebate_type_register), 3)
                , GcbFilterPopupWindow.FilterTagModel(getString(R.string.rebate_type_invite), 4)
                , GcbFilterPopupWindow.FilterTagModel(getString(R.string.rebate_type_activity), 6)
                , GcbFilterPopupWindow.FilterTagModel(getString(R.string.rebate_type_offline), 7)
        )
        filterPopupWindow = GcbFilterPopupWindow(this, listStatus, listType)
        filterPopupWindow.onConfirmListener = object : GcbFilterPopupWindow.OnConfirmListener {
            override fun onConfirm(statusValue: Int, typeValue: Int) {
                status = statusValue
                type = typeValue
                page = 0
                getRebateRecord()
            }
        }

    }

    override fun initEvent() {
        // 刷新
        content_view.setOnRefreshListener {
            page = 0
            getRebateRecord()
        }
        // 弹出筛选浮层
        gcbHeadView.onRightClickListener = object : GcbHeadView.OnRightClickListener {
            override fun onRightClick() {
                showFilterPopupWindow()
            }
        }
        // 列表item点击
        myRebateAdapter.setOnItemClickListener { _, _, position ->
            startRebateDetailActivity(this, myRebateAdapter.data[position].id)
        }
        // 加载更多
        myRebateAdapter.setOnLoadMoreListener({
            page++
            getRebateRecord()
        }, rycv_my_rebate)

        tv_withdraw.setOnClickListener {
            startWithdrawActivity(this)
        }
    }

    override fun initData() {
        msv.showLoading()
        getRebateRecord()
    }

    /**
     * 获取返利列表数据
     */
    private fun getRebateRecord() {
        createService(UserApi::class.java)
                .rebateRecord(page, perPage, status, type)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<RebateRecordlfModel>(this) {
                    override fun onSuccess(t: RebateRecordlfModel) {
                        msv.showContent()
                        content_view.isRefreshing = false
                        t.data?.let {
                            if (page == 0) {
                                // 头部数据
                                tvAvailable.text = moneyFormat(t.effective_amount)
                                tvPending.text = moneyFormat(t.pending_amount)
                                // 列表数据
                                myRebateAdapter.setNewData(it)
                            } else {
                                myRebateAdapter.addData(it)
                            }

                            if (page + 1 < t.page_count) myRebateAdapter.loadMoreComplete() else myRebateAdapter.loadMoreEnd(true)

                        }

                        if (myRebateAdapter.data.isNullOrEmpty()) {
                            msv.showEmpty()
                        }

                    }

                    override fun onFail(code: Int, msg: String) {
                        msv.showError()
                        content_view.isRefreshing = false
                    }
                })

    }

    private fun showFilterPopupWindow() {
        filterPopupWindow.show(gcbHeadView, 0, 0)

    }

}