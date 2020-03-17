package com.gocashback.module_me.activity

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_HELP_CHILD
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.HelpApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.help.HelpShowIfModel
import com.gocashback.module_me.R
import com.zzhoujay.richtext.RichText
import kotlinx.android.synthetic.main.activity_help_child.*


/**
 * @Author 55HAITAO
 * @Date 2019-06-12 13:42
 */
@Route(path = ACTIVITY_HELP_CHILD)
class HelpChildActivity : GcbBaseActivity() {

    @JvmField
    @Autowired(name = "id")
    var id = ""

    override fun setLayoutId(): Int {
        return R.layout.activity_help_child
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

    }

    override fun initData() {
        createService(HelpApi::class.java)
                .helpShow(id)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<HelpShowIfModel>(this) {
                    override fun onSuccess(t: HelpShowIfModel) {
                        tv_title.text = t.title
                        RichText.fromHtml(t.content).into(tv_Content)

                    }
                })
    }

}