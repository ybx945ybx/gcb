package com.gocashback.module_me.activity

import android.support.v7.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_WITHDRAWRECORD
import com.gocashback.lib_common.LOGIN_EXTRA
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.WithdrawRecordIfModel
import com.gocashback.lib_common.startWithdrawDetailActivity
import com.gocashback.lib_common.widget.GcbFilterPopupWindow
import com.gocashback.lib_common.widget.GcbHeadView
import com.gocashback.module_me.R
import com.gocashback.module_me.adapter.WithdrawRecordAdapter
import kotlinx.android.synthetic.main.activity_withdraw_record.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-11 17:37
 */
@Route(path = ACTIVITY_WITHDRAWRECORD, extras = LOGIN_EXTRA)
class WithdrawRecordActivity : GcbBaseActivity() {
    private lateinit var withdrawRecordAdapter: WithdrawRecordAdapter
    private var status = 0   // 	0全部 1支付中 2已支取 3提现失败
    private var type = 0     //   	0全部 1paypal 3支票 5支付宝 6礼品卡
    private lateinit var filterPopupWindow: GcbFilterPopupWindow

    override fun setLayoutId(): Int {
        return R.layout.activity_withdraw_record

    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        rycv_withdraw_recorder.apply {
            layoutManager = LinearLayoutManager(this@WithdrawRecordActivity)
            withdrawRecordAdapter = WithdrawRecordAdapter(listOf(), this@WithdrawRecordActivity)
            adapter = withdrawRecordAdapter
        }

        // 筛选弹窗
        val listStatus = listOf(GcbFilterPopupWindow.FilterTagModel(getString(R.string.withdraw_recorder_status_all), 0, true)
                , GcbFilterPopupWindow.FilterTagModel(getString(R.string.withdraw_recorder_status_processing), 1)
                , GcbFilterPopupWindow.FilterTagModel(getString(R.string.withdraw_recorder_status_paid), 2)
                , GcbFilterPopupWindow.FilterTagModel(getString(R.string.withdraw_recorder_status_failure), 3))

        //  0全部 1订单返利，2朋友订单返利，3注册奖励，4邀请奖励，6活动奖励，7线下活动
        val listType = listOf(GcbFilterPopupWindow.FilterTagModel(getString(R.string.withdraw_recorder_type_all), 0, true)
                , GcbFilterPopupWindow.FilterTagModel(getString(R.string.withdraw_recorder_type_paypal), 1)
                , GcbFilterPopupWindow.FilterTagModel(getString(R.string.withdraw_recorder_type_check), 3)
                , GcbFilterPopupWindow.FilterTagModel(getString(R.string.withdraw_recorder_type_alipay), 5)
                , GcbFilterPopupWindow.FilterTagModel(getString(R.string.withdraw_recorder_type_gift), 6))

        filterPopupWindow = GcbFilterPopupWindow(this, listStatus, listType)
        filterPopupWindow.onConfirmListener = object : GcbFilterPopupWindow.OnConfirmListener {
            override fun onConfirm(statusValue: Int, typeValue: Int) {
                status = statusValue
                type = typeValue
                page = 0
                getWithdrawRecord()
            }
        }
    }

    override fun initEvent() {
        // 刷新
        content_view.setOnRefreshListener {
            page = 0
            getWithdrawRecord()
        }
        // 弹出筛选浮层
        gcbHeadView.onRightClickListener = object : GcbHeadView.OnRightClickListener {
            override fun onRightClick() {
                showFilterPopupWindow()
            }
        }
        // 列表item点击
        withdrawRecordAdapter.setOnItemClickListener { _, _, position ->
            startWithdrawDetailActivity(this, withdrawRecordAdapter.data[position].id)
        }
        // 加载更多
        withdrawRecordAdapter.setOnLoadMoreListener({
            page++
            getWithdrawRecord()
        }, rycv_withdraw_recorder)
    }

    override fun initData() {
        msv.showLoading()
        getWithdrawRecord()
    }

    /**
     * 获取返利列表数据
     */
    private fun getWithdrawRecord() {
        createService(UserApi::class.java)
                .withdrawRecord(page, perPage, status, type)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<WithdrawRecordIfModel>(this) {
                    override fun onSuccess(t: WithdrawRecordIfModel) {
                        msv.showContent()
                        content_view.isRefreshing = false
                        t.data?.let {
                            if (page == 0) {
                                // 列表数据
                                withdrawRecordAdapter.setNewData(it)
                            } else {
                                withdrawRecordAdapter.addData(it)
                            }

                            if (page + 1 < t.page_count) withdrawRecordAdapter.loadMoreComplete() else withdrawRecordAdapter.loadMoreEnd(true)

                        }

                        if (withdrawRecordAdapter.data.isNullOrEmpty()) {
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