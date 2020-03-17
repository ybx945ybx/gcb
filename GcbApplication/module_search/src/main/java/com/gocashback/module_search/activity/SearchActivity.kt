package com.gocashback.module_search.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_SEARCH
import com.gocashback.lib_common.base.BasePagerAdapter
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.makeSearchDealFragment
import com.gocashback.lib_common.makeSearchStoreFragment
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.SearchApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.store.StoreItemModel
import com.gocashback.lib_common.network.model.store.StoreListIfModel
import com.gocashback.module_search.R
import com.gocashback.module_search.SearchKeywordEvent
import com.gocashback.module_search.getSearchHistory
import com.gocashback.module_search.setSearchHistory
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_search.*
import org.greenrobot.eventbus.EventBus

/**
 * @Author 55HAITAO
 * @Date 2019/4/8 7:25 PM
 */
@Route(path = ACTIVITY_SEARCH)
class SearchActivity : GcbBaseActivity() {
    private lateinit var historyAdapter: TagAdapter<String>
    private lateinit var hotAdapter: TagAdapter<StoreItemModel>
    //    private var searchFilterAdapter: SearchFilterAdapter = SearchFilterAdapter(mutableListOf())
//    private var searchResultAdapter: SearchResultAdapter = SearchResultAdapter(mutableListOf())
    private lateinit var tabs: List<String>
    private lateinit var historyList: ArrayList<String>

    override fun setLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun initVars() {
//        EventBus.getDefault().register(this)
        tabs = listOf(resources.getString(R.string.home_tab_stores), resources.getString(R.string.home_tab_deals))

    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        // 历史搜索
        getSearchHistory(this).let {
            historyList = it
            if (it.size > 0) {
                group_search_history.visibility = View.VISIBLE
                historyAdapter = object : TagAdapter<String>(it) {
                    override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                        val textView = LayoutInflater.from(this@SearchActivity).inflate(R.layout.item_search_tag, parent, false) as TextView
                        textView.text = t
                        return textView
                    }
                }
                tflyt_search_history.adapter = historyAdapter

            } else {
                group_search_history.visibility = View.GONE
            }
        }
        // 热门返利商家

        // 模糊匹配列表
//        rycv_search_filter.apply {
//            layoutManager = LinearLayoutManager(this@SearchActivity)
//            adapter = searchFilterAdapter
//        }

        // 搜索结果列表

        val searchStoreFragment = makeSearchStoreFragment()
        val searchDealFragment = makeSearchDealFragment()
//        view_pager.setScrollable(false)
        view_pager.adapter = BasePagerAdapter(supportFragmentManager, listOf(searchStoreFragment, searchDealFragment), tabs)
        tablayout.setupWithViewPager(view_pager)
        initTabView()

//        rycv_search_result.apply {
//            layoutManager = LinearLayoutManager(this@SearchActivity)
//            adapter = searchResultAdapter
//        }

    }

    private fun initTabView() {
        for (i in tabs.indices) {
            tablayout.getTabAt(i)?.apply {
                setCustomView(R.layout.view_commen_tab)
                customView?.findViewById<TextView>(R.id.tab_title)?.text = tabs[i]

            }

        }
    }

    override fun initEvent() {
        search_view.addFocusChangeListener(View.OnFocusChangeListener { _, hasFocus ->
            //                rlytContent.setVisibility(hasFocus ? View.GONE : View.VISIBLE);
//                rlytSearchResult.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
//                clyt_history.visibility = if (hasFocus) View.GONE else View.VISIBLE
////                    rycv_search_filter.visibility = View.VISIBLE
//                rlyt_search_result.visibility = View.VISIBLE
            if (!hasFocus) {
                hideSoftInput()
            }
        })
        search_view.addOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!TextUtils.isEmpty(search_view.getText())) {
                        hideSoftInput()
                        doSearch(search_view.getText().toString())
                    }
                    return false
                }
                return false

            }
        })
        search_view.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
//                    clyt_history.visibility = View.VISIBLE
                } else {
                    clyt_history.visibility = View.GONE
//                    rycv_search_filter.visibility = View.VISIBLE
                    rlyt_search_result.visibility = View.VISIBLE
                    EventBus.getDefault().post(SearchKeywordEvent(s.toString()))
//                    filterKeyWord(s.toString())
                }
            }

        })
        tflyt_search_history.setOnTagClickListener { _, position, _ ->
            doSearch(historyAdapter.getItem(position))
            return@setOnTagClickListener false
        }
        tflyt_store_cash_back.setOnTagClickListener { _, position, _ ->
            doSearch(hotAdapter.getItem(position).name)
            return@setOnTagClickListener false
        }

//        searchFilterAdapter.setOnItemClickListener { adapter, view, position -> doSearch(searchFilterAdapter.getItem(position)) }
//        tv_change.setOnClickListener { getCashBackStore() }

    }

    override fun initData() {
        getStores()

    }

    private fun doSearch(keyWord: String) {
        search_view.getEditText().setText(keyWord)
        search_view.getEditText().setSelection(keyWord.length)

//        clyt_history.visibility = View.GONE
////                    rycv_search_filter.visibility = View.VISIBLE
//        rlyt_search_result.visibility = View.VISIBLE
////        EventBus.getDefault().post(SearchKeywordEvent(keyWord))
//
//        keyWord?.let {

        var include = false
        for (name in historyList) {
            if (keyWord == name) {
                include = true
                break
            }
        }
        if (!include) {
            historyList.add(0, keyWord)
            if (historyList.size > 10) {
                historyList = ArrayList(historyList.subList(0, 10))
            }
            setSearchHistory(this, historyList)
            updataHistoryView(historyList)
        }
//        addSearchHistory(this, keyWord)
//
//            EventBus.getDefault().post(SearchKeywordEvent(keyWord))
//        }


    }

    private fun updataHistoryView(newHistoryList: ArrayList<String>) {
        if (newHistoryList.size > 0) {
            group_search_history.visibility = View.VISIBLE
            historyAdapter = object : TagAdapter<String>(newHistoryList) {
                override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                    val textView = LayoutInflater.from(this@SearchActivity).inflate(R.layout.item_search_tag, parent, false) as TextView
                    textView.text = t
                    return textView
                }
            }
            tflyt_search_history.adapter = historyAdapter

        } else {
            group_search_history.visibility = View.GONE
        }

    }

    override fun onBackPressed() {
        if (search_view.hasFocus()) {
            search_view.clearFocus()
        } else {
            if (clyt_history.visibility == View.VISIBLE) {
                super.onBackPressed()
            } else {
                clyt_history.visibility = View.VISIBLE
                rlyt_search_result.visibility = View.GONE
            }
        }
    }

    override fun finish() {
        hideSoftInput(search_view)
        super.finish()
    }

    /**
     * 热门商家
     */
    private fun getStores() {
        createService(SearchApi::class.java)
                .getSearchStore("", 0, 10)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<StoreListIfModel>(this) {
                    override fun onSuccess(t: StoreListIfModel) {
                        if (!t.store.isNullOrEmpty()) {
                            group_search_hot.visibility = View.VISIBLE
                            hotAdapter = object : TagAdapter<StoreItemModel>(t.store) {
                                override fun getView(parent: FlowLayout?, position: Int, t: StoreItemModel): View {
                                    val textView = LayoutInflater.from(this@SearchActivity).inflate(R.layout.item_search_tag, parent, false) as TextView
                                    textView.text = t.name
                                    return textView
                                }
                            }
                            tflyt_store_cash_back.adapter = hotAdapter

                        } else {
                            group_search_hot.visibility = View.GONE
                        }

                    }

                    override fun onFail(code: Int, msg: String) {
                        group_search_hot.visibility = View.GONE

                    }
                })
    }
}