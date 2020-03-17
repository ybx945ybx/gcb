package com.gocashback.module_me.activity

import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_FINDORDER_SUBMIT_SELECT_STORE
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.LostOrderShowIfModel
import com.gocashback.module_me.R
import com.gocashback.module_me.adapter.FindOrderSelectStoreAdapter
import com.gocashback.module_me.event.SelectStoreEvent
import kotlinx.android.synthetic.main.activity_find_order_select_store.*
import org.greenrobot.eventbus.EventBus

/**
 * @Author 55HAITAO
 * @Date 2019-06-10 20:24
 */
@Route(path = ACTIVITY_FINDORDER_SUBMIT_SELECT_STORE)
class FindOrderSelectStoreActivity : GcbBaseActivity() {

    private lateinit var findOrderSelectStoreAdapter: FindOrderSelectStoreAdapter
    private var keyWord = ""

    override fun setLayoutId(): Int {
        return R.layout.activity_find_order_select_store
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        rycv_select_store.apply {
            layoutManager = LinearLayoutManager(this@FindOrderSelectStoreActivity)
            findOrderSelectStoreAdapter = FindOrderSelectStoreAdapter(listOf())
            adapter = findOrderSelectStoreAdapter
        }

    }

    override fun initEvent() {
        gcbSearchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                keyWord = if (s.isNullOrBlank()) "" else s.toString()
                page = 0
                getStore()
            }
        })
        findOrderSelectStoreAdapter.setOnItemClickListener { _, _, position ->
            findOrderSelectStoreAdapter.data[position].isSelected = true
            findOrderSelectStoreAdapter.notifyItemChanged(position)
            EventBus.getDefault().post(SelectStoreEvent(findOrderSelectStoreAdapter.data[position]))
            finishDelay(200)
        }

        findOrderSelectStoreAdapter.setOnLoadMoreListener({
            page++
            getStore()
        }, rycv_select_store)

    }

    override fun initData() {
        page = 0
        getStore()
    }

    private fun getStore() {
        createService(UserApi::class.java)
                .lostOrderShow(page, perPage, keyWord)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<LostOrderShowIfModel>(this) {
                    override fun onSuccess(t: LostOrderShowIfModel) {
                        t.data?.let {
                            if (page == 0) {
                                findOrderSelectStoreAdapter.setNewData(it)
                            } else {
                                findOrderSelectStoreAdapter.addData(it)
                            }

                            if (page + 1 < t.page_count) findOrderSelectStoreAdapter.loadMoreComplete() else findOrderSelectStoreAdapter.loadMoreEnd(true)
                        }

                    }
                })

    }
}