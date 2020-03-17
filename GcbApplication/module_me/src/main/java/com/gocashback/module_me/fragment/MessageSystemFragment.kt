package com.gocashback.module_me.fragment

import android.support.v7.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.FRAGMENT_MESSAGE_SYSTEM
import com.gocashback.lib_common.base.GcbBaseFragment
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.NoticeIfModel
import com.gocashback.lib_common.startMessageDetailActivity
import com.gocashback.module_me.R
import com.gocashback.module_me.adapter.MessageSystemAdapter
import kotlinx.android.synthetic.main.fragment_message_system.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-12 16:05
 */
@Route(path = FRAGMENT_MESSAGE_SYSTEM)
class MessageSystemFragment : GcbBaseFragment() {
    private lateinit var messageSystemAdapter: MessageSystemAdapter

    override fun setLayoutId(): Int {
        return R.layout.fragment_message_system
    }

    override fun initView() {
        rycv_message_system.apply {
            layoutManager = LinearLayoutManager(mActivity)
            messageSystemAdapter = MessageSystemAdapter(listOf())
            adapter = messageSystemAdapter
        }

    }

    override fun initEvent() {
        // 刷新
        content_view.setOnRefreshListener {
            page = 0
            getMessageList()
        }
        // 加载更多
        messageSystemAdapter.setOnLoadMoreListener({
            page++
            getMessageList()
        }, rycv_message_system)
        // item点击事件
        messageSystemAdapter.setOnItemClickListener { _, _, position ->
            messageSystemAdapter.data[position].status = 1
            messageSystemAdapter.notifyItemChanged(position)
            startMessageDetailActivity(mActivity, messageSystemAdapter.data[position].id)
        }
        msv.setOnRetryClickListener {
            initData()
        }
    }

    override fun initData() {
        msv.showLoading()
        page = 0
        getMessageList()

    }

    private fun getMessageList() {

        createService(UserApi::class.java)
                .notice(page, perPage)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<NoticeIfModel>(mActivity) {
                    override fun onSuccess(t: NoticeIfModel) {
                        msv.showContent()
                        content_view.isRefreshing = false
                        t.data?.let {
                            if (page == 0) {
                                messageSystemAdapter.setNewData(it)
                            } else {
                                messageSystemAdapter.addData(it)
                            }

                            if (page + 1 < t.page_count) messageSystemAdapter.loadMoreComplete() else messageSystemAdapter.loadMoreEnd(true)
                        }
                        if (messageSystemAdapter.data.isNullOrEmpty()) {
                            msv.showEmpty(resources.getString(R.string.msv_no_message))
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