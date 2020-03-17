package com.gocashback.module_discover.fragment

import android.support.v7.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.FRAGMENT_DISCOVER
import com.gocashback.lib_common.base.GcbBaseApplication
import com.gocashback.lib_common.base.GcbBaseFragment
import com.gocashback.lib_common.language.LOCALE_CHINESE
import com.gocashback.lib_common.language.getLanguage
import com.gocashback.lib_common.model.JumpIfModel
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.IndexApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.index.IndexAdvertIfModel
import com.gocashback.lib_common.utils.jump
import com.gocashback.module_discover.R
import com.gocashback.module_discover.adapter.DiscoverAdapter
import kotlinx.android.synthetic.main.fragment_discover.*

/**
 * @Author 55HAITAO
 * @Date 2019/4/8 2:38 PM
 */
@Route(path = FRAGMENT_DISCOVER)
class DiscoverFragment : GcbBaseFragment() {
    private lateinit var discoverAdapter: DiscoverAdapter

    override fun setLayoutId(): Int {
        return R.layout.fragment_discover
    }

    override fun initView() {
        rycv_discover.apply {
            layoutManager = LinearLayoutManager(mActivity)
            discoverAdapter = DiscoverAdapter(listOf())
            adapter = discoverAdapter
        }
    }

    override fun initEvent() {
        content_view.setOnRefreshListener {
            getData()
        }
        discoverAdapter.setOnItemClickListener { _, _, position ->
            jump(mActivity, JumpIfModel().apply {
                this.click_type = discoverAdapter.data[position].click_type
                this.url = discoverAdapter.data[position].url
                this.gotag = discoverAdapter.data[position].gotag
                this.gotobuy_url = discoverAdapter.data[position].gotobuy_url
                this.store_id = discoverAdapter.data[position].store_id
            })
        }
        msv.setOnRetryClickListener {
            initData()
        }
    }

    override fun initData() {
        msv.showLoading()
        getData()
    }

    private fun getData() {
        createService(IndexApi::class.java).getIndexAdvert(if (LOCALE_CHINESE == getLanguage(GcbBaseApplication.application)) 84 else 83)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<IndexAdvertIfModel>(null) {
                    override fun onSuccess(t: IndexAdvertIfModel) {
                        msv.showContent()
                        content_view.isRefreshing = false
                        t.ad?.let {
                            discoverAdapter.setNewData(it)
                        }

                        if (discoverAdapter.data.isNullOrEmpty()) {
                            msv.showEmpty()
                        }
                    }

                    override fun onFail(code: Int, msg: String) {
                        msv.showError()
                        content_view.isRefreshing = false

                    }
                })
    }
}