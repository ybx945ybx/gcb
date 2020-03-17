package com.gocashback.module_me.activity

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_MESSAGE_DETAIL
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.NoticeDetailIfModel
import com.gocashback.lib_common.utils.dateFormat
import com.gocashback.module_me.R
import kotlinx.android.synthetic.main.activity_message_detail.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-12 16:16
 */
@Route(path = ACTIVITY_MESSAGE_DETAIL)
class MessageDetailActivity : GcbBaseActivity() {

    @JvmField
    @Autowired(name = "id")
    var id = ""

    override fun setLayoutId(): Int {
        return R.layout.activity_message_detail
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

    }

    override fun initData() {
        createService(UserApi::class.java)
                .noticeDetail(id)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<NoticeDetailIfModel>(this) {
                    override fun onSuccess(t: NoticeDetailIfModel) {
                        tv_message_title.text = t.title
                        tv_message_date.text = dateFormat(t.createline.toLong()*1000)
                        tv_Content.text = t.title

                    }
                })
    }
}